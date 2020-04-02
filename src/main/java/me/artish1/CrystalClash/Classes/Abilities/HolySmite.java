package me.artish1.CrystalClash.Classes.Abilities;

import me.artish1.CrystalClash.Util.Methods;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class HolySmite extends Ability{
 
	public HolySmite(Player player) {
		super("Holy Smite", 10, player);
	}
	int counter = 12;
	private int id;
	@Override
	public void onCast() {
		
		final Vector vec = player.getLocation().getDirection();
		vec.multiply(1.9);
		final Location lightning = player.getLocation();
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(getPlugin(), new Runnable(){

			public void run() {
				
				if(counter <= 0)
					Bukkit.getScheduler().cancelTask(id);
				
				
				counter--;
				lightning.add(vec);
				lightning.getWorld().spigot().strikeLightningEffect(lightning, true);
				Methods.createTeamExplosion(player, lightning, 4, 8);
				
			}
			
		}, 0, 1);
		
		
		super.onCast();
	}
	
}
