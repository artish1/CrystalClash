package me.artish1.CrystalClash.Menu.items;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.artish1.CrystalClash.CrystalClash;
import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Util.MySQLUtil;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BuyActivationItem extends MenuItem{
	private boolean hasActivated = false;
	private boolean activate = true;
	private UUID id;
	private int price = 0;
	private String table;
	private String column;
	private boolean donator = false;
	
	private List<String> descriptionLore = new ArrayList<String>();
	private List<String> finalLore = new ArrayList<String>();
	
	public BuyActivationItem(String name, ItemStack icon,UUID id,String table,String column,int price) {
		super(name, icon);
		this.id = id;
		this.table = table;
		this.column = column;
		this.price = price;
		addLore("Buy to activate");
		addLore("this item/upgrade!");
		hasActivated = MySQLUtil.getBoolean(id, table, column);
		addLore(ChatColor.YELLOW + ChatColor.BOLD.toString() + "--------"); 
		
		if(hasActivated)
		{
			addLore("You have already");
			addLore("bought this!"); 
		}else{
			addLore("Price: " + ChatColor.YELLOW.toString() + ChatColor.BOLD + price);
		}
		
		
	}
	
	public boolean isDonator() {
		return donator;
	}
	
	public void setDonator(boolean donator) {
		this.donator = donator;
	}
	
	public void addDescriptionLore(String message)
	{
		descriptionLore.add(ChatColor.GRAY + message);
	}
	

	@Override
	public void build() {
		ItemMeta meta = icon.getItemMeta();
		if(meta == null)
			return;
		
		for(String s : descriptionLore)
		{
			finalLore.add(s);
		}
		
		if(descriptionLore.size() > 0)
		{
			finalLore.add(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "-----------");
		}
		
		for(String s : lore)
		{
			finalLore.add(s);
		}
		
		
		meta.setDisplayName(name);
		meta.setLore(finalLore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS);
		icon.setItemMeta(meta); 
	}
	
	@Override
	public void onItemClicked(ItemClickedEvent e) {
		if(hasActivated)
		{
			e.getPlayer().sendMessage(ChatColor.RED + "You have already bought this activation!"); 
			return;
		}
		
		int currentPoints = MySQLUtil.getPoints(id);
		if(currentPoints < price)
		{
			e.getPlayer().sendMessage(ChatColor.RED + "You do not have enough points to buy this!");
			return;
		} 
		
		if(donator)
		{
			if(!e.getPlayer().hasPermission(CrystalClash.DONATOR_PERMISSION))
			{
				e.getPlayer().sendMessage(ChatColor.RED + "You must be a donator to purchase this upgrade!");
				return;
			}
		}
		
		
		MySQLUtil.setBoolean(id, table, column, activate);
		MySQLUtil.subtractPoints(id, price); 
		e.getPlayer().sendMessage(ChatColor.GREEN + "Successfully purchased activation!"); 
		e.setUpdate(true);
	}
	
	
	
	
	public void setActivate(boolean activate) {
		this.activate = activate;	
	}

}
