package com.brandonjja.jugg.game;

import org.bukkit.ChatColor;

public enum Role {
    JUGGERNAUT("Juggernaut"), CHASER("Chaser");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return ChatColor.AQUA + this.name;
    }
}
