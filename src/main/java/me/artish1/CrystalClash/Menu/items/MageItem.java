package me.artish1.CrystalClash.Menu.items;

import me.artish1.CrystalClash.Classes.ClassType;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
 
public class MageItem extends ClassMenuItem{
	

	
	public MageItem() {
		super(ChatColor.AQUA + "Mage", new ItemStack(Material.NETHER_STAR),ClassType.MAGE);
		addLore("It's magical!");
	}
	 
	
	

	
	
}
