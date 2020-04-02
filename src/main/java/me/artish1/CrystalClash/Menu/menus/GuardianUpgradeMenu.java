package me.artish1.CrystalClash.Menu.menus;

import me.artish1.CrystalClash.Menu.items.BackItem;
import me.artish1.CrystalClash.Menu.items.BuyActivationItem;
import me.artish1.CrystalClash.Util.Methods;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class GuardianUpgradeMenu extends PerPlayerUpdateMenu{

	public GuardianUpgradeMenu() {
		super(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Guardian Upgrades",
				9 * 6, Methods.getPlugin(), Menus.getUpgradeMenu());
	}
	
	
	@Override
	protected void registerItems(Player p) {
		
		BuyActivationItem slow = new BuyActivationItem(ChatColor.DARK_GRAY + ChatColor.BOLD.toString() + "Slow your enemies!",
				new ItemStack(Material.SLIME_BLOCK),
				p.getUniqueId(),
				"guardianinfo",
				"hitslow",
				20);
		
		slow.addDescriptionLore("Slow enemies when");
		slow.addDescriptionLore("you attack them!"); 
		
		addItem(4+ 9, slow);
		
		addItem(getSize() - 1, new BackItem());
	}
	
}
