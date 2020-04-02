package me.artish1.CrystalClash.Menu.items;

import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Menu.menus.Menus;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class UpgradeMenuItem extends MenuItem{

	public UpgradeMenuItem() {
		super(ChatColor.RED + ChatColor.BOLD.toString() + "Upgrade Shop", new ItemStack(Material.IRON_INGOT));
		addLore("Go here to upgrade");
		addLore("your current classes!");
 	}
	
	@Override
	public void onItemClicked(ItemClickedEvent e) {
		Menus.getUpgradeMenu().open(e.getPlayer());
 	}
	
}
