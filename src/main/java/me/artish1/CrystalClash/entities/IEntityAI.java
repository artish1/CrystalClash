package me.artish1.CrystalClash.entities;

import me.artish1.CrystalClash.Classes.ClassType;
import net.minecraft.server.v1_15_R1.EntityInsentient;

public interface IEntityAI {
	
	
	public EntityInsentient getEntity();
	
	public ClassType getClassType();
	public void setClassType(ClassType type);
	
	
}
