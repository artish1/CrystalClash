package me.artish1.CrystalClash.other;

import java.util.UUID;

import me.artish1.CrystalClash.CrystalClash;
import me.artish1.CrystalClash.Classes.Abilities.BlockThrow;

import org.bukkit.Bukkit;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;

public class RockThrowInfo {
	private boolean secondHit = false;
	private FallingBlock fb;
	private int id;
	private UUID uuID;
	private CrystalClash plugin;
	private boolean hitFirst = true;
	
	public RockThrowInfo(FallingBlock fb,Player player,CrystalClash plugin) {
		this.fb = fb;
		this.uuID = player.getUniqueId();
		this.plugin = plugin;
		check();
	}
	
	public FallingBlock getFb() {
		return fb;
	}
	
	public void setSecondHit(boolean hit){
		secondHit = hit;
	}
	
	public boolean hitSecond(){
		return secondHit;
	}
	
	public boolean hitFirst(){
		return hitFirst;
	}
	
	public void setHitFirst(boolean hitFirst) {
		this.hitFirst = hitFirst;
	}
	
	public void check(){
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){

			@Override
			public void run() {
				if(fb.isDead()){
					Bukkit.getScheduler().cancelTask(id);
					BlockThrow.map.remove(uuID);
					
				}
			}
			
		}, 0, 4);
	}
	
}
