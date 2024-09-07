package com.github.liamsh.BetterArmedBedwars;

import com.github.liamsh.BetterArmedBedwars.animation.ItemNbtAnimationsFix;
import com.github.liamsh.BetterArmedBedwars.gui.CustomGuiIngame;
import com.github.liamsh.BetterArmedBedwars.sound.SoundEventHandler;
import com.github.liamsh.BetterArmedBedwars.utils.ServerData;
import com.github.liamsh.BetterArmedBedwars.utils.TexturesLoader;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = BetterArmedBedwars.MODID, version = BetterArmedBedwars.VERSION, name = BetterArmedBedwars.NAME)
public class BetterArmedBedwars {
    public static final String MODID = "BetterArmedBedwars";
    public static final String VERSION = "1.0";
    public static final String NAME = "Better Armed Bedwars";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

        MinecraftForge.EVENT_BUS.register(new SoundEventHandler());
        MinecraftForge.EVENT_BUS.register(new ServerData());
        new ItemNbtAnimationsFix().intialize();
    }
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.ingameGUI = new CustomGuiIngame(mc);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        TexturesLoader texturesLoader = new TexturesLoader();
        texturesLoader.preInit(event);
    }


}
