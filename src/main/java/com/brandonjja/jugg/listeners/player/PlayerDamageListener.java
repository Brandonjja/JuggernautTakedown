package com.brandonjja.jugg.listeners.player;

import com.brandonjja.jugg.game.Game;
import com.brandonjja.jugg.game.Role;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamageListener implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player && event.getEntity() instanceof Player)) {
            return;
        }

        Player damager = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();

        Game game = Game.getGame();
        if (game == null) {
            event.setCancelled(true);
            return;
        }

        if (game.getPlayer(damager).getRole() == Role.CHASER && game.getPlayer(victim).getRole() == Role.CHASER) {
            event.setCancelled(true);
        }
    }
}
