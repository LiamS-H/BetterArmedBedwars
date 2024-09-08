package com.github.liamsh.BetterArmedBedwars.sound;

import com.github.liamsh.BetterArmedBedwars.utils.GunFinder;
import com.github.liamsh.BetterArmedBedwars.utils.PlayerProximity;
import com.github.liamsh.BetterArmedBedwars.utils.ServerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Constructor;

public class SoundEventHandler {
    private ItemStack lastGun = null;
    public static void playSound(String resourceName, float x, float y, float z) {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(
                new ResourceLocation(resourceName),
                x,
                y,
                z
        ));
    }
    public static void playSound(String resourceName) {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(
                new ResourceLocation(resourceName)
        ));
    }

    public static void playSound(String resourceName, float volume, float x, float y, float z) {
        Minecraft.getMinecraft().getSoundHandler().playSound(new PositionedSoundRecord(
                new ResourceLocation(resourceName),
                volume,
                1.0f,
                x,
                y,
                z
        ));
    }

    public static void relocateSound(String resourceName, ISound sound) {
        try {
            Constructor<PositionedSoundRecord> constructor = PositionedSoundRecord.class.getDeclaredConstructor(
                    ResourceLocation.class,
                    float.class,
                    float.class,
                    boolean.class,
                    int.class,
                    ISound.AttenuationType.class,
                    float.class,
                    float.class,
                    float.class
            );
            constructor.setAccessible(true);

            ISound newSound = constructor.newInstance(
                    new ResourceLocation(resourceName),
                    sound.getVolume(),
                    sound.getPitch(),
                    sound.canRepeat(),
                    sound.getRepeatDelay(),
                    sound.getAttenuationType(),
                    sound.getXPosF(),
                    sound.getYPosF(),
                    sound.getZPosF()
            );
            Minecraft.getMinecraft().getSoundHandler().playSound(newSound);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean isBlockChord(float value) {
        float decimalPart =  Math.abs(value) - Math.abs((int) value);
        return Float.compare(decimalPart, 0.5f) == 0;
    }

    public boolean handleSound(SoundEvent.SoundSourceEvent event) {
        ISound sound = event.sound;
        String name = event.name;
        if (ServerData.notInArmed()) {
            switch (name) {
                case "mob.irongolem.hit":
                case "fireworks.blast":
                case "fireworks.blast_far":
                case "fireworks.largeBlast":
                case "random.explode":
                case "fire.fire":
                case "mob.horse.gallop":
                case "random.successful_hit":
                case "tile.piston.in":
                case "random.orb":
                    return false;
                default: return true;
            }
        }

        float pitch = sound.getPitch();
        float vol = sound.getVolume();
        float x = sound.getXPosF();
        float y = sound.getYPosF();
        float z = sound.getZPosF();

        switch (name) {
            case "mob.irongolem.hit":
                if (pitch < 2.49f) return false;

                EntityPlayer closestPlayer = PlayerProximity.getClosestPlayer(Minecraft.getMinecraft().theWorld,x,y,z);
                if (closestPlayer == Minecraft.getMinecraft().thePlayer) {
                    if (lastGun.getItem().getRegistryName().equals("minecraft:wooden_hoe")) {
                        playSound("guns.pistol.shot", vol, x,y,z);
                        return true;
                    }
                    playSound("guns.magnum.shot", vol, x,y,z);
                    return true;
                }
                ItemStack itemStack = closestPlayer.getHeldItem();
                if (itemStack == null) {
                    playSound("guns.pistol.shot", vol, x,y,z);
                    return true;
                }
                Item item = itemStack.getItem();
                if (item == null) {
                    playSound("guns.pistol.shot", vol, x,y,z);
                    return true;
                }
                if (item.getRegistryName().equals("minecraft:wooden_hoe")) {
                    playSound("guns.pistol.shot", vol, x,y,z);
                    return true;
                }
                playSound("guns.magnum.shot", vol, x,y,z);
                return true;

            case "fireworks.blast":
                if (pitch < 2.49f) return false;
                playSound("guns.smg.shot", vol, x,y,z);
                return true;
            case "fireworks.blast_far":
                if (pitch < 2.49f) return false;
                playSound("guns.smg.shot", 0.5f, x,y,z);
                return true;
            case "fireworks.largeBlast":
                if (pitch < 2.49f) return false;
                playSound("guns.rifle.shot", vol, x,y,z);
                return true;
            case "random.explode":
                if (pitch < 2.49f) return false;
                playSound("guns.shotgun.shot", vol, x,y,z);
                return true;
            case "fire.fire":
                if (isBlockChord(x) && isBlockChord(y) && isBlockChord(z)) return false;
                playSound("guns.flamethrower.shot", vol, x,y,z);
                return true;
            case "mob.horse.gallop":
                switch(lastGun.getItem().getRegistryName()) {
                    case "minecraft:wooden_hoe":
                        playSound("guns.pistol.reload");
                        break;
                    case "minecraft:golden_hoe":
                        playSound("guns.magnum.reload");
                        break;
                    case "minecraft:stone_hoe":
                        playSound("guns.rifle.reload");
                        break;
                    case "minecraft:iron_hoe":
                        playSound("guns.shotgun.reload");
                        break;
                    case "minecraft:diamond_hoe":
                        playSound("guns.smg.reload");
                        break;
                    case "minecraft:flint_and_steel":
                        playSound("guns.flamethrower.reload");
                        break;
                }

                return true;
            case "random.successful_hit":
                if (pitch != 2.0f && pitch != 1.4920635f) return false;
                playSound("bullet.hit", vol, x,y,z);
                return true;
            case "tile.piston.in":
                if (pitch < 2.0f) return false;
                playSound("bullet.whizz", vol, x,y,z);
                return true;
            case "random.orb":
                EntityPlayer player = Minecraft.getMinecraft().thePlayer;
                if (player.deathTime > 0) return false;
                if (player.getHealth() <= 0) return false;
                playSound("bullet.kill");
                return true;
            default:
                return true;
        }
    }

    @SubscribeEvent
    public void onSoundPlaying(SoundEvent.SoundSourceEvent event) {
//        System.out.println(event.sound.getXPosF() + ", " + event.sound.getYPosF() + ", " + event.sound.getZPosF()+ " p:" + event.sound.getPitch() + " " + event.name);
        boolean soundPlayed = handleSound(event);

        if (soundPlayed) return;

        relocateSound("default."+event.name, event.sound);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player == null) return;
        ItemStack currentHeldItem = player.getHeldItem();
        if (currentHeldItem == null) return;
        if (GunFinder.isGun(currentHeldItem.getItem())) {
            lastGun = currentHeldItem;
        }
    }
}
