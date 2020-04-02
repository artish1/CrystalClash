package me.artish1.CrystalClash.other;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import me.artish1.CrystalClash.entities.Summoned;

import org.bukkit.entity.Player;

public class SummonManager {
	public static HashMap<UUID,SummonManager> managers = new HashMap<>();
	
	private HashSet<Summoned> summoned = new HashSet<Summoned>();
	
	public int limit = 2;
	
	public SummonManager(Player p) {
		managers.put(p.getUniqueId(), this);
	}
	
	public HashSet<Summoned> getSummoned() {
		return summoned;
	}
	
	
	public boolean reachedLimit(){
		if(summoned.size() >= limit){
			boolean isFull = true;
			HashSet<Summoned> toRemove = new HashSet<>();
			for(Summoned s : summoned){
				if(s.getEntity().isAlive())
					continue;
				
				toRemove.add(s);
				isFull= false;
				break;
			}
			
			for(Summoned summ : toRemove){
				summoned.remove(summ);
			}
			
			
			return isFull;
		}else{
			return false;
		}
	}
	
	public boolean addSummoned(Summoned summon){
		if(summoned.size() >=limit){
			boolean isFull = true;
			HashSet<Summoned> toRemove = new HashSet<>();
			for(Summoned s : summoned){
				if(s.getEntity().isAlive())
					continue;
				
				toRemove.add(s);
				isFull= false;
				summoned.add(summon);
				break;
			}
			
			for(Summoned summ : toRemove){
				summoned.remove(summ);
			}
			
			
			return !isFull;
			 
		}else{
			summoned.add(summon);
			return true;
		}
	
	}
	
	
	
	
	
	public static boolean hasManager(Player p){
		return managers.containsKey(p.getUniqueId());
	}
	
	public static void removeManager(Player p){
		managers.remove(p.getUniqueId());
	}
	
	public static SummonManager getManager(Player p){
		return managers.get(p.getUniqueId());
	}
	
}
