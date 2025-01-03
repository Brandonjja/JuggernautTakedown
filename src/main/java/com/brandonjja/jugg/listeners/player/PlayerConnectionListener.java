package com.brandonjja.jugg.listeners.player;

import com.brandonjja.jugg.game.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Game game = Game.getGame();
        if (game == null) {
            return;
        }

        game.removePlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Game game = Game.getGame();
        if (game == null) {
            return;
        }

        game.addPlayer(event.getPlayer());
    }
}
