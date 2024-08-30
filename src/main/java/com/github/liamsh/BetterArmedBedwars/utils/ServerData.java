package com.github.liamsh.BetterArmedBedwars.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ServerData {
    public static String gameType = null;
    private static boolean waitingForLocRaw = false;
    public static NBTTagCompound getNBT() {
        net.minecraft.client.multiplayer.ServerData serverData = Minecraft.getMinecraft().getCurrentServerData();
        if (serverData == null) {
            return null;
        }

        return serverData.getNBTCompound();
    }
    public static void printNBT() {
        NBTTagCompound nbtTagCompound = getNBT();
        if (nbtTagCompound == null) {
            System.out.println("Not connected to a server.");
            return;
        }

        String jsonString = nbtTagCompound.toString();


        System.out.println("NBT Data in JSON format: ");
        System.out.println(jsonString);

    }

    public static void printStats() {
        net.minecraft.client.multiplayer.ServerData serverData = Minecraft.getMinecraft().getCurrentServerData();
        if (serverData == null) {
            System.out.println("Not connected to a server.");
            return;
        }
        System.out.println("popInfo" + serverData.populationInfo);
    }

    public static boolean isNBT(String tag, String value) {
        NBTTagCompound nbtTagCompound = getNBT();
        if (nbtTagCompound == null) return false;
        return nbtTagCompound.getString(tag).equals(value);
    }

    public static String getNBT(String tag) {
        NBTTagCompound nbtTagCompound = getNBT();
        if (nbtTagCompound == null) return null;
        return nbtTagCompound.getString(tag);
    }

    public static boolean inGame(String mode) {
        if (gameType == null) return false;
        return gameType.toUpperCase().endsWith(mode.toUpperCase());
    }

    @SubscribeEvent
    public void onEntityJoinWorldEvent(EntityJoinWorldEvent event) {
        if (event.entity == null) return;

        if (event.entity != Minecraft.getMinecraft().thePlayer) return;

        gameType = null;
        if (!event.world.isRemote) return;

        if (Minecraft.getMinecraft().getCurrentServerData() == null) return;

        String serverIP = Minecraft.getMinecraft().getCurrentServerData().serverIP;
        if (serverIP == null) return;
        if (!serverIP.toLowerCase().contains("hypixel.net")) return;

        if (waitingForLocRaw) {
            return;
        }
        waitingForLocRaw = true;
        Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(new C01PacketChatMessage("/locraw"));
    }

    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event) {
        if (!waitingForLocRaw) return;
        if (event.message == null) return;
        String message = event.message.getUnformattedText();
        if (message == null) return;

        if (!message.startsWith("{") || !message.endsWith("}")) {
            return;
        }
        try {
            JsonObject locrawData = new JsonParser().parse(message).getAsJsonObject();
            String mode = locrawData.get("gametype").getAsString();
            gameType = mode;
            event.setCanceled(true);
            waitingForLocRaw = false;
            mode = locrawData.get("mode").getAsString();
            gameType = mode;
        } catch (JsonSyntaxException e) {
            // json was in unexpected format (cause in lobby servers)
        } catch (Exception e) {
            // null error
        }
        System.out.println("Detected: " + gameType);
    }
}
