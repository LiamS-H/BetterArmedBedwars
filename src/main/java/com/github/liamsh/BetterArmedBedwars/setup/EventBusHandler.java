package com.github.liamsh.BetterArmedBedwars.setup;

import com.github.liamsh.BetterArmedBedwars.animation.ItemNbtAnimationsFix;
import com.github.liamsh.BetterArmedBedwars.sound.SoundEventHandler;
import com.github.liamsh.BetterArmedBedwars.utils.ServerData;
import net.minecraftforge.common.MinecraftForge;

public class EventBusHandler {
    public static void registerEvents() {
        MinecraftForge.EVENT_BUS.register(new SoundEventHandler());
        MinecraftForge.EVENT_BUS.register(new ServerData());
        MinecraftForge.EVENT_BUS.register(new ItemNbtAnimationsFix());
        MinecraftForge.EVENT_BUS.register(new InitHandler());
    }
}
