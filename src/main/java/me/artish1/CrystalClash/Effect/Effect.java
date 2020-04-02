package me.artish1.CrystalClash.Effect;

import me.artish1.CrystalClash.CrystalClash;
import me.artish1.CrystalClash.Util.Methods;

import org.bukkit.Bukkit;


public class Effect {
	private int delay; //Delay in ticks, 20 ticks = 1 second.
	private int iterations;//How many times should it run?
	private int taskId;
	
	private CrystalClash plugin = Methods.getPlugin();
	
	public Effect(int delay,int iterations) {
		this.delay = delay;
		this.iterations = iterations;
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
				if(iterations <= 0){
					Bukkit.getScheduler().cancelTask(taskId);
					return;
				}
				onRun();
				iterations--;
				
			}
			
		}, 0, delay);
		
		
	}
	
	
	
}
