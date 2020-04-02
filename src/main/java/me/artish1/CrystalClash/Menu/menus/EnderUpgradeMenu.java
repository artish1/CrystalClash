package me.artish1.CrystalClash.Menu.menus;

import me.artish1.CrystalClash.Menu.items.BackItem;
import me.artish1.CrystalClash.Menu.items.BuyActivationItem;
import me.artish1.CrystalClash.Util.Methods;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class EnderUpgradeMenu extends PerPlayerUpdateMenu{

	public EnderUpgradeMenu() {
		super(ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "Ender Upgrades!",
				9 * 6, Methods.getPlugin(), Menus.getUpgradeMenu());
	}
	
	
	@Override
	protected void registerItems(Player p) {
		
		BuyActivationItem enchHelmet = new BuyActivationItem(ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "Buy Helmet Enchantment!",
				new ItemStack(Material.ENCHANTED_BOOK),
				p.getUniqueId(),
				"enderinfo",
				"helmetench",
				30);
		
		enchHelmet.addDescriptionLore("Buy the " + ChatColor.RED + "Protection 1");
		enchHelmet.addDescriptionLore("enchantment for your");
		enchHelmet.addDescriptionLore("helmet!"); 
		 
		BuyActivationItem enchSword = new BuyActivationItem(ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "Buy Sword Enchantment!",
				new ItemStack(Material.ENCHANTED_BOOK),
				p.getUniqueId(),
				"enderinfo",
				"swordench",
				20);
		enchSword.setDonator(true);
		enchSword.addDescriptionLore("Buy the " + ChatColor.RED + "Sharpness 1");
		enchSword.addDescriptionLore("enchantment for your");
		enchSword.addDescriptionLore("Sword!"); 		
		
		BuyActivationItem enchChest = new BuyActivationItem(ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "Buy Chestplate Enchantment!",
				new ItemStack(Material.ENCHANTED_BOOK),
				p.getUniqueId(),
				"enderinfo",
				"chestench",
				45);
		
		enchChest.addDescriptionLore("Buy the " + ChatColor.RED + "Protection 1");
		enchChest.addDescriptionLore("enchantment for your");
		enchChest.addDescriptionLore("Chestplate!"); 
		
		BuyActivationItem enchLegs = new BuyActivationItem(ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "Buy Leggings Enchantment!",
				new ItemStack(Material.ENCHANTED_BOOK),
				p.getUniqueId(),
				"enderinfo",
				"leggingsench",
				35);
		
		enchLegs.addDescriptionLore("Buy the " + ChatColor.RED + "Protection 1");
		enchLegs.addDescriptionLore("enchantment for your");
		enchLegs.addDescriptionLore("Leggings!"); 
		
		BuyActivationItem enchBoots = new BuyActivationItem(ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "Buy Boots Enchantment!",
				new ItemStack(Material.ENCHANTED_BOOK),
				p.getUniqueId(),
				"enderinfo",
				"bootsench",
				30);
		
		enchBoots.addDescriptionLore("Buy the " + ChatColor.RED + "Protection 1");
		enchBoots.addDescriptionLore("enchantment for your");
		enchBoots.addDescriptionLore("Boots!"); 
		
		
		addItem(4 + 9, enchHelmet);
		addItem(4 + 18, enchChest);
		addItem(4 + 27, enchLegs);
		addItem(4 + 36, enchBoots);
		addItem(2 + 18, enchSword);
		
		addItem(getSize() - 1, new BackItem());
	}

}
