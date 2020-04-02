package me.artish1.CrystalClash.Menu.items;

import java.util.UUID;

import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Util.MySQLUtil;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class UpgradeAssassinSwordPoisonItem extends MenuItem{
	private boolean hasUpgrade = false;
	private final int PRICE = 45;
	private UUID id;
	public UpgradeAssassinSwordPoisonItem(UUID id) {
		super(ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + "Add Poison", getItemStack());
		this.id = id;
		hasUpgrade = MySQLUtil.getBoolean(id, "assassininfo", "poisonedsword");
		 
		addLore("Buy this to");
		addLore("dip your sword");
		addLore("in poison! It's");
		addLore("a great failsafe ;)");
		
		
		if(hasUpgrade)
		{
			addLore(ChatColor.YELLOW + ChatColor.BOLD.toString() + "-----------");
			addLore(ChatColor.GREEN + "You already have"); 
			addLore(ChatColor.GREEN + "have this upgrade!");
		}else{
			addLore("Price: " + ChatColor.YELLOW.toString() + ChatColor.BOLD + PRICE); 
		}
	}
	
	private static ItemStack getItemStack()
	{
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.addCustomEffect(new PotionEffect(PotionEffectType.POISON,99999,0), true);
		item.setItemMeta(meta);
		return item;
	}
	
	@Override
	public void onItemClicked(ItemClickedEvent e) {
		if(hasUpgrade)
		{
			e.getPlayer().sendMessage(ChatColor.RED + "You already have this upgrade!");
			return;
		}
		int currentPoints = MySQLUtil.getPoints(id);
		
		if(currentPoints < PRICE)
		{
			e.getPlayer().sendMessage(ChatColor.RED + "You don't have enough points to purchase this upgrade!");
			return;
		}
		
		MySQLUtil.subtractPoints(id, PRICE);
		MySQLUtil.setBoolean(id, "assassininfo", "poisonedsword", true); 
		
		e.getPlayer().sendMessage(ChatColor.GREEN+ "You have purchased the Poison Sword Upgrade for the Assassin!");
		e.setUpdate(true);
	}
	
}
