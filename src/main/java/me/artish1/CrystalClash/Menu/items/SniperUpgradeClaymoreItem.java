package me.artish1.CrystalClash.Menu.items;

import java.util.UUID;

import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Util.MySQLUtil;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SniperUpgradeClaymoreItem extends MenuItem{
	
	private UUID id;
	private int currentAmount;
	
	public SniperUpgradeClaymoreItem(UUID id) {
		super(ChatColor.GOLD.toString() + ChatColor.BOLD + "Claymore", new ItemStack(Material.REDSTONE_LAMP));
		this.id = id;
		currentAmount = MySQLUtil.getAmountOf(id, "sniperinfo", "claymore");
		if(currentAmount >=5){
			addLore("Maximum amount of");
			addLore("Claymore reached.");
		}else{
			addLore("Purchase to add one");
			addLore("claymore to your ");
			addLore("sniper inventory.");
			addLore("----------");
			addLore("Price: " + ChatColor.AQUA + getPrice()); 
		}
	}
	
	
	@Override
	public void onItemClicked(ItemClickedEvent e) {
		if(currentAmount >= 5)
		{
			e.getPlayer().sendMessage(ChatColor.RED + "You've already reached the maximum amount of claymores!");
			return;
		}
		if(MySQLUtil.getPoints(id) < getPrice())
		{
			e.getPlayer().sendMessage(ChatColor.RED + "You don't have enough points to purchase this!"); 
			return;
		}
		int amounts = MySQLUtil.getAmountOf(id, "sniperinfo", "claymore");
		amounts++;
		MySQLUtil.setAmountOf(id, "sniperinfo", "claymore", amounts); 
		e.getPlayer().sendMessage(ChatColor.GRAY + "You have added a claymore! Total: " + amounts); 
		e.setUpdate(true);
		
	}
	
	
	
	
	private int getPrice()
	{
		return 15;
	}

}
