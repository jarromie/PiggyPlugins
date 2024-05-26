package com.piggyplugins;

import com.example.EthanApiPlugin.PiggyApiPlugin;
import com.example.PacketUtils.PacketApiPlugin;
import com.example.PrayerFlicker.EthanPrayerFlickerPlugin;
import com.piggyplugins.CannonReloader.CannonReloaderPlugin;
import com.piggyplugins.strategyexample.StrategySmithPlugin;
import com.polyplugins.AutoBoner.AutoBonerPlugin;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;
import net.runelite.client.plugins.PiggyBreakHandler.PiggyBreakHandlerPlugin;
import net.runelite.client.plugins.betterprofiles.PiggyProfilesPlugin;

public class PluginTester {
    public static void main(String[] args) throws Exception {
        ExternalPluginManager.loadBuiltin(PiggyApiPlugin.class, PacketApiPlugin.class,
                // Don't remove these
        /* Add your plugins in this method when running from the IDE.
           Make sure to include them as a dependency in the build.gradle via `testImplementation` */
                StrategySmithPlugin.class, AutoBonerPlugin.class, PiggyProfilesPlugin.class, CannonReloaderPlugin.class, EthanPrayerFlickerPlugin.class);
        RuneLite.main(args);
    }
}