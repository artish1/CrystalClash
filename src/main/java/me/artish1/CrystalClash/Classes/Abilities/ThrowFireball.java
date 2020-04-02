package me.artish1.CrystalClash.Classes.Abilities;

import me.artish1.CrystalClash.Effect.EntityFireEffect;
import me.artish1.CrystalClash.Listeners.Classes.MageListener;
import me.artish1.CrystalClash.other.ClassInventories;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;

public class ThrowFireball extends Ability{

	public ThrowFireball(Player player) {
		super("Fireball",(long) 1.5, ClassInventories.getFireballItem(), player);
	}
	
	@Override
	public void onCast() { 
		Entity e = player.launchProjectile(Snowball.class);
		Snowball snowball = (Snowball) e;
		e.setFireTicks(20 * 10);
		snowball.setShooter(player);
		//e.getVelocity().multiply(6.2);
		e.setVelocity(player.getLocation().getDirection().multiply(2.7));
		MageListener.uuids.add(e.getUniqueId());
		MageListener.shooters.put(e.getUniqueId(), player.getUniqueId());
		
		startDelayedTask(e);
		new EntityFireEffect(snowball).start();
	}
	
	
	
	private void startDelayedTask(final Entity e){
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			public void run() {
				if(e.isValid()){
					e.remove();
				}
			}
			
		}, 20 * 15);
	}
	
}
