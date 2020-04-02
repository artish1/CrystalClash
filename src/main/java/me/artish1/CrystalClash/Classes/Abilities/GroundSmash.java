package me.artish1.CrystalClash.Classes.Abilities;

import me.artish1.CrystalClash.Listeners.Classes.TankListener;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.other.ClassInventories;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

public class GroundSmash extends Ability{
    private int radius = 8;
    private double damage = 6;

	public GroundSmash(Player player) {
		super("Ground Smash", 15, ClassInventories.getTankAbilityItem(), player);
	}
	@Override
	public void onCast() {



		for(Entity e : Methods.getNearbyEntities(player.getLocation(), radius)){
			if(e instanceof Player){
			
			if(e.getUniqueId() == player.getUniqueId() || arena.isOnSameTeam(player,(Player) e))	
				continue;
					((Player) e).damage(damage, player);
					e.setVelocity(new Vector(0,1.4,0));
					
					
			}
		}
		player.sendMessage(ChatColor.YELLOW + "Ground Smashed!!");
		List<Location> circle = Methods.circle(player.getLocation(), radius, 1, true, false, 0);
		for(Location loc : circle)
		{
			final FallingBlock b = loc.getWorld().spawnFallingBlock(loc, Material.STONE, (byte) 0);
			b.setDropItem(false);
			Vector v = new Vector(0,1.4,0);
			b.setVelocity(v);
			TankListener.cancelBlockForms.add(b.getUniqueId());
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

				public void run() {
					if(TankListener.cancelBlockForms.contains(b.getUniqueId()))
					{
						TankListener.cancelBlockForms.remove(b.getUniqueId());
					}
					b.remove();
				}
				
			}, 35);
		}
		
	}
	
	
}
