package me.artish1.CrystalClash.Classes.Abilities;

import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.Util.MySQLUtil;
import me.artish1.CrystalClash.other.ClassInventories;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class LightningStorm extends Ability{
	
	private List<Location> locs;
	private Location loc;
	private List<Location> lightLocs;
	private int radius = 8;
	
	public LightningStorm(
			Player player) {
		super("Lightning Storm", getCooldown(player.getUniqueId()), ClassInventories.getLightningMageItem(), player);
	} 
	
	private static int getCooldown(UUID id)
	{
		int cooldown = 18;
		
		if(MySQLUtil.getBoolean(id, "mageinfo", "lstormcd"))
			cooldown -=5;
		
		return cooldown;
	}
	

	@Override
	public void onCast() {
		
		loc = player.getTargetBlock(null, 70).getLocation();
		locs = Methods.circle(loc, radius, 2, true, false, 1);
		lightLocs = Methods.circle(loc, radius, 1, false, false, 0);

        showVisuals();
		delay();

	}
	
	private void igniteLightning(){
		int rNum = Methods.getRandom().nextInt(4) + 3;
		
		for(int rA = rNum; rA > 0; rA--){
			Location randomLoc = Methods.getRandomLocation(lightLocs);
			lightLocs.remove(randomLoc);
			randomLoc.getWorld().spigot().strikeLightningEffect(randomLoc,true);
		}
		Methods.createTeamExplosion(player, loc, 9, 12);
		
	}
	
	int size = 0;
	int taskId;
	private void delay(){
		
		size = locs.size() - 1;
	 taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			public void run() {
				igniteLightning();
			}
			
		}, 40);



	}

	private void showVisuals(){
	    for(Location loc : locs){
	        loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 1);
        }


    }

	
	
	
}
