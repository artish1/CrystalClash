package me.artish1.CrystalClash.Menu.items;

import me.artish1.CrystalClash.Classes.ClassType;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AssassinItem extends ClassMenuItem{

	public AssassinItem() {
		super(ChatColor.RED + "Assassin", getEnchantedIcon(),ClassType.ASSASSIN);
		addLore("Ouch.");
	}
	
	private static ItemStack getEnchantedIcon(){
		ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
		item.addEnchantment(Enchantment.DAMAGE_ALL, 5);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setLore(null);
		item.setItemMeta(itemMeta);
		return item;
	}
	
	

	
}
