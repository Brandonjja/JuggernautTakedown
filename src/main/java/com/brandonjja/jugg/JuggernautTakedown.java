package com.brandonjja.jugg;

import com.brandonjja.jugg.commands.CommandManager;
import com.brandonjja.jugg.listeners.ListenerManager;
import org.bukkit.plugin.java.JavaPlugin;

public class JuggernautTakedown extends JavaPlugin {

    private static JuggernautTakedown plugin;

    @Override
    public void onEnable() {
        plugin = this;
        CommandManager.registerCommands();
        ListenerManager.registerListeners();
    }

    @Override
    public void onDisable() {
        plugin = null;
    }

    public static JuggernautTakedown getPlugin() {
        return plugin;
    }
}
