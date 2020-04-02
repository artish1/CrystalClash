package me.artish1.CrystalClash.Listeners.Classes;

import me.artish1.CrystalClash.Arena.Arena;
import me.artish1.CrystalClash.Arena.ArenaManager;
import me.artish1.CrystalClash.Arena.GameState;
import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Classes.Abilities.Dash;
import me.artish1.CrystalClash.Util.Methods;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;

public class ScoutListener implements Listener{
		
	
	//TODO +++++++++++++++SCOUT DASH+++++++++++++
	
	@EventHandler
	public void onScoutDash(PlayerInteractEvent e){
		if(!e.hasItem())
			return;
		
		if(Methods.getArena().getState() != GameState.INGAME)
			return;
		
		Player player = e.getPlayer();
		if(Methods.getArena().getArenaPlayer(player).getType() != ClassType.SCOUT)
			return;
		
		if(e.getItem().getType() == Material.IRON_SWORD){
			if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
			new Dash(player).cast();
			
			
		}
		
		
		
	}
	
	//TODO ++++++++++++++++++SCOUT PASSIVE++++++++++++++++++++++++++++
	@EventHandler
	public void onScoutPassive(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player player = (Player) e.getEntity();
			
			if(!ArenaManager.isInArena(player))
				return;
			
			
			Arena arena = ArenaManager.getArena(player);
			if(arena.getState() != GameState.INGAME)
				return;
			
			
			
			if(arena.getArenaPlayer(player).getType() == ClassType.SCOUT || arena.getArenaPlayer(player).getType() == ClassType.SPIDER)
			if(e.getCause() == DamageCause.FALL)
				e.setCancelled(true);
			
			
			
		}
	}
	
	
}
