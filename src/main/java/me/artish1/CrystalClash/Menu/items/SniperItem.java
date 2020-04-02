package me.artish1.CrystalClash.Menu.items;


import me.artish1.CrystalClash.Classes.ClassType;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SniperItem extends ClassMenuItem{

	public SniperItem() {
		super(ChatColor.RED + "Sniper", new ItemStack(Material.BLAZE_ROD),ClassType.SNIPER);
		addLore("Focus.");
	}
	

	
	
}
