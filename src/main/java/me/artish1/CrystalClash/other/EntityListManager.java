package me.artish1.CrystalClash.other;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.entity.Entity;

public class EntityListManager {
	private HashSet<Entity> ids = new HashSet<Entity>();
	private UUID owner;
	
	
	
	public EntityListManager(UUID owner) {
		this.owner = owner;
	}
	
	
	public HashSet<Entity> getIds() {
		return ids;
	}
	
	public UUID getOwner() {
		return owner;
	}
	
	
	
}
