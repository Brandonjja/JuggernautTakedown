package com.brandonjja.jugg.listeners.entity;

import com.brandonjja.jugg.game.Game;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ArrowHitEvent implements Listener {

    @EventHandler
    public void onArrow(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow && event.getEntity().getShooter() instanceof Player) {
            if (Game.getGame() == null) return;

            Arrow arrow = (Arrow) event.getEntity();
            Game.getGame().getPlayer((Player) arrow.getShooter()).updateArrowsShot();
        }
    }
}
