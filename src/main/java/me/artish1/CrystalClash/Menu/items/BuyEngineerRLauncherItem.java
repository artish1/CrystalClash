package me.artish1.CrystalClash.Menu.items;

import java.util.UUID;

import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Util.MySQLUtil;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BuyEngineerRLauncherItem extends MenuItem{
	private UUID id;
	private boolean hasBought = false;
	private int PRICE = 30;
	public BuyEngineerRLauncherItem(UUID id) {
		super(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "Buy Rocket Launcher",
				getItem());
		hasBought = MySQLUtil.getBoolean(id, "engineerinfo", "rocketlauncher");
		
		this.id = id;
		addLore("Click to buy a");
		addLore("Rocket Launcher for");
		addLore("the Mechanic Class!");
		
		if(hasBought)
		{
			addLore(ChatColor.YELLOW + ChatColor.BOLD.toString() + "-----------");
			addLore(ChatColor.YELLOW + "You have already"); 
			addLore(ChatColor.YELLOW + "bought this item!");
		}else{
			addLore("Price: " + ChatColor.YELLOW.toString() + ChatColor.BOLD + PRICE); 
		}
		
		
	}
	
	private static ItemStack getItem()
	{
		ItemStack item = new ItemStack(Material.IRON_HOE);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS,ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
		return item;
	} 
	
	
	@Override
	public void onItemClicked(ItemClickedEvent e) {
		if(hasBought)
		{
			e.getPlayer().sendMessage(ChatColor.RED + "You have already bought this item!");
			return;
		}
		
		int currentPoints = MySQLUtil.getPoints(id);
		if(currentPoints < PRICE)
		{
			e.getPlayer().sendMessage(ChatColor.RED + "You don't have enough points to buy this item!");
			return;
		}
		
		MySQLUtil.subtractPoints(id, PRICE);
		MySQLUtil.setBoolean(id, "engineerinfo", "rocketlauncher", true); 
		e.getPlayer().sendMessage(ChatColor.GREEN + "Successfully bought the Rocket Launcher!");
		e.setUpdate(true);
	}
	
}
