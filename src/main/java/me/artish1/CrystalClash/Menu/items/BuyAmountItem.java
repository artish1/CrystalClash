package me.artish1.CrystalClash.Menu.items;

import java.util.UUID;

import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Util.MySQLUtil;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.inventory.ItemStack;

public class BuyAmountItem extends MenuItem{
	private String table;
	private String column;
	private int price = 0;
	private int maxAmount;
	private UUID id;
	private int amount;
	
	public BuyAmountItem(String name,
			ItemStack icon,
			String table,
			String column,
			int price,UUID id,int amount,int maxAmount) {
		super(name, icon);
		
		this.table = table;
		this.column = column;
		this.id = id;
		this.amount = amount;
		this.price = price;
		this.maxAmount = maxAmount;
		
		
		addLore("Buy more of");
		addLore("this amount!"); 
		addLore(ChatColor.YELLOW.toString() + ChatColor.BOLD + "---------");
		int cAmount = MySQLUtil.getAmountOf(id, table, column);
		if(cAmount >= maxAmount){
			
			addLore("You have the max");
			addLore("amount of this!"); 
		}else{
			addLore("Amount to buy: " + ChatColor.DARK_AQUA.toString() + ChatColor.BOLD.toString() + amount);
			addLore("Price: " + ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + price);
		}
		
		addLore(ChatColor.YELLOW.toString() + ChatColor.BOLD + "---------");
		addLore("Current amount: " +ChatColor.WHITE + ChatColor.BOLD.toString() +  cAmount); 
	}
	
	
	
	@Override
	public void onItemClicked(ItemClickedEvent e) {
		int currentPoints = MySQLUtil.getPoints(id);
		if(currentPoints < price)
		{
			e.getPlayer().sendMessage("You don't have enough points to buy this!"); 
			return;
		}
		
		int cAmount = MySQLUtil.getAmountOf(id, table, column);
		if(cAmount >= maxAmount)
		{
			e.getPlayer().sendMessage(ChatColor.RED + "You have too many of these to buy more!");
			return;
		}
		
		MySQLUtil.setAmountOf(id, table, column, cAmount + amount);
		MySQLUtil.subtractPoints(id, price);
		e.getPlayer().sendMessage(ChatColor.GREEN + "Successfully purchased!");
		e.setUpdate(true);
	}
	
	
	
	
	public int getPrice() {
		return price;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
	
}
