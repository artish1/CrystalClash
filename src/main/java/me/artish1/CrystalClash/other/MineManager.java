package me.artish1.CrystalClash.other;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.UUID;

public class MineManager {
	private HashSet<Location> locs;
	private UUID owner;
	
	
	public MineManager(Player player) {
		this.owner = player.getUniqueId();
		locs = new HashSet<>();
	}
	
	public Player getOwner() {
		return Bukkit.getPlayer(owner);
	}
	
	public HashSet<Location> getLocs() {
		return locs;
	}
	
	public void addLocation(Location loc){
		locs.add(loc);
	}
	
	public boolean isMine(Location loc){

		return (locs.contains(loc) && loc.getBlock().getType() == Material.STONE_PRESSURE_PLATE);
	}
	
	public void clearMines(){
		for(Location loc : locs){
			loc.getBlock().setType(Material.AIR);
		}
		locs.clear();
	}
	
	public void removeLocation(Location loc){
		locs.remove(loc);
	}
	
}
