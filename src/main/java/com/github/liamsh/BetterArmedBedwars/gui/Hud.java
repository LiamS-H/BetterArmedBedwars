package com.github.liamsh.BetterArmedBedwars.gui;

import com.github.liamsh.BetterArmedBedwars.utils.GunUtil;
import com.github.liamsh.BetterArmedBedwars.utils.ServerData;
import com.github.liamsh.BetterArmedBedwars.utils.StateHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.*;
import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.ARMOR;

public class Hud extends GuiIngameForge {
    private RenderGameOverlayEvent eventParent;
    public ResourceLocation gun_cooldown = new ResourceLocation("textures/gui/gun_cooldown.png");

    public Hud(Minecraft mc) {
        super(mc);
        System.out.println("Hud has been initialized");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void renderGameOverlay(float partialTicks) {
        ScaledResolution res = new ScaledResolution(mc);
        eventParent = new RenderGameOverlayEvent(partialTicks, res);
        super.renderGameOverlay(partialTicks);
    }


    @Override
    public void renderExperience(int width, int height) {
        if (ServerData.notInArmed()) {
            super.renderExperience(width, height);
            return;
        }
        if (pre(EXPERIENCE)) return;
//        bind(icons);

        if (!Minecraft.getMinecraft().playerController.gameIsSurvivalOrAdventure()) return;

        ItemStack heldStack = Minecraft.getMinecraft().thePlayer.getHeldItem();
        if (heldStack == null) return;
        Item heldItem = heldStack.getItem();
        if (heldItem == null) return;

        if (!GunUtil.isGun(heldItem)) return;

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;


        int barWidth = 182;

//        int filledWidth = (int)(player.experience * (float)(barWidth + 1));
//        if (filledWidth > 0) {
//            mc.mcProfiler.startSection("expBar");
//            int xpBarX = width / 2 - barWidth / 2;
//            int xpBarY = height - 32 + 3;
//
//            Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(xpBarX, xpBarY, 0, 64, barWidth, 5);
//            Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(xpBarX, xpBarY, 0, 69, filledWidth, 5);
//            mc.mcProfiler.endSection();
//        }


        int hotbarX = width / 2 - barWidth / 2;
        int hotbarY = height - 22;
        int xpTextX = hotbarX + barWidth + 10;
        int xpTextY = hotbarY + 9;

        mc.mcProfiler.startSection("expLevel");
        String ammoText = String.valueOf(StateHandler.getCurrentAmmo());
        ammoText = ammoText.length() < 2 ? " " + ammoText : ammoText;
        String magazineText = ammoText + "/" + StateHandler.getCurrentMaxAmmo();
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(magazineText, xpTextX, xpTextY, 0xFFFFFF);
        mc.mcProfiler.endSection();
        post(EXPERIENCE);

        float threshold = (player.experienceLevel <= 15 ? 1/((float)player.experienceLevel*2+6) : 1/((float)player.experienceLevel*5-39));

        if (player.experience + threshold > 1.0f) return;
        GlStateManager.pushMatrix();

        int cooldown_progress = (int)(player.experience * 17.0);
        bind(gun_cooldown);
//        bind(new ResourceLocation("textures/gui/test2.png"));

        GlStateManager.enableBlend();

        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

//        GlStateManager.tryBlendFuncSeparate(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR, 1, 0);
//        GlStateManager.enableAlpha();
//        drawTexturedModalRect(width / 2 - 7 + cooldown_progress, height / 2 + 9, cooldown_progress, 0, 16-cooldown_progress, 16);
//        drawTexturedModalRect(width / 2 - 7, height / 2 + 9, 16, 0, cooldown_progress, 16);
//        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        drawTexturedModalRect(width / 2 - 7 + cooldown_progress, height -33, cooldown_progress, 0, 16-cooldown_progress, 16);
        drawTexturedModalRect(width / 2 - 7, height -33, 16, 0, cooldown_progress, 16);
        GlStateManager.disableBlend();

        GlStateManager.popMatrix();
    }

    @Override
    public void renderFood(int width, int height) {
        if (ServerData.notInArmed()) {super.renderFood(width, height);}
    }
    @Override
    public void renderHealth(int width, int height)
    {
        if (ServerData.notInArmed()) {
            super.renderHealth(width, height);
            return;
        }

        bind(icons);
        if (pre(HEALTH)) return;
        mc.mcProfiler.startSection("health");
        GlStateManager.enableBlend();

        EntityPlayer player = (EntityPlayer)this.mc.getRenderViewEntity();
        int health = MathHelper.ceiling_float_int(player.getHealth());
        boolean highlight = healthUpdateCounter > (long)updateCounter && (healthUpdateCounter - (long)updateCounter) / 3L %2L == 1L;

        if (health < this.playerHealth && player.hurtResistantTime > 0)
        {
            this.lastSystemTime = Minecraft.getSystemTime();
            this.healthUpdateCounter = (this.updateCounter + 20);
        }
        else if (health > this.playerHealth && player.hurtResistantTime > 0)
        {
            this.lastSystemTime = Minecraft.getSystemTime();
            this.healthUpdateCounter = (this.updateCounter + 10);
        }

        if (Minecraft.getSystemTime() - this.lastSystemTime > 1000L)
        {
            this.playerHealth = health;
            this.lastPlayerHealth = health;
            this.lastSystemTime = Minecraft.getSystemTime();
        }

        this.playerHealth = health;
        int healthLast = this.lastPlayerHealth;

        IAttributeInstance attrMaxHealth = player.getEntityAttribute(SharedMonsterAttributes.maxHealth);
        float healthMax = (float)attrMaxHealth.getAttributeValue();
        float absorb = player.getAbsorptionAmount();

        int healthRows = MathHelper.ceiling_float_int((healthMax + absorb) / 2.0F / 10.0F);
        int rowHeight = Math.max(10 - (healthRows - 2), 3);

        this.rand.setSeed( ((long)updateCounter * 312871));

        int left = width / 2 - 91;
        int top = height - left_height + 7;
        left_height += (healthRows * rowHeight);
        if (rowHeight != 10) left_height += 10 - rowHeight;

        int regen = -1;
        if (player.isPotionActive(Potion.regeneration))
        {
            regen = updateCounter % 25;
        }

        final int TOP =  9 * (mc.theWorld.getWorldInfo().isHardcoreModeEnabled() ? 5 : 0);
        final int BACKGROUND = (highlight ? 25 : 16);
        int MARGIN = 16;
        if (player.isPotionActive(Potion.poison))      MARGIN += 36;
        else if (player.isPotionActive(Potion.wither)) MARGIN += 72;
        float absorbRemaining = absorb;

        for (int i = MathHelper.ceiling_float_int((healthMax + absorb) / 2.0F) - 1; i >= 0; --i)
        {
            //int b0 = (highlight ? 1 : 0);
            int row = MathHelper.ceiling_float_int((float)(i + 1) / 10.0F) - 1;
            int x = left + i % 10 * 8;
            int y = top - row * rowHeight;

            if (health <= 4) y += rand.nextInt(2);
            if (i == regen) y -= 2;

            drawTexturedModalRect(x, y, BACKGROUND, TOP, 9, 9);

            if (highlight)
            {
                if (i * 2 + 1 < healthLast)
                    drawTexturedModalRect(x, y, MARGIN + 54, TOP, 9, 9); //6
                else if (i * 2 + 1 == healthLast)
                    drawTexturedModalRect(x, y, MARGIN + 63, TOP, 9, 9); //7
            }

            if (absorbRemaining > 0.0F)
            {
                if (absorbRemaining == absorb && absorb % 2.0F == 1.0F)
                    drawTexturedModalRect(x, y, MARGIN + 153, TOP, 9, 9); //17
                else
                    drawTexturedModalRect(x, y, MARGIN + 144, TOP, 9, 9); //16
                absorbRemaining -= 2.0F;
            }
            else
            {
                if (i * 2 + 1 < health)
                    drawTexturedModalRect(x, y, MARGIN + 36, TOP, 9, 9); //4
                else if (i * 2 + 1 == health)
                    drawTexturedModalRect(x, y, MARGIN + 45, TOP, 9, 9); //5
            }
        }

        GlStateManager.disableBlend();
        mc.mcProfiler.endSection();
        post(HEALTH);
    }
    protected void renderArmor(int width, int height)
    {
        if (ServerData.notInArmed()) {
            super.renderArmor(width, height);
            return;
        }
        if (pre(ARMOR)) return;
        mc.mcProfiler.startSection("armor");

        GlStateManager.enableBlend();
        int left = width / 2 + 91 - 80;
        int top = height - 33;

        int level = ForgeHooks.getTotalArmorValue(mc.thePlayer);
        for (int i = 1; level > 0 && i < 20; i += 2)
        {
            if (i < level)
            {
                drawTexturedModalRect(left, top, 34, 9, 9, 9);
            }
            else if (i == level)
            {
                drawTexturedModalRect(left, top, 25, 9, 9, 9);
            }
            else //if (i > level)
            {
                drawTexturedModalRect(left, top, 16, 9, 9, 9);
            }
            left += 8;
        }
        left_height += 10;

        GlStateManager.disableBlend();
        mc.mcProfiler.endSection();
        post(ARMOR);
    }
    private boolean pre(RenderGameOverlayEvent.ElementType type)
    {
        return MinecraftForge.EVENT_BUS.post(new RenderGameOverlayEvent.Pre(eventParent, type));
    }
    private void post(RenderGameOverlayEvent.ElementType type)
    {
        MinecraftForge.EVENT_BUS.post(new RenderGameOverlayEvent.Post(eventParent, type));
    }
    private void bind(ResourceLocation res)
    {
        mc.getTextureManager().bindTexture(res);
    }
}
