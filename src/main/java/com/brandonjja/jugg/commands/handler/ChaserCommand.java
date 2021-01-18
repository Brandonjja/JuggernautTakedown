package com.brandonjja.jugg.commands.handler;

import com.brandonjja.jugg.commands.JuggernautTakedownCommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ChaserCommand extends JuggernautTakedownCommand {

    private final String chaserString = ChatColor.GREEN + "You are now a " + ChatColor.AQUA + "Chaser";
    private final static int numOfGoldenApples = 16;
    private final static int numOfPlanks = 64 * 4;

    @Override
    public boolean execute(Player player, String[] args) {
        giveChaserItems(player);

        player.sendMessage(chaserString);
        return true;
    }

    /**
     * Gives the Chaser kit to a desired player
     *
     * @param player - the player to give the Chaser items to
     */
    public static void giveChaserItems(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.clear();

        ItemStack item;

        item = new ItemStack(Material.DIAMOND_SWORD);
        addUnbreakingEnchant(item);
        inventory.addItem(item);

        item = new ItemStack(Material.DIAMOND_HELMET);
        addUnbreakingEnchant(item);
        inventory.setHelmet(item);

        item = new ItemStack(Material.DIAMOND_CHESTPLATE);
        addUnbreakingEnchant(item);
        inventory.setChestplate(item);

        item = new ItemStack(Material.DIAMOND_LEGGINGS);
        addUnbreakingEnchant(item);
        inventory.setLeggings(item);

        item = new ItemStack(Material.DIAMOND_BOOTS);
        addUnbreakingEnchant(item);
        inventory.setBoots(item);

        item = new ItemStack(Material.GOLDEN_APPLE, numOfGoldenApples);
        item.setDurability((short) 1); // Notch Apple
        inventory.addItem(item);

        item = new ItemStack(Material.WOOD, numOfPlanks);
        inventory.addItem(item);

        item = new ItemStack(Material.DIAMOND_SPADE);
        addUnbreakingEnchant(item);
        inventory.addItem(item);

        item = new ItemStack(Material.DIAMOND_AXE);
        addUnbreakingEnchant(item);
        inventory.addItem(item);

        item = new ItemStack(Material.DIAMOND_PICKAXE);
        addUnbreakingEnchant(item);
        inventory.addItem(item);

        item = new ItemStack(Material.COMPASS);
        inventory.addItem(item);
    }

    /**
     * Adds Unbreaking 2 to a given item
     *
     * @param item - the item to add the enchantment to
     */
    private static void addUnbreakingEnchant(ItemStack item) {
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 2);
    }
}
