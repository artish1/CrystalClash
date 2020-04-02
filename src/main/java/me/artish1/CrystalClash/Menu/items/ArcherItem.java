package me.artish1.CrystalClash.Menu.items;

import me.artish1.CrystalClash.Classes.ClassType;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ArcherItem extends ClassMenuItem{

	public ArcherItem() {
		super(ChatColor.GREEN + "Archer", new ItemStack(Material.ARROW,16),ClassType.ARCHER);
		addLore("Pointy!");
	}
	
	

}
