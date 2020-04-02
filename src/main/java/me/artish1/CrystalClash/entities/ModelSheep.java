package me.artish1.CrystalClash.entities;

import me.artish1.CrystalClash.Arena.ArenaTeam;
import net.minecraft.server.v1_15_R1.*;

public class ModelSheep extends EntitySheep {

    public ModelSheep(EntityTypes<? extends EntitySheep> entityTypes, World world){
        this(world);
    }
	public ModelSheep(World world) {
		super(EntityTypes.SHEEP, world);
	}
	
	public void setWoolColor(EnumColor color){
		setColor(color);
	}
	
	
	public void updateName(ArenaTeam type){
        ChatComponentText nameBase = new ChatComponentText(type.getChatColor() + "Join " + type.getName() +": " + type.getQueue().size());
        setCustomName(nameBase);
        setCustomNameVisible(true);
	}


    @Override
    public void move(EnumMoveType enummovetype, Vec3D vec3d) {

    }

    @Override
	public void setOnFire(int i) {
		
	}
	
	
	@Override
	public boolean damageEntity(DamageSource arg0, float arg1) {
		return false;
	}
	
	
	
}
