package me.artish1.CrystalClash.Menu.items;

import java.util.UUID;

import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Util.MySQLUtil;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class UpgradeChestplateItem extends MenuItem{
	private ClassType type;
	private UUID id;
	private Material mat;
	
	public UpgradeChestplateItem(Material mat,ClassType type, UUID id) {
		super(ChatColor.DARK_BLUE.toString() + ChatColor.BOLD + "Chestplate", new ItemStack(mat));
		this.mat = mat;
		this.type = type;
		if(getPrice() != 0){
			addLore("Upgrade your Chestplate!");
			addLore("Upgrade Price: " + ChatColor.AQUA.toString() + getPrice());
		}else{
			addLore("No upgrades available.");
		}
		this.id = id;
	}
	
	
	@Override
	public void onItemClicked(ItemClickedEvent e) {
		
		int points = MySQLUtil.getPoints(id);
		
		if(getPrice() == 0)
		{
			e.getPlayer().sendMessage(ChatColor.RED + "Maximum upgrade reached!");
			return;
		}
		
		if(points < getPrice())
		{
			e.getPlayer().sendMessage(ChatColor.RED + "You do not have enough points to purchase this!");  
			return;
		}
		
		
		
		switch(mat)
		{
		case LEATHER_CHESTPLATE:
			MySQLUtil.setChestplate(id, type, Material.GOLDEN_CHESTPLATE);
			break;
		case GOLDEN_CHESTPLATE:
			MySQLUtil.setChestplate(id, type, Material.CHAINMAIL_CHESTPLATE);

			break;
		case CHAINMAIL_CHESTPLATE:
			MySQLUtil.setChestplate(id, type, Material.IRON_CHESTPLATE);
			break;
		case IRON_CHESTPLATE:
			MySQLUtil.setChestplate(id, type, Material.DIAMOND_CHESTPLATE);
			break;
		case DIAMOND_CHESTPLATE:
			break;
		case AIR:
			break;
			default:
				break;
		}
		MySQLUtil.subtractPoints(id, getPrice());
		e.setUpdate(true);
		e.getPlayer().sendMessage(ChatColor.GREEN + "You have upgraded your chestplate for " + type.getName()); 
	}

	
	private int getPrice()
	{
		switch(mat)
		{
		case LEATHER_CHESTPLATE:
			return 30;
		case GOLDEN_CHESTPLATE:
			return 45;
		case CHAINMAIL_CHESTPLATE:
			if(type == ClassType.ARCHER || type == ClassType.ENGINEER)
				return 0;
			return 60;
		case IRON_CHESTPLATE:
			return 80;
		case DIAMOND_CHESTPLATE:
			return 0;
		case AIR:
			return 0;
			default:
				return 0;
		}
		

	}
	
	

}
