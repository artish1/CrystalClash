package me.artish1.CrystalClash.Effect;


import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;

import me.artish1.CrystalClash.Util.Methods;

public class EntityFireEffect extends MyEntityEffect{

	public EntityFireEffect(Entity e) {
		super(e,1);
	}
	
	@Override
	public void onRun() {
	    Location loc = getEntity().getLocation();
	    loc.getWorld().spawnParticle(Particle.FLAME, loc, 2);
	}
	
	
}
