package me.artish1.CrystalClash.Menu.items;

import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CloseItem extends MenuItem{
	
	
	
	public CloseItem() {
		super(ChatColor.RED + "Close", new ItemStack(Material.SNOWBALL));
		addLore("Click to close");
	}
	
	
	@Override
	public void onItemClicked(ItemClickedEvent e) {
		e.setClose(true);
	}
	
}
