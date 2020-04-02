package me.artish1.CrystalClash.Menu.items;

import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Menu.Menu;
import me.artish1.CrystalClash.Menu.menus.ShopClassMenu;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GuardianItem extends ClassMenuItem {
	
	
	public GuardianItem(Menu menu) {
		super(ChatColor.YELLOW + "Guardian", new ItemStack(Material.GOLDEN_SWORD),ClassType.GUARDIAN);
		addLore("Holy."); 
		
		
		if(menu instanceof ShopClassMenu){ 
			addLore("Price: " + ChatColor.AQUA + ClassType.GUARDIAN.getPrice()); 
		}
	}
	
	
	
	
	
}
