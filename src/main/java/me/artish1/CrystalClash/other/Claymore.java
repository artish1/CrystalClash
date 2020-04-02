package me.artish1.CrystalClash.other;

import me.artish1.CrystalClash.CrystalClash;
import me.artish1.CrystalClash.Util.Methods;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Claymore {
	CrystalClash plugin;
	Item item;
	private int id;
	Player owner;
	public Claymore(CrystalClash plugin, Item item, Player owner) {
		this.plugin = plugin;
		this.item = item;
		this.owner = owner;
		ClaymoreInfo.addClaymore(owner.getUniqueId(), this);
	}
	
	public void remove(boolean removeInfo){
		Bukkit.getScheduler().cancelTask(id);
		item.remove();
		if(removeInfo)
		ClaymoreInfo.removeClaymore(owner.getUniqueId(),this); 
	}
	
	private void findAndExplode(){
		for(Entity e : item.getNearbyEntities(5, 3, 5)){
			if(e instanceof Player){
				final Player target = (Player) e; 
				if(!plugin.getArena().isOnSameTeam(target, owner)){
					
					item.setItemStack(new ItemStack(Material.REDSTONE_BLOCK));
					Methods.playFirework(item.getLocation(), Color.ORANGE);
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

						@Override
						public void run() {
							Methods.createTeamExplosion(owner, target.getLocation(), 6, 13.0);
							remove(true);
							Methods.playFirework(item.getLocation(), Color.RED);

						}
						
					}, 20);
					Bukkit.getScheduler().cancelTask(id);
					return;
				}
			}
		}
	}
	
	public void startDetection(){
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){

			@Override
			public void run() {
				findAndExplode();
			}
			
		}, 0, 10);
	}
	
	
}
