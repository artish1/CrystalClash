package me.artish1.CrystalClash.Menu.items;

import me.artish1.CrystalClash.other.ClassInventories;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LifeDrainItem extends IngameMenuItem{

	public LifeDrainItem() {
		super(ChatColor.DARK_RED + "Vampire's Eye", LifeDrainItem.getVampireEye(), 225);
		setItem(getVampireEye());
		setLimit(1);
		addLore("You gain 20% LifeDrain");
		showPrice();
	}
	
	public static ItemStack getVampireEye(){
		ItemStack item = new ItemStack(Material.SPIDER_EYE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_RED + "Vampire's Eye");
		meta.setLore(ClassInventories.createLore("You gain 20% LifeDrain"));
		item.setItemMeta(meta);
		return item;
	}
	
	
}
