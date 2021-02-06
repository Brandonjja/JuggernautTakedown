package com.brandonjja.jugg.game;

import com.brandonjja.jugg.JuggernautTakedown;
import com.brandonjja.jugg.nms.NMSUtils;
import com.brandonjja.jugg.utils.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Game {

    private static Game currentGame;

    private final int chaserCount; // number of chasers
    private final static int gracePeriod = 10; // time until roles are assigned once a game has started
    private int gameTimer; // time until the game ends
    private String currentMap; // name of the current map
    private final Map<String, PlayerJT> gamePlayerList; // list of players in the current game
    private World gameWorld;
    private boolean isGracePeriod;

    private int gracePeriodID = -1;
    private int gameTimerID = -1;

    private String juggernautName;

    public Game(String mapName) {
        if (currentGame != null) {
            endGame();
        }

        chaserCount = Bukkit.getOnlinePlayers().size() - 1;
        gameTimer = 1500;
        currentMap = mapName;
        isGracePeriod = true;

        gamePlayerList = new HashMap<>();
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

        NMSUtils.removeOceans();
        gameWorld = FileManager.generateNewWorld();
        Location spawnLocation = gameWorld.getSpawnLocation();
        spawnLocation.setY(spawnLocation.getWorld().getHighestBlockYAt(spawnLocation));
        spawnLocation.getWorld().getWorldBorder().setSize(500);
        gameWorld.setSpawnLocation(spawnLocation.getBlockX(), spawnLocation.getBlockY(), spawnLocation.getBlockZ());

        currentMap = gameWorld.getName();

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.teleport(spawnLocation);
            player.setHealth(20);
            player.setFoodLevel(20);
            player.sendMessage(graceMsg);

            player.getInventory().clear();
            player.getInventory().setHelmet(null);
            player.getInventory().setChestplate(null);
            player.getInventory().setLeggings(null);
            player.getInventory().setBoots(null);

            for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                player.removePotionEffect(potionEffect.getType());
            }
        }

        gracePeriodID = Bukkit.getScheduler().scheduleSyncDelayedTask(JuggernautTakedown.getPlugin(), () -> {
            List<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());
            Collections.shuffle(playerList);

            gamePlayerList.put(playerList.get(0).getName(), new PlayerJT(playerList.get(0), Role.JUGGERNAUT));
            playerList.get(0).sendMessage(juggernautRoleMsg);
            gamePlayerList.get(playerList.get(0).getName()).applyRoleKit();
            juggernautName = playerList.get(0).getName();
            gamePlayerList.get(playerList.get(0).getName()).addScoreboard(gameTimer);
            playerList.get(0).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 0));

            for (int i = 1; i <= chaserCount; i++) {
                gamePlayerList.put(playerList.get(i).getName(), new PlayerJT(playerList.get(i), Role.CHASER));
                playerList.get(i).sendMessage(chaserRoleMsg);
                gamePlayerList.get(playerList.get(i).getName()).applyRoleKit();
                gamePlayerList.get(playerList.get(i).getName()).addScoreboard(gameTimer);
            }

            isGracePeriod = false;

            gameTimerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(JuggernautTakedown.getPlugin(), () -> {
                if (--gameTimer <= 0) {
                    endGame();
                }

                for (PlayerJT jtPlayer : gamePlayerList.values()) {
                    jtPlayer.updateScoreboardTime(gameTimer);
                    if (jtPlayer.getRole() == Role.JUGGERNAUT) {
                        jtPlayer.getPlayer().setRemainingAir(jtPlayer.getPlayer().getRemainingAir() / 2);
                    }
                }

            }, 20, 20);


        }, (long) (gracePeriod * 20) + 20);
    }

    private final StringBuilder juggernautWinMsg = new StringBuilder()
            .append(ChatColor.GREEN)
            .append("Congratulations to the ")
            .append(ChatColor.RED)
            .append("Juggernaut")
            .append(ChatColor.GREEN)
            .append(" (");

    public void endGame() {
        if (gracePeriodID > 0) {
            Bukkit.getScheduler().cancelTask(gracePeriodID);
            gracePeriodID = -1;
        }
        if (gameTimerID > 0) {
            Bukkit.getScheduler().cancelTask(gameTimerID);
            gameTimerID = -1;
        }

        if (currentGame == null) return;

        if (gameTimer <= 0) {
            juggernautWinMsg.append(ChatColor.AQUA)
                    .append(juggernautName)
                    .append(ChatColor.GREEN)
                    .append(") on surviving this round!\nChasers, better luck next time!");
            Bukkit.broadcastMessage(juggernautWinMsg.toString());
        }

        isGracePeriod = true;

        Bukkit.getScheduler().scheduleSyncDelayedTask(JuggernautTakedown.getPlugin(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.spigot().respawn();
            }
        }, 4 * 20);

        Bukkit.getScheduler().scheduleSyncDelayedTask(JuggernautTakedown.getPlugin(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.teleport(Bukkit.getWorld("world").getSpawnLocation());
                player.getInventory().clear();
            }

            if (currentMap != null && !currentMap.equalsIgnoreCase("world")) {
                FileManager.unloadWorld(gameWorld);
                FileManager.deleteWorld(gameWorld.getWorldFolder());
            }

            currentMap = "world";
            currentGame = null;

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            }

            gamePlayerList.clear();
        }, 8 * 20);
    }

    public boolean isGracePeriod() {
        return isGracePeriod;
    }

    public int getTime() {
        return gameTimer;
    }

    public PlayerJT getPlayer(Player player) {
        if (gamePlayerList != null) {
            return gamePlayerList.get(player.getName());
        }
        return null;
    }

    public PlayerJT getJuggernaut() {
        return gamePlayerList.get(juggernautName);
    }

    public void updateJuggernaut(String name) {
        for (PlayerJT player : gamePlayerList.values()) {
            player.updateScoreboardJuggernautName(name);
        }
        juggernautName = name;

    }

    public void addPlayer(Player player) {
        if (gamePlayerList != null) {
            Role role = Role.CHASER;
            if (player.getName().equalsIgnoreCase(juggernautName)) {
                role = Role.JUGGERNAUT;
            }
            gamePlayerList.put(player.getName(), new PlayerJT(player, role));
        }
        gamePlayerList.get(player.getName()).addScoreboard(gameTimer);
    }

    public void removePlayer(Player player) {
        if (gamePlayerList != null) {
            gamePlayerList.get(player.getName()).setPlayer(null);
            gamePlayerList.remove(player.getName());
        }
    }

    public Location getSpawnLocation() {
        return gameWorld.getSpawnLocation();
    }

    public String getJuggernautName() {
        return juggernautName;
    }
}
