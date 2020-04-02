package me.artish1.CrystalClash.Listeners.Classes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.artish1.CrystalClash.Arena.Arena;
import me.artish1.CrystalClash.Arena.ArenaManager;
import me.artish1.CrystalClash.Arena.GameState;
import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Classes.Abilities.GroundSmash;
import me.artish1.CrystalClash.Util.Methods;

public class TankListener implements Listener{
	
	public HashMap<UUID,GroundSmash> abilityInstance = new HashMap<UUID,GroundSmash>();
	public static HashSet<UUID> cancelBlockForms = new HashSet<UUID>();
	@EventHandler
	public void onGroundSmash(PlayerInteractEvent e){
		if(!e.hasItem())
			return;
		
		if(!ArenaManager.isInArena(e.getPlayer()))
			return;
		
		Player player = e.getPlayer();
		Arena arena = ArenaManager.getArena(player);
		
		if(arena.getState() != GameState.INGAME)
			return;
		
		if(arena.getArenaPlayer(player).getType() != ClassType.TANK)
			return;
		
		
		if(e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR){
			if(!e.getItem().hasItemMeta())
				return;
			
			if(e.getItem().getItemMeta().getDisplayName() == "" || e.getItem().getItemMeta().getDisplayName() == null)
				return;
			
			if(ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Ground Smash")){
				new GroundSmash(player).cast();
			}
		}
	}
	
	@EventHandler
	public void onGroundSmashBlockFormEvent(final EntityChangeBlockEvent e)
	{
		if(e.getEntity() instanceof FallingBlock)
		{
			FallingBlock b = (FallingBlock) e.getEntity();
			
			if(cancelBlockForms.contains(b.getUniqueId()))
			{
				
				e.setCancelled(true);
				e.getEntity().remove();
				Bukkit.getScheduler().scheduleSyncDelayedTask(Methods.getPlugin(), new Runnable(){
					 
					@Override
					public void run() {
						e.getBlock().setType(Material.AIR);
					}
					
				}, 1);
			
			}
			
		}
	}
	
	
}
