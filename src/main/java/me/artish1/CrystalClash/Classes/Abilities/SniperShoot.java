package me.artish1.CrystalClash.Classes.Abilities;

import me.artish1.CrystalClash.Cooldown.Cooldown;
import me.artish1.CrystalClash.Listeners.Classes.SniperListener;
import me.artish1.CrystalClash.other.ArrowStraightener;
import me.artish1.CrystalClash.other.ClassInventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;

public class SniperShoot extends Ability{

	public SniperShoot(Player player) {
		super("Sniper Rifle", 2, ClassInventories.getSniperItem(), player);
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
	
	
	@Override
	public void cast() {
		if(item != null){
			if(!player.getInventory().getItemInHand().equals(item))
				return;
		}
		
		if(Cooldown.isCooling(player.getUniqueId(), name)){
			Cooldown.coolDurMessage(player, name);
			return;
		}
		ItemStack ammo = new ItemStack(Material.SNOWBALL, 1);
		if(player.getInventory().contains(Material.SNOWBALL,1)){
			onCast();
			player.getInventory().removeItem(ammo);
			player.updateInventory();
			Cooldown.add(player.getUniqueId(), name, getCooldown(), System.currentTimeMillis());
		}else{
			player.sendMessage(ChatColor.RED + "You have no more ammo!");
		}
	}
	
	
	
	@Override
	public void onCast() {
		final Snowball ball = player.launchProjectile(Snowball.class);
		ball.setVelocity(player.getLocation().getDirection().multiply(4.1));//4.5 originally
		ball.setShooter(player);
		new ArrowStraightener(ball,ball.getVelocity()).start();
		removeAfterDelay(ball, 20 * 6);
		SniperListener.ids.add(ball.getUniqueId());
		
		super.onCast();
		
		
	}
	
	
}	
