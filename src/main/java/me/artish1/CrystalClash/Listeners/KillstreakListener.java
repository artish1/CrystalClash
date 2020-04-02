package me.artish1.CrystalClash.Listeners;

import me.artish1.CrystalClash.Arena.Arena;
import me.artish1.CrystalClash.Arena.ArenaTeam;
import me.artish1.CrystalClash.Arena.GameState;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.killstreaks.Killstreak;
import me.artish1.CrystalClash.killstreaks.Napalm;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class KillstreakListener implements Listener{
	
//	public static HashSet<UUID> napalms = new HashSet<UUID>();
	public static HashSet<Location> redNapalmLocs = new HashSet<Location>();
	public static HashSet<Location> blueNapalmLocs = new HashSet<Location>();
	public static HashMap<UUID, ArenaTeam> napalms = new HashMap<UUID,ArenaTeam>();
	
	
	@EventHandler
	public void immunityToTeamFire(EntityDamageEvent e){
		
		if(e.getCause() == DamageCause.FIRE || e.getCause() == DamageCause.FIRE_TICK){
	
			if(e.getEntity() instanceof Player){ 
				Player player = (Player) e.getEntity();
				if(Methods.getArena().isBlue(player)){
					for(Location b : blueNapalmLocs){
						if((player.getLocation().getBlockX() == b.getX() || player.getLocation().getBlockX() == b.getX() + 1) 
								&& (player.getLocation().getBlockZ() == b.getZ() || player.getLocation().getBlockZ() == b.getZ() + 1)){
							e.setCancelled(true); 
							player.setFireTicks(0);
							player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 15, 1), true);
							return;
						}
					}
				}
				
				if(Methods.getArena().isRed(player)){
					for(Location b : redNapalmLocs){
						if((player.getLocation().getBlockX() == b.getX() || player.getLocation().getBlockX() == b.getX() + 1) 
								&& (player.getLocation().getBlockZ() == b.getZ() || player.getLocation().getBlockZ() == b.getZ() + 1)){
							e.setCancelled(true);
							player.setFireTicks(0); 
							player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 15, 1), true);
							return;
						}
					}
				}
				
			}
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		if(Methods.getArena().getState() != GameState.INGAME)
			return;
		
		
		
		if(Methods.getArena().isBlue(e.getPlayer())){
			if(blueNapalmLocs.size() <= 0)
				return;
			
			if(blueNapalmLocs.contains(e.getPlayer().getLocation().getBlock().getLocation())){
				e.getPlayer().setFireTicks(0);
				e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 15, 1), true);
				return;
			}
		}else{
			
			if(Methods.getArena().isRed(e.getPlayer())){
				if(redNapalmLocs.size() <= 0)
					return;
				
				
			}
			return;
		}
		
	}
	
	
	@EventHandler
	public void onNapalmForm(EntityChangeBlockEvent e){
		if(napalms.containsKey(e.getEntity().getUniqueId())){
			ArenaTeam type = napalms.get(e.getEntity().getUniqueId());
			if(type == Arena.redTeam){
				redNapalmLocs.add(e.getBlock().getLocation());
				Napalm.removeSafeLocs(e.getBlock(), type);
				removeFire(e.getBlock().getLocation());
			} 
			
			if(type == Arena.blueTeam){
				blueNapalmLocs.add(e.getBlock().getLocation());
				Napalm.removeSafeLocs(e.getBlock(), type); 
				removeFire(e.getBlock().getLocation());
			} 
			
			e.setCancelled(false);
		}
		
	}
	
	@EventHandler
	public void onActivateKillstreak(final PlayerInteractEvent e){
		if(!e.hasItem())
			return;
		if(Methods.getPlugin().getArena().getState() != GameState.INGAME)
			return;
		
		if(e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		
		
		for(Killstreak ks : Killstreak.getKillstreaks()){
			if(ks != null && ks.getItem() != null)
			if(ks.getItem().equals(e.getItem())){
				ks.activateKillstreak(e.getPlayer());
				//if(Methods.getPlugin().getArena().getArenaPlayer(e.getPlayer()).getAvaliable().contains(ks))
				//Methods.getPlugin().getArena().getArenaPlayer(e.getPlayer()).getAvaliable().remove(ks);
				
				e.setCancelled(true); 
				e.setUseItemInHand(Result.DENY); 
				//e.getPlayer().setItemInHand(new ItemStack(Material.AIR));
				//Methods.getPlugin().getArena().getArenaPlayer(e.getPlayer()).getBought().remove(ks.getItem());
				
				break;
			}
		}
		
	}
	
	public void removeFire(final Location b){
		Bukkit.getScheduler().scheduleSyncDelayedTask(Methods.getPlugin(), new Runnable(){

			@Override
			public void run() {
				
					if(b.getBlock().getType()== Material.FIRE){
						b.getBlock().setType(Material.AIR); 
					}
				
			}
			
		}, 20 * 32);
	}
	
	
	
	
}
