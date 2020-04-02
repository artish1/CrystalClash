package me.artish1.CrystalClash.Menu.items;

import me.artish1.CrystalClash.Classes.ClassType;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TankItem extends ClassMenuItem{
	
	
	public TankItem() {
		super(ChatColor.BLUE + "Tank", new ItemStack(Material.DIAMOND_CHESTPLATE),ClassType.TANK);
		addLore("Duh.");
	}

	
	
}
