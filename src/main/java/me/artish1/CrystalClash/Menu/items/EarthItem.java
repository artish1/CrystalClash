package me.artish1.CrystalClash.Menu.items;

import me.artish1.CrystalClash.Classes.ClassType;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class EarthItem extends ClassMenuItem{

	public EarthItem() {
		super(ChatColor.YELLOW + "Earth", new ItemStack(Material.DIRT),ClassType.EARTH);
		addLore("Grassy ass.");
	}
	
	
	
}
