package me.artish1.CrystalClash.killstreaks;

import java.util.ArrayList;
import java.util.List;

import me.artish1.CrystalClash.Util.Methods;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Killstreak {
	
	private static List<Killstreak> killstreaks = new ArrayList<Killstreak>();
	public static List<Killstreak> getKillstreaks() {
		return killstreaks;
	}
	
	public static void loadKillstreaks(){
		killstreaks.add(new Napalm());
		killstreaks.add(new Nuke());
		killstreaks.add(new DeathCall());
	//	killstreaks.add(new ChopperGunner()); 
	}
	
	public Killstreak(String name) {
		this.name = name;
	}
	
	private ItemStack item;
	private int killsNeeded;
	
	private String name;
	
	public ItemStack getItem() {
		return item;
	}
	
	public String getName() {
		return name;
	}
	
	public int getKillsNeeded() {
		return killsNeeded;
	}
	
	
	public void activateKillstreak(Player p){
		
		onActivate(p);
		
	}
	
	public void removeKillstreak(Player p){
		if(Methods.getPlugin().getArena().getArenaPlayer(p).getAvaliable().contains(this)){
			Methods.getPlugin().getArena().getArenaPlayer(p).getAvaliable().remove(this);
			Methods.getPlugin().getArena().getArenaPlayer(p).getBought().remove(getItem());
			if(p.getInventory().contains(getItem())){
				p.getInventory().remove(getItem());
			}
		}
	}
	
	public void setItem(ItemStack item) {
		this.item = item;
	}
	
	public void setKillsNeeded(int killsNeeded) {
		this.killsNeeded = killsNeeded;
	}
	
	
	public void onActivate(Player p){  
		p.sendMessage(ChatColor.GRAY + "You have activated: " + getName());
	}
	
	
	
	
}
