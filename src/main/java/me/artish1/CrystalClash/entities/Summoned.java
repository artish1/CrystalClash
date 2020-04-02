package me.artish1.CrystalClash.entities;


import org.bukkit.entity.Player;

import net.minecraft.server.v1_15_R1.EntityInsentient;


public interface Summoned {
	
	

	
	public Player getOwner();
	public void setOwner(Player p);
	
	public EntityInsentient getEntity();
	
	
	
	
}
