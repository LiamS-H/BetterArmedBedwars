package com.github.liamsh.BetterArmedBedwars.setup;

import net.minecraftforge.common.config.Configuration;
import org.lwjgl.input.Keyboard;

import java.io.File;

public class ConfigHandler {
    public static Configuration config;
    public static int reloadKey = Keyboard.KEY_R;
    public static int fireKey = -100;
    public static int adsKey = -99;

    public static void loadConfig(File configFile) {
        config = new Configuration(configFile);
        reloadKey = config.getInt("reloadKey", "keybindings", reloadKey, -100, 255, "Button to reload when holding gun.");
        fireKey = config.getInt("fireKey", "keybindings", fireKey, -100, 255, "Button to fire when holding gun.");
        adsKey = config.getInt("adsKey", "keybindings", adsKey, -100, 255, "Button to aim down sights when holding gun.");

        if (config.hasChanged()) {
            config.save();
        }
    }

}
