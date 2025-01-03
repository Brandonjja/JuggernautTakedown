package com.brandonjja.jugg.listeners.entity;

import com.brandonjja.jugg.game.Game;
import com.brandonjja.jugg.game.PlayerJT;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.ProjectileSource;

public class ArrowHitEvent implements Listener {

    @EventHandler
    public void onArrow(ProjectileHitEvent event) {
        if (Game.getGame() == null) {
            return;
        }

        Projectile projectile = event.getEntity();
        if (!(projectile instanceof Arrow)) {
            return;
        }

        ProjectileSource shooter = event.getEntity().getShooter();
        if (!(shooter instanceof Player)) {
            return;
        }

        PlayerJT player = Game.getGame().getPlayer((Player) shooter);
        if (player == null) {
            return;
        }

        player.updateArrowsShot();
    }
}
