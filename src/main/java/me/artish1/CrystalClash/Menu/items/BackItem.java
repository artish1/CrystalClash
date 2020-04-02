package me.artish1.CrystalClash.Menu.items;

import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BackItem extends MenuItem{
	
	public BackItem() {
		super(ChatColor.RED + "Back", new ItemStack(Material.SLIME_BALL));
		addLore("Click to go back");
	}
	
	@Override
	public void onItemClicked(ItemClickedEvent e) {
		e.setGoBack(true);
	}
	
}
