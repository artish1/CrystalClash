package me.artish1.CrystalClash.Menu.items;

import java.util.UUID;

import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Util.MySQLUtil;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class HelmetItem extends MenuItem{
	
	private UUID id;
	private ClassType type;
	private Material mat;
	
	public HelmetItem(Material mat,ClassType type,UUID id) {
		super(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "Helmet", new ItemStack(mat));
		this.mat = mat;
		this.type = type;
		if(getPrice() != 0){
			addLore("Upgrade your Helmet!");
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
		case LEATHER_HELMET:
			MySQLUtil.setHelmet(id, type, Material.GOLDEN_HELMET);
			break;
		case GOLDEN_HELMET:
			MySQLUtil.setHelmet(id, type, Material.CHAINMAIL_HELMET);

			break;
		case CHAINMAIL_HELMET:
			MySQLUtil.setHelmet(id, type, Material.IRON_HELMET);
			break;
		case IRON_HELMET:
			MySQLUtil.setHelmet(id, type, Material.DIAMOND_HELMET);
			break;
		case DIAMOND_HELMET:
			
			break;
		case AIR:
			break;
			default:
				break;
		}
		MySQLUtil.subtractPoints(id, getPrice());
		e.setUpdate(true);
		e.getPlayer().sendMessage(ChatColor.GREEN + "You have upgraded your helmet for " + type.getName()); 
	}
	
	
	private int getPrice()
	{
		switch(mat)
		{
		case LEATHER_HELMET:
			return 21;
		case GOLDEN_HELMET:
			return 30;
		case CHAINMAIL_HELMET:
			if(type == ClassType.ARCHER || type == ClassType.ENGINEER)
				return 0;
			return 40;
		case IRON_HELMET:
			if(type == ClassType.SCOUT)
				return 0;
			return 60;
			default:
				return 0;
		}
	}
	
}
