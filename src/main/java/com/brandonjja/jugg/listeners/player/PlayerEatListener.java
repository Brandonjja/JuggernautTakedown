package com.brandonjja.jugg.listeners.player;

import com.brandonjja.jugg.game.Game;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class PlayerEatListener implements Listener {

    @EventHandler
    public void onEatGoldenApple(PlayerItemConsumeEvent event) {
        Game game = Game.getGame();
        if (game == null) return;

        if (event.getItem().getType() == Material.GOLDEN_APPLE) {
            game.getPlayer(event.getPlayer()).updateScoreboardApples();
        }
    }
}
