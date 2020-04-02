package me.artish1.CrystalClash.other;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;


public class ClaymoreInfo {
	
	private static HashMap<UUID,HashSet<Claymore>> claymores = new HashMap<>();
 	
	public static HashSet<Claymore> getClaymore(UUID owner){
		return claymores.get(owner);
	}
	
	public static boolean hasClaymore(UUID owner){
		return claymores.containsKey(owner);
	}
	
	public static void addClaymore(UUID owner, Claymore claymore){
		if(!claymores.containsKey(owner)){
		HashSet<Claymore> list = new HashSet<Claymore>();
		list.add(claymore);
		claymores.put(owner, list); 
		}else{
			HashSet<Claymore> list = claymores.get(owner);
			list.add(claymore);
			claymores.put(owner, list);
		}
	}
	
	public static void removeClaymore(UUID owner, Claymore claymore){
		if(claymores.get(owner).contains(claymore))
			claymores.get(owner).remove(claymore);
	}
	
	public static void clearClaymores(UUID owner){
		for(Claymore claymore : claymores.get(owner)){
			claymore.remove(false);
		}
		
		claymores.remove(owner);
		
	}
	
}
