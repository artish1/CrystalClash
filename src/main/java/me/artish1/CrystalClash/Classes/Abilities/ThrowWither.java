package me.artish1.CrystalClash.Classes.Abilities;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;

import me.artish1.CrystalClash.Effect.EntityTrailEffect;

public class ThrowWither extends Ability{

	public ThrowWither( Player player) {
		super("Wither Wand", 2, player);
		
	}
	
	
	@Override
	public void onCast() {
		Entity e = player.launchProjectile(WitherSkull.class);
		WitherSkull wither = (WitherSkull) e;
		wither.setShooter(player);
		wither.setVelocity(player.getLocation().getDirection().multiply(2.5));
		startDelayedTask(wither);
		new EntityTrailEffect(wither, Particle.REDSTONE).start();
	}
	

	private void startDelayedTask(final Entity e){
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			public void run() {
				if(e.isValid()){
					e.remove();
				}
			}
			
		}, 20 * 13);
	}
	
	
}
