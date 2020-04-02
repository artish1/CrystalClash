package me.artish1.CrystalClash.Listeners.Classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import me.artish1.CrystalClash.CrystalClash;
import me.artish1.CrystalClash.Arena.Arena;
import me.artish1.CrystalClash.Arena.ArenaManager;
import me.artish1.CrystalClash.Arena.GameState;
import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Classes.Abilities.BlockThrow;
import me.artish1.CrystalClash.Classes.Abilities.EarthDome;
import me.artish1.CrystalClash.Classes.Abilities.EarthWall;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.other.ClassInventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class EarthListener implements Listener{
	
	//TODO: ++++++++++++++++++++++++++++++++++++++BLOCKTHROW BELOW+++++++++++++++++++++++++++++++++++++++
	
	public static HashSet<UUID> ids = new HashSet<UUID>();
	public static HashSet<UUID> firstCasts = new HashSet<UUID>();
	public static HashMap<UUID,UUID> owners = new HashMap<>();
	static CrystalClash plugin;
	
	public EarthListener(CrystalClash test) {
		EarthListener.plugin = test;
		}
	
	public static List<Integer> getBreakableOnExplosion(){
		return plugin.kits.getIntegerList("Earth.BlockThrow.AllowBreakOnExplosion");
	}

	@EventHandler
	public void onFallingBlockExplode(EntityExplodeEvent e){
		for(Arena a : ArenaManager.getArenas()){
			if(!e.getLocation().getWorld().equals(Methods.getArena().getRedSpawn().getWorld()))
				continue;
				
			
			if(a.getState() != GameState.INGAME)
				continue;
			
			

					List<Block> blocksToRemove = new ArrayList<Block>();
					for(Block block : e.blockList()){
						if(!getBreakableOnExplosion().contains(block.getType().getId())){
							blocksToRemove.add(block);
						}
					}
					e.setYield(0F);
					for(Block block :blocksToRemove){
						e.blockList().remove(block);
					}
				break;
			
		}
		
	}
	
	
	@EventHandler
	public void onHit(final EntityChangeBlockEvent e){
		if(e.getEntity() instanceof FallingBlock){
			
			if(firstCasts.contains(e.getEntity().getUniqueId())){
				e.setCancelled(true);
				firstCasts.remove(e.getEntity().getUniqueId());
			}
			
			if(ids.contains(e.getEntity().getUniqueId())){
				e.setCancelled(true);
				if(!owners.containsKey(e.getEntity().getUniqueId()))
					return;
				Player player = Bukkit.getPlayer(owners.get(e.getEntity().getUniqueId()));
				Methods.createTeamExplosion(player, e.getEntity().getLocation(), 6, 7.5);
				ids.remove(e.getEntity().getUniqueId());
				owners.remove(e.getEntity().getUniqueId());
				e.getEntity().remove();
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
					
					@Override
					public void run() {
						e.getBlock().setType(Material.AIR);
					}
					
				}, 1);
			}
			}
	}
	
	
	
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onBlockThrow(PlayerInteractEvent e){
		Player player = e.getPlayer();
		if(!ArenaManager.isInArena(player))
			return;
		Arena arena = ArenaManager.getArena(player);
		
		if(arena.getState() != GameState.INGAME)
			return;
		
		if(arena.getArenaPlayer(player).getType() != ClassType.EARTH)
			return;
		
		if(!e.hasItem())
			return;
		
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_AIR){
			BlockThrow bts = new BlockThrow(player);
			if(e.getItem().getType() == Material.STICK){
				bts.cast();
			}
		}
		
		
		
		
		
	}
	
	
	//TODO ++++++++++++++++++++++++++++++EARTHDOME BELOW+++++++++++++++++++++++++++++++++++++++++++++++++++
	
	@EventHandler 
	public void onPlayerInteract(PlayerInteractEvent e){
		Player player = e.getPlayer();
		if(!ArenaManager.isInArena(player))
			return;
		
		Arena arena = ArenaManager.getArena(player);
		if(arena.getState() != GameState.INGAME)
			return;
		
		
		if(arena.getArenaPlayer(player).getType() != ClassType.EARTH)
			return;
		
		
		if(!e.hasItem())
			return;
		
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_AIR){
			EarthDome dome = new EarthDome(player);
			if(e.getItem().equals(ClassInventories.getEarthDomeItem())){
				dome.cast();
				
			}
			
		}
		
	
	}
	
	
	//TODO++++++++++++++++++++++++++++++++EARTH WALL BELOW+++++++++++++++++++++++++++++++++++++++++++++++++
	
	@EventHandler 
	public void onEarthWall(PlayerInteractEvent e){
		Player player = e.getPlayer();
		if(!ArenaManager.isInArena(player))
			return;
		Arena arena = ArenaManager.getArena(player);
		if(arena.getState() != GameState.INGAME)
			return;
	
		if(arena.getArenaPlayer(player).getType() != ClassType.EARTH)
			return;
		
		if(!e.hasItem())
			return;
		
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR | e.getAction() == Action.RIGHT_CLICK_AIR){
			EarthWall wall = new EarthWall(player);
			if(e.getItem().equals(ClassInventories.getEarthWallItem())){
				wall.cast();
			}
		}
	}
	
	
	
	
	
}
