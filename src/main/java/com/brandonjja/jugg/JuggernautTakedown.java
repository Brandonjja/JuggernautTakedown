package com.brandonjja.jugg;

import org.bukkit.plugin.java.JavaPlugin;

public class JuggernautTakedown extends JavaPlugin {

    private static JuggernautTakedown plugin;

    @Override
    public void onEnable() {
        plugin = this;
    }

    @Override
    public void onDisable() {
        plugin = null;
    }

    public static JuggernautTakedown getPlugin() {
        return plugin;
    }
}
