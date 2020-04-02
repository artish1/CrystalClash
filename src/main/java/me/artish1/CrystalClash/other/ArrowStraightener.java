package me.artish1.CrystalClash.other;

import me.artish1.CrystalClash.Util.Methods;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class ArrowStraightener {
	
	private Entity e;
	private Vector v;
	public ArrowStraightener(Entity e, Vector first) {
		this.e = e;
		this.v = first;
	}
	
	private int id;
	
	public void stop(){
		Bukkit.getScheduler().cancelTask(id);
	}
	
	public void start(){
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Methods.getPlugin(), new Runnable(){

			@Override
			public void run() {
				if(e.isValid() && !e.isOnGround()){
					e.setVelocity(v);
				}else{
					Bukkit.getScheduler().cancelTask(id);
				}
			}
			
		}, 0, 2);
	}
	
	
}
