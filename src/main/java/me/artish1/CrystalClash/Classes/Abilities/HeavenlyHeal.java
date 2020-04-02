package me.artish1.CrystalClash.Classes.Abilities;

import me.artish1.CrystalClash.Util.Methods;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class HeavenlyHeal extends Ability{

	public HeavenlyHeal(Player player) {
		super("Heavenly Heal", 15, player);
		
	}
	
	
	@Override
	public void onCast() {
		List<Entity> entities = player.getNearbyEntities(8, 8, 8);
		for(Entity e : entities){
			if(e instanceof Player){
				Player p = (Player) e;
				if(!Methods.getArena().isOnSameTeam(p, player))
					continue;
				
				
				int healAmount = 10;


				Methods.playFirework(p.getEyeLocation(), Color.fromRGB(0, 255, 0));  
				p.setHealth(p.getHealth() + healAmount); 
				p.sendMessage(ChatColor.GREEN + "You've been healed by " + ChatColor.BOLD.toString() + player.getName()); 
				
			}
		}
		
		int healAmount = 14;



		Methods.playFirework(player.getEyeLocation(), Color.fromRGB(0, 255, 0)); 
		Methods.createHelix(player, Particle.VILLAGER_HAPPY);
		player.setHealth(player.getHealth() + healAmount); 
		player.sendMessage(ChatColor.GREEN + "You have healed yourself and others around you!");
		
		//TODO effects.
		
		
		super.onCast();
	}
	
}
