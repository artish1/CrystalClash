package me.artish1.CrystalClash.Menu.items;

import java.util.ArrayList;
import java.util.List;

import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Menu.events.ItemClickedEventHandler;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuItem implements ItemClickedEventHandler{
	protected String name;
	protected ItemStack icon;
	protected List<String> lore;
	
	
	
	public MenuItem(String name, ItemStack icon) {
		lore = new ArrayList<String>();
		this.icon = icon;
		this.name = name;
		
	}

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    public String getName() {
		return name;
	}
	
	public List<String> getLore() {
		return lore;
	}
	
	public void addLore(String...strings){
		List<String> o = new ArrayList<>();
		for(String s : strings){
			o.add(ChatColor.GRAY + s);
		}
		for(String s: o){
			lore.add(s);
		}
	}
	
	public ItemStack getIcon() {
		build();
		return icon;
	}
	
	public void build(){
		ItemMeta meta = icon.getItemMeta();
		if(meta == null)
			return;
		
		meta.setDisplayName(name);
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS);
		icon.setItemMeta(meta); 
	}
	
	

	@Override
	public void onItemClicked(ItemClickedEvent e) { 
		e.getPlayer().sendMessage(ChatColor.GRAY + "You have picked the " + getName() + " class.");
	}
	
	
	
	
}
