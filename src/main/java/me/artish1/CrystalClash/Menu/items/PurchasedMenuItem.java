package me.artish1.CrystalClash.Menu.items;


import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PurchasedMenuItem extends MenuItem{

	public PurchasedMenuItem(String name) {
		super(ChatColor.GREEN.toString() +ChatColor.BOLD.toString() + "PURCHASED: "+ name,new ItemStack(Material.PAPER)); 
		addLore("You have already");
		addLore("purchased " + name); 
	}
		
	
	@Override
	public void onItemClicked(ItemClickedEvent e) {
		e.setUpdate(true);
	}
	
}
