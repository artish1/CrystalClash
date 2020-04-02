package me.artish1.CrystalClash.objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BuildingManager {
	
	private static List<Turret> turrets = new ArrayList<Turret>();
	
	
	public static List<Turret> getTurrets() {
		return turrets;
	}
	
	public static boolean ownsTurret(Player p){
		for(Turret t : turrets){
			if(t.getOwner().getUniqueId() == p.getUniqueId()){
				return true;
			}
		}
		return false;
	}
	
	public static Turret getTurret(Player p){
		for(Turret t : turrets){
			if(t.getOwner().getUniqueId() == p.getUniqueId()){
				return t;
			}
		}
		return null;
	}
	
	public static Turret getTurretFromBreakPoint(Location loc){
		for(Turret t : turrets){
			int x = t.getPlaced().getBlockX();
			int y = t.getPlaced().getBlockY();
			int z = t.getPlaced().getBlockZ();
			if(x == loc.getBlockX() && y == loc.getBlockY() && z == loc.getBlockZ()){
				return t;
			}
		}
		
		return null;
	}
	
	public static boolean isTurretBreakPoint(Location loc){
		for(Turret t : turrets){
			int x = t.getPlaced().getBlockX();
			int y = t.getPlaced().getBlockY();
			int z = t.getPlaced().getBlockZ();
			if(x == loc.getBlockX() && y == loc.getBlockY() && z == loc.getBlockZ()){
				return true;
			}
		}
		
		return false;
	}
	
	
}
