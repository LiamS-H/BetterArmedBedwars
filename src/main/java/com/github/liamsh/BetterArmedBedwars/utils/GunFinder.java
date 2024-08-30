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
}
