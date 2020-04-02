package me.artish1.CrystalClash.other;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Listeners.GameListener;
import me.artish1.CrystalClash.Util.Methods;

public class AmmoReplenisher {
	
	private ItemStack item;
	private int amount = 1;
	private int maxReplenish;
	private long delay;
	private Player p;
	private String specifics = "explosives grenade";
	
	
	public AmmoReplenisher(Player p, ItemStack item) {
		this.item = item;
		this.p = p;
	}
	
	public void setMaxReplenish(int maxReplenish) {
		this.maxReplenish = maxReplenish;
	}
	
	public void setDelay(long delay) {
		this.delay = delay;
	}
	
	public void setSpecifics(String specifics) {
		this.specifics = specifics;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public long getDelay() {
		return delay;
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public int getMaxReplenish() {
		return maxReplenish;
	}
	
	private int id;
	public void start()
	{
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Methods.getPlugin(), new Runnable(){

			@Override
			public void run() {
				
				if(Methods.getArena().getArenaPlayer(p).getType() != ClassType.EXPLOSIVES)
				{
					stop();
					return;
				}
				
				if(p.getInventory().containsAtLeast(item, maxReplenish))
				{
					p.sendMessage("Nope.");
					return;
				}else{
					
					if(GameListener.respawnQueue.contains(p.getUniqueId()))
					{
						return;
					}else{
						if(specifics.equalsIgnoreCase("explosives grenade"))
						{
							
							
							if(p.getInventory().getItemInHand() != null && p.getInventory().getItemInHand().getType() == Material.EGG)
							{
								p.getInventory().getItemInHand().setAmount(p.getInventory().getItemInHand().getAmount() + 1);
							}else{
								if(p.getInventory().getItemInHand() == null)
								{
									p.getInventory().setItemInHand(item);
								}
							}
							p.updateInventory();
							p.sendMessage("yep.");
						}
					}
					
					
				}
			}
			
		
		}, 0, delay);
	}
	
	public void stop()
	{
		Bukkit.getScheduler().cancelTask(id);
	}
	
	
	
	
}
