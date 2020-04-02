package me.artish1.CrystalClash.Classes.Abilities;


import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Dash extends Ability {

	public Dash(Player player) {
		super("Dash", 6, player);
	}
	private int taskId;
	
	private int counter = 4;
	@Override 
	public void onCast() {
		final Vector direction = player.getLocation().getDirection();
		direction.setY(0);
		direction.multiply(4);
		
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20,5,true), true);
		taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(getPlugin(), new Runnable(){

			public void run() {
			if(counter >0){
				player.setVelocity(direction);
				counter--;
			}else{
				Bukkit.getScheduler().cancelTask(taskId);
				player.setVelocity(new Vector(0,0,0));
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable(){

					public void run() {
						player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999,2,true), true);
						
					}
					
				}, 10);
			}
			
			}
			
		}, 0, 1);
		player.sendMessage(ChatColor.AQUA + "Dashed!"); 
		super.onCast();
	}
	

}
