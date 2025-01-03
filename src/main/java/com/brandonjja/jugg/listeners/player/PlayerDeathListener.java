package com.brandonjja.jugg.listeners.player;

import com.brandonjja.jugg.game.Game;
import com.brandonjja.jugg.game.PlayerJT;
import com.brandonjja.jugg.game.Role;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getDrops().clear();

        Game game = Game.getGame();
        if (game == null) {
            return;
        }

        PlayerJT jtPlayer = game.getPlayer(event.getEntity());
        if (jtPlayer == null) {
            return;
        }

        jtPlayer.updateScoreboardDeaths();

        if (jtPlayer.getRole() == Role.JUGGERNAUT) {
            Bukkit.broadcastMessage(ChatColor.GREEN + "The " + ChatColor.RED + "Chasers" + ChatColor.GREEN + " win this round! Congratulations!\n" + "The Juggernaut never stood a chance...");
            game.endGame();
            return;
        }

        PlayerJT juggernaut = game.getJuggernaut();
        if (juggernaut == null) {
            return;
        }

        juggernaut.updateScoreboardKills();
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Game game = Game.getGame();
        if (game == null) {
            return;
        }

        event.setRespawnLocation(game.getSpawnLocation());

        PlayerJT jtPlayer = game.getPlayer(event.getPlayer());
        if (jtPlayer == null) {
            return;
        }

        jtPlayer.applyRoleKit();
    }
}
