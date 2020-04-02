package me.artish1.CrystalClash.other;

import me.artish1.CrystalClash.Util.Methods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class GunningInfo {
	
	private Player player;
	private int maxShots = 20;
	private int currentShots;
	private boolean canShoot = true;
	private boolean reloading = false;
	private int reloadSeconds = 3;
	
	public GunningInfo(Player player) {
		this.player = player;
		currentShots = maxShots;
	}
	
	
	
	public void minusShot()
	{
		if(canShoot)
		{
			currentShots--;
		
			if(currentShots > 0)
			{
				
			}else{
				setCanShoot(false); 
				if(!reloading)
				{
					reload();
				}
			}
		
		}
		
	}
	
	
	
	@SuppressWarnings("deprecation")
	public void reload()
	{
		reloading = true;
		Methods.sendTitle(player,"Reloading...",5,25,9,ChatColor.RED);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Methods.getPlugin(), new Runnable(){
			@Override
			public void run() {
				
				setCanShoot(true);
				currentShots = maxShots;
				reloading = false;
				player.sendMessage(ChatColor.GREEN + "Machine gun: Reloaded!");
				player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1.1f);
			}
			
		}, 20 * reloadSeconds);
		
		
	}
	
	
	
	
	public int getMaxShots() {
		return maxShots;
	}
	
	
	public int getCurrentShots() {
		return currentShots;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	
	public boolean isCanShoot() {
		return canShoot;
	}
	
	
	public void setCanShoot(boolean canShoot) {
		this.canShoot = canShoot;
	}
	
	
	
}
