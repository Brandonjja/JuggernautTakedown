package com.brandonjja.jugg.listeners.player;

import com.brandonjja.jugg.JuggernautTakedown;
import com.brandonjja.jugg.game.Game;
import com.brandonjja.jugg.game.PlayerJT;
import com.brandonjja.jugg.game.Role;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerDamageListener implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        Game game = Game.getGame();
        if (game == null || game.isGracePeriod()) {
            event.setCancelled(true);
            return;
        }

        boolean arrow = false;
        if (!(event.getDamager() instanceof Player && event.getEntity() instanceof Player)) {
            if (event.getEntity() instanceof Player && event.getDamager() instanceof Arrow) {
                arrow = true;
            } else {
                return;
            }
        }

        Player damager;
        Player victim = (Player) event.getEntity();
        if (arrow) {
            Entity shooter = (Entity) ((Arrow) event.getDamager()).getShooter();
            if (!(shooter instanceof Player)) {
                return;
            }

            damager = (Player) shooter;
            Bukkit.getScheduler().scheduleSyncDelayedTask(JuggernautTakedown.getPlugin(), () -> {
                PlayerJT jtPlayer = game.getPlayer(damager);
                if (jtPlayer != null) {
                    jtPlayer.updateArrowsHit();
                }
            }, 1);
        } else {
            damager = (Player) event.getDamager();
        }

        PlayerJT jtDamager = game.getPlayer(damager);
        if (jtDamager == null) {
            return;
        }

        PlayerJT jtVictim = game.getPlayer(victim);
        if (jtVictim == null) {
            return;
        }

        ItemStack weapon = damager.getItemInHand();
        boolean hitWithBoosterStick = isItemBoosterStick(weapon);

        if (jtDamager.getRole() == Role.CHASER && jtVictim.getRole() == Role.CHASER) {
            if (hitWithBoosterStick) {
                event.setDamage(0);
                return;
            }

            event.setCancelled(true);
            return;
        }

        if (hitWithBoosterStick && jtDamager.getRole() == Role.CHASER && jtVictim.getRole() == Role.JUGGERNAUT) {
            event.setCancelled(true);
            damager.sendMessage(ChatColor.RED + "You cannot hit the Juggernaut with your Booster Stick!");
            return;
        }

        double damage = event.getDamage();
        jtDamager.updateScoreboardDamageDealt(damage);
        jtVictim.updateScoreboardDamageTaken(damage);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        Game game = Game.getGame();
        if (game == null || game.isGracePeriod()) {
            event.setCancelled(true);
        }
    }

    private boolean isItemBoosterStick(ItemStack weapon) {
        if (!weapon.hasItemMeta()) {
            return false;
        }

        ItemMeta weaponItemMeta = weapon.getItemMeta();
        if (!weaponItemMeta.hasDisplayName()) {
            return false;
        }

        return weaponItemMeta.getDisplayName().contains("Booster Stick");
    }
}
