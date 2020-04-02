package me.artish1.CrystalClash.killstreaks;

import me.artish1.CrystalClash.Listeners.GameListener;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.other.ClassInventories;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class Nuke extends Killstreak{
	
	public Nuke() {
		super(ChatColor.RED + "Nuke");
		setKillsNeeded(20);
		setItem(Methods.createItem(Material.EXPERIENCE_BOTTLE, ChatColor.RED + "Nuke", ClassInventories.createLore("Click to " +
                "activate!")));
	//	Killstreak.getKillstreaks().add(this);
	}
	
	
	

	
	@Override
	public void onActivate(Player p) {
		for(Player players : Bukkit.getOnlinePlayers()){
			if(Methods.getPlugin().getArena().isOnSameTeam(players, p))
				continue;
			
			if(GameListener.respawnQueue.contains(players.getUniqueId()))
				continue;
			
			players.damage(200.0, p); 
			Methods.displayParticleEffect(Particle.EXPLOSION_LARGE, false, players.getLocation(), 10, 10, 10, 10, 10);
			 
		}
		removeKillstreak(p);
		super.onActivate(p);
	}
	
	
}
