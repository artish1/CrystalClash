package me.artish1.CrystalClash.crates.items;

import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Util.MySQLUtil;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AwardActivationCrateItem extends CrateItem{
	private ClassType type;
	private String column;
	private String pieceName;
	private String table;
	public AwardActivationCrateItem(String name, ItemStack icon,ClassType type, String column,String pieceName) {
		super(name, icon);
		
		this.type = type;
		this.column = column;
		this.pieceName = pieceName;
		this.table = type.toString().toLowerCase() + "info";
		
		
		
	}
	
	
	
	@Override
	public boolean onAward(Player player) {
		
		if(MySQLUtil.getBoolean(player.getUniqueId(), table, column))
		{
			return false;
		}
		
		MySQLUtil.setBoolean(player.getUniqueId(), table, column, true); 
		player.sendMessage(ChatColor.GREEN + "You have been awarded the " + pieceName + " for the " + type.getName() + " class!"); 
		
		return super.onAward(player);
	}
	
}
