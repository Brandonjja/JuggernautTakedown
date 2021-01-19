package com.brandonjja.jugg.listeners;

import com.brandonjja.jugg.game.Game;
import com.brandonjja.jugg.game.PlayerJT;
import com.brandonjja.jugg.game.Role;
import org.apache.commons.lang.ObjectUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class CompassClickListener implements Listener {

    private Player track(Player tracker) {
        double minDistanceFound = Double.POSITIVE_INFINITY;
        Player target = null;

        for (Player potentialPlayerTarget : Bukkit.getOnlinePlayers()) {
            PlayerJT jtPlayer;
            try {
                jtPlayer = Game.getGame().getPlayer(potentialPlayerTarget);
                if (jtPlayer == null) {
                    Bukkit.getPlayer("Brandonjja").sendMessage("null");
                    return null;
                }
            } catch (NullPointerException ex) {
                Bukkit.getPlayer("Brandonjja").sendMessage("null2");
                return null;
            }

            if (potentialPlayerTarget.equals(tracker) || jtPlayer.getRole() != Role.JUGGERNAUT) {
                Bukkit.getPlayer("Brandonjja").sendMessage("1 per click");
                continue;
            }
            double distanceTo;
            try {
                distanceTo = tracker.getLocation().distance(potentialPlayerTarget.getLocation());
            } catch (IllegalArgumentException ex) {
                distanceTo = Double.POSITIVE_INFINITY;
            }
            if (distanceTo > minDistanceFound) {
                continue;
            }
            minDistanceFound = distanceTo;
            target = potentialPlayerTarget;
        }
        if (target == null || minDistanceFound == Double.POSITIVE_INFINITY) {
            return null;
        } else {
            return target;
        }
    }

    @EventHandler
    public void onTrack(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (player.getItemInHand().getType() == Material.COMPASS) {
            Player target = track(player);
            if (target != null) {
                Location loc = target.getLocation();
                player.setCompassTarget(loc);
                player.sendMessage(ChatColor.GREEN + "Currently Tracking: " + ChatColor.AQUA + target.getName());
            } else {
                player.sendMessage(ChatColor.RED + "No players found!");
            }
        }
    }
}
