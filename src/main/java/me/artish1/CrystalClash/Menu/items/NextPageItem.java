package me.artish1.CrystalClash.Menu.items;

import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Menu.menus.DeathCallMenu;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class NextPageItem extends MenuItem{

	public NextPageItem() {
		super("Next Page", new ItemStack(Material.ENDER_PEARL));
		addLore("Go to the next page!"); 
	}
	
	
	@Override
	public void onItemClicked(ItemClickedEvent e) {
		if(e.getMenu() instanceof DeathCallMenu)
		{
			DeathCallMenu menu = (DeathCallMenu) e.getMenu();
			menu.currentPage++;
			e.setUpdate(true);
		}
	}
	

}
