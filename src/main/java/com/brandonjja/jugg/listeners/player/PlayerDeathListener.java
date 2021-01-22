package com.brandonjja.jugg.listeners.player;

import com.brandonjja.jugg.game.Game;
import com.brandonjja.jugg.game.Role;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerDeathListener implements Listener {

    private final String chaserWinMsg = ChatColor.GREEN + "The " + ChatColor.RED + "Chasers" + ChatColor.GREEN
            + " win this round! Congratulations!\n" + "The Juggernaut never stood a chance...";
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getDrops().clear();
        Game game = Game.getGame();
        if (game == null) return;

        game.getPlayer(event.getEntity()).updateScoreboardDeaths();
        if (game.getPlayer(event.getEntity()).getRole() == Role.JUGGERNAUT) {
            Bukkit.broadcastMessage(chaserWinMsg);
            game.endGame();
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Game game = Game.getGame();
        if (game == null) return;

        if (!game.isGracePeriod()) game.getPlayer(event.getPlayer()).applyRoleKit();

        event.setRespawnLocation(game.getSpawnLocation());
        game.getPlayer(event.getPlayer()).applyRoleKit();
    }
}
