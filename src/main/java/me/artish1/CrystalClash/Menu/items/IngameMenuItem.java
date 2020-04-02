package me.artish1.CrystalClash.Menu.items;

import java.util.HashSet;
import java.util.UUID;

import me.artish1.CrystalClash.Arena.Arena;
import me.artish1.CrystalClash.Arena.ArenaManager;
import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class IngameMenuItem extends MenuItem{
	
	private static HashSet<UUID> tips = new HashSet<UUID>();
	
	private int price;
	
	private ItemStack item;
	private int limit = 0;
	public IngameMenuItem(String name, ItemStack icon, int price) {
		super(name, icon);
		this.price = price;
	}
	
	public void showPrice(){
		addLore("Price: " + ChatColor.AQUA + getPrice());
	}
	
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	@Override 
	public void build() {
		ItemMeta meta = icon.getItemMeta();
		meta.setDisplayName(name); 
		meta.setLore(lore);
		icon.setItemMeta(meta);
	}
	
	public int getLimit() {
		return limit;
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	
	public void setItem(ItemStack item) {
		this.item = item;
	}
	
	public int getPrice() {
		return price;
	}
	
	
	public void setPrice(int price) {
		this.price = price;
	}
	
	
	
	@Override
	public void onItemClicked(ItemClickedEvent e) {
		Player player = e.getPlayer();
		if(!ArenaManager.isInArena(player))
			return;
		
		if(limit != 0){
			if(player.getInventory().contains(getItem())){
				player.sendMessage(ChatColor.RED + "Sorry, you may only have 1 of these!");
				return;
			}
		}
		
		
		Arena arena = ArenaManager.getArena(player);
		if(arena.getArenaPlayer(player).hasEnough(getPrice())){ 
			arena.getArenaPlayer(player).subtractMoney(getPrice());
			arena.getArenaPlayer(player).getBought().add(getItem());
			player.sendMessage(ChatColor.GRAY + "You have bought the " + getName());
	 		player.getInventory().addItem(getItem());
		}else{ 
			player.sendMessage(ChatColor.RED + "You don't have enough funds to buy this item.");
			player.sendMessage(ChatColor.RED + "Your money: " + arena.getArenaPlayer(player).getMoney() + ", Price of Item: " + getPrice());
		}
		if(!tips.contains(player.getUniqueId())){
			player.sendMessage(ChatColor.GREEN + "Tip: All Items will be placed in your inventory and will never be lost even after you die.");
			player.sendMessage(ChatColor.GREEN + "Tip: the amount of money you have is shown by your level(XP Bar).");
			tips.add(player.getUniqueId());
		}
	}
	
	
}
