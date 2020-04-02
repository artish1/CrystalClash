package me.artish1.CrystalClash.Menu.items;

import java.util.UUID;

import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Util.MySQLUtil;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class UpgradeAmmoItem extends MenuItem{

	private int amount;
	private ClassType type;
	
	public UpgradeAmmoItem(Material mat, ClassType type, UUID id) {
		super(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "Ammo Amount.", new ItemStack(mat));
		this.type = type;
		this.amount = MySQLUtil.getAmmo(id, type);
		
		if(getPrice() != 0)
		{
			addLore("Upgrade the");
			addLore("ammo amount by 10!");
			addLore("Current Ammo: " + ChatColor.YELLOW + amount); 
			addLore("Next upgrade: " + ChatColor.GREEN + (amount + 10));
			addLore("Price: " + ChatColor.AQUA + getPrice()); 
		}else{
			addLore("No upgrades available.");
			addLore("Current ammo:" + ChatColor.YELLOW + amount); 
		}
	}

	
	
	
	
	@Override
	public void onItemClicked(ItemClickedEvent e) {
		
		if(getPrice() == 0)
		{
			e.getPlayer().sendMessage("No upgrades available.");
			return;
		}
			
		int playerpoints = MySQLUtil.getPoints(e.getPlayer().getUniqueId());
		if(playerpoints < getPrice())
		{
			e.getPlayer().sendMessage("You don't have enough points for this!"); 
			return;
		}
		
		
		MySQLUtil.addAmmo(e.getPlayer().getUniqueId(), type, 10); 
		MySQLUtil.subtractPoints(e.getPlayer().getUniqueId(), getPrice()); 
		e.getPlayer().sendMessage(ChatColor.GRAY + "You have add more ammo for " + type.getName() + ChatColor.GRAY + ". Total ammo: " + MySQLUtil.getAmmo(e.getPlayer().getUniqueId(),type));
		e.setUpdate(true);
	}
	
	
	
	
	
	public int getPrice()
	{
		if(amount >= 750)
		{
			return 0;
		}
		
		
		return 10;
	}
}
