package me.artish1.CrystalClash.Menu.menus;

import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Menu.items.BackItem;
import me.artish1.CrystalClash.Menu.items.BuyActivationItem;
import me.artish1.CrystalClash.Menu.items.BuyAmountItem;
import me.artish1.CrystalClash.Menu.items.UpgradeSwordItem;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.Util.MySQLUtil;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ExplosivesUpgradeMenu extends PerPlayerUpdateMenu{

	public ExplosivesUpgradeMenu() {
		super(ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + "Explosives Upgrades!",
				9 * 6, Methods.getPlugin(), Menus.getUpgradeMenu());
	}
	
	
	@Override
	protected void registerItems(Player p) {
		BuyAmountItem addGrenades = new BuyAmountItem(ChatColor.RED + ChatColor.BOLD.toString() + "Buy more Grenades",
				new ItemStack(Material.EGG),
				"explosivesinfo",
				"grenades",
				2,
				p.getUniqueId(),
				1,
				64);
		addItem(4 + 18, addGrenades);
		
		
		BuyAmountItem addC4 = new BuyAmountItem(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Buy more C4",
				new ItemStack(Material.SPONGE),
				"explosivesinfo",
				"c4amount",
				4,
				p.getUniqueId(),
				1,
				96);
		
		addItem(5 + 18, addC4);
		
		BuyAmountItem addMines = new BuyAmountItem(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "Buy more Landmines!",
				new ItemStack(Material.STONE_PRESSURE_PLATE),
				"explosivesinfo",
				"mineamount",
				3,
				p.getUniqueId(),
				1,
				20);
		
		addItem(3 + 18, addMines);
		
		
		BuyActivationItem deathExplode = new BuyActivationItem(ChatColor.DARK_RED + ChatColor.BOLD.toString() + "Death Explosion!",
				new ItemStack(Material.GUNPOWDER),
				p.getUniqueId(),
				"explosivesinfo",
				"deathexplode",
				20);
		deathExplode.setDonator(true);
		
		deathExplode.addDescriptionLore("Explode when you die");
		deathExplode.addDescriptionLore("damaging nearby enemies."); 
		
		addItem(4 + 27, deathExplode);
		Material mat = MySQLUtil.getItem(p.getUniqueId(), ClassType.EXPLOSIVES, "sword");
		addItem(3 + 27, new UpgradeSwordItem(mat,ClassType.EXPLOSIVES,p.getUniqueId())); 
		
		
		BuyActivationItem c4Bow = new BuyActivationItem(ChatColor.GREEN.toString() + ChatColor.BOLD + "C4 Bow and Arrows!",
				new ItemStack(Material.BOW),
				p.getUniqueId(),
				"explosivesinfo",
				"c4bow",
				30);
		c4Bow.addDescriptionLore("Use the bow to");
		c4Bow.addDescriptionLore("shoot C4-Tipped ");
		c4Bow.addDescriptionLore("arrows, then ");
		c4Bow.addDescriptionLore("left click your");
		c4Bow.addDescriptionLore("bow to make the");
		c4Bow.addDescriptionLore("arrows explode!");
		addItem(4, c4Bow);
		
		if(MySQLUtil.getBoolean(p.getUniqueId(), "explosivesinfo", "c4bow"))
		{
			BuyAmountItem c4Arrows = new BuyAmountItem(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Buy more C4 Arrows!",
					new ItemStack(Material.ARROW),
					"explosivesinfo",
					"c4arrows",
					3,
					p.getUniqueId(),
					1,
					64);
			addItem(5, c4Arrows);
		}
		
		addItem(getSize() - 1,new BackItem());
	}
	

}
