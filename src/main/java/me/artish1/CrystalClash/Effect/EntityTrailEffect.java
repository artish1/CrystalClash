package me.artish1.CrystalClash.Effect;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;

public class EntityTrailEffect extends MyEntityEffect{
	
	private Particle effect;
	
	public EntityTrailEffect(Entity e, Particle effect) {
		super(e, 1);
		this.effect = effect;
	}
	
	
	public Particle getEffect() {
		return effect;
	}
	@Override
	public void onRun() {
        Location loc = getEntity().getLocation();
		loc.getWorld().spawnParticle(effect, loc, 2);


	}
	
	
	
	
}
