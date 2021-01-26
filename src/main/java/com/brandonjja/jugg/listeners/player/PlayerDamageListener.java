package com.brandonjja.jugg.listeners.player;

import com.brandonjja.jugg.game.Game;
import com.brandonjja.jugg.game.PlayerJT;
import com.brandonjja.jugg.game.Role;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

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
            if (shooter instanceof Player) {
                damager = (Player) shooter;
            } else {
                return;
            }
        } else {
            damager = (Player) event.getDamager();
        }

        PlayerJT jtDamager = game.getPlayer(damager);
        PlayerJT jtVictim = game.getPlayer(victim);

        ItemStack weapon = damager.getItemInHand();

        if (jtDamager.getRole() == Role.CHASER && jtVictim.getRole() == Role.CHASER) {
            if (weapon.hasItemMeta() && weapon.getItemMeta().hasDisplayName()) {
                if (weapon.getItemMeta().getDisplayName().contains("Booster Stick")) {
                    event.setDamage(0);
                    return;
                }
            }
            event.setCancelled(true);
            return;
        }

        if (jtDamager.getRole() == Role.CHASER && jtVictim.getRole() == Role.JUGGERNAUT) {
            if (weapon.hasItemMeta() && weapon.getItemMeta().hasDisplayName()) {
                if (weapon.getItemMeta().getDisplayName().contains("Booster Stick")) {
                    event.setCancelled(true);
                    damager.sendMessage(ChatColor.RED + "You cannot hit the Juggernaut with your Booster Stick!");
                    return;
                }
            }
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
}
