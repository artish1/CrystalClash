package me.artish1.CrystalClash.entities;


import com.google.common.collect.Sets;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;

import me.artish1.CrystalClash.CrystalClash;

import java.lang.reflect.Field;
import java.util.EnumMap;

public class EntityVillagerMerchant extends EntityVillager{

    public EntityVillagerMerchant(EntityTypes<? extends EntityVillager> entityTypes, World world) {
        this(world);

    }


	public EntityVillagerMerchant(World world) {
		super(EntityTypes.VILLAGER, world);

        try {
            Field bField = PathfinderGoalSelector.class.getDeclaredField("c");
            bField.setAccessible(true);
            Field cField = PathfinderGoalSelector.class.getDeclaredField("d");
            cField.setAccessible(true);
            bField.set(goalSelector, new EnumMap(PathfinderGoal.Type.class));
            cField.set(goalSelector, Sets.newLinkedHashSet());
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));

        if (!CrystalClash.useHolograms)
        {
            ChatComponentText nameBase = new ChatComponentText(ChatColor.AQUA + "Merchant");
            setCustomName(nameBase);
            setCustomNameVisible(true);
        }
		
		Villager villager = (Villager) getBukkitEntity();
		villager.setAdult();
		villager.setRemoveWhenFarAway(false); 
		villager.setProfession(Profession.WEAPONSMITH);
	}


    @Override
    public void setOnFire(int i, boolean callEvent) {

    }


    @Override
	public void setOnFire(int i) {
		
	}

    @Override
    protected float getSoundVolume() {
        return 0f;
    }

    @Override
    protected boolean playStepSound() {
        return false;
    }


    @Override
    public void move(EnumMoveType enummovetype, Vec3D vec3d) {

    }

    @Override
	public boolean damageEntity(DamageSource arg0, float arg1) {
		return false;
	}
	
}
