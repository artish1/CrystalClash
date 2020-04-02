package me.artish1.CrystalClash.entities.pathfinders;

import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.entities.Summoned;
import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.Navigation;
import net.minecraft.server.v1_15_R1.PathEntity;
import net.minecraft.server.v1_15_R1.PathfinderGoal;
import org.bukkit.Location;

public class PathFinderGoalFollowOwner extends PathfinderGoal {
	
	public Summoned summoned;
	private Navigation navigation;
	private double speed = 1.4;
	public PathFinderGoalFollowOwner(Summoned summoned) {
		this.summoned = summoned;
		this.navigation = (Navigation) summoned.getEntity().getNavigation();
	}
	
	@Override
	public boolean a() {
		if(summoned.getEntity().getGoalTarget() != null){
			return false;
		}
		
		Location loc = summoned.getOwner().getLocation();
		Location summonedLoc = summoned.getEntity().getBukkitEntity().getLocation();
		
		double distance = loc.distance(summonedLoc);
        return !(distance < 5);
    }
	
	
	@Override
	public void c() {
        BlockPosition blockPos = new BlockPosition(summoned.getOwner().getLocation().getX() + Methods.getRandomRange(3) ,
                summoned.getOwner().getLocation().getY(),
                summoned.getOwner().getLocation().getZ() + Methods.getRandomRange(3));
        PathEntity pathEntity = this.navigation.a(blockPos,1);
        this.navigation.a(pathEntity, speed);
	}
	

}
