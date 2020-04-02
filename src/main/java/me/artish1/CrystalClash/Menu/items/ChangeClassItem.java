package me.artish1.CrystalClash.Menu.items;

import me.artish1.CrystalClash.Arena.ArenaPlayer;
import me.artish1.CrystalClash.Menu.Menu;
import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Menu.menus.ClassMenu;
import me.artish1.CrystalClash.Menu.menus.Menus;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public class ChangeClassItem extends MenuItem{
	
	private Menu parentMenu;
	
	public ChangeClassItem(Menu theParent) {
		super(ChatColor.AQUA + ChatColor.BOLD.toString() + "Select Class",null);
		addLore("Select your current");
		addLore("class here!");
		addLore("Selected: " + ChatColor.BOLD.toString());
		parentMenu = theParent;
	}
	
	
	public ItemStack getIcon(ArenaPlayer ap)
	{
		icon = ap.getType().getMenuItem().getIcon();
		getLore().set(2, ChatColor.GRAY + "Current: " + ChatColor.BOLD.toString() + ap.getType().getName());
		build();
		return getIcon();
	}
	
	@Override
	public void onItemClicked(ItemClickedEvent e) {
		ClassMenu classMenu = Menus.getClassMenu();
		classMenu.setParent(parentMenu);
		classMenu.open(e.getPlayer());
		
	}
	
	
}
