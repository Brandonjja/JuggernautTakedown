package com.brandonjja.jugg.commands.handler;

import com.brandonjja.jugg.commands.JuggernautTakedownCommand;
import com.brandonjja.jugg.game.Game;
import com.brandonjja.jugg.game.PlayerJT;
import com.brandonjja.jugg.game.Role;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class JuggernautCommand extends JuggernautTakedownCommand {

    private static final int[] PLAYERS_PER_TIER = {3, 6, 9, 12};
    private static final int GOLDEN_APPLE_AMOUNT = 48;
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
        if (jtPlayer != null) {
            jtPlayer.setRole(Role.JUGGERNAUT);
            jtPlayer.addScoreboard(game.getTime());
        }

        game.updateJuggernaut(player.getName());
        giveJuggernautItems(player);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1_000_000, 0));
        Bukkit.getServer().broadcastMessage(ChatColor.AQUA + player.getName() + ChatColor.GREEN + " is now a Juggernaut!");
        return true;
    }

    /**
     * Gives the Juggernaut kit items to a desired player
     *
     * @param player the player to apply the kit to
     */
    public static void giveJuggernautItems(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.clear();

        ItemStack item;

        item = new ItemStack(Material.DIAMOND_SWORD);
        addUnbreakingEnchant(item);
        addSwordEnchants(item);
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

        item = new ItemStack(Material.GOLDEN_APPLE, GOLDEN_APPLE_AMOUNT);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Golden Apple");
        item.setItemMeta(meta);
        inventory.addItem(item);

        addNotchApples(player);

        item = new ItemStack(Material.COOKED_BEEF, 64);
        inventory.addItem(item);

        item = new ItemStack(Material.WOOD, PLANKS_AMOUNT);
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

        item = new ItemStack(Material.DIAMOND_HELMET);
        addUnbreakingEnchant(item);
        addProtection(item);
        inventory.addItem(item);

        item = new ItemStack(Material.DIAMOND_CHESTPLATE);
        addUnbreakingEnchant(item);
        addProtection(item);
        inventory.addItem(item);

        item = new ItemStack(Material.DIAMOND_LEGGINGS);
        addUnbreakingEnchant(item);
        addProtection(item);
        inventory.addItem(item);

        item = new ItemStack(Material.DIAMOND_BOOTS);
        addUnbreakingEnchant(item);
        addProtection(item);
        inventory.addItem(item);

        item = new ItemStack(Material.ARROW);
        inventory.addItem(item);
    }

    /**
     * Adds protection based on the number of players in a game
     *
     * @param item the item to add the enchantment to
     */
    private static void addProtection(ItemStack item) {
        int size = Bukkit.getOnlinePlayers().size();
        if (size > PLAYERS_PER_TIER[3]) {
            item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 6);
            item.addEnchantment(Enchantment.PROTECTION_FIRE, 1);
        } else if (size > PLAYERS_PER_TIER[2]) {
            item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
            item.addEnchantment(Enchantment.PROTECTION_FIRE, 1);
        } else if (size > PLAYERS_PER_TIER[1]) {
            item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
            item.addEnchantment(Enchantment.PROTECTION_FIRE, 1);
        } else if (size > PLAYERS_PER_TIER[0]) {
            item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        } else {
            item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        }
    }

    /**
     * Adds sword enchantments based on the number of players in a game
     *
     * @param item the item to add the enchantment to
     */
    private static void addSwordEnchants(ItemStack item) {
        int size = Bukkit.getOnlinePlayers().size();
        if (size > PLAYERS_PER_TIER[3]) {
            item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
            item.addEnchantment(Enchantment.KNOCKBACK, 2);
        } else if (size > PLAYERS_PER_TIER[2]) {
            item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 4);
            item.addEnchantment(Enchantment.KNOCKBACK, 2);
        } else if (size > PLAYERS_PER_TIER[1]) {
            item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 3);
            item.addEnchantment(Enchantment.KNOCKBACK, 2);
        } else if (size > PLAYERS_PER_TIER[0]) {
            item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
            item.addEnchantment(Enchantment.KNOCKBACK, 2);
        } else {
            item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
            item.addEnchantment(Enchantment.KNOCKBACK, 1);
        }
    }

    /**
     * Adds bow enchantments based on the number of players in a game
     *
     * @param item the item to add the enchantment to
     */
    private static void addBowEnchants(ItemStack item) {
        int size = Bukkit.getOnlinePlayers().size();
        if (size > PLAYERS_PER_TIER[3]) {
            item.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 5);
            item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 4);
            item.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
        } else if (size > PLAYERS_PER_TIER[2]) {
            item.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 4);
            item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);
            item.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
        } else if (size > PLAYERS_PER_TIER[1]) {
            item.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 3);
            item.addEnchantment(Enchantment.KNOCKBACK, 2);
            item.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
        } else if (size > PLAYERS_PER_TIER[0]) {
            item.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 2);
            item.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
            item.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
        } else {
            item.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
        }

        item.addEnchantment(Enchantment.ARROW_INFINITE, 1);
    }

    /**
     * Adds Unbreaking 5 to a given item
     *
     * @param item the item to add the enchantment to
     */
    private static void addUnbreakingEnchant(ItemStack item) {
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
    }

    private static void addNotchApples(Player player) {
        int size = Bukkit.getOnlinePlayers().size();
        ItemStack item = new ItemStack(Material.GOLDEN_APPLE);
        item.setDurability((short) 1); // Notch Apple
        if (size > PLAYERS_PER_TIER[3]) {
            item.setAmount(8);
        } else if (size > PLAYERS_PER_TIER[2]) {
            item.setAmount(6);
        } else if (size > PLAYERS_PER_TIER[1]) {
            item.setAmount(4);
        } else if (size > PLAYERS_PER_TIER[0]) {
            item.setAmount(2);
        } else {
            item.setAmount(1);
        }

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Notch Apple");
        item.setItemMeta(meta);
        player.getInventory().addItem(item);
    }
}
