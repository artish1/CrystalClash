package me.artish1.CrystalClash.Menu.items;

import me.artish1.CrystalClash.Classes.ClassType;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ExplosivesItem extends ClassMenuItem{

	public ExplosivesItem() {
		super(ChatColor.LIGHT_PURPLE + "Bomber", new ItemStack(Material.TNT),ClassType.EXPLOSIVES);
		addLore( "Boom.");
	}
	
	

}
