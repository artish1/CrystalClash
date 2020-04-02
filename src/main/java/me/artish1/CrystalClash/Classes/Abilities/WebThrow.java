package me.artish1.CrystalClash.Classes.Abilities;


import me.artish1.CrystalClash.Effect.EntityTrailEffect;
import me.artish1.CrystalClash.Util.Methods;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class WebThrow extends Ability implements Listener{

	public WebThrow(Player player) {
		super("Web Throw", 8, player);
		

		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	private HashSet<UUID> ids = new HashSet<UUID>();
	
	
	
	@Override
	public void onCast() {
		Snowball snowball = player.launchProjectile(Snowball.class);
		snowball.setShooter(player);
		snowball.setVelocity(player.getLocation().getDirection().multiply(3.5));
		
		
		ids.add(snowball.getUniqueId());
		new EntityTrailEffect(snowball, Particle.SNOW_SHOVEL).start();
		
	}
	
	public void loopThrough(Location loc1, Location loc2) {
		List<Location> locs = new ArrayList<Location>();

		World w = loc1.getWorld();
	   
		 int minx = Math.min(loc1.getBlockX(), loc2.getBlockX()),
	    miny = Math.min(loc1.getBlockY(), loc2.getBlockY()),
	    minz = Math.min(loc1.getBlockZ(), loc2.getBlockZ()),
	    maxx = Math.max(loc1.getBlockX(), loc2.getBlockX()),
	    maxy = Math.max(loc1.getBlockY(), loc2.getBlockY()),
	    maxz = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
	    for(int x = minx; x<=maxx;x++){
	        for(int y = miny; y<=maxy;y++){
	            for(int z = minz; z<=maxz;z++){
	                Block b = w.getBlockAt(x, y, z);
	                if(b.isEmpty()){
	                b.setType(Material.COBWEB);
	                locs.add(b.getLocation());
	                }
	            }
	        }
	    }
		Methods.startRemoveDelay(locs, 20 * 7);

	}
	
	private void spawnCobwebs(Location loc, int height, int xSide, int zSide){
		
		Location point1 = new Location(loc.getWorld(), loc.getBlockX() + xSide, loc.getBlockY() + height, loc.getBlockZ() + zSide);
		Location point2 = new Location (loc.getWorld(), loc.getBlockX() - xSide, loc.getBlockY(), loc.getBlockZ() - zSide);
		loopThrough(point1, point2);
		 
		
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e){
		if(ids.contains(e.getDamager().getUniqueId())){
			e.setCancelled(true);
			spawnCobwebs(e.getEntity().getLocation(), 2, 1, 1);
			e.getDamager().remove();
		}
	}
	
	@EventHandler
	public void onProjHit(ProjectileHitEvent e){
		if(ids.contains(e.getEntity().getUniqueId())){
			spawnCobwebs(e.getEntity().getLocation(), 2, 1, 1);
			
		}
		
		
		
	}
	
	
}
