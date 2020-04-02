package me.artish1.CrystalClash.Menu.menus;

import me.artish1.CrystalClash.Menu.items.BackItem;
import me.artish1.CrystalClash.Menu.items.BuyActivationItem;
import me.artish1.CrystalClash.Util.Methods;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class NecromancerUpgradeMenu extends PerPlayerUpdateMenu{

	public NecromancerUpgradeMenu() {
		super(ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "Necromancer Upgrades", 
				9 * 6, Methods.getPlugin(), Menus.getUpgradeMenu());
	}
	
	
	@Override
	protected void registerItems(Player p) {
		BuyActivationItem summonSkCd = new BuyActivationItem(ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD.toString()
				+ "Lower Summ. Skel. Cooldown",
				new ItemStack(Material.BONE),
				p.getUniqueId(),
				"necromancerinfo",
				"sskcd",
				20);
		summonSkCd.addDescriptionLore("Lower the cooldown");
		summonSkCd.addDescriptionLore("of Summon Skeleton");
		summonSkCd.addDescriptionLore("by 7 seconds");

		BuyActivationItem summonZbCd = new BuyActivationItem(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD.toString()
				+ "Lower Summ. Skel. Cooldown",
				new ItemStack(Material.ROTTEN_FLESH),
				p.getUniqueId(),
				"necromancerinfo",
				"szbcd",
				20);
		summonZbCd.addDescriptionLore("Lower the cooldown");
		summonZbCd.addDescriptionLore("of Summon Zombie");
		summonZbCd.addDescriptionLore("by 7 seconds");
		summonZbCd.setDonator(true);
		
		addItem(3, summonSkCd);
		addItem(4, summonZbCd);
		
		
		addItem(getSize()- 1, new BackItem());
	}
	
}
