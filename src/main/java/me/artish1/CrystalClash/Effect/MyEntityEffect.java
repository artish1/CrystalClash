package me.artish1.CrystalClash.Effect;

import me.artish1.CrystalClash.CrystalClash;
import me.artish1.CrystalClash.Util.Methods;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

public class MyEntityEffect{
	private int delay;
	private int iterations;
	private int taskId;
	private boolean infiniteIfAlive = false;
	protected Entity e;
	private CrystalClash plugin = Methods.getPlugin();
	
	public MyEntityEffect(Entity e) {
		this.e = e;
		this.delay = 1;
		this.infiniteIfAlive = true;
	}
	
	public MyEntityEffect(Entity e, int delay) {
		this.e = e;
		this.delay = delay;
		infiniteIfAlive = true;
		
	}
	
	public boolean isInfiniteIfAlive() {
		return infiniteIfAlive;
	}
	
	public void setInfiniteIfAlive(boolean infinite) {
		infiniteIfAlive = infinite;
	}
	
	public MyEntityEffect(Entity e, int delay, int iterations) {
		this.e = e;
		this.delay = delay;
		if(iterations > 0){
		this.iterations = iterations;
		}else{
			this.infiniteIfAlive = true;
		}
	}
	
	public Entity getEntity(){
		return e;
	}
	
	public void setEntity(Entity entity){
		e = entity;
	}
	
	public int getDelay() {
		return delay;
	}
	public int getIterations() {
		return iterations;
	}
	
	public void onRun(){
		
		
		
	}
	
	
	public void start(){
		taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){

			public void run() {
				
				if(!infiniteIfAlive){
				if(iterations <= 0){
					Bukkit.getScheduler().cancelTask(taskId);
					return;
				}
				}else{
					if(!e.isValid()){
						Bukkit.getScheduler().cancelTask(taskId);
						return;
					}
					
				}
				
				
				onRun();
				iterations--;
				
			}
			
		}, 0, delay);
		
		
	}
}
