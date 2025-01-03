package com.brandonjja.jugg.game;

import org.bukkit.ChatColor;

public enum Role {

    JUGGERNAUT("Juggernaut"),
    CHASER("Chaser");

    private final String cleanName;

    Role(String cleanName) {
        this.cleanName = cleanName;
    }

    @Override
    public String toString() {
        return ChatColor.AQUA + this.cleanName;
    }
}
