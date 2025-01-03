package com.brandonjja.jugg.listeners;

import com.brandonjja.jugg.game.Game;
import com.brandonjja.jugg.game.PlayerJT;
import com.brandonjja.jugg.game.Role;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class CompassClickListener implements Listener {

    @EventHandler
    public void onTrackPlayer(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getType() != Material.COMPASS) {
            return;
        }

        Player target = getClosestTarget(player);
        if (target != null) {
            Location targetLocation = target.getLocation();
            player.setCompassTarget(targetLocation);
            player.sendMessage(ChatColor.GREEN + "Currently Tracking: " + ChatColor.AQUA + target.getName());
            return;
        }

        player.sendMessage(ChatColor.RED + "No players found!");
    }

    private Player getClosestTarget(Player tracker) {
        Game game = Game.getGame();
        if (game == null) {
            return null;
        }

        double minDistanceFound = Double.POSITIVE_INFINITY;
        Player target = null;

        for (Player potentialPlayerTarget : Bukkit.getOnlinePlayers()) {
            if (potentialPlayerTarget.equals(tracker)) {
                continue;
            }

            PlayerJT jtPlayer = game.getPlayer(potentialPlayerTarget);
            if (jtPlayer == null || jtPlayer.getRole() != Role.JUGGERNAUT) {
                continue;
            }

            double distanceTo;
            try {
                distanceTo = tracker.getLocation().distanceSquared(potentialPlayerTarget.getLocation());
            } catch (IllegalArgumentException ex) {
                distanceTo = Double.POSITIVE_INFINITY;
            }

            if (distanceTo > minDistanceFound) {
                continue;
            }

            minDistanceFound = distanceTo;
            target = potentialPlayerTarget;
        }

        return (target == null || minDistanceFound == Double.POSITIVE_INFINITY) ? null : target;
    }
}
