package me.artish1.CrystalClash.other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class BlockPlaceManager {
	
	public static HashMap<UUID,BlockPlaceManager> instances = new HashMap<UUID,BlockPlaceManager>();
	public static List<BlockPlaceManager> list = new ArrayList<BlockPlaceManager>();
	
	
	private UUID player;

	private List<Block> blocks;
	
	public BlockPlaceManager(Player player, List<Block> blocks) {
		this.blocks = blocks;
		this.player = player.getUniqueId();
		instances.put(player.getUniqueId(), this);
		list.add(this);
	}
	public BlockPlaceManager(Player player, Block b) {
		this.blocks = new ArrayList<Block>();
		
		blocks.add(b);
		
		this.player = player.getUniqueId();
		instances.put(player.getUniqueId(), this);
		list.add(this);
	}
	
	public static void removeManager(Player player){
		instances.remove(player.getUniqueId());
		
	}
	
	public void removeBlock(Block b){
		if(getBlocks().contains(b)){
			getBlocks().remove(b);
		}
	}
	
	
	public UUID getPlayer() {
		return player;
	}
	
	public static List<BlockPlaceManager> getList() {
		return list;
	}
	
	public static boolean isC4(Block b){
		
		for(BlockPlaceManager m : list){
			if(m.getBlocks().contains(b)){
				
				return true;
			}
		}
		
		
		return false;
	}
	
	
	public static BlockPlaceManager getOwner(Block b){
		for(BlockPlaceManager m : list){
			if(m.getBlocks().contains(b)){
				return m;
			}
		}
		return null;
	}
	
	public List<Block> getBlocks() {
		return blocks;
	}
	
	public void addBlock(Block block){
		getBlocks().add(block);
	}
	
	
	
	public static BlockPlaceManager getManager(Player player){
		return instances.get(player.getUniqueId());
	}
	
	public static boolean hasBlockPlaceManager(Player player){
		if(instances.containsKey(player.getUniqueId())){
			return true;
		}else{
			return false;
		}
	}
	
}
