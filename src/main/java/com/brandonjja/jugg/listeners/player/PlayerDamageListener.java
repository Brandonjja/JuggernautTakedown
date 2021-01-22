package com.brandonjja.jugg.listeners.player;

import com.brandonjja.jugg.game.Game;
import com.brandonjja.jugg.game.PlayerJT;
import com.brandonjja.jugg.game.Role;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

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
            //if (!(event.getDamager() instanceof Arrow) && !(event.getEntity() instanceof Player)) {
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

        if (jtDamager.getRole() == Role.CHASER && jtVictim.getRole() == Role.CHASER) {
            event.setCancelled(true);
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
}
