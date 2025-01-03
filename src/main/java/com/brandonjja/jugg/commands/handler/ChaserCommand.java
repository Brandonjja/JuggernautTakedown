package com.brandonjja.jugg.commands.handler;

import com.brandonjja.jugg.commands.JuggernautTakedownCommand;
import com.brandonjja.jugg.game.Game;
import com.brandonjja.jugg.game.PlayerJT;
import com.brandonjja.jugg.game.Role;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

public class ChaserCommand extends JuggernautTakedownCommand {

    private static final int GOLDEN_APPLE_AMOUNT = 4;
    private static final int NOTCH_APPLE_AMOUNT = 2;
    private static final int PLANKS_AMOUNT = 64 * 4;

    @Override
    public boolean execute(Player player, String[] args) {
        if (!player.isOp()) {
            return false;
        }

        Game game = Game.getGame();
        if (game == null) {
            return false;
        }

        PlayerJT jtPlayer = game.getPlayer(player);
        if (jtPlayer == null) {
            return false;
        }

        jtPlayer.setRole(Role.CHASER);
        jtPlayer.addScoreboard(game.getTime());
        giveChaserItems(player);

        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }

        player.sendMessage(ChatColor.GREEN + "You are now a " + ChatColor.AQUA + "Chaser");
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
        ItemMeta meta;

        item = new ItemStack(Material.IRON_SWORD);
        addUnbreakingEnchant(item);
        inventory.addItem(item);

        item = new ItemStack(Material.DIAMOND_HELMET);
        addUnbreakingEnchant(item);
        inventory.setHelmet(item);

        item = new ItemStack(Material.IRON_CHESTPLATE);
        addUnbreakingEnchant(item);
        inventory.setChestplate(item);

        item = new ItemStack(Material.IRON_LEGGINGS);
        addUnbreakingEnchant(item);
        inventory.setLeggings(item);

        item = new ItemStack(Material.DIAMOND_BOOTS);
        addUnbreakingEnchant(item);
        item.addEnchantment(Enchantment.DEPTH_STRIDER, 1);
        inventory.setBoots(item);

        item = new ItemStack(Material.GOLDEN_APPLE, NOTCH_APPLE_AMOUNT);
        item.setDurability((short) 1); // Notch Apple
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Notch Apple");
        item.setItemMeta(meta);
        inventory.addItem(item);

        item = new ItemStack(Material.GOLDEN_APPLE, GOLDEN_APPLE_AMOUNT);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Golden Apple");
        item.setItemMeta(meta);
        inventory.addItem(item);

        item = new ItemStack(Material.COOKED_BEEF, 64);
        inventory.addItem(item);

        item = new ItemStack(Material.WOOD, PLANKS_AMOUNT);
        inventory.addItem(item);

        item = new ItemStack(Material.STICK);
        item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Booster Stick");
        item.setItemMeta(meta);
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
