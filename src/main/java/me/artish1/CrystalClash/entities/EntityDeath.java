package me.artish1.CrystalClash.entities;


import com.google.common.collect.Sets;
import me.artish1.CrystalClash.entities.pathfinders.PathfinderGoalDeathAttack;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.craftbukkit.v1_15_R1.util.UnsafeList;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.EnumMap;


public class EntityDeath extends EntityEnderman{
	
	public EntityLiving target;
	public Player owner;

	public EntityDeath(EntityTypes<? extends EntityEnderman> entityTypes, World world){
	    this(world);
    }

	public EntityDeath(World world) {
		super(EntityTypes.ENDERMAN, world);

		try {
            Field bField = PathfinderGoalSelector.class.getDeclaredField("c");
            bField.setAccessible(true);
            Field cField = PathfinderGoalSelector.class.getDeclaredField("d");
            cField.setAccessible(true);
            bField.set(goalSelector, new EnumMap(PathfinderGoal.Type.class));
            cField.set(goalSelector, Sets.newLinkedHashSet());
            bField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
            cField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
			} catch (Exception exc) {
				exc.printStackTrace();
			}

		
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.targetSelector.a(1, new PathfinderGoalDeathAttack(this));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, 1.0D, false));
        
		getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(16);
		getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.49);
		getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(55);

        ChatComponentText nameBase = new ChatComponentText(ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD.toString() + "Death");
        setCustomName(nameBase);
        setCustomNameVisible(true);
	}
	
	public void setOwner(Player owner) {
		this.owner = owner;
	}
	
	public Player getOwner() {
		return owner;
	}


    @Override
    protected void dropDeathLoot(DamageSource damagesource, int i, boolean flag) {

    }

    @Override
    protected void dropExperience() {

    }

    @Override
    protected void dropInventory() {

    }


    @Override
	public int getExpReward() {
		return 0;
	}
	
	
	@Override
	public void setOnFire(int i) {
		
	}
	
	

}	



