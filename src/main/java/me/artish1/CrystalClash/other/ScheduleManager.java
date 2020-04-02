package me.artish1.CrystalClash.other;

import me.artish1.CrystalClash.Arena.ArenaPlayer;
import me.artish1.CrystalClash.Listeners.CustomEntityListener;
import me.artish1.CrystalClash.Listeners.GameListener;
import me.artish1.CrystalClash.Util.Methods;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ScheduleManager {
	private Player player;
	private int delay;
	private String type;
	private int id;
	
	public ScheduleManager(Player player,int delay, String type) {
		this.player = player;
		this.delay = delay;
		this.type = type;
		start();
	}
	
	
	
	public void start(){
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Methods.getPlugin(), new Runnable(){

			@Override
			public void run() {
				if(player == null)
					Bukkit.getScheduler().cancelTask(id);
				
				
				if(type.equalsIgnoreCase("ride")){
				if(!player.isInsideVehicle()){
					Bukkit.getScheduler().cancelTask(id);
					player.getInventory().removeItem(Methods.getGunnerTrigger());
					player.setAllowFlight(false);
					ArenaPlayer ap = Methods.getPlugin().getArena().getArenaPlayer(player);
					if(ap.isZoomed()){
						ap.zoomOut();
					}
					if(!GameListener.respawnQueue.contains(player.getUniqueId())){
						CustomEntityListener.loadInventory(player);
					}
				}
				
				}else{
					if(type.equalsIgnoreCase("straightarrow")){
						
						
						
					}else{
						Bukkit.getScheduler().cancelTask(id);
					}
				}
				
			}
			
		}, 0, delay);
		
	}
	
	
	public Player getPlayer() {
		return player;
	}
	public int getDelay() {
		return delay;
	}
	
}
