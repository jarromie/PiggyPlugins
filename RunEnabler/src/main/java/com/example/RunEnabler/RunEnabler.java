package com.example.RunEnabler;

import com.example.EthanApiPlugin.PiggyApiPlugin;
import com.example.Packets.MousePackets;
import com.example.Packets.WidgetPackets;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;


@PluginDescriptor(name = "RunEnabler", description = "", enabledByDefault = false, tags = {"ethan"})
public class RunEnabler extends Plugin {
    @Subscribe
    public void onGameTick(GameTick e) {
        if (!PiggyApiPlugin.loggedIn()) {
            return;
        }
        if (runIsOff() && hasMoreThanZeroEnergy()) {
            enableRun();
        }
    }

    boolean runIsOff() {
        return PiggyApiPlugin.getClient().getVarpValue(173) == 0;
    }

    boolean hasMoreThanZeroEnergy() {
        return PiggyApiPlugin.getClient().getEnergy() > 100;
    }

    void enableRun() {
        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetActionPacket(1, 10485787, -1, -1);
    }
}
