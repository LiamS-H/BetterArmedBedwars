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
    public static boolean isHoldingNonGun() {return gun == null;}

    public static Item getHeldItem() {
        if (mc == null) return null;
        if (mc.thePlayer == null) return null;
        ItemStack curStack = mc.thePlayer.getHeldItem();
        if (curStack == null) return null;
        return curStack.getItem();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (mc.thePlayer == null) return;
        if (ServerData.notInArmed()) return;
        gun = GunUtil.whichGun(getHeldItem());
        maxAmmo = GunUtil.maxAmmo(gun);
        ammo = mc.thePlayer.experienceLevel;

        //        if (mc.thePlayer.experience)


        if (cooldown > 0) cooldown -= 1;
    }

}
