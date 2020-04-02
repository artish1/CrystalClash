package me.artish1.CrystalClash.Menu.items;

import java.util.UUID;

import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Util.MySQLUtil;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class UpgradeLeggingsItem extends MenuItem{
	private ClassType type;
	private UUID id;
	private Material mat;
	
	public UpgradeLeggingsItem(Material mat,ClassType type, UUID id) {
		super(ChatColor.GOLD.toString() + ChatColor.BOLD + "Leggings", new ItemStack(mat));
		this.mat = mat;
		this.type = type;

		if(getPrice() != 0){
			addLore("Upgrade your Leggings!");
			addLore("Upgrade Price: " + ChatColor.AQUA.toString() + getPrice());
		}else{
			addLore("No upgrades available.");
		}
		this.id = id;
	}
	
	
	@Override
	public void onItemClicked(ItemClickedEvent e) {
		
		int points = MySQLUtil.getPoints(id);
		
		if(points < getPrice())
		{
			e.getPlayer().sendMessage(ChatColor.RED + "You do not have enough points to purchase this!");  
			return;
		}
		
		if(getPrice() == 0)
		{
			e.getPlayer().sendMessage(ChatColor.RED + "Maximum upgrade reached!");
			return;
		}
		
		switch(mat)
		{
		case LEATHER_LEGGINGS:
			MySQLUtil.setLeggings(id, type, Material.GOLDEN_LEGGINGS);
			break;
		case GOLDEN_LEGGINGS:
			MySQLUtil.setLeggings(id, type, Material.CHAINMAIL_LEGGINGS);

			break;
		case CHAINMAIL_LEGGINGS:
			MySQLUtil.setLeggings(id, type, Material.IRON_LEGGINGS);
			break;
		case IRON_LEGGINGS:
			MySQLUtil.setLeggings(id, type, Material.DIAMOND_LEGGINGS);
			break;
		case DIAMOND_LEGGINGS:
			break;
		case AIR:
			break;
			default:
				break;
		}
		MySQLUtil.subtractPoints(id, getPrice());
		e.setUpdate(true);
		e.getPlayer().sendMessage(ChatColor.GREEN + "You have upgraded your leggings for " + type.getName()); 
	}

	
	private int getPrice()
	{
		switch(mat)
		{
		case LEATHER_LEGGINGS:
			return 20;
		case GOLDEN_LEGGINGS:
			return 30;
		case CHAINMAIL_LEGGINGS:
			if(type == ClassType.ARCHER || type == ClassType.ENGINEER)
				return 0;
			return 40;
		case IRON_LEGGINGS:
			if(type == ClassType.SCOUT)
				return 0;
			return 50;
		case DIAMOND_LEGGINGS:
			return 0;
		case AIR:
			return 0;
			default:
				return 0;
		}
		

	}
	
	
}
