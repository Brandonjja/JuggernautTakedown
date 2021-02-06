package com.brandonjja.jugg.nms;

import net.minecraft.server.v1_8_R3.BiomeBase;

import java.lang.reflect.Field;

public class NMSUtils {

    public static void removeOceans() {
        Field biomesField;
        try {
            biomesField = BiomeBase.class.getDeclaredField("biomes");
            biomesField.setAccessible(true);

            BiomeBase[] biomes = (BiomeBase[]) biomesField.get(null);
            biomes[BiomeBase.DEEP_OCEAN.id] = BiomeBase.PLAINS;
            biomes[BiomeBase.OCEAN.id] = BiomeBase.PLAINS;
            biomes[BiomeBase.SWAMPLAND.id] = BiomeBase.PLAINS;
            biomes[BiomeBase.BEACH.id] = BiomeBase.DESERT;
            biomes[BiomeBase.RIVER.id] = BiomeBase.MUSHROOM_SHORE;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
