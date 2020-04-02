package me.artish1.CrystalClash.other;

import me.artish1.CrystalClash.Util.Methods;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Rocket {
	
	private int id;
	private Entity e;
	private boolean hasTarget = false;
	private Player target;
	private ArrowStraightener straightener;
	private Player owner;
	
	public Rocket(Entity rocket,ArrowStraightener straight,Player owner) {
		this.e = rocket;
		this.straightener = straight;
		this.owner = owner;
	}
	
	public ArrowStraightener getStraightener() {
		return straightener;
	}
	
	public void edgeToLocation(Location loc){
	//	Vector from = new Vector(e.getLocation().getX(), e.getLocation().getY(), e.getLocation().getZ());
		Vector to = new Vector(loc.getX(), loc.getY(), loc.getZ());
		e.getVelocity().subtract(to).normalize().multiply(2.5); 
		///Vector vector = from.subtract(to);
		//vector.normalize();
		//vector.multiply(2.5);
		
	}
	
	public void start(){
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Methods.getPlugin(), new Runnable(){

			@Override
			public void run() {
				if(hasTarget){
					if(!target.isDead() && target.isValid()){
						Methods.sendEntityToLocation(e, target.getEyeLocation());
						//edgeToLocation(target.getEyeLocation());
					}else{
						Bukkit.getScheduler().cancelTask(id);
					}
				}else{
					findTarget();
				}
			}
			
		}, 0, 4);
	}
	
	private void findTarget(){
		for(Entity ent : e.getNearbyEntities(14, 14, 14)){
			if(ent instanceof Player){
				if(Methods.getPlugin().getArena().isOnSameTeam(owner, (Player) ent))
					continue;
				
				
				target = (Player) ent;
				hasTarget = true;
				straightener.stop();
				break;
			}
		}
	}
	
	
}
