package me.artish1.CrystalClash.Menu.items;

import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Menu.menus.Menus;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ShopKeysItem extends MenuItem{

	public ShopKeysItem() {
		super(ChatColor.BLUE + "Key Shop", new ItemStack(Material.ENDER_CHEST)); 
		addLore("Go here to buy");
		addLore("Keys to unlock crates!"); 
	}
	
	
	@Override
	public void onItemClicked(ItemClickedEvent e) {
		Menus.getShopKeysMenu().open(e.getPlayer()); 
	}
	
}
