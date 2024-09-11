package com.github.liamsh.BetterArmedBedwars.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;



public class StateHandler {
    private static guns gun;
    private static gunState gunState;
    private static int maxAmmo;
    private static int ammo;
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static int cooldown = 0;

    public static guns getCurrentGun() {
        return gun;
    }

    public static gunState getCurrentGunState() {
        return gunState;
    }

    public static int getCurrentMaxAmmo() {
        return maxAmmo;
    }

    public static int getCurrentAmmo() {
        return ammo;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        try {
            ItemStack curStack = mc.thePlayer.getHeldItem();
            Item curItem = curStack.getItem();
            gun = GunUtil.whichGun(curItem);
            maxAmmo = GunUtil.maxAmmo(gun);
            ammo = mc.thePlayer.experienceLevel;

            //        if (mc.thePlayer.experience)


            if (cooldown > 0) cooldown -= 1;
        } catch (NullPointerException e) {
            return;
        }
    }

}
