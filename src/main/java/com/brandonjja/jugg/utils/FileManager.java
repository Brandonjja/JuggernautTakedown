package com.brandonjja.jugg.utils;

import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteWorld(file);
                } else {
                    file.delete();
                }
            }
        }
        path.delete();
    }

    /**
     * Copy a world to a new folder
     **/
    public static void copyWorld(File source, File target) {
        try {
            List<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.lock")); // session.dat ??
            if (!ignore.contains(source.getName())) {
                if (source.isDirectory()) {
                    if (!target.exists())
                        target.mkdirs();
                    String[] files = source.list();
                    for (String file : files) {
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        copyWorld(srcFile, destFile);
                    }
                } else {
                    InputStream inputStream = new FileInputStream(source);
                    OutputStream outputStream = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int inStreamLength;
                    while ((inStreamLength = inputStream.read(buffer)) > 0)
                        outputStream.write(buffer, 0, inStreamLength);
                    inputStream.close();
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param absolutePath The path to format (ex. Bukkit.getWorldContainer().getAbsolutePath() )
     * @return Formatted path (C:\\Users formatted to C:/Users)
     */
    private static String formatAbsolutePath(String absolutePath) {
        char[] charArr = absolutePath.toCharArray();
        StringBuilder path = new StringBuilder("");
        for (char character : charArr) {
            if (character == '.') {
                continue;
            } else if (character == '\\') {
                character = '/';
            }
            path.append(character);
        }
        return path.toString();
    }

    public static File getFileFromName(String name) {
        String path = formatAbsolutePath(Bukkit.getWorldContainer().getAbsolutePath());
        String worldName = path + name;

        return new File(worldName);
    }

    /**
     * Generates a new survival world with a random name
     */
    public static World generateNewWorld() {
        String name = RandomStringUtils.random(10, true, false);
        return Bukkit.createWorld(new WorldCreator(name));
    }

}
