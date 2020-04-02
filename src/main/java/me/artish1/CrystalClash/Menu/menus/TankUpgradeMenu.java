package me.artish1.CrystalClash.Menu.menus;

import me.artish1.CrystalClash.Menu.items.BackItem;
import me.artish1.CrystalClash.Menu.items.BuyActivationItem;
import me.artish1.CrystalClash.Util.Methods;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TankUpgradeMenu extends PerPlayerUpdateMenu{

	public TankUpgradeMenu() {
		super(ChatColor.BLUE.toString() + ChatColor.BOLD + "Tank Upgrades",
				9*6,Methods.getPlugin(), Menus.getUpgradeMenu());
	}
	
	
	@Override
	protected void registerItems(Player p) {
		BuyActivationItem enchHelmet = new BuyActivationItem(ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "Buy Helmet Enchantment!",
				new ItemStack(Material.ENCHANTED_BOOK),
				p.getUniqueId(),
				"tankinfo",
				"helmetench",
				30);
		
		enchHelmet.addDescriptionLore("Buy the " + ChatColor.RED + "Protection 1");
		enchHelmet.addDescriptionLore("enchantment for your");
		enchHelmet.addDescriptionLore("helmet!"); 
		 
		BuyActivationItem enchChest = new BuyActivationItem(ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "Buy Chestplate Enchantment!",
				new ItemStack(Material.ENCHANTED_BOOK),
				p.getUniqueId(),
				"tankinfo",
				"chestench",
				45);
		
		enchChest.addDescriptionLore("Buy the " + ChatColor.RED + "Protection 1");
		enchChest.addDescriptionLore("enchantment for your");
		enchChest.addDescriptionLore("Chestplate!"); 
		enchChest.setDonator(true);
		
		BuyActivationItem enchLegs = new BuyActivationItem(ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "Buy Leggings Enchantment!",
				new ItemStack(Material.ENCHANTED_BOOK),
				p.getUniqueId(),
				"tankinfo",
				"leggingsench",
				35);
	
		
		enchLegs.addDescriptionLore("Buy the " + ChatColor.RED + "Protection 1");
		enchLegs.addDescriptionLore("enchantment for your");
		enchLegs.addDescriptionLore("Leggings!"); 
		
		BuyActivationItem enchBoots = new BuyActivationItem(ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "Buy Boots Enchantment!",
				new ItemStack(Material.ENCHANTED_BOOK),
				p.getUniqueId(),
				"tankinfo",
				"bootsench",
				30);
		
		enchBoots.addDescriptionLore("Buy the " + ChatColor.RED + "Protection 1");
		enchBoots.addDescriptionLore("enchantment for your");
		enchBoots.addDescriptionLore("Boots!"); 
		
		
		addItem(4 + 9, enchHelmet);
		addItem(4 + 18, enchChest);
		addItem(4 + 27, enchLegs);
		addItem(4 + 36, enchBoots);
		
		
		addItem(getSize()-1, new BackItem());
	}
	
}
