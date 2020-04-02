package me.artish1.CrystalClash.Classes.Abilities;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;

import me.artish1.CrystalClash.Effect.EntityTrailEffect;
import me.artish1.CrystalClash.Listeners.Classes.EngineerListener;
import me.artish1.CrystalClash.other.ArrowStraightener;

public class EngineerFireRocketLauncher extends Ability{

	public EngineerFireRocketLauncher(Player player) {
		super("Rocket Launcher", 7, player);
	}
	
	
	@Override
	public void onCast() {
		Snowball snowball = player.launchProjectile(Snowball.class);
		snowball.setVelocity(player.getLocation().getDirection().multiply(3.5));
		snowball.setShooter(player);
		EngineerListener.rockets.add(snowball.getUniqueId());
		new ArrowStraightener(snowball,snowball.getVelocity()).start();
		removeAfterDelay(snowball, 20 * 9);
		
		new EntityTrailEffect(snowball, Particle.SMOKE_LARGE).start();
	}
	
	

	private void removeAfterDelay(final Entity e, int delay){
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			public void run() {
				if(e.isValid()){
					e.remove();
				}
			}
			
		}, delay);
	}
	
}
