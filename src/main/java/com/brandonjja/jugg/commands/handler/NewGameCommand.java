package com.brandonjja.jugg.commands.handler;

import com.brandonjja.jugg.commands.JuggernautTakedownCommand;
import com.brandonjja.jugg.game.Game;
import org.bukkit.entity.Player;

public class NewGameCommand extends JuggernautTakedownCommand {
    @Override
    public boolean execute(Player player, String[] args) {
        if (!player.isOp()) {
            return false;
        }

        Game.newGame("world");

        return true;
    }
}
