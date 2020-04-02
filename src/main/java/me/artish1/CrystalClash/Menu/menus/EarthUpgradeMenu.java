package me.artish1.CrystalClash.Menu.menus;

import me.artish1.CrystalClash.Menu.items.BackItem;
import me.artish1.CrystalClash.Menu.items.BuyActivationItem;
import me.artish1.CrystalClash.Util.Methods;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class EarthUpgradeMenu extends PerPlayerUpdateMenu{

	public EarthUpgradeMenu() {
		super(ChatColor.DARK_GRAY + ChatColor.BOLD.toString() + "Earth Upgrades",
				9 * 6,Methods.getPlugin(), Menus.getUpgradeMenu());
	}
	
	@Override
	protected void registerItems(Player p) {
		BuyActivationItem enchHelmet = new BuyActivationItem(ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "Buy Helmet Enchantment!",
				new ItemStack(Material.ENCHANTED_BOOK),
				p.getUniqueId(),
				"earthinfo",
				"helmetench",
				30);
		
		enchHelmet.addDescriptionLore("Buy the " + ChatColor.RED + "Protection 1");
		enchHelmet.addDescriptionLore("enchantment for your");
		enchHelmet.addDescriptionLore("helmet!"); 
		 
		BuyActivationItem enchChest = new BuyActivationItem(ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "Buy Chestplate Enchantment!",
				new ItemStack(Material.ENCHANTED_BOOK),
				p.getUniqueId(),
				"earthinfo",
				"chestench",
				45);
		
		enchChest.addDescriptionLore("Buy the " + ChatColor.RED + "Protection 1");
		enchChest.addDescriptionLore("enchantment for your");
		enchChest.addDescriptionLore("Chestplate!"); 
		
		addItem(4 + 9, enchHelmet);
		addItem(4 + 18, enchChest);
		
		addItem(getSize()-1, new BackItem());
	}
	
	
}
