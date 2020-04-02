package me.artish1.CrystalClash.Listeners.Classes;

import me.artish1.CrystalClash.Classes.Abilities.SpawnSkeleton;
import me.artish1.CrystalClash.Classes.Abilities.SpawnZombie;
import me.artish1.CrystalClash.Classes.Abilities.ThrowWither;
import me.artish1.CrystalClash.entities.Summoned;
import me.artish1.CrystalClash.other.ClassInventories;
import me.artish1.CrystalClash.other.SummonManager;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class NecromancerListener implements Listener{
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		Player player = e.getPlayer();
		if(!e.hasItem())
			return;
		
		if(e.getItem().equals(ClassInventories.getNecromancerWand())){
			if(e.getAction() != Action.PHYSICAL)
			{
				new ThrowWither(player).cast();
				
			}
			
		}
		if(e.getItem().equals(ClassInventories.getNecromancerSkeletonItem())){
			SummonManager manager;
			if(SummonManager.hasManager(player)){
				manager = SummonManager.getManager(player);
			}else{
				manager = new SummonManager(player);
			}
			
			if(!manager.reachedLimit()){
				new SpawnSkeleton(player).cast();
			}else{
				player.sendMessage(ChatColor.RED + "You cannot have more than " + manager.limit + " summons at a time!"); 
			}
			 
		}
		if(e.getItem().equals(ClassInventories.getNecromancerZombieItem())){
			SummonManager manager;
			if(SummonManager.hasManager(player)){
				manager = SummonManager.getManager(player);
			}else{
				manager = new SummonManager(player);
			}
			
			if(!manager.reachedLimit()){
				new SpawnZombie(player).cast();
			}else{
				player.sendMessage(ChatColor.RED + "You cannot have more than " + manager.limit + " summons at a time!"); 
			}
			
		}
		
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e){
		Player player = e.getEntity();
		if(SummonManager.hasManager(player)){
			for(Summoned ent : SummonManager.getManager(player).getSummoned()){
				ent.getEntity().getBukkitEntity().remove();
			}
			SummonManager.getManager(player).getSummoned().clear();
		}
	}
	
}
