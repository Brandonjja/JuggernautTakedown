package com.brandonjja.jugg.listeners;

import com.brandonjja.jugg.JuggernautTakedown;
import com.brandonjja.jugg.listeners.entity.ArrowHitEvent;
import com.brandonjja.jugg.listeners.player.PlayerConnectionListener;
import com.brandonjja.jugg.listeners.player.PlayerDamageListener;
import com.brandonjja.jugg.listeners.player.PlayerDeathListener;
import com.brandonjja.jugg.listeners.player.PlayerEatListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class ListenerManager {
    public static void registerListeners() {
        register(new ArrowHitEvent());

        register(new PlayerConnectionListener());
        register(new PlayerDamageListener());
        register(new PlayerDeathListener());
        register(new PlayerEatListener());

        register(new CompassClickListener());
    }

    private static void register(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, JuggernautTakedown.getPlugin());
    }
}
