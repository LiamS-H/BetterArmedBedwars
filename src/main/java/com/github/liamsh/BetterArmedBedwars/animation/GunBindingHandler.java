package com.github.liamsh.BetterArmedBedwars.animation;

import com.github.liamsh.BetterArmedBedwars.setup.ConfigHandler;
import com.github.liamsh.BetterArmedBedwars.utils.ServerData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class GunBindingHandler {
//    private static KeyBinding reloadBind;
//    private static KeyBinding fireBind;
//    private static KeyBinding adsBind;
    public static boolean reloadKeyDown = false;
    public static boolean fireKeyDown = false;
    public static boolean adsKeyDown = false;
    private static final Minecraft mc = Minecraft.getMinecraft();

    private static long lastFired = 0;




    public static void preInit(FMLPreInitializationEvent event) {
//        reloadBind = new KeyBinding("Reload Gun", ConfigHandler.reloadKey, "key.categories.Armed_Bedwars");
//        fireBind = new KeyBinding("Fire Gun", ConfigHandler.fireKey, "key.categories.Armed_Bedwars");
//        adsBind = new KeyBinding("ADS Gun", ConfigHandler.adsKey, "key.categories.Armed_Bedwars");
//        ClientRegistry.registerKeyBinding(reloadBind);
//        ClientRegistry.registerKeyBinding(fireBind);
//        ClientRegistry.registerKeyBinding(adsBind);
    }

    public static void init() {

    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (ServerData.notInArmed()) return;
        if (StateHandler.isHoldingNonGun()) return;

        processKeys();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        EntityPlayer player = mc.thePlayer;
        if (player == null) return;
        if (fireKeyDown) {
            if (player.experience * 17 < 16) return;
            if (System.currentTimeMillis() - lastFired < 100) return;
            fire();
        }
    }

    @SubscribeEvent
    public void onMouseEvent(MouseEvent event) {
        if (ServerData.notInArmed()) return;
        if (StateHandler.isHoldingNonGun()) return;
        if (ConfigHandler.fireKey > -98 && ConfigHandler.reloadKey > -98) return;
        // 0 left, 1 right, 2 middle : event.button
        // -100 left, -99 right, -98 middle : config
        if (!processMouse(event)) return;
        if (!event.isCancelable()) return;
        event.setCanceled(true);
    }
    private boolean processMouse(MouseEvent event) {
        int button = event.button -100;
        if (button == ConfigHandler.fireKey) {
            if (!fireKeyDown && event.buttonstate) {
                fire();
                fireKeyDown = true;
            } else if (fireKeyDown && !event.buttonstate) {
                fireKeyDown = false;
            }
            return true;
        }

        if (button == ConfigHandler.reloadKey) {
            if (!reloadKeyDown && event.buttonstate) {
                reload();
                reloadKeyDown = true;
            } else if (reloadKeyDown && !event.buttonstate) {
                reloadKeyDown = false;
            }
            return true;
        }
        if (button == ConfigHandler.adsKey) {
            if (!adsKeyDown && event.buttonstate) {
                adsKeyDown = true;
            } else if (adsKeyDown && !event.buttonstate) {
                adsKeyDown = false;
            }
            return true;
        }
        return false;

    }

    private void processKeys() {

        if (ConfigHandler.reloadKey > 0 && ConfigHandler.reloadKey <= Keyboard.KEYBOARD_SIZE) {
            if (!reloadKeyDown && Keyboard.isKeyDown(ConfigHandler.reloadKey)) {
                reload();
                reloadKeyDown = true;
                adsKeyDown = false;
            }
            if (reloadKeyDown && !Keyboard.isKeyDown(ConfigHandler.reloadKey)) {
                reloadKeyDown = false;
            }
        }
        if (ConfigHandler.fireKey > 0 && ConfigHandler.fireKey <= Keyboard.KEYBOARD_SIZE) {
            if (!fireKeyDown && Keyboard.isKeyDown(ConfigHandler.fireKey)) {
                fire();
                fireKeyDown = true;
            }
            if (fireKeyDown && !Keyboard.isKeyDown(ConfigHandler.fireKey)) {
                fireKeyDown = false;
            }
        }
        if (ConfigHandler.adsKey > 0 && ConfigHandler.adsKey <= Keyboard.KEYBOARD_SIZE) {
            if (!adsKeyDown && Keyboard.isKeyDown(ConfigHandler.adsKey)) {
                adsKeyDown = true;
            }
            if (adsKeyDown && !Keyboard.isKeyDown(ConfigHandler.adsKey)) {
                adsKeyDown = false;
            }
        }
    }


    private void reload() {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player == null) return;
        player.swingItem();
    }
    private void fire() {
        EntityPlayer player = mc.thePlayer;
        if (player == null) return;
        lastFired = System.currentTimeMillis();
        mc.playerController.sendUseItem(player, mc.theWorld, player.getHeldItem());
    }

}
