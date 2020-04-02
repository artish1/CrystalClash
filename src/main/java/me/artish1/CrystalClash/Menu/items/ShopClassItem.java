package me.artish1.CrystalClash.Menu.items;

import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Menu.menus.Menus;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ShopClassItem extends MenuItem{

	public ShopClassItem() {
		super(ChatColor.YELLOW + "Class Shop", new ItemStack(Material.GOLDEN_CHESTPLATE));
		addLore("Go to the Class Shop to buy");
		addLore("More classes!");  
		
	}
		
	
	@Override
	public void onItemClicked(ItemClickedEvent e) {
		
		Menus.getClassShopMenu().open(e.getPlayer());
	}
	
}	
