package me.artish1.CrystalClash.entities;

import com.google.common.collect.Sets;
import me.artish1.CrystalClash.Arena.ArenaManager;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.entities.pathfinders.PathfinderGoalNearestAttackableNonTeammate;
import me.artish1.CrystalClash.other.ClassInventories;
import net.minecraft.server.v1_15_R1.*;
import net.minecraft.server.v1_15_R1.Item;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.util.UnsafeList;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.EnumMap;
import java.util.UUID;

public class SummonedZombie extends EntityZombie implements Summoned{
	
	private UUID owner;
	public SummonedZombie(EntityTypes<? extends EntityZombie> entityTypes, World world) {
	    this(world);
    }
	public SummonedZombie(World world) {
		super(EntityTypes.ZOMBIE, world);
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
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, 1.0D, false));
        this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.targetSelector.a(1, new PathfinderGoalNearestAttackableNonTeammate(this));

        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.34);
        getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(15);
        getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(7);
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(60);
		Zombie sk = (Zombie) getBukkitEntity();
		sk.getEquipment().setItemInHand(new org.bukkit.inventory.ItemStack(Material.IRON_SWORD));

	}
		
	public void giveColoredArmor(Color color){
		ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
		ClassInventories.setLeatherColor(chestplate, color);
		Zombie zomb = (Zombie) this.getBukkitEntity();
		zomb.getEquipment().setChestplate(chestplate);
	}
		
	public void setOwner(Player player){
		this.owner = player.getUniqueId();
		if(Methods.getPlugin().getArena().isBlue(player)){
			Methods.getBlueSummons().add(this.getBukkitEntity().getUniqueId());
			giveColoredArmor(Color.BLUE);
            ChatComponentText nameBase = new ChatComponentText(ChatColor.BLUE + player.getName() + "'s Zombie");
            setCustomName(nameBase);
            setCustomNameVisible(true);
		}else{
			if(Methods.getPlugin().getArena().isRed(player)){
				Methods.getRedSummons().add(this.getBukkitEntity().getUniqueId());
                ChatComponentText nameBase = new ChatComponentText(ChatColor.RED + player.getName() + "'s Zombie");
                setCustomName(nameBase);
                setCustomNameVisible(true);
                giveColoredArmor(Color.RED);
			}else{
                ChatComponentText nameBase = new ChatComponentText(ChatColor.GOLD + player.getName() + "'s Zombie");
                setCustomName(nameBase);
                setCustomNameVisible(true);		}
		}
		setCustomNameVisible(true);
		
		
	}
		
	public Player getOwner(){
		return Bukkit.getPlayer(owner);
	}
	/*
	@Override
	public boolean B(Entity entity)
	  {
	    float f = (float)getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).getValue();
	    int i = 0;
	    if ((entity instanceof EntityLiving))
	    {

	    	 f += EnchantmentManager.a(getEquipment(4), ((EntityLiving)entity).getMonsterType());
	    	 i += EnchantmentManager.a(this);   
	      }
	    boolean flag = entity.damageEntity(DamageSource.playerAttack(((CraftPlayer)getOwner()).getHandle()), f);
	    if (flag)
	    {
	      if (i > 0)
	      {
	        entity.g(-MathHelper.sin(this.yaw * 3.141593F / 180.0F) * i * 0.5F, 0.1D, MathHelper.cos(this.yaw * 3.141593F / 180.0F) * i * 0.5F);
	        this.motX *= 0.6D;
	        this.motZ *= 0.6D;
	      }
	      int j = EnchantmentManager.getFireAspectEnchantmentLevel(this);
	      if (j > 0)
	      {
	        EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(getBukkitEntity(), entity.getBukkitEntity(), j * 4);
	        Bukkit.getPluginManager().callEvent(combustEvent);
	        if (!combustEvent.isCancelled()) {
	          entity.setOnFire(combustEvent.getDuration());
	        }
	      }
	      a(this, entity);
	    }


	    return flag;
	  }

	*/


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
    public MinecraftKey getLootTable() {
        return null;
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
	public void setOnFire(int i) {
		
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
