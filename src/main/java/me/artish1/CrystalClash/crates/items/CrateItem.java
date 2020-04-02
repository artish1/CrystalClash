package me.artish1.CrystalClash.crates.items;


import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CrateItem {
	protected String name;
	protected ItemStack icon;
	protected List<String> lore;
	public CrateItem(String name,ItemStack icon) {
		this.name = name;
		this.icon = icon;
		lore = new ArrayList<String>();
	}
	
	public void addLore(String message)
	{
		lore.add(ChatColor.GRAY + message);
	}
	
	
	public List<String> getLore() {
		return lore;
	}
	
	/**
	 * Awards the player with the specified CrateItem.
	 * @param player
	 * The player to award it to.
	 * @return true/false whether the awarding was successful. This method will return false if the player
	 * already has the award that is not multiplicative. 
	 */
	public boolean onAward(Player player)
	{		
		return true;
	}
	
	
	private void build()
	{
		ItemMeta meta = icon.getItemMeta();
		meta.setLore(lore);
		meta.setDisplayName(getName());
		icon.setItemMeta(meta);
	}
	
	
	public ItemStack getIcon() {
		build();
		return icon;
	}
	
	public String getName() {
		return name;
	}
	
	
	
	public void setName(String name) {
		this.name = name;
	}
	
}
