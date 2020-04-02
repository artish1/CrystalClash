package me.artish1.CrystalClash.Menu.items;

import java.util.UUID;

import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Util.MySQLUtil;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class UpgradeBootsItem extends MenuItem{

	private ClassType type;
	private UUID id;
	private Material mat;
	
	public UpgradeBootsItem(Material mat,ClassType type, UUID id) {
		super(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "Boots", new ItemStack(mat));
		this.mat = mat;
		this.type = type;

		if(getPrice() != 0){
			addLore("Upgrade your Boots!");
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
		case LEATHER_BOOTS:
			MySQLUtil.setBoots(id, type, Material.GOLDEN_BOOTS);
			break;
		case GOLDEN_BOOTS:
			MySQLUtil.setBoots(id, type, Material.CHAINMAIL_BOOTS);

			break;
		case CHAINMAIL_BOOTS:
			MySQLUtil.setBoots(id, type, Material.IRON_BOOTS);
			break;
		case IRON_BOOTS:
			MySQLUtil.setBoots(id, type, Material.DIAMOND_BOOTS);
			break;
		case DIAMOND_BOOTS:
			break;
		case AIR:
			break;
			default:
				break;
		}
		MySQLUtil.subtractPoints(id, getPrice());
		e.setUpdate(true);
		e.getPlayer().sendMessage(ChatColor.GREEN + "You have upgraded your boots for " + type.getName()); 
	}
	
	
	private int getPrice()
	{
		switch(mat)
		{
		case LEATHER_BOOTS:
			return 15;
		case GOLDEN_BOOTS:
			return 20;
		case CHAINMAIL_BOOTS:
			if(type == ClassType.ARCHER || type == ClassType.ENGINEER)
				return 0;
			return 30;
		case IRON_BOOTS:
			
			return 45;
		case DIAMOND_BOOTS:
			if(type == ClassType.SCOUT)
				return 0;
			return 0;
			
		case AIR:
			return 0;
			default:
				return 0;
		}
		

	}
	
	
}
