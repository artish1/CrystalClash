package me.artish1.CrystalClash.Classes.Abilities;

import me.artish1.CrystalClash.Util.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LandOfFire extends Ability{
    private int radius = 5;
	public LandOfFire(Player player) {
		super("Land of Fire", 15, new ItemStack(Material.BLAZE_ROD), player);
	}
	
	
	@Override
	public void onCast() {
		List<Location> range = Methods.circle(player.getLocation(), radius, 0, true, false, 0);
		for(Location loc : range){
			if(loc.getBlock().getType() != Material.AIR && !loc.getBlock().isEmpty())
				continue;
			
			loc.getBlock().setType(Material.FIRE);
		}
		
		startDelayedTask(range);
		
		
	}
	
	private void startDelayedTask(final List<Location> locs){
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			public void run() {
				for(Location loc : locs){
					if(loc.getBlock().getType() != Material.FIRE)
						continue;
					loc.getBlock().setType(Material.AIR);
					
				}
				
			}
			
		}, 20 * 5);
		
	}

}
