package me.artish1.CrystalClash.entities;

import com.google.common.collect.Sets;
import me.artish1.CrystalClash.CrystalClash;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Zombie;

import java.lang.reflect.Field;
import java.util.EnumMap;
import java.util.HashSet;

public class ModelZombie
  extends EntityZombie
{


     public ModelZombie(EntityTypes<? extends EntityZombie> entitytypes, World world){
         this(world);
     }


public ModelZombie(World world)
  {
    super(EntityTypes.ZOMBIE, world);

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
  
  public ModelZombie(World world, String customName, org.bukkit.inventory.ItemStack[] armor, org.bukkit.inventory.ItemStack inHand)
  {
    this(world);

    if (!CrystalClash.useHolograms)
    {
        ChatComponentText nameBase = new ChatComponentText(customName);
        setCustomName(nameBase);
        setCustomNameVisible(true);
    }
    Zombie zombie = (Zombie)getBukkitEntity();
    zombie.getEquipment().setArmorContents(armor);
    zombie.getEquipment().setItemInHand(inHand);
  }

  
  protected void damageArmor(float f) {}
  
  public void b(net.minecraft.server.v1_15_R1.ItemStack itemstack) {}
  
  public void setEquipment(org.bukkit.inventory.ItemStack[] armor, org.bukkit.inventory.ItemStack inHand)
  {
    Zombie zombie = (Zombie)getBukkitEntity();
    zombie.getEquipment().setArmorContents(armor);
    zombie.getEquipment().setItemInHand(inHand);
  }
  
  public void setOnFire(int i) {}


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

    public boolean damageEntity(DamageSource arg0, float arg1)
  {
    return false;
  }
  
}
