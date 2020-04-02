package me.artish1.CrystalClash.Menu.items;

import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Util.MySQLUtil;
import me.artish1.CrystalClash.crates.Crate;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.inventory.ItemStack;


public class BuyCrateKeyItem extends MenuItem{
	private Crate crate;
	public BuyCrateKeyItem(Crate crate) {
		super(crate.getName() + " Key", new ItemStack(crate.getKeyMaterial()));
		addLore("Buy this key");
		addLore("to open up");
		addLore("the " + crate.getName() + ChatColor.GRAY + " later.");
		addLore("Price: " + ChatColor.YELLOW + ChatColor.BOLD.toString() + crate.getKeyPrice());
		this.crate = crate;
	}
	
	
	
	@Override
	public void onItemClicked(ItemClickedEvent e) {
		int money = MySQLUtil.getPoints(e.getPlayer().getUniqueId());
		
		if(money >= crate.getKeyPrice())
		{
			MySQLUtil.subtractPoints(e.getPlayer().getUniqueId(), crate.getKeyPrice()); 
			MySQLUtil.addCrateKeyAmount(e.getPlayer().getUniqueId(), crate.getType(), 1);
			e.getPlayer().sendMessage(ChatColor.GREEN + "Successfully purchased the " + crate.getName() + " key"); 
			e.setUpdate(true);
		}else{
			e.getPlayer().sendMessage(ChatColor.RED + "You don't have enough points to buy this key!"); 
			return;
		}
		
	}

}
