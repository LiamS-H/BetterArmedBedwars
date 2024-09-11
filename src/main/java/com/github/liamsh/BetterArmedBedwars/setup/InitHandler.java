package com.github.liamsh.BetterArmedBedwars.setup;


import com.github.liamsh.BetterArmedBedwars.animation.ItemNbtAnimationsFix;
import com.github.liamsh.BetterArmedBedwars.gui.Hud;
import com.github.liamsh.BetterArmedBedwars.utils.TexturesLoader;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class InitHandler {
    private static boolean postPostInitSuccess = false;

    private static final Minecraft mc = Minecraft.getMinecraft();
    public static void preInit(FMLPreInitializationEvent event) {
        EventBusHandler.registerEvents();
        TexturesLoader.registerResourcePack();
    }

    public static void init(FMLInitializationEvent event) {
        ItemNbtAnimationsFix.init();
    }

    public static void postInit(FMLPostInitializationEvent event) {
        //post init
    }

    public static void postPostInit(TickEvent.ClientTickEvent event) {
        mc.ingameGUI = new Hud(mc);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (postPostInitSuccess) return;
        postPostInit(event);
        postPostInitSuccess = true;
    }
}
