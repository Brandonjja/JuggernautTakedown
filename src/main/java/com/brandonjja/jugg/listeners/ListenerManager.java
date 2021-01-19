package com.brandonjja.jugg.listeners;

import com.brandonjja.jugg.JuggernautTakedown;
import com.brandonjja.jugg.listeners.player.PlayerDamageListener;
import com.brandonjja.jugg.listeners.player.PlayerDeathListener;
import com.brandonjja.jugg.listeners.player.PlayerEatListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class ListenerManager {
    public static void registerListeners() {
        register(new CompassClickListener());
        register(new PlayerDamageListener());
        register(new PlayerDeathListener());
        register(new PlayerEatListener());
    }

    private static void register(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, JuggernautTakedown.getPlugin());
    }
}
