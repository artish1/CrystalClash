package me.artish1.CrystalClash.Listeners;

import me.artish1.CrystalClash.Arena.GameState;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.powerups.PowerUp;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PowerUpListener implements Listener{
	
	@EventHandler
	public void onPickUp(PlayerPickupItemEvent e){
		if(Methods.getPlugin().getArena().getState() != GameState.INGAME)
			return;
		
		PowerUp toRemove = null;
		for(PowerUp powerUp : PowerUp.getPowerups()){
			if(powerUp.getItemEntity().getUniqueId().equals(e.getItem().getUniqueId())){
				powerUp.activatePowerUp(e.getPlayer());
				e.setCancelled(true); 
				toRemove = powerUp;
				e.getItem().remove();
			}
		}
		
		if(toRemove != null)
			PowerUp.getPowerups().remove(toRemove);
		
	}
		
		
		
	
}
