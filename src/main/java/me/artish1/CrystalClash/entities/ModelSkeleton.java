package me.artish1.CrystalClash.entities;


import com.google.common.collect.Sets;
import me.artish1.CrystalClash.CrystalClash;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.EnumMap;

public class ModelSkeleton extends EntitySkeleton{

    public ModelSkeleton(EntityTypes<? extends EntitySkeleton> entitytypes, World world) {
        this(world);

    }

    public ModelSkeleton(World world){
        super(EntityTypes.SKELETON, world);

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
//    this.fireProof = true; TODO ?
    }


    public ModelSkeleton(World world, String customName, ItemStack[] armor, ItemStack inHand) {
        this(world);

        if (!CrystalClash.useHolograms)
        {
            ChatComponentText nameBase = new ChatComponentText(customName);
            setCustomName(nameBase);
            setCustomNameVisible(true);
        }
        Skeleton skeleton = (Skeleton)getBukkitEntity();
        skeleton.getEquipment().setArmorContents(armor);
        skeleton.getEquipment().setItemInHand(inHand);
    }


    @Override
	public void a(EntityLiving el, float f)
	{
		
	}
	
	public void setEquipment(ItemStack[] armor, ItemStack inHand){
		Skeleton skeleton = (Skeleton) getBukkitEntity();
        skeleton.getEquipment().setArmorContents(armor);
        skeleton.getEquipment().setItemInHand(inHand);
	}
	
	
	@Override
	protected void damageArmor(float f) {
	
	}

    @Override
    public void move(EnumMoveType enummovetype, Vec3D vec3d) {

    }


	
	@Override
	public void setOnFire(int i) {
		
	}

	
	@Override
	public void b(net.minecraft.server.v1_15_R1.ItemStack itemstack) {
		
	}



    @Override
	public boolean damageEntity(DamageSource arg0, float arg1) {
		
		return false;
	}

    @Override
    protected float getSoundVolume() {
        return 0f;
    }

    @Override
    protected boolean playStepSound() {
        return false;
    }

	

	
}
