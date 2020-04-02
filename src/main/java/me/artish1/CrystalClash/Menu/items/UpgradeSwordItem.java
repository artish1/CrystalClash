package me.artish1.CrystalClash.Menu.items;

import java.util.UUID;

import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Util.MySQLUtil;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class UpgradeSwordItem extends MenuItem{
	private ClassType type;
	private UUID id;
	private Material mat;
	
	public UpgradeSwordItem(Material mat,ClassType type, UUID id) {
		super(ChatColor.GOLD.toString() + ChatColor.BOLD + "Sword", new ItemStack(mat));
		this.mat = mat;
		this.type = type;

		if(getPrice() != 0){
			addLore("Upgrade your Sword!");
			addLore("Upgrade Price: " + ChatColor.AQUA.toString() + getPrice());
		}else{
			addLore("No upgrades available.");
		}
		this.id = id;
	}
	
	
	@Override
	public void onItemClicked(ItemClickedEvent e) {
		
		int points = MySQLUtil.getPoints(id);
		int price = getPrice();
		if(points < price)
		{
			e.getPlayer().sendMessage(ChatColor.RED + "You do not have enough points to purchase this!");  
			return;
		}
		
		if(price == 0)
		{
			e.getPlayer().sendMessage(ChatColor.RED + "Maximum upgrade reached!");
			return;
		}
		
		switch(mat)
		{
		case WOODEN_SWORD:
			MySQLUtil.setItemStack(id, type, Material.STONE_SWORD, "sword");
			break;
		case STONE_SWORD:
			MySQLUtil.setItemStack(id, type, Material.GOLDEN_SWORD, "sword");
			break;
		case GOLDEN_SWORD:
			MySQLUtil.setItemStack(id, type, Material.IRON_SWORD, "sword");
			break;
		case IRON_SWORD:
			MySQLUtil.setItemStack(id, type, Material.DIAMOND_SWORD, "sword");
			break;
		case DIAMOND_LEGGINGS:
			break;
		case AIR:
			break;
			default:
				break;
		}
		MySQLUtil.subtractPoints(id, price);
		e.setUpdate(true);
		e.getPlayer().sendMessage(ChatColor.GREEN + "You have upgraded your sword for " + type.getName()); 
	}

	
	private int getPrice()
	{
		switch(mat)
		{
		case WOODEN_SWORD:
			return 10;
		case STONE_SWORD:
			if(type == ClassType.ARCHER)
				return 0;
			return 30;
		case GOLDEN_SWORD:
			return 40;
		case IRON_SWORD:
			if(type == ClassType.SCOUT || type == ClassType.EXPLOSIVES)
				return 0;
			
			return 50;
		case DIAMOND_SWORD:
			return 0;
		case AIR:
			return 0;
			default:
				return 0;
		}
		

	}
	
}
