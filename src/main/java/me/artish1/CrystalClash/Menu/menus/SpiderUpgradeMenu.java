package me.artish1.CrystalClash.Menu.menus;

import me.artish1.CrystalClash.Menu.items.BackItem;
import me.artish1.CrystalClash.Menu.items.BuyActivationItem;
import me.artish1.CrystalClash.Util.Methods;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SpiderUpgradeMenu extends PerPlayerUpdateMenu{

	public SpiderUpgradeMenu() {
		super(ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD.toString() + "Spider Upgrades", 9 * 6,
				Methods.getPlugin(),Menus.getUpgradeMenu());
		
	}
	
	
	@Override
	protected void registerItems(Player p) {
		BuyActivationItem wallClimb = new BuyActivationItem(ChatColor.GRAY.toString() + ChatColor.BOLD.toString() + "Wall Climbing",
				new ItemStack(Material.LADDER),
				p.getUniqueId(),
				"spiderinfo",
				"wallclimb",
				30);
		wallClimb.addDescriptionLore("Right-Click your");
		wallClimb.addDescriptionLore("sword on a block");
		wallClimb.addDescriptionLore("to propel yourself");
		wallClimb.addDescriptionLore("upwards. Good for");
		wallClimb.addDescriptionLore("climbing walls."); 
		
		addItem(4 + 18, wallClimb);
		
		addItem(getSize()-1, new BackItem());
		
	}
	
}
