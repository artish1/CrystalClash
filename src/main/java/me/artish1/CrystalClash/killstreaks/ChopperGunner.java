package me.artish1.CrystalClash.killstreaks;

import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.other.ClassInventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ChopperGunner extends Killstreak{
	public ChopperGunner() {
		super(ChatColor.YELLOW + "Chopper Gunner");
		setItem(Methods.createItem(Material.ANVIL, getName(), ClassInventories.createLore("Interact to activate!")));
		setKillsNeeded(15); 
	}  
	 
	
	private void stop(Player p){
		
	}
	
	
	
	@Override
	public void onActivate(final Player p) {
		
		
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Methods.getPlugin(), new Runnable(){

			@Override
			public void run() {
				stop(p); 
			}
			
		}, 20 * 15);
		
		super.onActivate(p);
	}
	

}

