package com.brandonjja.jugg.game;

import com.brandonjja.jugg.JuggernautTakedown;
import com.brandonjja.jugg.utils.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {

    private static Game currentGame;

    private final int chaserCount; // number of chasers
    private final static int gracePeriod = 4; // time until roles are assigned once a game has started
    private int gameTimer; // time until the game ends
    private String currentMap; // name of the current map
    private final List<PlayerJT> gamePlayerList; // list of players in the current game
    private World gameWorld;

    private int gracePeriodID = -1;
    private int gameTimerID = -1;

    private String juggernautName;

    public Game(String mapName) {
        if (currentGame != null) {
            endGame();
        }

        chaserCount = Bukkit.getOnlinePlayers().size() - 1;
        gameTimer = 600;
        currentMap = mapName;

        gamePlayerList = new ArrayList<>();
        startGame();
    }

    public int getChaserCount() {
        return chaserCount;
    }

    public static void setGame(Game game) {
        currentGame = game;
    }

    public static Game getGame() {
        return currentGame;
    }

    public static void newGame(String mapName) {
        setGame(new Game(mapName));
    }

    private final static String juggernautRoleMsg = ChatColor.GREEN + "You are now a " + Role.JUGGERNAUT.toString();
    private final static String chaserRoleMsg = ChatColor.GREEN + "You are now a " + Role.CHASER.toString();
    private final static String graceMsg = ChatColor.YELLOW + "The Grace Period has begun. You now have " + ChatColor.RED
            + gracePeriod + ChatColor.YELLOW + " seconds to spread out!";

    private void startGame() {

        Bukkit.broadcastMessage(ChatColor.GREEN + "A new " + ChatColor.YELLOW + "Juggernaut Takedown " + ChatColor.GREEN + "game is starting!");
        Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "Loading Map... Please Wait (:");

        gameWorld = FileManager.generateNewWorld();
        Location spawnLocation = gameWorld.getSpawnLocation();
        spawnLocation.setY(spawnLocation.getWorld().getHighestBlockYAt(spawnLocation));
        spawnLocation.getWorld().getWorldBorder().setSize(500);

        currentMap = gameWorld.getName();

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.teleport(spawnLocation);
            player.setHealth(20);
            player.setFoodLevel(20);
            player.sendMessage(graceMsg);


            for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                player.removePotionEffect(potionEffect.getType());
            }
        }

        gracePeriodID = Bukkit.getScheduler().scheduleSyncDelayedTask(JuggernautTakedown.getPlugin(), () -> {
            // TODO: start game timer, give scoreboards
            List<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());
            Collections.shuffle(playerList);

            gamePlayerList.add(new PlayerJT(playerList.get(0), Role.JUGGERNAUT));
            playerList.get(0).sendMessage(juggernautRoleMsg);
            gamePlayerList.get(0).applyRoleKit();
            juggernautName = playerList.get(0).getName();
            gamePlayerList.get(0).addScoreboard(gameTimer);

            for (int i = 1; i <= chaserCount; i++) {
                gamePlayerList.add(new PlayerJT(playerList.get(i), Role.CHASER));
                playerList.get(i).sendMessage(chaserRoleMsg);
                gamePlayerList.get(i).applyRoleKit();
                gamePlayerList.get(i).addScoreboard(gameTimer);
            }

            gameTimerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(JuggernautTakedown.getPlugin(), () -> {
                if (--gameTimer <= 0) {
                    endGame();
                }

                for (PlayerJT jtPlayer : gamePlayerList) {
                    jtPlayer.updateScoreboardTime(gameTimer);
                }

            }, 20, 20);


        }, (long) (gracePeriod * 20) + 20);
    }

    public void endGame() {
        if (gracePeriodID > 0) {
            Bukkit.getScheduler().cancelTask(gracePeriodID);
            gracePeriodID = -1;
        }
        if (gameTimerID > 0) {
            Bukkit.getScheduler().cancelTask(gameTimerID);
            gameTimerID = -1;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.teleport(Bukkit.getWorld("world").getSpawnLocation());
            player.getInventory().clear();
        }

        if (currentMap != null && !currentMap.equalsIgnoreCase("world")) {
            FileManager.unloadWorld(gameWorld);
            FileManager.deleteWorld(gameWorld.getWorldFolder());
        }

        currentGame = null;
        currentMap = "world";

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }

        gamePlayerList.clear();

    }

    public PlayerJT getPlayer(Player player) {
        if (gamePlayerList != null) {
            for (PlayerJT playerInGame : gamePlayerList) {
                if (playerInGame.getPlayer().equals(player)) {
                    return playerInGame;
                }
            }
        }
        return null;
    }

    public Location getSpawnLocation() {
        return gameWorld.getSpawnLocation();
    }

    public String getJuggernautName() {
        return juggernautName;
    }
}
