package me.artish1.CrystalClash.Menu.menus;

import me.artish1.CrystalClash.Menu.items.BackItem;
import me.artish1.CrystalClash.Menu.items.BuyActivationItem;
import me.artish1.CrystalClash.Util.Methods;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MageUpgradeMenu extends PerPlayerUpdateMenu{

	public MageUpgradeMenu() {
		super(ChatColor.AQUA + ChatColor.BOLD.toString() + "Mage Upgrades", 9 * 6, Methods.getPlugin(),Menus.getUpgradeMenu());
	}
	
	@Override
	protected void registerItems(Player p) {
		BuyActivationItem lightningCooldown = new BuyActivationItem(ChatColor.AQUA.toString() + ChatColor.BOLD.toString()
				+ "Lower LStorm Cooldown",
				new ItemStack(Material.NETHER_STAR),
				p.getUniqueId(),
				"mageinfo",
				"lstormcd",
				20);
		lightningCooldown.addDescriptionLore("Lower the cooldown");
		lightningCooldown.addDescriptionLore("of Lightning Storm");
		lightningCooldown.addDescriptionLore("by 5 seconds");
		
		BuyActivationItem meteorCooldown = new BuyActivationItem(ChatColor.RED.toString() + ChatColor.BOLD.toString()
				+ "Lower M-Shower Cooldown",
				new ItemStack(Material.BLAZE_ROD),
				p.getUniqueId(),
				"mageinfo",
				"mshowercd",
				20);
		meteorCooldown.addDescriptionLore("Lower the cooldown");
		meteorCooldown.addDescriptionLore("of Meteor Shower");
		meteorCooldown.addDescriptionLore("by 5 seconds");
		meteorCooldown.setDonator(true);
		
		addItem(4 + 18, lightningCooldown);
		addItem(5 + 18, meteorCooldown);
		
		addItem(getSize() - 1, new BackItem());
	}
	
}
