package com.github.liamsh.BetterArmedBedwars.utils;

import net.minecraft.item.Item;

public class GunFinder {
    public static boolean isGun(Item item) {
        if (item == null) return false;
        return item.getRegistryName().equals("minecraft:wooden_hoe")||
                item.getRegistryName().equals("minecraft:stone_hoe") ||
                item.getRegistryName().equals("minecraft:iron_hoe") ||
                item.getRegistryName().equals("minecraft:golden_hoe") ||
                item.getRegistryName().equals("minecraft:diamond_hoe") ||
                item.getRegistryName().equals("minecraft:flint_and_steel");
    }
    public static String whichGun(Item item) {
        switch (item.getRegistryName()) {
            case "minecraft:wooden_hoe":
                return "Pistol";
            case "minecraft:stone_hoe":
                return "Rifle";
            case "minecraft:iron_hoe":
                return "Shotgun";
            case "minecraft:golden_hoe":
                return "Magnum";
            case "minecraft:diamond_hoe":
                return "Smg";
            case "minecraft:flint_and_steel":
                return "Flamethrower";
            default:
                return null;
        }
    }
    public static int maxAmmo(Item item) {
        switch (item.getRegistryName()) {
            case "minecraft:wooden_hoe":
                return 12;
            case "minecraft:stone_hoe":
                return 25;
            case "minecraft:iron_hoe":
                return 4;
            case "minecraft:golden_hoe":
                return 6;
            case "minecraft:diamond_hoe":
                return 45;
            case "minecraft:flint_and_steel":
                return 50;
            default:
                return -1;
        }
    }
}
