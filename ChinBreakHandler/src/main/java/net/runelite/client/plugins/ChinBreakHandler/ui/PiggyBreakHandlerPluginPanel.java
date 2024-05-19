package net.runelite.client.plugins.ChinBreakHandler.ui;

import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.Units;
import net.runelite.client.plugins.ChinBreakHandler.PiggyBreakHandlerPlugin;
import net.runelite.client.plugins.ChinBreakHandler.util.JMultilineLabel;
import net.runelite.client.plugins.ChinBreakHandler.util.OnOffToggleButton;
import net.runelite.client.plugins.ChinBreakHandler.util.UnitFormatterFactory;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PiggyBreakHandlerPluginPanel extends JPanel
{
    private final ConfigManager configManager;
    private final Plugin plugin;
    private final boolean configurable;

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(PluginPanel.PANEL_WIDTH, super.getPreferredSize().height);
    }

    PiggyBreakHandlerPluginPanel(PiggyBreakHandlerPlugin piggyBreakHandlerPluginPlugin, Plugin plugin, boolean configurable)
    {
        this.configManager = piggyBreakHandlerPluginPlugin.getConfigManager();
        this.plugin = plugin;
        this.configurable = configurable;

        if (configurable)
        {
            setupDefaults();
        }

        setLayout(new BorderLayout());
        setBackground(PiggyBreakHandlerPanel.BACKGROUND_COLOR);

        init();
    }

    private void init()
    {
        JPanel titleWrapper = new JPanel(new BorderLayout());
        titleWrapper.setBackground(PiggyBreakHandlerPanel.BACKGROUND_COLOR);
        titleWrapper.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, PiggyBreakHandlerPanel.PANEL_BACKGROUND_COLOR),
                BorderFactory.createLineBorder(PiggyBreakHandlerPanel.BACKGROUND_COLOR)
        ));

        JLabel title = new JLabel();
        title.setText(plugin.getName());
        title.setFont(PiggyBreakHandlerPanel.NORMAL_FONT);
        title.setPreferredSize(new Dimension(0, 24));
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(0, 8, 0, 0));

        JPanel titleActions = new JPanel(new BorderLayout(3, 0));
        titleActions.setBackground(PiggyBreakHandlerPanel.BACKGROUND_COLOR);
        titleActions.setBorder(new EmptyBorder(0, 0, 0, 8));

        if (configurable)
        {
            String pluginName = PiggyBreakHandlerPlugin.sanitizedName(plugin);

            JToggleButton onOffToggle = new OnOffToggleButton();

            onOffToggle.setSelected(Boolean.parseBoolean(configManager.getConfiguration("piggyBreakHandler", pluginName + "-enabled")));
            onOffToggle.addItemListener(i ->
                    configManager.setConfiguration("piggyBreakHandler", pluginName + "-enabled", onOffToggle.isSelected()));

            titleActions.add(onOffToggle, BorderLayout.EAST);
        }

        titleWrapper.add(title, BorderLayout.CENTER);
        titleWrapper.add(titleActions, BorderLayout.EAST);

        add(titleWrapper, BorderLayout.NORTH);

        if (configurable)
        {
            add(breakPanel(), BorderLayout.CENTER);
            add(typePanel(), BorderLayout.SOUTH);
        }
        else
        {
            add(notConfigurable(), BorderLayout.CENTER);
        }
    }

    private JSpinner createSpinner(int value)
    {
        SpinnerModel model = new SpinnerNumberModel(value, 0, Integer.MAX_VALUE, 1);
        JSpinner spinner = new JSpinner(model);
        Component editor = spinner.getEditor();
        JFormattedTextField spinnerTextField = ((JSpinner.DefaultEditor) editor).getTextField();
        spinnerTextField.setColumns(4);
        spinnerTextField.setFormatterFactory(new UnitFormatterFactory(Units.MINUTES));

        return spinner;
    }

    private JPanel notConfigurable()
    {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(PiggyBreakHandlerPanel.BACKGROUND_COLOR);
        JMultilineLabel description = new JMultilineLabel();

        description.setText("The break timings for this plugin are not configurable.");
        description.setFont(PiggyBreakHandlerPanel.SMALL_FONT);
        description.setDisabledTextColor(Color.WHITE);
        description.setBackground(PiggyBreakHandlerPanel.BACKGROUND_COLOR);

        contentPanel.add(description, BorderLayout.CENTER);

        return contentPanel;
    }

    private JPanel breakPanel()
    {
        String pluginName = PiggyBreakHandlerPlugin.sanitizedName(plugin);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(PiggyBreakHandlerPanel.BACKGROUND_COLOR);
        contentPanel.setBorder(new CompoundBorder(
                new CompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, PiggyBreakHandlerPanel.PANEL_BACKGROUND_COLOR),
                        BorderFactory.createLineBorder(PiggyBreakHandlerPanel.BACKGROUND_COLOR)
                ), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        JSpinner thresholdFrom = createSpinner(
                parseInt(configManager.getConfiguration("piggyBreakHandler", pluginName + "-thresholdfrom"), 60)
        );

        JSpinner thresholdTo = createSpinner(
                parseInt(configManager.getConfiguration("piggyBreakHandler", pluginName + "-thresholdto"), 120)
        );

        JSpinner breakFrom = createSpinner(
                parseInt(configManager.getConfiguration("piggyBreakHandler", pluginName + "-breakfrom"), 10)
        );

        JSpinner breakTo = createSpinner(
                parseInt(configManager.getConfiguration("piggyBreakHandler", pluginName + "-breakto"), 15)
        );

        thresholdFrom.addChangeListener(e ->
                configManager.setConfiguration("piggyBreakHandler", pluginName + "-thresholdfrom", thresholdFrom.getValue()));

        thresholdTo.addChangeListener(e ->
                configManager.setConfiguration("piggyBreakHandler", pluginName + "-thresholdto", thresholdTo.getValue()));

        breakFrom.addChangeListener(e ->
                configManager.setConfiguration("piggyBreakHandler", pluginName + "-breakfrom", breakFrom.getValue()));

        breakTo.addChangeListener(e ->
                configManager.setConfiguration("piggyBreakHandler", pluginName + "-breakto", breakTo.getValue()));

        GridBagConstraints c = new GridBagConstraints();


        c.insets = new Insets(2, 0, 2, 0);
        c.fill = GridBagConstraints.CENTER;
        c.weightx = 0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        contentPanel.add(new JLabel("After running for"), c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        contentPanel.add(thresholdFrom, c);

        c.fill = GridBagConstraints.CENTER;
        c.weightx = 0.75;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 1;
        contentPanel.add(new JLabel(" - "), c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridwidth = 1;
        c.gridx = 2;
        c.gridy = 1;
        contentPanel.add(thresholdTo, c);

        c.insets = new Insets(8, 0, 2, 0);
        c.fill = GridBagConstraints.CENTER;
        c.weightx = 0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 2;
        contentPanel.add(new JLabel("Schedule a break for"), c);

        c.insets = new Insets(2, 0, 2, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 3;
        contentPanel.add(breakFrom, c);

        c.fill = GridBagConstraints.CENTER;
        c.weightx = 0.75;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 3;
        contentPanel.add(new JLabel(" - "), c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridwidth = 1;
        c.gridx = 2;
        c.gridy = 3;
        contentPanel.add(breakTo, c);

        return contentPanel;
    }

    private JPanel typePanel()
    {
        String pluginName = PiggyBreakHandlerPlugin.sanitizedName(plugin);

        JPanel contentPanel = new JPanel(new GridLayout(0, 2));
        contentPanel.setBackground(PiggyBreakHandlerPanel.BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        ButtonGroup buttonGroup = new ButtonGroup();

        JCheckBox logoutButton = new JCheckBox("Logout");
        JCheckBox afkButton = new JCheckBox("AFK");

        boolean logout = Boolean.parseBoolean(configManager.getConfiguration("piggyBreakHandler", pluginName + "-logout"));

        logoutButton.setSelected(logout);
        afkButton.setSelected(!logout);

        logoutButton.addActionListener(e ->
                configManager.setConfiguration("piggyBreakHandler", pluginName + "-logout", logoutButton.isSelected()));

        afkButton.addActionListener(e ->
                configManager.setConfiguration("piggyBreakHandler", pluginName + "-logout", !afkButton.isSelected()));

        buttonGroup.add(logoutButton);
        buttonGroup.add(afkButton);

        contentPanel.add(logoutButton);
        contentPanel.add(afkButton);

        return contentPanel;
    }

    private void setupDefaults()
    {
        String pluginName = PiggyBreakHandlerPlugin.sanitizedName(plugin);

        String enabled = configManager.getConfiguration("piggyBreakHandler", PiggyBreakHandlerPlugin.sanitizedName(plugin) + "-thresholdfrom");
        String logout = configManager.getConfiguration("piggyBreakHandler", PiggyBreakHandlerPlugin.sanitizedName(plugin) + "-logout");

        String thresholdfrom = configManager.getConfiguration("piggyBreakHandler", PiggyBreakHandlerPlugin.sanitizedName(plugin) + "-thresholdfrom");
        String thresholdto = configManager.getConfiguration("piggyBreakHandler", PiggyBreakHandlerPlugin.sanitizedName(plugin) + "-thresholdto");
        String breakfrom = configManager.getConfiguration("piggyBreakHandler", PiggyBreakHandlerPlugin.sanitizedName(plugin) + "-breakfrom");
        String breakto = configManager.getConfiguration("piggyBreakHandler", PiggyBreakHandlerPlugin.sanitizedName(plugin) + "-breakto");

        if (enabled == null)
        {
            configManager.setConfiguration("piggyBreakHandler", pluginName + "-enabled", false);
        }

        if (logout == null)
        {
            configManager.setConfiguration("piggyBreakHandler", pluginName + "-logout", true);
        }

        if (!PiggyBreakHandlerPlugin.isNumeric(thresholdfrom) || (PiggyBreakHandlerPlugin.isNumeric(thresholdfrom) && Integer.parseInt(thresholdfrom) < 0))
        {
            configManager.setConfiguration("piggyBreakHandler", pluginName + "-thresholdfrom", 60);
        }

        if (!PiggyBreakHandlerPlugin.isNumeric(thresholdto) || (PiggyBreakHandlerPlugin.isNumeric(thresholdto) && Integer.parseInt(thresholdto) < 0))
        {
            configManager.setConfiguration("piggyBreakHandler", pluginName + "-thresholdto", 120);
        }

        if (!PiggyBreakHandlerPlugin.isNumeric(breakfrom) || (PiggyBreakHandlerPlugin.isNumeric(breakfrom) && Integer.parseInt(breakfrom) < 0))
        {
            configManager.setConfiguration("piggyBreakHandler", pluginName + "-breakfrom", 10);
        }

        if (!PiggyBreakHandlerPlugin.isNumeric(breakto) || (PiggyBreakHandlerPlugin.isNumeric(breakto) && Integer.parseInt(breakto) < 0))
        {
            configManager.setConfiguration("piggyBreakHandler", pluginName + "-breakto", 15);
        }
    }

    private int parseInt(String value, int def)
    {
        try
        {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException e)
        {
            return def;
        }
    }
}
