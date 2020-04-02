package me.artish1.CrystalClash.killstreaks;

import me.artish1.CrystalClash.Menu.menus.DeathCallMenu;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.other.ClassInventories;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class DeathCall extends Killstreak{

	public DeathCall() {
		super(ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "Death Call");
		setKillsNeeded(5); 
		setItem(Methods.createItem(Material.PAPER, ChatColor.DARK_PURPLE + "Death Call", ClassInventories.createLore("Click to activate!"))); 
		
	}
	
	
	@Override
	public void onActivate(Player p) {
		p.sendMessage(ChatColor.RED + "Calling death...");
		DeathCallMenu menu = new DeathCallMenu(9*5, Methods.getPlugin(),this,p);
		menu.open(p);
		sendDeathMessage(p, "Who do you want? ;)"); 
		DeathCallMenu.deathCallMenus.add(menu);
	}
	
	
	public static void sendDeathMessage(Player p,String message){
		p.sendMessage(ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "Death" + ChatColor.GRAY +": "  + message);
	}
	
}
