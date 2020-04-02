package me.artish1.CrystalClash.Menu.items;

import me.artish1.CrystalClash.other.ClassInventories;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FlashBombItem extends IngameMenuItem{

	public FlashBombItem() {
		super(ChatColor.AQUA + "FlashBomb", FlashBombItem.getFlashBomb(), 20);
		setItem(getFlashBomb());
		addLore("Throw to blind enemies.");
		showPrice();
	}
	
	
	public static ItemStack getFlashBomb(){
		ItemStack item = new ItemStack(Material.SNOWBALL);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "FlashBomb");
		meta.setLore(ClassInventories.createLore("Throw to blind enemies."));
		item.setItemMeta(meta);
		return item;
	}

}
