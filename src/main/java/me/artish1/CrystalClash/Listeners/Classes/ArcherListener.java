package me.artish1.CrystalClash.Listeners.Classes;
import me.artish1.CrystalClash.Arena.ArenaManager;
import me.artish1.CrystalClash.Arena.GameState;
import me.artish1.CrystalClash.Classes.Abilities.ArrowBarrage;
import me.artish1.CrystalClash.Classes.Abilities.BowChangeMode;
import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.CrystalClash;
import me.artish1.CrystalClash.other.ClassInventories;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class ArcherListener implements Listener{
	
	public static HashMap<UUID,BowChangeMode> map = new HashMap<>();
	
	public HashMap<UUID,ArrowBarrage> arrowBarrageInstances = new HashMap<>();
	public static HashSet<UUID> debuggers = new HashSet<UUID>();
	
	CrystalClash plugin;
	public ArcherListener(CrystalClash plugin) {
		this.plugin = plugin;
	}

	
	
	@EventHandler	(priority = EventPriority.HIGHEST)
	public void onChangeBowMode(PlayerInteractEvent e){
		
		if(!e.hasItem())
			return;
		
		Player player = e.getPlayer();
		
		
		if(!ArenaManager.isInArena(player)){
			return;
		}
		if(plugin.getArena().getArenaPlayer(player).getType() != ClassType.ARCHER)
			return;
		
		
		
		if(plugin.getArena().getState() != GameState.INGAME)
			return;
		
		
		if(!e.getItem().hasItemMeta()){
			return;
		}
		if(e.getItem().getItemMeta().getDisplayName() == null || e.getItem().getItemMeta().getDisplayName() == "")
			return;
		
		if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK){
			
			if(ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Multi-Mode Bow")){
				if(map.containsKey(player.getUniqueId())){
					BowChangeMode bowChange = map.get(player.getUniqueId());
					bowChange.cast();
					
					
				}else{
					BowChangeMode bowChange = new BowChangeMode(player);
					bowChange.cast();
					map.put(player.getUniqueId(), bowChange);
				}
			}
		}
	}
	
	@EventHandler
	public void onBarrage(PlayerInteractEvent e){
		if(!e.hasItem())
			return;
		
		Player player = e.getPlayer();
		
		if(!ArenaManager.isInArena(player))
			return;
		
		if(plugin.getArena().getArenaPlayer(player).getType() != ClassType.ARCHER)
			return;
		

		if(plugin.getArena().getState() != GameState.INGAME)
			return;
		
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_AIR){
			if(e.getItem().equals(ClassInventories.getArcherArrowBarrage()) || ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName()).equalsIgnoreCase(
					ChatColor.stripColor(ClassInventories.getArcherArrowBarrage().getItemMeta().getDisplayName()))){ 
				new ArrowBarrage(player).cast();
				
				
			}
		}
		
	}
	
}
