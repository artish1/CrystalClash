package me.artish1.CrystalClash.Menu.items;

import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.crates.Crate;

import org.bukkit.inventory.ItemStack;

public class CrateBorderItem extends MenuItem{

	public CrateBorderItem(Crate crate) {
		super(crate.getName(), new ItemStack(crate.getBorderMaterial()));
	}
	
	
	@Override
	public void onItemClicked(ItemClickedEvent e) {
		
	}

}
