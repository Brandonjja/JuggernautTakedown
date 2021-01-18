package com.brandonjja.jugg.commands.handler;

import com.brandonjja.jugg.commands.JuggernautTakedownCommand;
import com.brandonjja.jugg.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class JuggernautCommand extends JuggernautTakedownCommand {

    @Override
    public boolean execute(Player player, String[] args) {
        giveJuggernautItems(player);
        Game.setGame(new Game("map"));
        Bukkit.getServer().broadcastMessage(ChatColor.AQUA + player.getName() + ChatColor.GREEN + " is now the Juggernaut!");
        return true;
    }

    public static void giveJuggernautItems(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.clear();

        ItemStack item;

        item = new ItemStack(Material.DIAMOND_SWORD);
        addUnbreakingEnchant(item);
        addSharpness(item);
        inventory.addItem(item);

        item = new ItemStack(Material.BOW);
        addUnbreakingEnchant(item);
        addBowEnchants(item);
        inventory.addItem(item);

        item = new ItemStack(Material.DIAMOND_HELMET);
        addUnbreakingEnchant(item);
        addProtection(item);
        inventory.setHelmet(item);

        item = new ItemStack(Material.DIAMOND_CHESTPLATE);
        addUnbreakingEnchant(item);
        addProtection(item);
        inventory.setChestplate(item);

        item = new ItemStack(Material.DIAMOND_LEGGINGS);
        addUnbreakingEnchant(item);
        addProtection(item);
        inventory.setLeggings(item);

        item = new ItemStack(Material.DIAMOND_BOOTS);
        addUnbreakingEnchant(item);
        addProtection(item);
        inventory.setBoots(item);

        item = new ItemStack(Material.GOLDEN_APPLE, 64);
        inventory.addItem(item);

        item = new ItemStack(Material.WOOD, 256);
        inventory.addItem(item);

        item = new ItemStack(Material.DIAMOND_SPADE);
        addUnbreakingEnchant(item);;
        inventory.addItem(item);

        item = new ItemStack(Material.DIAMOND_AXE);
        addUnbreakingEnchant(item);
        inventory.addItem(item);

        item = new ItemStack(Material.DIAMOND_PICKAXE);
        addUnbreakingEnchant(item);
        inventory.addItem(item);
    }

    private static void addProtection(ItemStack itemStack) {
        int size = Bukkit.getOnlinePlayers().size();
        if (size > 16) {
            itemStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 6);
            itemStack.addEnchantment(Enchantment.PROTECTION_FIRE, 1);
        } else if (size > 11) {
            itemStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
            itemStack.addEnchantment(Enchantment.PROTECTION_FIRE, 1);
        } else if (size > 8) {
            itemStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
            itemStack.addEnchantment(Enchantment.PROTECTION_FIRE, 1);
        } else if (size > 3) {
            itemStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        } else {
            itemStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        }
    }

    private static void addSharpness(ItemStack itemStack) {
        int size = Bukkit.getOnlinePlayers().size();
        if (size > 16) {
            itemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
            itemStack.addEnchantment(Enchantment.KNOCKBACK, 2);
        } else if (size > 11) {
            itemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 4);
            itemStack.addEnchantment(Enchantment.KNOCKBACK, 2);
        } else if (size > 8) {
            itemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 3);
            itemStack.addEnchantment(Enchantment.KNOCKBACK, 2);
        } else if (size > 3) {
            itemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
            itemStack.addEnchantment(Enchantment.KNOCKBACK, 2);
        } else {
            itemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
            itemStack.addEnchantment(Enchantment.KNOCKBACK, 1);
        }
    }

    private static void addBowEnchants(ItemStack itemStack) {
        int size = Bukkit.getOnlinePlayers().size();
        if (size > 16) {
            itemStack.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 5);
            itemStack.addUnsafeEnchantment(Enchantment.KNOCKBACK, 4);
            itemStack.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
        } else if (size > 11) {
            itemStack.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 4);
            itemStack.addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);
            itemStack.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
        } else if (size > 8) {
            itemStack.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 3);
            itemStack.addEnchantment(Enchantment.KNOCKBACK, 2);
            itemStack.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
        } else if (size > 3) {
            itemStack.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 2);
            itemStack.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
            itemStack.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
        } else {
            itemStack.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
        }
        itemStack.addEnchantment(Enchantment.ARROW_INFINITE, 1);
    }

    /**
     * Adds Unbreaking 5 to a given item
     *
     * @param item - the item to add the enchantment to
     */
    private static void addUnbreakingEnchant(ItemStack item) {
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
    }
}
