package com.brandonjja.jugg.utils;

import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;

public class FileManager {

    /**
     * Unloads the world so that it can be deleted
     *
     * @param world the world to be unloaded
     */
    public static void unloadWorld(World world) {
        if (world != null) {
            Bukkit.getServer().unloadWorld(world, true);
        }
    }

    /**
     * Deletes the world folder (entire world)
     *
     * @param path the path to the world [usage: deleteWorld(world.getWorldFolder()); ]
     */
    public static void deleteWorld(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteWorld(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
        path.delete();
    }

    /**
     * Generates a new survival world with a random name
     */
    public static World generateNewWorld() {
        String name = RandomStringUtils.random(10, true, false);
        return Bukkit.createWorld(new WorldCreator(name));
    }

}
