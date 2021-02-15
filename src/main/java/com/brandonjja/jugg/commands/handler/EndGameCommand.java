package com.brandonjja.jugg.commands.handler;

import com.brandonjja.jugg.commands.JuggernautTakedownCommand;
import com.brandonjja.jugg.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class EndGameCommand extends JuggernautTakedownCommand {

    @Override
    public boolean execute(Player player, String[] args) {
        if (!player.isOp()) {
            return false;
        }

        Game game = Game.getGame();
        if (game != null) {
            Bukkit.broadcastMessage(ChatColor.RED + "Game forced to end, please wait.");
            game.endGame();
        } else {
            player.sendMessage(ChatColor.RED + "No current game");
        }

        return true;
    }
}
