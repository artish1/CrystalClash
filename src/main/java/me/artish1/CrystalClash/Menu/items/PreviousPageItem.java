package me.artish1.CrystalClash.Menu.items;

import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Menu.menus.DeathCallMenu;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PreviousPageItem extends MenuItem{

	public PreviousPageItem() {
		super("Previous Page", new ItemStack(Material.ENDER_EYE));
		addLore("Go to the previous page!");
	}
	
	
	@Override
	public void onItemClicked(ItemClickedEvent e) {
		if(e.getMenu() instanceof DeathCallMenu)
		{
			DeathCallMenu menu = (DeathCallMenu) e.getMenu();
			menu.currentPage--;
			e.setUpdate(true);
		}
	}
	
}
