package com.github.liamsh.BetterArmedBedwars.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class TexturesLoader {

    public static void registerResourcePack() {
        InputStream inputStream = TexturesLoader.class.getClassLoader().getResourceAsStream("assets/BetterArmedBedwars/textures.zip");

        if (inputStream == null) {
            System.out.println("Failed to Locate Mod Textures");
            return;
        }

        try {
            File resourceFile = new File(System.getProperty("java.io.tmpdir"), "§f[MOD] §2Better §4Armed §1Bedwars");

            FileOutputStream outputStream = new FileOutputStream(resourceFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();

            ResourcePackRepository resourcePackRepository = Minecraft.getMinecraft().getResourcePackRepository();

            @SuppressWarnings("JavaReflectionMemberAccess") Constructor<ResourcePackRepository.Entry> constructor = ResourcePackRepository.Entry.class.getDeclaredConstructor(ResourcePackRepository.class, File.class);
            constructor.setAccessible(true);
            ResourcePackRepository.Entry packEntry = constructor.newInstance(resourcePackRepository, resourceFile);
            packEntry.updateResourcePack();

            List<ResourcePackRepository.Entry> currentEntries = resourcePackRepository.getRepositoryEntries();
            List<ResourcePackRepository.Entry> newEntries = new ArrayList<>(currentEntries);
            newEntries.add(packEntry);

            resourcePackRepository.setRepositories(newEntries);

            Minecraft.getMinecraft().refreshResources();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
