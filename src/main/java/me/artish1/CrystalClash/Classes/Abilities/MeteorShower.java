package me.artish1.CrystalClash.Classes.Abilities;

import me.artish1.CrystalClash.Effect.EntityFireEffect;
import me.artish1.CrystalClash.Listeners.Classes.MageListener;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.Util.MySQLUtil;
import me.artish1.CrystalClash.other.ClassInventories;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MeteorShower extends Ability{
    private int radius = 8;
	public MeteorShower(Player player) {
		super("Meteor Shower", getCooldown(player.getUniqueId()), ClassInventories.getMeteorShowerItem(), player);
	
	}
private int id;
	private static int getCooldown(UUID id)
	{
		int cooldown = 20;
		if(MySQLUtil.getBoolean(id, "mageinfo", "mshowercd"))
			cooldown-= 5;
		return cooldown;
	}



	private int waves = 4;
	private int arrowsPerWave = 20;
	private int arrows = arrowsPerWave;

	@Override
	public void onCast() {
		Location loc = player.getTargetBlock(null, 40).getLocation();
		player.sendMessage(ChatColor.AQUA + "Meteor Shower incoming!");
		
		final List<Location> locs = Methods.circle(loc, radius, 40, false, false, 40);


        final List<Location> vLoc = Methods.circle(loc, radius, 1, true, false, 0);

        for(Location l: vLoc){
            l.getWorld().spawnParticle(Particle.FLAME, l, 1);
        }


		final Random r = new Random();
		
		
		
		
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){

			public void run() {
				if(waves <= 0){
					Bukkit.getScheduler().cancelTask(id);
					waves = 4;
					arrows = arrowsPerWave;
				}
				
				if(arrows <= 0){
					waves--;
					arrows = arrowsPerWave;
				}
				
				int rindex = r.nextInt(locs.size());
				
				Location toSpawn = locs.get(rindex);
				Snowball arrow = (Snowball) toSpawn.getWorld().spawnEntity(toSpawn, EntityType.SNOWBALL);
				arrow.setFireTicks(20 * 60); 
				new EntityFireEffect(arrow).start();
				MageListener.uuids.add(arrow.getUniqueId());
				MageListener.shooters.put(arrow.getUniqueId(), player.getUniqueId());
				arrow.setShooter(player);
				
				arrows--;
				
				
			}
			
		}, 0, 2);
		
	}
	
	
	
	
}
