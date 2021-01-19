package com.brandonjja.jugg.game;

import com.brandonjja.jugg.commands.handler.ChaserCommand;
import com.brandonjja.jugg.commands.handler.JuggernautCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class PlayerJT {
    Player player;
    Role role;
    Scoreboard scoreboard;
    Objective objective;

    private int applesEaten;
    private int deaths;
    private double damageDealt;
    private double damageTaken;

    public PlayerJT(Player player, Role role) {
        this.player = player;
        this.role = role;
        this.applesEaten = 0;
        this.deaths = 0;
        this.damageDealt = 0;
        this.damageTaken = 0;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    /** Gives kit items to the player based on what role they are */
    public void applyRoleKit() {
        if (role == Role.JUGGERNAUT) {
            JuggernautCommand.giveJuggernautItems(player);
        } else {
            ChaserCommand.giveChaserItems(player);
        }
    }

    public Player getPlayer() {
        return player;
    }

    private final String applesPrefix = ChatColor.GREEN + "Apples Eaten: " + ChatColor.WHITE;
    private final String deathsPrefix = ChatColor.GREEN + "Deaths: " + ChatColor.WHITE;
    private final String damageDealtPrefix = ChatColor.GREEN + "Damage Dealt: " + ChatColor.WHITE;
    private final String damageTakenPrefix = ChatColor.GREEN + "Damage Taken: " + ChatColor.WHITE;
    private final String timePrefix = ChatColor.YELLOW + "Time: " + ChatColor.WHITE;
    public void addScoreboard(int startingTime) {
        if (scoreboard != null) {
            player.setScoreboard(scoreboard);
            return;
        }

        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective("juggernaut", "dummy");
        objective.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Juggernaut Takedown");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.getScore(ChatColor.RED + "Juggernaut: " + ChatColor.AQUA + Game.getGame().getJuggernautName()).setScore(8);
        objective.getScore(" ").setScore(7);

        objective.getScore(applesPrefix + applesEaten).setScore(6);
        objective.getScore(damageDealtPrefix + damageDealt).setScore(5);
        objective.getScore(damageTakenPrefix + damageTaken).setScore(4);
        objective.getScore(deathsPrefix + deaths).setScore(3);

        objective.getScore("  ").setScore(2);

        objective.getScore(timePrefix + formatTime(startingTime)).setScore(1);

        player.setScoreboard(scoreboard);
    }

    public void updateScoreboardApples() {
        objective.getScoreboard().resetScores(applesPrefix + applesEaten);
        objective.getScore(applesPrefix + ++applesEaten).setScore(4);
    }

    public void updateScoreboardDamageDealt(double damage) {
        objective.getScoreboard().resetScores(damageDealtPrefix + damageDealt);
        damageDealt += damage;
        objective.getScore(damageDealtPrefix + damageDealt).setScore(4);
    }

    public void updateScoreboardDamageTaken(double damage) {
        objective.getScoreboard().resetScores(damageTakenPrefix + damageTaken);
        damageTaken += damage;
        objective.getScore(damageTakenPrefix + damageTaken).setScore(4);
    }

    public void updateScoreboardDeaths() {
        objective.getScoreboard().resetScores(deathsPrefix + deaths);
        objective.getScore(deathsPrefix + ++deaths).setScore(1);
    }

    public void updateScoreboardTime(int time) {
        time++;
        objective.getScoreboard().resetScores(timePrefix + formatTime(time));
        objective.getScore(timePrefix + formatTime(--time)).setScore(0);
    }

    private String formatTime(int time) {
        int minutes = time / 60;
        time %= 60;
        return String.format("%02d:%02d", minutes, time);
    }

}
