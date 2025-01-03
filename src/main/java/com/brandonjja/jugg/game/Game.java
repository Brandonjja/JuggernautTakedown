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

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    private final static int GRACE_PERIOD = 10; // time in seconds until roles are assigned once a game has started
    private final static String GRACE_MSG = ChatColor.YELLOW + "The Grace Period has begun. You now have " + ChatColor.RED + GRACE_PERIOD + ChatColor.YELLOW + " seconds to spread out!";
    private final static String JUGGERNAUT_ROLE_MSG = ChatColor.GREEN + "You are now a " + Role.JUGGERNAUT;
    private final static String CHASER_ROLE_MSG = ChatColor.GREEN + "You are now a " + Role.CHASER;

    private static Game currentGame;

    private final int chaserCount; // number of chasers
    private int gameTimer; // time until the game ends
    private String currentMap; // name of the current map
    private final Map<String, PlayerJT> gamePlayerList; // list of players in the current game
    private World gameWorld;
    private boolean isGracePeriod;

    private int gracePeriodID = -1;
    private int gameTimerID = -1;

    private String juggernautName;

    private Game(String mapName) {
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

    public static void setGame(Game game) {
        currentGame = game;
    }

    public static Game getGame() {
        return currentGame;
    }

    public static void newGame(String mapName) {
        setGame(new Game(mapName));
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

        if (currentGame == null) {
            return;
        }

        if (gameTimer <= 0) {
            String juggernautWinMessage = ChatColor.GREEN + "Congratulations to the " + ChatColor.RED + "Juggernaut"
                    + ChatColor.GREEN + " (" + ChatColor.AQUA + juggernautName + ChatColor.GREEN
                    + ") on surviving this round!\nChasers, better luck next time!";

            Bukkit.broadcastMessage(juggernautWinMessage);
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

    @Nullable
    public PlayerJT getPlayer(Player player) {
        return gamePlayerList == null ? null : gamePlayerList.get(player.getName());
    }

    @Nullable
    public PlayerJT getJuggernaut() {
        return gamePlayerList == null ? null : gamePlayerList.get(juggernautName);
    }

    public void updateJuggernaut(String name) {
        if (gamePlayerList == null) {
            throw new NullPointerException("Game player list wasn't created!");
        }

        for (PlayerJT player : gamePlayerList.values()) {
            player.updateScoreboardJuggernautName(name);
        }

        juggernautName = name;
    }

    public void addPlayer(Player player) {
        if (gamePlayerList == null) {
            throw new NullPointerException("Game player list wasn't created!");
        }

        Role role = Role.CHASER;
        if (player.getName().equalsIgnoreCase(juggernautName)) {
            role = Role.JUGGERNAUT;
        }

        PlayerJT jtPlayer = new PlayerJT(player, role);
        jtPlayer.addScoreboard(gameTimer);
        gamePlayerList.put(player.getName(), jtPlayer);
    }

    public void removePlayer(Player player) {
        if (gamePlayerList == null) {
            return;
        }

        PlayerJT jtPlayer = gamePlayerList.remove(player.getName());
        if (jtPlayer == null) {
            return;
        }

        jtPlayer.setPlayer(null);
    }

    public Location getSpawnLocation() {
        return gameWorld.getSpawnLocation();
    }

    public String getJuggernautName() {
        return juggernautName;
    }

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
            player.sendMessage(GRACE_MSG);

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

            Player firstPlayer = playerList.get(0);
            String firstPlayerName = firstPlayer.getName();
            PlayerJT juggernaut = new PlayerJT(firstPlayer, Role.JUGGERNAUT);
            gamePlayerList.put(firstPlayerName, juggernaut);

            firstPlayer.sendMessage(JUGGERNAUT_ROLE_MSG);
            firstPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1_000_000, 0));

            juggernaut.applyRoleKit();
            juggernaut.addScoreboard(gameTimer);

            juggernautName = firstPlayerName;

            for (int i = 1; i <= chaserCount; i++) {
                Player chaser = playerList.get(i);
                PlayerJT jtChaser = new PlayerJT(chaser, Role.CHASER);
                gamePlayerList.put(chaser.getName(), jtChaser);
                chaser.sendMessage(CHASER_ROLE_MSG);
                jtChaser.applyRoleKit();
                jtChaser.addScoreboard(gameTimer);
            }

            isGracePeriod = false;

            gameTimerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(JuggernautTakedown.getPlugin(), () -> {
                if (--gameTimer <= 0) {
                    endGame();
                }

                for (PlayerJT jtPlayer : gamePlayerList.values()) {
                    jtPlayer.updateScoreboardTime(gameTimer);
                    if (jtPlayer.getRole() != Role.JUGGERNAUT) {
                        continue;
                    }

                    Player juggernautPlayer = jtPlayer.getPlayer();
                    if (juggernautPlayer != null) {
                        juggernautPlayer.setRemainingAir(juggernautPlayer.getRemainingAir() / 2);
                    }
                }

            }, 20, 20);

        }, (long) (GRACE_PERIOD * 20) + 20);
    }
}
