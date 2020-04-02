package me.artish1.CrystalClash.Menu.items;

import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Menu.Menu;
import me.artish1.CrystalClash.Menu.menus.ShopClassMenu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class NecromancerItem extends ClassMenuItem{

	public NecromancerItem(Menu menu) {
		super(ChatColor.DARK_PURPLE + "Necromancer", new ItemStack(Material.WITHER_SKELETON_SKULL,1,(byte)1),ClassType.NECROMANCER);
		addLore("Forbidden.");
		if(menu instanceof ShopClassMenu){ 
			addLore("Price: " + ChatColor.AQUA + ClassType.GUARDIAN.getPrice()); 
		}
		
	}
	
	
	

}
