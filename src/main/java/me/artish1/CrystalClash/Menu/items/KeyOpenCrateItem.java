package me.artish1.CrystalClash.Menu.items;

import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Util.MySQLUtil;
import me.artish1.CrystalClash.crates.Crate;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KeyOpenCrateItem extends MenuItem{
	private Crate crate;
	private Player player;
	public KeyOpenCrateItem(Crate crate,Player player) {
		super(crate.getName() + " Key.", new ItemStack(crate.getKeyMaterial()));
		this.crate = crate;
		int keys = MySQLUtil.getCrateKeyAmount(player.getUniqueId(), crate.getType());
		if(keys <= 0)
		{
			addLore("You don't have");
			addLore("any keys to ");
			addLore("open this crate!");
			
		}else{
			addLore("Click to open");
			addLore("one of your crates!");
			addLore("You have " + ChatColor.YELLOW + ChatColor.BOLD.toString() + keys + ChatColor.GRAY + " keys.");
		}
		this.player = player;
	}
	
	
	@Override
	public ItemStack getIcon() {
		build();
		icon.setAmount(MySQLUtil.getCrateKeyAmount(player.getUniqueId(), crate.getType()));
		return icon;
	}
	
	
	@Override
	public void onItemClicked(ItemClickedEvent e) {
		int keys = MySQLUtil.getCrateKeyAmount(e.getPlayer().getUniqueId(), crate.getType());
		if(keys <= 0)
		{
			e.getPlayer().sendMessage(ChatColor.RED + "You don't have enough keys to open up this crate!");
			return;
		}
		
		int crates = MySQLUtil.getCrateAmount(e.getPlayer().getUniqueId(),crate.getType());
		if(crates <= 0)
		{
			e.getPlayer().sendMessage(ChatColor.RED + "You don't have any crates to open!"); 
			return;
		}
		
		
		crate.open(player,e.getMenu());
		MySQLUtil.subtractCrateAmount(e.getPlayer().getUniqueId(), crate.getType(), 1);
		MySQLUtil.subtractCrateKeyAmount(e.getPlayer().getUniqueId(),crate.getType(), 1);
		
	}
	
	
}
