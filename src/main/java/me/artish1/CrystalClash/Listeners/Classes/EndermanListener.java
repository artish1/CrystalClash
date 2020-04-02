package me.artish1.CrystalClash.Listeners.Classes;

import java.util.HashSet;
import java.util.UUID;

import me.artish1.CrystalClash.Arena.Arena;
import me.artish1.CrystalClash.Arena.ArenaManager;
import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Classes.Abilities.EnderTeleport;
import me.artish1.CrystalClash.other.ClassInventories;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;

public class EndermanListener implements Listener{
	
	public static HashSet<UUID> uuids = new HashSet<UUID>();
	
	@EventHandler
	public void onTP(PlayerInteractEvent e){
		Player player = e.getPlayer();
		if(!ArenaManager.isInArena(player))
			return;
		
		Arena arena = ArenaManager.getArena(player);
		
		if(arena.getArenaPlayer(player).getType() != ClassType.ENDER)
			return;
		
		
		if(!e.hasItem())
			return;
		
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(e.getItem().equals(ClassInventories.getEndermanTeleportItem())){
				e.setCancelled(true);
				new EnderTeleport(player).cast();
			}
		}
	}
	
	@EventHandler
	public void noDamage(EntityDamageEvent e){
		if(e.getCause() == DamageCause.FALL){
			if(uuids.contains(e.getEntity().getUniqueId())){
				e.setCancelled(true);
				uuids.remove(e.getEntity().getUniqueId());
			}
		}
	}
	
	
}
