package me.artish1.CrystalClash.crates.items;

import me.artish1.CrystalClash.Util.MySQLUtil;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class C4TippedArrAmountItem extends CrateItem{
	private int amount;
	public C4TippedArrAmountItem(int amount) {
		super(ChatColor.GREEN +ChatColor.BOLD.toString() + "C4-Tipped Arrows Upgrade", new ItemStack(Material.ARROW,amount));
		this.amount = amount;
		
		addLore("You have been awarded");
		addLore(ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + amount + ChatColor.GRAY + " extra ");
		addLore(ChatColor.GREEN + "C4-Tipped Arrows" + ChatColor.GRAY + " for the");
		addLore("Bomber class!"); 
	}

	
	@Override
	public boolean onAward(Player player) {
		if(!MySQLUtil.getBoolean(player.getUniqueId(), "explosivesinfo", "c4bow"))
			return false;
		
		int currentAmount = MySQLUtil.getAmountOf(player.getUniqueId(), "explosivesinfo", "c4arrows");
		currentAmount+= amount;
		MySQLUtil.setAmountOf(player.getUniqueId(), "explosivesinfo", "c4arrows", currentAmount);
		player.sendMessage(ChatColor.GREEN + "You have been awarded " + ChatColor.BOLD.toString() + amount + ChatColor.GREEN + " extra C4-Tipped Arrows for the Bomber class!"); 
		return true;
	}
	
}
