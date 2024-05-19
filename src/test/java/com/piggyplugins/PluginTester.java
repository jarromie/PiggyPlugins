package com.piggyplugins;

import com.example.EthanApiPlugin.PiggyApiPlugin;
import com.example.PacketUtils.PacketApiPlugin;
import com.piggyplugins.PiggyUtils.PiggyUtilsPlugin;
import com.piggyplugins.strategyexample.StrategySmithPlugin;
import com.polyplugins.AutoBoner.AutoBonerPlugin;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class PluginTester {
    public static void main(String[] args) throws Exception {
        ExternalPluginManager.loadBuiltin(PiggyApiPlugin.class, PacketApiPlugin.class,
                PiggyUtilsPlugin.class// Don't remove these
        /* Add your plugins in this method when running from the IDE.
           Make sure to include them as a dependency in the build.gradle via `testImplementation` */,
                StrategySmithPlugin.class, AutoBonerPlugin.class);
        RuneLite.main(args);
    }
}