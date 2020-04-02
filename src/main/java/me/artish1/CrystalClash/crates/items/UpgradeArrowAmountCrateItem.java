package me.artish1.CrystalClash.crates.items;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Util.MySQLUtil;

public class UpgradeArrowAmountCrateItem extends CrateItem{
	private int amount;
	
	public UpgradeArrowAmountCrateItem(int amount) {
		super(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "Arrow Amount Upgrade!",
				new ItemStack(Material.ARROW,amount)); 
		addLore("You have been awarded:");
		addLore(ChatColor.GREEN.toString() + ChatColor.BOLD + amount + ChatColor.GRAY + " extra arrows");
		addLore("for the Archer Class!");
		this.amount = amount;
		}
	
	
	@Override
	public boolean onAward(Player player) {
		MySQLUtil.addAmmo(player.getUniqueId(), ClassType.ARCHER, amount); 
		player.sendMessage(ChatColor.GREEN + "You have been awarded " + amount+" extra arrows for the Archer class!");
		return true;
	}
	
	
}
