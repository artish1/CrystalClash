package me.artish1.CrystalClash.crates.items;


import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Util.MySQLUtil;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class UpgradeAmountCrateItem extends CrateItem{
	private int amount;
	private String column;
	private String pieceName;
	private ClassType type;
	public UpgradeAmountCrateItem(String name, ItemStack icon,ClassType type, String column, int amount,String pieceName) {
		super(name, icon);
		this.amount = amount;
		this.column = column;
		this.type = type;
		this.pieceName = pieceName;
		addLore("You have been awarded:");
		addLore(ChatColor.GREEN + ChatColor.BOLD.toString() + amount + ChatColor.GRAY + " extra " + pieceName + "(s)");
		addLore("for the " + type.getName() + ChatColor.GRAY + " class.");
	}
	
	
	@Override
	public boolean onAward(Player player) {
		String table = type.toString().toLowerCase() + "info";
		int currentAmount = MySQLUtil.getAmountOf(player.getUniqueId(), table, column);
		currentAmount+= amount;
		MySQLUtil.setAmountOf(player.getUniqueId(), table, column, currentAmount);
		player.sendMessage(ChatColor.GREEN + "You have been awarded " + amount + " extra " + pieceName + "(s) for the " + type.getName() + ChatColor.GREEN + " class!");
		
		return true;
	}
	
}
