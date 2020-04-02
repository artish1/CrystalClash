package me.artish1.CrystalClash.entities.pathfinders;

import me.artish1.CrystalClash.Listeners.GameListener;
import me.artish1.CrystalClash.entities.EntityDeath;
import net.minecraft.server.v1_15_R1.PathfinderGoal;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

public class PathfinderGoalDeathAttack extends PathfinderGoal {
	
	private EntityDeath entity;
	public PathfinderGoalDeathAttack(EntityDeath entity) {
		this.entity = entity;
	}
	
	
	
	@Override
	public boolean a() {
		if(!entity.isAlive() || !entity.valid){ 
			entity.die();
			return false;
		}	
		if(this.entity.target == null){
			entity.die();
			return false;
		}
		
		CraftLivingEntity livEnt = (CraftLivingEntity) this.entity.target.getBukkitEntity();
		if(livEnt instanceof Player){
			Player player = (Player) livEnt;
			if(GameListener.respawnQueue.contains(player.getUniqueId())){
				entity.die();
				return false;
			}
			
		}
		
		Location targ = this.entity.target.getBukkitEntity().getLocation();
		Location thisLoc = this.entity.getBukkitEntity().getLocation();
		double distance = targ.distance(thisLoc);
		if(distance > 25){
			entity.die();
			return false;
		}
		
		
		return true;
	}
	
	@Override
	public void c() {
		entity.setGoalTarget(this.entity.target, TargetReason.CUSTOM, false);
	}
	

}
