package me.artish1.CrystalClash.Menu.items;

import me.artish1.CrystalClash.Classes.ClassType;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ScoutItem extends ClassMenuItem{

	public ScoutItem() {
		super(ChatColor.AQUA + "Scout", new ItemStack(Material.IRON_BOOTS),ClassType.SCOUT);
		addLore("Whoosh!");
	}
		
	

	
}
