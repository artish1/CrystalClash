package me.artish1.CrystalClash.Cooldown;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;



import me.artish1.CrystalClash.Util.Methods;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Cooldown {
public static HashMap<UUID, AbilityCooldown> cooldownPlayers = new HashMap<UUID, AbilityCooldown>();
	
	
	public static void add(UUID player, String ability, long seconds, long systime) {
        if(!cooldownPlayers.containsKey(player)) cooldownPlayers.put(player, new AbilityCooldown(player));
        if(isCooling(player, ability)) return;
        cooldownPlayers.get(player).cooldownMap.put(ability, new AbilityCooldown(player, seconds * 1000, System.currentTimeMillis()));
    }
	
	public static boolean isCooling(UUID player, String ability) {
		if(!cooldownPlayers.containsKey(player)) return false;
		if(!cooldownPlayers.get(player).cooldownMap.containsKey(ability)) return false;
		return true;
		}
	
	public static double getRemaining(UUID player, String ability) {
        if(!cooldownPlayers.containsKey(player)) return 0.0;
        if(!cooldownPlayers.get(player).cooldownMap.containsKey(ability)) return 0.0;
        
        return UtilMath.convert((cooldownPlayers.get(player).cooldownMap.get(ability).seconds + cooldownPlayers.get(player).cooldownMap.get(ability).systime) - System.currentTimeMillis(), TimeUnit.SECONDS, 1);
    }
	
	 public static void handleCooldowns() {
	        if(cooldownPlayers.isEmpty()) {
	            return;
	        }
	        
	        for(Iterator<UUID> it = cooldownPlayers.keySet().iterator(); it.hasNext();) {
	            UUID key = it.next();
	            for(Iterator<String> iter = cooldownPlayers.get(key).cooldownMap.keySet().iterator(); iter.hasNext();) {
	                String name = iter.next();
	                if(getRemaining(key, name) <= 0.0) {
	                   iter.remove(); //removeCooldown(key, name);
	                }
	            }
	        }
	    }
	
	
	public static void coolDurMessage(Player player, String ability) {
        if(player == null) {
            return;
        }
        if(!isCooling(player.getUniqueId(), ability)) {
            return;
        }
        Methods.sendActionBar(player, ChatColor.GRAY + ability + " Cooldown: " + ChatColor.GREEN + getRemaining(player.getUniqueId(), ability) + " Seconds");
     //   player.sendMessage(ChatColor.GRAY + ability + " Cooldown: " + ChatColor.GREEN + getRemaining(player.getUniqueId(), ability) + " Seconds");
    }
	
	
	public static void removeCooldown(UUID player, String ability) {
        if(!cooldownPlayers.containsKey(player)) {
            return;
        }
        if(!cooldownPlayers.get(player).cooldownMap.containsKey(ability)) {
            return;
        }
        cooldownPlayers.get(player).cooldownMap.remove(ability);
       
    }
}
