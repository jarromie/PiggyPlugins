package com.piggyplugins.strategyexample.tasks;

import com.example.EthanApiPlugin.Collections.Bank;
import com.example.EthanApiPlugin.Collections.NPCs;
import com.example.EthanApiPlugin.Collections.TileObjects;
import com.example.EthanApiPlugin.EthansApiPlugin;
import com.piggyplugins.PiggyUtils.strategy.AbstractTask;
import com.piggyplugins.strategyexample.StrategySmithConfig;
import com.piggyplugins.strategyexample.StrategySmithPlugin;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.NPC;
import net.runelite.api.TileObject;

import java.util.Optional;

@Slf4j
public class OpenBank extends AbstractTask<StrategySmithPlugin, StrategySmithConfig> {

    public OpenBank(StrategySmithPlugin plugin, StrategySmithConfig config) {
        super(plugin, config);
    }

    @Override
    public boolean validate() {
        return !Bank.isOpen() && (!plugin.hasEnoughBars() || plugin.hasBarsButNotEnough() || !plugin.hasHammer());
    }

    @Override
    public void execute() {
        log.info("Open Bank");
        findBank();
    }

    private void findBank() {
        Optional<NPC> banker = NPCs.search().withAction("Bank").withId(2897).nearestToPlayer();
        Optional<TileObject> bank = TileObjects.search().withAction("Bank").nearestToPlayer();
        if (!Bank.isOpen()) {
            if (banker.isPresent()) {
//                NPCInteraction.interact(banker.get(), "Bank");
                interactNpc(banker.get(), "Bank");
                plugin.timeout = config.tickDelay() == 0 ? 1 : config.tickDelay();
            } else if (bank.isPresent()) {
//                TileObjectInteraction.interact(bank.get(), "Bank");
                interactObject(bank.get(), "Bank");
                plugin.timeout = config.tickDelay() == 0 ? 1 : config.tickDelay();
            } else {
                EthansApiPlugin.sendClientMessage("Couldn't find bank or banker");
                EthansApiPlugin.stopPlugin(plugin);
            }
        }
    }
}
