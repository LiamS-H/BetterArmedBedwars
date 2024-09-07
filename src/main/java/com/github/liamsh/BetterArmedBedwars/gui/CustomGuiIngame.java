package com.github.liamsh.BetterArmedBedwars.gui;

import com.github.liamsh.BetterArmedBedwars.utils.GunFinder;
import com.github.liamsh.BetterArmedBedwars.utils.ServerData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class CustomGuiIngame extends GuiIngameForge {
    private boolean needsInit = true;

    public void init() {
        this.mc.ingameGUI = this;
        needsInit = false;
        System.out.println("CustomGuiIngame has been initialized");
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!needsInit) return;
        init();
    }

    public CustomGuiIngame(Minecraft mc) {
        super(mc);
        System.out.println("CustomGuiIngame has been initialized");
        MinecraftForge.EVENT_BUS.register(this);
    }

//    @Override
//    public void renderGameOverlay(float partialTicks) {
//        super.renderGameOverlay(partialTicks);
//    }


    @Override
    public void renderExperience(int width, int height) {
        if (ServerData.notInArmed()) {
            super.renderExperience(width, height);
            return;
        }
        if (!Minecraft.getMinecraft().playerController.gameIsSurvivalOrAdventure()) return;

        ItemStack heldStack = Minecraft.getMinecraft().thePlayer.getHeldItem();
        if (heldStack == null) return;
        Item heldItem = heldStack.getItem();
        if (heldItem == null) return;

        if (!GunFinder.isGun(heldItem)) return;

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;


        int barWidth = 182;

        int filledWidth = (int)(player.experience * (float)(barWidth + 1));
        if (filledWidth > 0) {
            int xpBarX = width / 2 - barWidth / 2;
            int xpBarY = height - 32 + 3;

            Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(xpBarX, xpBarY, 0, 64, barWidth, 5);
            Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(xpBarX, xpBarY, 0, 69, filledWidth, 5);
        }

        int hotbarX = width / 2 - barWidth / 2;
        int hotbarY = height - 22;
        int xpTextX = hotbarX + barWidth + 10;
        int xpTextY = hotbarY + 9;

        String xpText = String.valueOf(player.experienceLevel);
        xpText = xpText.length() < 2 ? " " + xpText : xpText;
        String magazineText = xpText + "/" + GunFinder.maxAmmo(heldItem) ;
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(magazineText, xpTextX, xpTextY, 0xFFFFFF);
    }
}
