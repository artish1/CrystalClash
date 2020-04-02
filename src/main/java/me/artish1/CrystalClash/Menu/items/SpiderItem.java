package me.artish1.CrystalClash.Menu.items;

import me.artish1.CrystalClash.Classes.ClassType;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SpiderItem extends ClassMenuItem{

	
	public SpiderItem() {
		super(ChatColor.LIGHT_PURPLE + "Spider", new ItemStack(Material.STRING),ClassType.SPIDER);
		addLore("Creepy!");
	}
	
	


	
}
