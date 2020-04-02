package me.artish1.CrystalClash.Listeners.Classes;

import me.artish1.CrystalClash.Classes.Abilities.LightningStorm;
import me.artish1.CrystalClash.Classes.Abilities.MeteorShower;
import me.artish1.CrystalClash.Classes.Abilities.ThrowFireball;
import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.CrystalClash;
import me.artish1.CrystalClash.Util.Methods;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MageListener implements Listener{
	
	public static List<UUID> uuids = new ArrayList<UUID>();
	public static HashMap<UUID,UUID> shooters = new HashMap<UUID,UUID>();
	
	CrystalClash plugin;
	public MageListener(CrystalClash plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onFireballHit(ProjectileHitEvent e){
		if(uuids.contains(e.getEntity().getUniqueId())){
			Player shooter = Bukkit.getPlayer(shooters.get(e.getEntity().getUniqueId()));
			
			Methods.createTeamFireworkExplosion(shooter, e.getEntity().getLocation(), 4, 5.0);

		}
	}

	


	@EventHandler
	public void onFireball(PlayerInteractEvent e){
		Player player = e.getPlayer();
		if(!e.hasItem())
			return;
		
		
		if(plugin.getArena().getArenaPlayer(player).getType() != ClassType.MAGE)
			return;
		ThrowFireball tf = new ThrowFireball(player);
		if(!e.getItem().equals(tf.getItem()))
			return;
		
		tf.cast();
		
	}
	
	@EventHandler
	public void onMeteorShower(PlayerInteractEvent e){
		Player player = e.getPlayer();
		if(!e.hasItem())
			return;
		
		if(plugin.getArena().getArenaPlayer(player).getType() != ClassType.MAGE)
			return;
		MeteorShower ms = new MeteorShower(player);
		if(!e.getItem().equals(ms.getItem()))
			return;
		
		if(!plugin.getArena().isOn())
			return;
		
		ms.cast();
	}
	
	@EventHandler
	public void onLightningStrike(PlayerInteractEvent e){
		Player player = e.getPlayer();
		if(!e.hasItem())
			return;
		
		if(plugin.getArena().getArenaPlayer(player).getType() != ClassType.MAGE)
			return;

		LightningStorm ls = new LightningStorm(player);

		if(!e.getItem().equals(ls.getItem()))
			return;
		
		if(!plugin.getArena().isOn())
			return;
		

		ls.cast();
		
		
		
	}
	
	
}
