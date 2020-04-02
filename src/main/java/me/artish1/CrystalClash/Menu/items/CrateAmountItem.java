package me.artish1.CrystalClash.Menu.items;

import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Util.MySQLUtil;
import me.artish1.CrystalClash.crates.Crate;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CrateAmountItem extends MenuItem{
	private Crate crate;
	private Player player;
	public CrateAmountItem(Crate crate,ItemStack item,Player player) {
		super(crate.getName(), item);
		this.crate = crate;
		this.player = player;
		int crates = MySQLUtil.getCrateAmount(player.getUniqueId(), crate.getType());
		if(crates <= 0)
		{
			addLore("You don't have");
			addLore("any crates to");
			addLore("open up!");
			addLore("Earn them inside");
			addLore("the crystalclash game"); 
			return;
		}else{
			addLore("Click your keys");
			addLore("to open up one");
			addLore("of these crates!");
			addLore("You have " + ChatColor.YELLOW + ChatColor.BOLD + crates + ChatColor.GRAY + " crates."); 
		}
	}
	
	@Override
	public ItemStack getIcon() {
		build();
		icon.setAmount(MySQLUtil.getCrateAmount(player.getUniqueId(), crate.getType()));
		return icon;
	}
	
	@Override
	public void onItemClicked(ItemClickedEvent e) {	
		
	}
	
}
