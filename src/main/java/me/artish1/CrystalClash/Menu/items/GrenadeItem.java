package me.artish1.CrystalClash.Menu.items;

import me.artish1.CrystalClash.other.ClassInventories;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GrenadeItem extends IngameMenuItem{

	public GrenadeItem() {
		super(ChatColor.RED + "BGrenade", GrenadeItem.getGrenadeItem(), 15);
		addLore("Throw to explode.");
		setItem(getGrenadeItem());
		showPrice();
	}
	
	
	public static ItemStack getGrenadeItem(){
		ItemStack item = new ItemStack(Material.EGG);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "BGrenade");
		meta.setLore(ClassInventories.createLore("Throw to explode."));
		item.setItemMeta(meta);
		return item;
	}

}
