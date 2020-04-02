package me.artish1.CrystalClash.other;

import java.util.ArrayList;
import java.util.List;

import me.artish1.CrystalClash.Util.Methods;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class BlockManager {
	
	private List<Block> blocks;
	private List<Material> material;
	private int seconds;
	private Material mat;
	
	
	
	public BlockManager(List<Block> blocks, int seconds, Material mat) {
		this.seconds = seconds;
		this.blocks = blocks;
		this.mat = mat;
		material = new ArrayList<Material>();
		
		for(Block b : blocks){
			material.add(b.getType());
		}
		
		
		start();
	}

	private void start(){
		Bukkit.getScheduler().scheduleSyncDelayedTask(Methods.getPlugin(), new Runnable(){

			@Override
			public void run() {
				
				for(int x = 0; x < blocks.size(); x++){
					Block b = blocks.get(x);
					if(b.getType() != mat){
						continue;
					}
					
					b.setType(material.get(x));
				}
				
			}
			
		}, seconds * 20);
	}
	
	
}
