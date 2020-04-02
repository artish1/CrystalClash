package me.artish1.CrystalClash.objects;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class CBuilding {
	
	private Player owner;
	private List<Block> blocks;
	
	private boolean isAlive = false;
	
	public CBuilding(Player player, List<Block> blocks) {
		this.owner = player;
		this.blocks = blocks;
		isAlive = true;
	}
	
	
	public boolean isAlive() {
		return isAlive;
	}
	
	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
	
	public void remove(){
		for(Block b : blocks){
			b.setType(Material.AIR);
		} 
		
		
		isAlive = false;
	}
	
	
	public Player getOwner() {
		return owner;
	}
	
	public List<Block> getBlocks() {
		return blocks;
	}
	
	
}
