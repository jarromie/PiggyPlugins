package net.runelite.client.plugins.ChinBreakHandler.ui;

import com.google.inject.Inject;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import net.runelite.client.plugins.ChinBreakHandler.PiggyBreakHandler;
import net.runelite.client.plugins.ChinBreakHandler.PiggyBreakHandlerPlugin;
import net.runelite.client.plugins.ChinBreakHandler.util.ConfigPanel;
import net.runelite.client.plugins.ChinBreakHandler.util.JMultilineLabel;
import net.runelite.client.plugins.ChinBreakHandler.util.SwingUtilExtended;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.PluginErrorPanel;
import net.runelite.client.util.ImageUtil;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ChinBreakHandlerPanel extends PluginPanel {
    public final static Color PANEL_BACKGROUND_COLOR = ColorScheme.DARK_GRAY_COLOR;
    final static Color BACKGROUND_COLOR = ColorScheme.DARKER_GRAY_COLOR;

    static final Font NORMAL_FONT = FontManager.getRunescapeFont();
    static final Font SMALL_FONT = FontManager.getRunescapeSmallFont();

    private static final ImageIcon HELP_ICON;
    private static final ImageIcon HELP_HOVER_ICON;

    static
    {
        final BufferedImage helpIcon =
                ImageUtil.recolorImage(
                        ImageUtil.loadImageResource(PiggyBreakHandlerPlugin.class, "help.png"), ColorScheme.BRAND_ORANGE
                );
        HELP_ICON = new ImageIcon(helpIcon);
        HELP_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(helpIcon, 0.53f));
    }

    private final PiggyBreakHandlerPlugin piggyBreakHandlerPluginPlugin;
    private final PiggyBreakHandler piggyBreakHandler;
    private final ConfigPanel configPanel;

    public @NonNull Disposable pluginDisposable;
    public @NonNull Disposable activeDisposable;
    public @NonNull Disposable currentDisposable;
    public @NonNull Disposable startDisposable;
    public @NonNull Disposable configDisposable;

    private final JPanel unlockAccountPanel = new JPanel(new BorderLayout());
    private final JPanel breakTimingsPanel = new JPanel(new GridLayout(0, 1));

    @Inject
    private ChinBreakHandlerPanel(PiggyBreakHandlerPlugin piggyBreakHandlerPluginPlugin, PiggyBreakHandler piggyBreakHandler, ConfigPanel configPanel)
    {
        super(false);

        configPanel.init(piggyBreakHandlerPluginPlugin.getOptionsConfig());

        this.piggyBreakHandlerPluginPlugin = piggyBreakHandlerPluginPlugin;
        this.piggyBreakHandler = piggyBreakHandler;
        this.configPanel = configPanel;

        pluginDisposable = piggyBreakHandler
                .getPluginObservable()
                .subscribe((Map<Plugin, Boolean> plugins) ->
                        SwingUtilExtended.syncExec(() ->
                                buildPanel(plugins)));

        activeDisposable = piggyBreakHandler
                .getActiveObservable()
                .subscribe(
                        (ignored) ->
                                SwingUtilExtended.syncExec(() ->
                                        buildPanel(piggyBreakHandler.getPlugins()))
                );

        currentDisposable = piggyBreakHandler
                .getActiveBreaksObservable()
                .subscribe(
                        (ignored) ->
                                SwingUtilExtended.syncExec(() ->
                                        buildPanel(piggyBreakHandler.getPlugins()))
                );

        startDisposable = piggyBreakHandler
                .getActiveObservable()
                .subscribe(
                        (ignored) ->
                                SwingUtilExtended.syncExec(() -> {
                                    unlockAccountsPanel();
                                    unlockAccountPanel.revalidate();
                                    unlockAccountPanel.repaint();

                                    breakTimingsPanel();
                                    breakTimingsPanel.revalidate();
                                    breakTimingsPanel.repaint();
                                })
                );

        configDisposable = piggyBreakHandler
                .configChanged
                .subscribe(
                        (ignored) ->
                                SwingUtilExtended.syncExec(() -> {
                                    unlockAccountsPanel();
                                    unlockAccountPanel.revalidate();
                                    unlockAccountPanel.repaint();
                                })
                );

        this.setBackground(PANEL_BACKGROUND_COLOR);
        this.setLayout(new BorderLayout());

        buildPanel(piggyBreakHandler.getPlugins());
    }

    void buildPanel(Map<Plugin, Boolean> plugins)
    {
        removeAll();

        if (plugins.isEmpty())
        {
            PluginErrorPanel errorPanel = new PluginErrorPanel();
            errorPanel.setContent("Piggy Break Handler", "There were no plugins that registered themselves with the break handler.");

            add(errorPanel, BorderLayout.NORTH);
        }
        else
        {
            JPanel contentPanel = new JPanel(new BorderLayout());

            contentPanel.add(statusPanel(), BorderLayout.NORTH);
            contentPanel.add(tabbedPane(plugins), BorderLayout.CENTER);

            add(titleBar(), BorderLayout.NORTH);
            add(contentPanel, BorderLayout.CENTER);
        }

        revalidate();
        repaint();
    }

    private JPanel titleBar()
    {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel();
        JLabel help = new JLabel(HELP_ICON);

        title.setText("Piggy Break Handler");
        title.setForeground(Color.WHITE);

        help.setToolTipText("Info");
        help.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent mouseEvent)
            {
                JOptionPane.showMessageDialog(
                        piggyBreakHandlerPluginPlugin.getFrame(),
                        "<html><center>The configs in this panel can be used to <b>schedule</b> breaks.<br>" +
                                "When the timer hits zero, a break is scheduled. This does not mean that the break will be taken immediately!<br>" +
                                "The break handler informs the plugins that it is time for a break, and the plugins are responsible for deciding<br>" +
                                "when to start the break. A plugin would finish your current task before logging out and beginning your break, <br>" +
                                "as opposed to interrupting your current task. For help implementing Open Break Handler into your plugins,<br>" +
                                "check out the jampack Discord server or look at examples in the GitHub repository. Happy cheating!<br><br></center></html>",
                        "Chin break handler",
                        JOptionPane.QUESTION_MESSAGE
                );
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent)
            {
                help.setIcon(HELP_HOVER_ICON);
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent)
            {
                help.setIcon(HELP_ICON);
            }
        });
        help.setBorder(new EmptyBorder(0, 3, 0, 0));

        titlePanel.add(title, BorderLayout.WEST);
        titlePanel.add(help, BorderLayout.EAST);

        return titlePanel;
    }

    private boolean unlockAccountsPanel()
    {
        Set<Plugin> activePlugins = piggyBreakHandler.getActivePlugins();

        LoginMode loginMode = LoginMode.parse(piggyBreakHandlerPluginPlugin.getConfigManager().getConfiguration("piggyBreakHandler", "accountselection"));

        String data = PiggyBreakHandlerPlugin.data;

        if (activePlugins.isEmpty() || loginMode != LoginMode.PROFILES || (data != null && !data.trim().isEmpty()))
        {
            return false;
        }

        return true;
    }

    private boolean breakTimingsPanel()
    {
        breakTimingsPanel.removeAll();

        Set<Plugin> pluginStream = piggyBreakHandler.getActivePlugins().stream().filter(e -> !piggyBreakHandlerPluginPlugin.isValidBreak(e)).collect(Collectors.toSet());

        if (pluginStream.isEmpty())
        {
            return false;
        }

        for (Plugin plugin : pluginStream)
        {
            JPanel wrapperPanel = new JPanel(new BorderLayout());

            JPanel titleWrapper = new JPanel(new BorderLayout());
            titleWrapper.setBackground(new Color(125, 40, 40));
            titleWrapper.setBorder(new CompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(115, 30, 30)),
                    BorderFactory.createLineBorder(new Color(125, 40, 40))
            ));

            JLabel title = new JLabel();
            title.setText("Warning");
            title.setFont(NORMAL_FONT);
            title.setPreferredSize(new Dimension(0, 24));
            title.setForeground(Color.WHITE);
            title.setBorder(new EmptyBorder(0, 8, 0, 0));

            titleWrapper.add(title, BorderLayout.CENTER);

            wrapperPanel.add(titleWrapper, BorderLayout.NORTH);

            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBackground(new Color(125, 40, 40));

            JMultilineLabel description = new JMultilineLabel();

            description.setText("The break timings for " + plugin.getName() + " are invalid!");
            description.setFont(SMALL_FONT);
            description.setDisabledTextColor(Color.WHITE);
            description.setBackground(new Color(115, 30, 30));

            description.setBorder(new EmptyBorder(5, 5, 10, 5));

            contentPanel.add(description, BorderLayout.CENTER);

            wrapperPanel.add(contentPanel, BorderLayout.CENTER);

            breakTimingsPanel.add(wrapperPanel);
        }

        return true;
    }

    private JPanel statusPanel()
    {
        Set<Plugin> activePlugins = piggyBreakHandler.getActivePlugins();

        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        if (unlockAccountsPanel())
        {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(null, "Please enter your profiles password to allow break handling.");
            });
        }

        if (breakTimingsPanel())
        {
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            c.gridy += 1;
            c.insets = new Insets(5, 10, 0, 10);

            contentPanel.add(breakTimingsPanel, c);
        }

        if (activePlugins.isEmpty())
        {
            return contentPanel;
        }

        for (Plugin plugin : activePlugins)
        {
            ChinBreakHandlerStatusPanel statusPanel = new ChinBreakHandlerStatusPanel(piggyBreakHandlerPluginPlugin, piggyBreakHandler, plugin);

            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            c.gridy += 1;
            c.insets = new Insets(5, 10, 0, 10);

            contentPanel.add(statusPanel, c);
        }

        JButton scheduleBreakButton = new JButton("Start Break Now");

        if (activePlugins.size() > 0)
        {
            scheduleBreakButton.addActionListener(e -> activePlugins.forEach(plugin -> {
                if (!piggyBreakHandler.isBreakActive(plugin))
                {
                    piggyBreakHandler.planBreak(plugin, Instant.now());
                }
            }));

            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            c.gridy += 1;
            c.insets = new Insets(5, 10, 0, 10);

            contentPanel.add(scheduleBreakButton, c);
        }

        return contentPanel;
    }

    private JTabbedPane tabbedPane(Map<Plugin, Boolean> plugins)
    {
        JTabbedPane mainTabPane = new JTabbedPane();

        JScrollPane pluginPanel = wrapContainer(contentPane(plugins));
        JScrollPane repositoryPanel = wrapContainer(new ChinBreakHandlerAccountPanel(piggyBreakHandlerPluginPlugin, piggyBreakHandler));
        JScrollPane optionsPanel = wrapContainer(configPanel);

        mainTabPane.add("Plugins", pluginPanel);
        mainTabPane.add("Accounts", repositoryPanel);
        mainTabPane.add("Options", optionsPanel);

        return mainTabPane;
    }

    private JPanel contentPane(Map<Plugin, Boolean> plugins)
    {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        if (piggyBreakHandler.getPlugins().isEmpty())
        {
            return contentPanel;
        }

        for (Map.Entry<Plugin, Boolean> plugin : plugins.entrySet())
        {
            ChinBreakHandlerPluginPanel panel = new ChinBreakHandlerPluginPanel(piggyBreakHandlerPluginPlugin, plugin.getKey(), plugin.getValue());

            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            c.gridy += 1;
            c.insets = new Insets(5, 10, 0, 10);

            contentPanel.add(panel, c);
        }

        return contentPanel;
    }

    public static JScrollPane wrapContainer(final JPanel container)
    {
        final JPanel wrapped = new JPanel(new BorderLayout());
        wrapped.add(container, BorderLayout.NORTH);
        wrapped.setBackground(PANEL_BACKGROUND_COLOR);

        final JScrollPane scroller = new JScrollPane(wrapped);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        scroller.setBackground(PANEL_BACKGROUND_COLOR);

        return scroller;
    }
}