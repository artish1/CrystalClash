package me.artish1.CrystalClash.Menu.items;

import java.util.ArrayList;
import java.util.List;

import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Menu.menus.OpenCrateMenu;
import me.artish1.CrystalClash.Util.MySQLUtil;
import me.artish1.CrystalClash.crates.Crate;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CrateItem extends MenuItem{
	private Crate crate;
	public CrateItem(Crate crate, ItemStack icon) {
		super(crate.getName(), icon);
		this.crate = crate;
		
	}
	
	
	public ItemStack getIcon(Player player) {
		build();
		icon.setAmount(MySQLUtil.getCrateAmount(player.getUniqueId(), crate.getType()));
		ItemMeta meta = icon.getItemMeta();
		List<String> lores = new ArrayList<String>();
		lores.add(ChatColor.GRAY + "You have:");
		lores.add(ChatColor.YELLOW.toString() + ChatColor.BOLD + MySQLUtil.getCrateAmount(player.getUniqueId(), crate.getType())
				+ " " + crate.getName() + "(s)");
		icon.setItemMeta(meta);
		return icon;
	}
	
	@Override
	public void onItemClicked(ItemClickedEvent e) {
		new OpenCrateMenu(crate).open(e.getPlayer());
	}
	
}
