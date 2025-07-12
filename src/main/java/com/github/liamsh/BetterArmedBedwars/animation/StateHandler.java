package com.github.liamsh.BetterArmedBedwars.animation;

import com.github.liamsh.BetterArmedBedwars.utils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;



public class StateHandler {
    private static GunUtil.guns gun;
    private static StateHandler.gunState gunState;
    private static int maxAmmo;
    private static int ammo;
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static int cooldown = 0;

    private static float fovSetting;
    public static float zoom;
    private static long zoomTimer = -1;

    public static GunUtil.guns getCurrentGun() {
        return gun;
    }

    public static StateHandler.gunState getCurrentGunState() {
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

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        long curTime = System.nanoTime();

        if (!GunBindingHandler.adsKeyDown) {
            if (zoom == 0) {
                zoomTimer = -1;
                fovSetting = mc.gameSettings.fovSetting;
            }

            if (zoom > 0) {
                if (zoomTimer == -1) zoomTimer = curTime;
                float deltaTime = (curTime - zoomTimer) / 125_000_000F;
                zoom = Math.max(0, zoom - deltaTime);
                zoomTimer = curTime;
            }
        }

        if (GunBindingHandler.adsKeyDown && zoom < 1) {
            if (zoomTimer == -1) zoomTimer = curTime;
            float deltaTime = (curTime - zoomTimer) / 250_500_000F;
            zoom = Math.min(1, zoom + deltaTime + 0.025F);
            zoomTimer = curTime;

            if (zoom == 1) zoomTimer = -1;
        }

        if (zoom > 0) {
            mc.gameSettings.fovSetting = Math.max(fovSetting - (50 * Curves.sigmoid(zoom * 10 - 5)), 1.0F);
        } else {
            mc.gameSettings.fovSetting = fovSetting;
        }
    }

    public enum gunState {
        RELOADING,
        FIRING,
        SHOOTING,
    }
}
