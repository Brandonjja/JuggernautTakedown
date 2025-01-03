package com.brandonjja.jugg.commands;

import com.brandonjja.jugg.commands.handler.ChaserCommand;
import com.brandonjja.jugg.commands.handler.EndGameCommand;
import com.brandonjja.jugg.commands.handler.JuggernautCommand;
import com.brandonjja.jugg.commands.handler.NewGameCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CommandManager implements CommandExecutor {

    private static final Map<String, JuggernautTakedownCommand> COMMAND_LIST = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        return COMMAND_LIST.get(commandLabel).execute((Player) sender, args);
    }

    public static void registerCommands() {
        COMMAND_LIST.put("juggernaut", new JuggernautCommand());
        COMMAND_LIST.put("chaser", new ChaserCommand());
        COMMAND_LIST.put("newgame", new NewGameCommand());
        COMMAND_LIST.put("endgame", new EndGameCommand());

        for (String cmdLabel : COMMAND_LIST.keySet()) {
            register(cmdLabel, new CommandManager());
        }
    }

    private static void register(String cmdLabel, CommandExecutor command) {
        Bukkit.getPluginCommand(cmdLabel).setExecutor(command);
    }
}
