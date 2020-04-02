package me.artish1.CrystalClash.entities;
import java.lang.reflect.Field;
import java.util.EnumMap;

import com.google.common.collect.Sets;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.v1_15_R1.util.UnsafeList;
import org.bukkit.entity.Sheep;

import me.artish1.CrystalClash.CrystalClash;


public class EntityGunSheep extends EntitySheep{
	
	private Entity passenger;

    public EntityGunSheep(EntityTypes<? extends EntitySheep> entityTypes, World world) {
        this(world);
    }

    public EntityGunSheep(World world) {
        super(EntityTypes.SHEEP, world);

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

        if(!CrystalClash.useHolograms){
            ChatComponentText nameBase = new ChatComponentText(ChatColor.RED + "Machine Gun (Click to mount)");
            setCustomName(nameBase);
            setCustomNameVisible(true);
        }
        Sheep sheep = (Sheep) getBukkitEntity();
        sheep.setAgeLock(true);
        sheep.setColor(DyeColor.PURPLE);
    }


    public void setColor(DyeColor color){
		Sheep sheep = (Sheep) getBukkitEntity();
		sheep.setColor(color);
		
	}


    @Override
    public void e(Vec3D vec3d) {
//        super.e(vec3d);
        if (this.passenger == null || !(this.passenger instanceof EntityHuman)) {
            // super.g(arg0, arg1);
            //this.S = 0.5F;    // Make sure the entity can walk over half slabs, instead of jumping
            return;
        }

        this.lastYaw = this.yaw = this.passenger.yaw;

        this.pitch = this.passenger.pitch * 0.5F;

        // Set the entity's pitch, yaw, head rotation etc.
        this.setYawPitch(this.yaw, this.pitch); //[url]https://github.com/Bukkit/mc-dev/blob/master/net/minecraft/server/Entity.java#L163-L166[/url]
//        this.aI=this.aG= this.yaw;
    }

	@Override
	public void setOnFire(int i) {
	
	}

	@Override
	public boolean damageEntity(DamageSource arg0, float arg1) {
		return false;
	}
	
	
}
