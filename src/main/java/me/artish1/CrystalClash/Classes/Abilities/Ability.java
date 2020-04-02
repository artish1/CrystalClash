package me.artish1.CrystalClash.Classes.Abilities;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.artish1.CrystalClash.CrystalClash;
import me.artish1.CrystalClash.Arena.Arena;
import me.artish1.CrystalClash.Arena.ArenaManager;
import me.artish1.CrystalClash.Cooldown.Cooldown;
import me.artish1.CrystalClash.Util.Methods;
import net.minecraft.server.v1_15_R1.EntityInsentient;


public class Ability {
	private int hotkey;
	protected String name;
	protected long cooldown;
	protected Player player;
	protected UUID id;
	public static CrystalClash plugin = Methods.getPlugin();
	protected Arena arena;
	protected static ItemStack item;
	
	public static HashSet<Ability> abilities = new HashSet<Ability>();
	
	public Ability(String name, int cooldown, int hotkey,Player player) {
		this.hotkey = hotkey - 1;
		this.player = player;
		this.name = name;
		this.id = player.getUniqueId();
		this.cooldown = cooldown;
		this.arena = ArenaManager.getArena(player);
	}
	public Ability(String name, long cooldown,Player player) {
		this.hotkey = hotkey - 1;
		this.player = player;
		this.name = name;
		this.id = player.getUniqueId();
		this.cooldown = cooldown;
		this.arena = ArenaManager.getArena(player);
	}
	public Ability(String name, int cooldown,ItemStack item,Player player) {
		this.player = player;
		this.name = name;
		this.id = player.getUniqueId();
		this.cooldown = cooldown;
		this.arena = ArenaManager.getArena(player);
		Ability.item = item;
	}
	public Ability(String name, long cooldown,ItemStack item, Player player) {
		this.hotkey = hotkey - 1;
		this.player = player;
		this.name = name;
		Ability.item = item;
		this.id = player.getUniqueId();
		this.cooldown = cooldown;
		this.arena = ArenaManager.getArena(player);
	}

	public Ability(String name, int cooldown, EntityInsentient id){
		this.name = name;
		this.cooldown = cooldown;
		this.id = id.getBukkitEntity().getUniqueId();
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public void cast(){
		if(player != null)
		Methods.debug(player, "Starting to Cast Ability: " + getName());
		
		
		
		if(player != null){
			if(Cooldown.isCooling(player.getUniqueId(), name)){
				Cooldown.coolDurMessage(player, name);
				return;
			}
			Methods.debug(player, "Starting onCast");
			onCast();
			Methods.debug(player, "Ended onCast");
			Cooldown.add(player.getUniqueId(), name, cooldown, System.currentTimeMillis());
		}else{
			
			if(Cooldown.isCooling(id, name)){
				return;
			}
			onCast(); 
			Cooldown.add(id, name, cooldown, System.currentTimeMillis());
		}
	}
	
	public static CrystalClash getPlugin() {
		return plugin;
	}
	
	public void onCast(){
		
	}
	
	public long getCooldown() {
		return cooldown;
	}
	
	public int getHotkey() {
		return hotkey;
	}
	
	
	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}
	
	public void setHotkey(int hotkey) {
		this.hotkey = hotkey;
	}
	
	public String getName() {
		return name;
	}
	
	
	
	
	
}
