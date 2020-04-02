package me.artish1.CrystalClash.Menu.items;

import java.util.UUID;

import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Util.MySQLUtil;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ArcherFireBarrageUpgradeItem extends MenuItem{
	
	private UUID id;
	
	public ArcherFireBarrageUpgradeItem(UUID id) {
		super(ChatColor.RED + ChatColor.BOLD.toString() + "But I set fire to the rain!", new ItemStack(Material.STICK));
		this.id = id;
		addLore("Watched it pour as I ");
		addLore("touched your face,");
		addLore("Well it burned while i cried,");
		addLore("Cause i heard it screaming");
		addLore("out your name, your name.");
		addLore("-----------");
		addLore(ChatColor.AQUA + "Purchase this to set");
		addLore(ChatColor.AQUA + "Arrow barrage arrows");
		addLore(ChatColor.AQUA + "on fire");
		addLore("-----------");
		
		if(MySQLUtil.getBoolean(id, "archerinfo", "firebarrage"))
		{
			addLore(ChatColor.GREEN + "Upgrade already ");
			addLore(ChatColor.GREEN + "bought!");
		}else{
			addLore("Price: " + ChatColor.AQUA + getPrice()); 
		}		
	}
	
	
	
	@Override
	public void onItemClicked(ItemClickedEvent e) {
		if(MySQLUtil.getBoolean(id, "archerinfo", "firebarrage"))
		{
			e.getPlayer().sendMessage(ChatColor.RED + "You have already purchased this upgrade!"); 
			return;
		}
		int currentPoints = MySQLUtil.getPoints(id);
		
		if(currentPoints < getPrice())
		{
			e.getPlayer().sendMessage("You do not have enough points to purchase this!"); 
			return; 
		}
		
		
		MySQLUtil.setBoolean(id, "archerinfo", "firebarrage", true); 
		MySQLUtil.subtractPoints(id, getPrice()); 
		e.getPlayer().sendMessage(ChatColor.GRAY + "You have purchased the Fire Arrow Barrage for " + ClassType.ARCHER.getName()); 
		e.setUpdate(true);
	}
	
	
	
	private int getPrice()
	{
		return 35;
	}

}
