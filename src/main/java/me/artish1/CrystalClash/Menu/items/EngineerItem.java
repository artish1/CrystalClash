package me.artish1.CrystalClash.Menu.items;

import me.artish1.CrystalClash.Classes.ClassType;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class EngineerItem extends ClassMenuItem{
	
	
	public EngineerItem() {
		super(ChatColor.DARK_GRAY + "Engineer", new ItemStack(Material.IRON_PICKAXE),ClassType.ENGINEER);
		addLore("Mechanical.");
	}
	
	
	
}
