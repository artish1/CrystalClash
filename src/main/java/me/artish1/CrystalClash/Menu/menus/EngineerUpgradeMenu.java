package me.artish1.CrystalClash.Menu.menus;

import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Menu.items.BackItem;
import me.artish1.CrystalClash.Menu.items.BuyEngineerRLauncherItem;
import me.artish1.CrystalClash.Menu.items.HelmetItem;
import me.artish1.CrystalClash.Menu.items.UpgradeBootsItem;
import me.artish1.CrystalClash.Menu.items.UpgradeChestplateItem;
import me.artish1.CrystalClash.Menu.items.UpgradeLeggingsItem;
import me.artish1.CrystalClash.Menu.items.UpgradeSwordItem;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.Util.MySQLUtil;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.entity.Player;


public class EngineerUpgradeMenu extends PerPlayerUpdateMenu{
	private ClassType type = ClassType.ENGINEER;
	public EngineerUpgradeMenu() {
		super(ChatColor.DARK_GRAY + ChatColor.BOLD.toString() + "Engineer Upgrades.", 9 * 6,
				Methods.getPlugin(), Menus.getUpgradeMenu());
	}
	
	
	@Override
	protected void registerItems(Player p) {
		
		Material boots = MySQLUtil.getBoots(p.getUniqueId(), type);
		Material leggings = MySQLUtil.getLeggings(p.getUniqueId(), type);
		Material sword = MySQLUtil.getItem(p.getUniqueId(), type, "sword");
		Material helmet = MySQLUtil.getHelmet(p.getUniqueId(), type);
		Material chestplate = MySQLUtil.getChestplate(p.getUniqueId(), type);
		
		addItem(13, new HelmetItem(helmet,type,p.getUniqueId()));
		addItem(13 + 9, new UpgradeChestplateItem(chestplate,type,p.getUniqueId()));
		addItem(13 + 18, new UpgradeLeggingsItem(leggings, type,p.getUniqueId()));
		
	    addItem(22 + 18, new UpgradeBootsItem(boots,type,p.getUniqueId()));
	    addItem(3 + 18, new UpgradeSwordItem(sword,type,p.getUniqueId()));  
		
	    addItem(13 + 2, new BuyEngineerRLauncherItem(p.getUniqueId())); 
		addItem(getSize() - 1, new BackItem());
	}
	
	
}
