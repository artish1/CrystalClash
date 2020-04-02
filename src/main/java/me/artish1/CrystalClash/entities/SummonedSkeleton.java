package me.artish1.CrystalClash.entities;

import com.google.common.collect.Sets;
import me.artish1.CrystalClash.Arena.ArenaManager;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.entities.pathfinders.PathfinderGoalNearestAttackableNonTeammate;
import me.artish1.CrystalClash.other.ClassInventories;
import net.minecraft.server.v1_15_R1.*;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.Item;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_15_R1.util.UnsafeList;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.EnumMap;
import java.util.UUID;

public class SummonedSkeleton extends EntitySkeleton implements Summoned{
	
	private UUID owner;
	public SummonedSkeleton(EntityTypes<? extends EntitySkeleton> entityTypes, World world) {
	    this(world);
    }

	public SummonedSkeleton(World world) {
		super(EntityTypes.SKELETON, world);
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
        this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalNearestAttackableNonTeammate(this));
        
		getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.34);
		getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(15);
		getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(7);
		this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(60);
		Skeleton sk = (Skeleton) getBukkitEntity();
		sk.getEquipment().setItemInHand(new org.bukkit.inventory.ItemStack(Material.IRON_SWORD));

	}


    public void giveColoredArmor(Color color){
		ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
		ClassInventories.setLeatherColor(chestplate, color);
		Skeleton zomb = (Skeleton) this.getBukkitEntity();
		zomb.getEquipment().setChestplate(chestplate);
	}
	
	public void setOwner(Player player){
		this.owner = player.getUniqueId();
		if(Methods.getPlugin().getArena().isBlue(player)){
			Methods.getBlueSummons().add(this.getBukkitEntity().getUniqueId());
            ChatComponentText nameBase = new ChatComponentText(ChatColor.BLUE + player.getName() + "'s Skeleton");
            setCustomName(nameBase);
            setCustomNameVisible(true);
			giveColoredArmor(Color.BLUE);
		}else{
			if(Methods.getPlugin().getArena().isRed(player)){
				Methods.getRedSummons().add(this.getBukkitEntity().getUniqueId());
                ChatComponentText nameBase = new ChatComponentText(ChatColor.RED + player.getName() + "'s Skeleton");
                setCustomName(nameBase);
                setCustomNameVisible(true);
				giveColoredArmor(Color.RED);
			}else{
                ChatComponentText nameBase = new ChatComponentText(ChatColor.GOLD + player.getName() + "'s Skeleton");
                setCustomName(nameBase);
                setCustomNameVisible(true);
			}
		}
		setCustomNameVisible(true);
	}
	
	public Player getOwner(){
		return Bukkit.getPlayer(owner);
	}



//	public boolean r(Entity entity)
//	  {
//	    float f = (float)getAttributeInstance(GenericAttributes.e).getValue();
//	    int i = 0;
//
//	    boolean flag = entity.damageEntity(DamageSource.playerAttack(((CraftPlayer)getOwner()).getHandle()), f);
//	    if (flag)
//	    {
//	      if (i > 0)
//	      {
//	        entity.g(-MathHelper.sin(this.yaw * 3.141593F / 180.0F) * i * 0.5F, 0.1D, MathHelper.cos(this.yaw * 3.141593F / 180.0F) * i * 0.5F);
//	        this.motX *= 0.6D;
//	        this.motZ *= 0.6D;
//	      }
//	      int j = EnchantmentManager.getFireAspectEnchantmentLevel(this);
//	      if (j > 0)
//	      {
//	        EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(getBukkitEntity(), entity.getBukkitEntity(), j * 4);
//	        Bukkit.getPluginManager().callEvent(combustEvent);
//	        if (!combustEvent.isCancelled()) {
//	          entity.setOnFire(combustEvent.getDuration());
//	        }
//	      }
//	      a(this, entity);
//	    }
//	    return flag;
//	  }


    @Override
    protected void dropDeathLoot(DamageSource var0, int var1, boolean var2) {

    }

    @Override
    protected void dropExperience() {

    }

    @Override
    protected void dropInventory() {

    }

    @Override
	public void setOnFire(int i) {
		
	}

	@Override
	public int getExpReward() {
		return 0;
	}
	
	@Override
	protected int getExpValue(EntityHuman entityhuman) {
		return 0;
	}
	
	@Override
	protected void damageArmor(float f) {
		
	}
	
	@Override
	public boolean damageEntity(DamageSource damagesource, float f) {
		if(damagesource.getEntity() instanceof Player){
			Player player = (Player) damagesource.getEntity();
			if(ArenaManager.isInArena(getOwner())){
				if(ArenaManager.getArena(getOwner()).isOnSameTeam(getOwner(), player)){
					return false;
				}
				
			}
			
		}else if(damagesource.getEntity() instanceof Arrow || damagesource.getEntity() instanceof Snowball){
			Projectile proj = (Projectile) damagesource.getEntity();
			if(proj.getShooter() instanceof Player){
				if(ArenaManager.getArena(getOwner()).isOnSameTeam(getOwner(), (Player)proj.getShooter())){
					return false;
				}
			}
			
		}
		return super.damageEntity(damagesource, f);
	}

	
	
	@Override
	public EntityInsentient getEntity() {
		return this;
	}
	
	

	
	
}
