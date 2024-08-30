package com.github.liamsh.BetterArmedBedwars.utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class PlayerProximity {

    public static double getDistanceSquared(EntityPlayer player, double targetX, double targetY, double targetZ) {
        double playerX = player.posX;
        double playerY = player.posY;
        double playerZ = player.posZ;

        return (playerX - targetX) * (playerX - targetX) +
                (playerY - targetY) * (playerY - targetY) +
                (playerZ - targetZ) * (playerZ - targetZ);
    }

    public static EntityPlayer getClosestPlayer(World world, double targetX, double targetY, double targetZ) {
        EntityPlayer closestPlayer = null;
        double closestDistanceSquared = Double.MAX_VALUE;
        for (EntityPlayer player : world.playerEntities) {
            double distanceSquared = getDistanceSquared(player, targetX, targetY, targetZ);

            if (distanceSquared < closestDistanceSquared) {
                closestDistanceSquared = distanceSquared;
                closestPlayer = player;
            }
        }
        return closestPlayer;
    }
}
