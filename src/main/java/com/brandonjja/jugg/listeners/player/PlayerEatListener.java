package com.brandonjja.jugg.listeners.player;

import com.brandonjja.jugg.game.Game;
import com.brandonjja.jugg.game.PlayerJT;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class PlayerEatListener implements Listener {

    @EventHandler
    public void onEatGoldenApple(PlayerItemConsumeEvent event) {
        Game game = Game.getGame();
        if (game == null) {
            return;
        }

        Material eatenItemType = event.getItem().getType();
        if (eatenItemType != Material.GOLDEN_APPLE) {
            return;
        }

        PlayerJT jtPlayer = game.getPlayer(event.getPlayer());
        if (jtPlayer == null) {
            return;
        }

        jtPlayer.updateScoreboardApples();
    }
}
