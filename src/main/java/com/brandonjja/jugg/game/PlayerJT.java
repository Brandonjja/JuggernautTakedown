package com.brandonjja.jugg.game;

import com.brandonjja.jugg.JuggernautTakedown;
import com.brandonjja.jugg.commands.handler.ChaserCommand;
import com.brandonjja.jugg.commands.handler.JuggernautCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerJT {

    private static final String APPLES_PREFIX = ChatColor.GREEN + "Apples Eaten: " + ChatColor.WHITE;
    private static final String DEATHS_PREFIX = ChatColor.GREEN + "Deaths: " + ChatColor.WHITE;
    private static final String DAMAGE_DEALT_PREFIX = ChatColor.GREEN + "Damage Dealt: " + ChatColor.WHITE;
    private static final String DAMAGE_TAKEN_PREFIX = ChatColor.GREEN + "Damage Taken: " + ChatColor.WHITE;
    private static final String TIME_PREFIX = ChatColor.YELLOW + "Time: " + ChatColor.WHITE;
    private static final String KILLS_PREFIX = ChatColor.GREEN + "Kills: " + ChatColor.WHITE;
    private static final String ARROW_ACCURACY_PREFIX = ChatColor.GREEN + "Arrow Accuracy: " + ChatColor.WHITE;

    private UUID playerUuid;
    private Role role;
    private Objective objective;

    private int applesEaten;
    private int deaths;
    private double damageDealt;
    private double damageTaken;
    private int kills;
    private float shotsTaken;
    private float shotsHit;

    public PlayerJT(Player player, Role role) {
        this.playerUuid = player.getUniqueId();
        this.role = role;
        this.applesEaten = 0;
        this.deaths = 0;
        this.damageDealt = 0;
        this.damageTaken = 0;
        this.kills = 0;
        this.shotsTaken = 0;
        this.shotsHit = 0;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    /**
     * Gives kit items to the player based on what role they are
     */
    public void applyRoleKit() {
        Player player = Bukkit.getPlayer(playerUuid);
        if (player == null) {
            JuggernautTakedown.getPlugin().getLogger().log(Level.WARNING, String.format("Tried to give role items to an offline player: %s", playerUuid));
            return;
        }

        if (role == Role.JUGGERNAUT) {
            JuggernautCommand.giveJuggernautItems(player);
        } else {
            ChaserCommand.giveChaserItems(player);
        }
    }

    @Nullable
    public Player getPlayer() {
        return Bukkit.getPlayer(playerUuid);
    }

    public void setPlayer(Player player) {
        this.playerUuid = player == null ? null : player.getUniqueId();
    }

    public void addScoreboard(int startingTime) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective("juggernaut", "dummy");
        objective.setDisplayName(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Juggernaut Takedown");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        if (role == Role.JUGGERNAUT) {
            objective.getScore(ChatColor.RED + "Juggernaut: " + ChatColor.AQUA + Game.getGame().getJuggernautName()).setScore(9);
            objective.getScore(" ").setScore(8);
            objective.getScore(ARROW_ACCURACY_PREFIX + getArrowAccuracy() + "%").setScore(7);
            objective.getScore(KILLS_PREFIX + kills).setScore(3);
        } else {
            objective.getScore(ChatColor.RED + "Juggernaut: " + ChatColor.AQUA + Game.getGame().getJuggernautName()).setScore(8);
            objective.getScore(" ").setScore(7);
            objective.getScore(DEATHS_PREFIX + deaths).setScore(3);
        }

        objective.getScore(APPLES_PREFIX + applesEaten).setScore(6);
        objective.getScore(DAMAGE_DEALT_PREFIX + damageDealt).setScore(5);
        objective.getScore(DAMAGE_TAKEN_PREFIX + damageTaken).setScore(4);

        objective.getScore("  ").setScore(2);

        objective.getScore(TIME_PREFIX + formatTime(startingTime)).setScore(1);

        Player player = getPlayer();
        if (player != null) {
            player.setScoreboard(scoreboard);
        }
    }

    public void updateScoreboardApples() {
        objective.getScoreboard().resetScores(APPLES_PREFIX + applesEaten);
        objective.getScore(APPLES_PREFIX + ++applesEaten).setScore(6);
    }

    public void updateScoreboardDamageDealt(double damage) {
        objective.getScoreboard().resetScores(DAMAGE_DEALT_PREFIX + damageDealt);
        damageDealt += damage;
        objective.getScore(DAMAGE_DEALT_PREFIX + damageDealt).setScore(5);
    }

    public void updateScoreboardDamageTaken(double damage) {
        objective.getScoreboard().resetScores(DAMAGE_TAKEN_PREFIX + damageTaken);
        damageTaken += damage;
        objective.getScore(DAMAGE_TAKEN_PREFIX + damageTaken).setScore(4);
    }

    public void updateScoreboardDeaths() {
        if (role == Role.CHASER) {
            objective.getScoreboard().resetScores(DEATHS_PREFIX + deaths);
            objective.getScore(DEATHS_PREFIX + ++deaths).setScore(3);
        }
    }

    public void updateScoreboardKills() {
        if (role == Role.JUGGERNAUT) {
            objective.getScoreboard().resetScores(KILLS_PREFIX + kills);
            objective.getScore(KILLS_PREFIX + ++kills).setScore(3);
        }
    }

    public void updateScoreboardTime(int time) {
        time++;
        objective.getScoreboard().resetScores(TIME_PREFIX + formatTime(time));
        objective.getScore(TIME_PREFIX + formatTime(--time)).setScore(1);
    }

    public void updateArrowsShot() {
        if (role == Role.JUGGERNAUT) {
            objective.getScoreboard().resetScores(ARROW_ACCURACY_PREFIX + getArrowAccuracy() + "%");
            shotsTaken++;
            objective.getScore(ARROW_ACCURACY_PREFIX + getArrowAccuracy() + "%").setScore(7);
        }
    }

    public void updateArrowsHit() {
        if (role == Role.JUGGERNAUT) {
            objective.getScoreboard().resetScores(ARROW_ACCURACY_PREFIX + getArrowAccuracy() + "%");
            shotsHit++;
            objective.getScore(ARROW_ACCURACY_PREFIX + getArrowAccuracy() + "%").setScore(7);
        }
    }

    public void updateScoreboardJuggernautName(String name) {
        objective.getScoreboard().resetScores(ChatColor.RED + "Juggernaut: " + ChatColor.AQUA + Game.getGame().getJuggernautName());
        objective.getScore(ChatColor.RED + "Juggernaut: " + ChatColor.AQUA + name).setScore(9);
    }

    private String formatTime(int time) {
        int minutes = time / 60;
        time %= 60;
        return String.format("%02d:%02d", minutes, time);
    }

    private String getArrowAccuracy() {
        return shotsTaken == 0 ? "0.00" : String.format("%.2f", (shotsHit / shotsTaken) * 100);
    }

}
