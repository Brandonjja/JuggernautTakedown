package com.brandonjja.jugg.commands.handler;

import com.brandonjja.jugg.commands.JuggernautTakedownCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class JuggernautCommand extends JuggernautTakedownCommand {

    @Override
    public boolean execute(Player player, String[] args) {
        Bukkit.getServer().broadcastMessage(ChatColor.GREEN + player.getName() + " is now the Juggernaut!");
        return true;
    }
}
