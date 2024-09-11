package com.github.liamsh.BetterArmedBedwars.utils;

import net.minecraft.item.Item;

enum gunState {
    RELOADING,
    FIRING,
    SHOOTING,
}

enum guns {
    PISTOL,
    MAGNUM,
    SMG,
    RIFLE,
    SHOTGUN,
    FLAMETHROWER,
}

public class GunUtil {
    public static boolean isGun(Item item) {
        if (item == null) return false;
        return item.getRegistryName().equals("minecraft:wooden_hoe")||
                item.getRegistryName().equals("minecraft:stone_hoe") ||
                item.getRegistryName().equals("minecraft:iron_hoe") ||
                item.getRegistryName().equals("minecraft:golden_hoe") ||
                item.getRegistryName().equals("minecraft:diamond_hoe") ||
                item.getRegistryName().equals("minecraft:flint_and_steel");
    }
    public static guns whichGun(Item item) {
        switch (item.getRegistryName()) {
            case "minecraft:wooden_hoe":
                return guns.PISTOL;
            case "minecraft:stone_hoe":
                return guns.RIFLE;
            case "minecraft:iron_hoe":
                return guns.SHOTGUN;
            case "minecraft:golden_hoe":
                return guns.MAGNUM;
            case "minecraft:diamond_hoe":
                return guns.SMG;
            case "minecraft:flint_and_steel":
                return guns.FLAMETHROWER;
            default:
                return null;
        }
    }
    public static int maxAmmo(guns gun) {
        switch (gun) {
            case PISTOL:
                return 12;
            case RIFLE:
                return 25;
            case SHOTGUN:
                return 4;
            case MAGNUM:
                return 6;
            case SMG:
                return 45;
            case FLAMETHROWER:
                return 50;
            default:
                return -1;
        }
    }

    public static int maxAmmo(Item item) {
        guns gun = whichGun(item);
        if (gun == null) return -1;
        return maxAmmo(gun);
    }
}
