package me.artish1.CrystalClash.Arena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.artish1.CrystalClash.CrystalClash;
import me.artish1.CrystalClash.Util.Methods;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ArenaManager {
	
	
	private static List<Arena> arenas = new ArrayList<Arena>();
	private static HashMap<UUID,Arena> players = new HashMap<UUID,Arena>();
	
	
	public static void removeArena(Player player){
		players.remove(player.getUniqueId());
	}
	
	public static void addArena(Player player, Arena arena){
		players.put(player.getUniqueId(), arena);
	}
	
	public static List<Arena> getArenas(){
		return arenas;
	}
	
	public static void deleteArena(Player player, String arenaName){
		if (Methods.getPlugin().getConfig().contains(arenaName)){
            Methods.getPlugin().getConfig().set(arenaName, null);
            Methods.getPlugin().arenas.set("Arenas." + arenaName, null);
            
            Methods.removeFromList(arenaName);
            
            Methods.saveYamls();
            Methods.getPlugin().saveConfig();
            CrystalClash.sendMessage(player, "You have deleted " + ChatColor.DARK_RED + arenaName);
          }
          else
          {
            CrystalClash.sendMessage(player, "Sorry, there is no such arena named " + ChatColor.RED + arenaName);
          }
	}
	
	public static void createArena(String arenaName){
		Methods.getPlugin().arenas.addDefault("Arenas." + arenaName, arenaName);
        Methods.getPlugin().getConfig().addDefault(arenaName + ".Countdown", Integer.valueOf(15));
        Methods.getPlugin().getConfig().addDefault(arenaName + ".MaxPlayers", Integer.valueOf(20));
        Methods.getPlugin().getConfig().addDefault(arenaName + ".PointsGoal", 100);
        Methods.getPlugin().getConfig().addDefault(arenaName + ".AutoStartPlayers", Integer.valueOf(8));
        Methods.getPlugin().getConfig().addDefault(arenaName + ".EndTime", Integer.valueOf(600));
        Arena arena = new Arena(arenaName, Methods.getPlugin());
        ArenaManager.addArena(arena);
        Methods.addToList(arena);
        Methods.saveYamls();
        Methods.getPlugin().saveConfig();
	}
	
	public static void stopArena(Arena arena, Player stopper){
		
		arena.sendAll(ChatColor.RED + stopper.getName() + ChatColor.GRAY + " Has stopped the arena!");
		arena.stop(StopReason.PLAYER_COMMAND);
	}
	
	public static Arena getArena(Player player){
		if(players.containsKey(player.getUniqueId()))
		return players.get(player.getUniqueId());
		return null;
	}
	
	public static boolean isInArena(Player player){
		if(player == null){
			return false;
		}
		if(player.getUniqueId() == null){
			return false;
		}
		
		if(players.containsKey(player.getUniqueId())){
			return true;
		}else{
			return false;
		}
	}
	
	
	public static void addArena(Arena arena) {
		if(!arenas.contains(arena)){
			arenas.add(arena);
		}
	}
	
	public static void removeArena(Arena arena){
		if(arenas.contains(arena)){
			arenas.remove(arena);
		}
	}
	
	public static Arena getArena(String name){
		for(Arena arena:arenas){
			if(arena.getName().equalsIgnoreCase(name)){
				return arena;
			}
		}
		return null;
	}
	
	public static boolean arenaExists(String name){
		for(Arena arena : arenas){
			if(arena.getName().equalsIgnoreCase(name)){
				return true;
			}
		}
		
		return false;
	}

}
