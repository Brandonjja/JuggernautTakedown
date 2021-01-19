package com.brandonjja.jugg.listeners.player;

import com.brandonjja.jugg.game.Game;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getDrops().clear();
        Game.getGame().getPlayer(event.getEntity()).updateScoreboardDeaths();
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Game game = Game.getGame();
        if (game == null) return;

        Location spawnLocation = Game.getGame().getSpawnLocation();
        spawnLocation.setY(spawnLocation.getWorld().getHighestBlockYAt(spawnLocation));
        event.setRespawnLocation(spawnLocation);
        game.getPlayer(event.getPlayer()).applyRoleKit();
    }
}
