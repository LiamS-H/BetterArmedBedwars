package com.github.liamsh.BetterArmedBedwars.animation;

import com.github.liamsh.BetterArmedBedwars.utils.ServerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Field;

import static com.github.liamsh.BetterArmedBedwars.utils.GunFinder.isGun;

//        field_110931_c
//        Type: net.minecraft.util.ResourceLocation
//
//        field_110929_d
//        Type: net.minecraft.util.ResourceLocation
//
//        field_78455_a
//        Type: net.minecraft.client.Minecraft

//        field_78453_b
//        Type: net.minecraft.item.ItemStack

//        field_78454_c
//        Type: float - equipped progress

//        field_78451_d
//        Type: float - prev equipped progress

//        field_178111_g
//        Type: net.minecraft.client.renderer.entity.RenderManager
//
//        field_178112_h
//        Type: net.minecraft.client.renderer.entity.RenderItem
//
//        field_78450_g
//        Type: int - slot

public class ItemNbtAnimationsFix {
    private ItemStack lastStack = null;
    private int lastDamage = -1;
    private int lastSlot = -1;
    private int disableTime = 0;
    private static Field equippedProgressField;
    private static Field itemToRenderField;
    private static Field prevEquippedProgressField;




    @SuppressWarnings("JavaReflectionMemberAccess")
    public static void init() {
        try {
            equippedProgressField = ItemRenderer.class.getDeclaredField("field_78454_c");
            equippedProgressField.setAccessible(true);
            itemToRenderField = ItemRenderer.class.getDeclaredField("field_78453_b");
            itemToRenderField.setAccessible(true);
            prevEquippedProgressField = ItemRenderer.class.getDeclaredField("field_78451_d");
            prevEquippedProgressField.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleEquipAnimation() {
        if (ServerData.notInArmed()) return;

        Minecraft mc = Minecraft.getMinecraft();
        ItemRenderer itemRenderer = mc.entityRenderer.itemRenderer;
        EntityPlayerSP player = mc.thePlayer;

        if (itemRenderer == null) {
            return;
        }
        try {
            ItemStack curStack = player.getHeldItem();
            int curSlot = player.inventory.currentItem;
            int prevDamage = lastDamage;
            lastDamage = -1;
            int tempDisableTime = disableTime;
            disableTime = -10;

            ItemStack prevStack = lastStack;
            lastStack = curStack;

            int prevSlot = lastSlot;
            lastSlot = curSlot;

            if (curStack == null) {
                return;
            }
            if (prevStack == null) {
                return;
            }

            Item heldItem = curStack.getItem();
            Item prevHeldItem = prevStack.getItem();

            if (heldItem == null) {
                return;
            }
            if (prevHeldItem == null) {
                return;
            }
            if (!isGun(heldItem)) {
                return;
            }

            if (prevSlot != curSlot) {
                return;
            }
            if (prevHeldItem != heldItem) {
                return;
            }

            disableTime = tempDisableTime;

            if (disableTime < 0) {
                return;
            }


            int curDamage = curStack.getItemDamage();
            lastDamage = curDamage;

            if (prevDamage == -1) {
                return;
            }


            if (curDamage == prevDamage && disableTime == 0) {
                return;
            }

            int var = 10;

            if (curDamage != prevDamage) {disableTime = var;}

//            float currentProgress = equippedProgressField.getFloat(itemRenderer);
            equippedProgressField.setFloat(itemRenderer, 1.0F);

            itemToRenderField.set(itemRenderer, curStack);

            prevEquippedProgressField.setFloat(itemRenderer, 1.0F);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if(disableTime >0) disableTime--;
        else if(disableTime<0) disableTime++;
    }

    @SubscribeEvent
    public void onRenderHand(RenderHandEvent event) {
        handleEquipAnimation();
    }

    @SubscribeEvent
    public void onEntityJoinWorldEvent(EntityJoinWorldEvent event) {
        if (event.entity == null) return;
        if (event.entity != Minecraft.getMinecraft().thePlayer) return;

        lastStack = null;
        lastDamage = -1;
        lastSlot = -1;
        disableTime = 0;
    }

}
