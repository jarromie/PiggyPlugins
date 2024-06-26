package com.piggyplugins.strategyexample.tasks;

import com.example.EthanApiPlugin.Collections.Bank;
import com.example.EthanApiPlugin.Collections.Inventory;
import com.example.EthanApiPlugin.PiggyApiPlugin;
import com.example.PacketUtils.WidgetInfoExtended;
import com.example.Packets.MousePackets;
import com.example.Packets.WidgetPackets;
import com.piggyplugins.PiggyUtils.strategy.AbstractTask;
import com.piggyplugins.strategyexample.StrategySmithConfig;
import com.piggyplugins.strategyexample.StrategySmithPlugin;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.widgets.Widget;

@Slf4j
public class Banking extends AbstractTask<StrategySmithPlugin, StrategySmithConfig> {
//    @Inject
//    protected Client client;
//    @Inject
//    protected ClientThread clientThread;

    public Banking(StrategySmithPlugin plugin, StrategySmithConfig config) {
        super(plugin, config);
    }

    @Override
    public boolean validate() {
        return Bank.isOpen() && (!plugin.hasEnoughBars() || plugin.hasBarsButNotEnough() || !plugin.hasHammer());
    }

    @Override
    public void execute() {
        log.info("Do Banking");
        bankHandler();
    }

    private void bankHandler() {
        Widget depositInventory = plugin.getClient().getWidget(WidgetInfoExtended.BANK_DEPOSIT_INVENTORY.getPackedId());
        if (depositInventory != null) {
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetAction(depositInventory, "Deposit inventory");
        }

        Bank.search().withName("Hammer").first().ifPresentOrElse(hammer -> {
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetAction(hammer, "Withdraw-1");
        }, () -> {
            if (Inventory.getItemAmount("Hammer") > 0) return;
            PiggyApiPlugin.sendClientMessage("No hammer in bank or inventory");
            PiggyApiPlugin.stopPlugin(plugin);
        });
        Bank.search().withName(config.bar().getName()).first().ifPresentOrElse(bar -> {
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetAction(bar, "Withdraw-All");
        }, () -> {
            PiggyApiPlugin.sendClientMessage("No bars left");
            PiggyApiPlugin.stopPlugin(plugin);
        });
        plugin.timeout = config.tickDelay();
    }
}
