package me.artish1.CrystalClash.Menu.items;

import me.artish1.CrystalClash.Classes.ClassType;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class EnderItem extends ClassMenuItem{

	public EnderItem() {
		super(ChatColor.DARK_PURPLE + "Ender", new ItemStack(Material.ENDER_EYE),ClassType.ENDER);
		addLore("Weeeee!");
	}
	



	
	
}
