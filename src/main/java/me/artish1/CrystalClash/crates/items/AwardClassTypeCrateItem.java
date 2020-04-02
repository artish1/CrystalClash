package me.artish1.CrystalClash.crates.items;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Util.MySQLUtil;

public class AwardClassTypeCrateItem extends CrateItem{
	
	private ClassType type;
	
	public AwardClassTypeCrateItem(ClassType type) {
		super(ChatColor.GRAY + "Class Awarded: " + ChatColor.BOLD + type.getName(), type.getMenuItem().getIcon());
		this.type = type;
		
		addLore("You have been awarded");
		addLore("the " + ChatColor.BOLD + type.getName() + ChatColor.GRAY + " class!"); 
	}
	
	
	@Override
	public boolean onAward(Player player) {
		if(type.isFree())
			return false;
		if(MySQLUtil.hasClass(player.getUniqueId(), getType())){
			return false;
		}
		
		MySQLUtil.addClass(player.getUniqueId(), getType());
		player.sendMessage(ChatColor.GREEN + "You have been awarded the " + ChatColor.BOLD + type.getName() + ChatColor.GRAY + " class!");
		
		return true;
	}
	
	
	
	public ClassType getType() {
		return type;
	}
	
	
}
