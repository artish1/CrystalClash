package me.artish1.CrystalClash.other;

import me.artish1.CrystalClash.Util.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class ParticleProjectile {
	private Particle particle;
	private Location startingLocation;
	private double damage;
	private Location currentLocation;
	private Vector direction;
	public ParticleProjectile(Particle displayParticle, Location startingLocation, double damage, Vector direction) {
		this.particle = displayParticle;
		this.startingLocation = startingLocation;
		this.damage = damage;
		this.direction = direction;
	}
	
	
	private int taskId;
	
	private static boolean canHitEntity(Location current, LivingEntity e)
	{
		
		if(current.getBlock() == e.getLocation().getBlock() || current.getBlock() == e.getEyeLocation().getBlock())
			return true;

		return false;
	}
	
	public void start()
	{
		currentLocation = startingLocation;
		taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Methods.getPlugin(), new Runnable()
		{

			public void run() {
				currentLocation.getWorld().spawnParticle(particle, currentLocation, 10);
				for(LivingEntity e : currentLocation.getWorld().getLivingEntities())
				{
					if(canHitEntity(currentLocation,e))
					{
						e.damage(damage);
						Bukkit.getScheduler().cancelTask(taskId);
						break;
					}
				}
				
				currentLocation.add(direction);
				
				
				
				
			}
			
		}, 0, 1);
	}
	
	public Location getCurrentLocation() {
		return currentLocation;
	}
	
	public Vector getDirection() {
		return direction;
	}
	public double getDamage() {
		return damage;
	}
	public Particle getParticle() {
		return particle;
	}
	public Location getStartingLocation() {
		return startingLocation;
	}
	
	
	
}
