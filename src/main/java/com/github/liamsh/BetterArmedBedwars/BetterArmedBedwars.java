package com.github.liamsh.BetterArmedBedwars;

import com.github.liamsh.BetterArmedBedwars.animation.ItemNbtAnimationsFix;
import com.github.liamsh.BetterArmedBedwars.gui.CustomGuiIngame;
import com.github.liamsh.BetterArmedBedwars.setup.InitHandler;
import com.github.liamsh.BetterArmedBedwars.utils.TexturesLoader;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = BetterArmedBedwars.MODID, version = BetterArmedBedwars.VERSION, name = BetterArmedBedwars.NAME)
public class BetterArmedBedwars {
    public static final String MODID = "BetterArmedBedwars";
    public static final String VERSION = "1.0";
    public static final String NAME = "Better Armed Bedwars";
    public static final boolean DEBUG = true;
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        InitHandler.preInit(event);
    }
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        InitHandler.init(event);
    }
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        InitHandler.postInit(event);
    }
}
