package me.artish1.CrystalClash.entities.pathfinders;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.entity.Player;

import me.artish1.CrystalClash.Arena.ArenaManager;
import me.artish1.CrystalClash.Listeners.GameListener;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.entities.Summoned;


public class PathfinderGoalNearestAttackableNonTeammate
extends PathfinderGoal
{
protected Summoned summoned;
public EntityLiving target = null; 

//
private Navigation navigation;
private double speed = 1.4;
//

public PathfinderGoalNearestAttackableNonTeammate(Summoned summoned)
{
  this.summoned = summoned; 
	this.navigation = (Navigation) summoned.getEntity().getNavigation();
 
}


public boolean a()
{
	if(summoned.getEntity().getGoalTarget() != null){
		if(!summoned.getEntity().getGoalTarget().isAlive()){
			summoned.getEntity().setGoalTarget(null, null, false); 
		}else{
			Location loc = summoned.getEntity().getGoalTarget().getBukkitEntity().getLocation();
			Location summon = summoned.getEntity().getBukkitEntity().getLocation();
		
			double distanceTarget  = loc.distance(summon);
			if(distanceTarget > 20){
				summoned.getEntity().setGoalTarget(null, null, false);
			}
		
		}
	}
  List<EntityLiving> list = new ArrayList<>();
  
  for(org.bukkit.entity.Entity e : summoned.getEntity().getBukkitEntity().getNearbyEntities(10, 10, 10)){
	  CraftEntity ent = (CraftEntity) e;
	  if(ent.getHandle() instanceof EntityPlayer){
		  
		  list.add((EntityPlayer) ent.getHandle());
		
	  }
	  
  }
  
  
  if (list.isEmpty()) { 
	  followCheck();
    return false;
  }
  
  for(EntityLiving ep : list){
	 
		if(ep instanceof EntityPlayer){
		  Player player = ((EntityPlayer)ep).getBukkitEntity();
		  
		  if(ArenaManager.isInArena(summoned.getOwner())){
			  if(!ArenaManager.getArena(summoned.getOwner()).isOnSameTeam(player, summoned.getOwner())) {

                  if (GameListener.respawnQueue.contains(ep.getUniqueID())) {
                      continue;
                  }
                  this.target = ep;
                  return true;
              }

		  }
		  
		}
	  
  }
  followCheck();
  return false;
}
public void followCheck() {
	if(summoned.getEntity().getGoalTarget() != null){
		return;
	}
	
	Location loc = summoned.getOwner().getLocation();
	Location summonedLoc = summoned.getEntity().getBukkitEntity().getLocation();
	
	double distance = loc.distance(summonedLoc);
	if(distance < 4){
		return;
	}

	
	follow();
}


public void follow() {
    BlockPosition blockPos = new BlockPosition(summoned.getOwner().getLocation().getX() + Methods.getRandomRange(3) ,
            summoned.getOwner().getLocation().getY(),
            summoned.getOwner().getLocation().getZ() + Methods.getRandomRange(3));
	 PathEntity pathEntity = this.navigation.a(blockPos,1);

	 this.navigation.a(pathEntity, speed);
	
	
}


public void c()
{
  this.summoned.getEntity().setGoalTarget(this.target, null,false);
}


}

