package me.artish1.CrystalClash.objects;


import me.artish1.CrystalClash.Listeners.Classes.EngineerListener;
import me.artish1.CrystalClash.Listeners.GameListener;
import me.artish1.CrystalClash.Util.Methods;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;

import java.util.ArrayList;

import static me.artish1.CrystalClash.Arena.Arena.blueTeam;
import static me.artish1.CrystalClash.Arena.Arena.redTeam;

public class Turret extends CBuilding{
	
	private Location placed;
	private Location dispenser;
	
	public Turret(Player player, Location placed) {
		super(player,new ArrayList<Block>() );
		this.placed = placed;
		setup();
		BuildingManager.getTurrets().add(this);
	}
	
	
	public Dispenser getDispenser() {
		return (Dispenser) dispenser.getBlock().getState();
	}
	
	@Override
	public void remove() {
		if(BuildingManager.getTurrets().contains(this))
		BuildingManager.getTurrets().remove(this);
		
		
		super.remove();
	}
	
	public Location getPlaced() {
		return placed;
	}
	

	public void setup(){
		Block leg = placed.getBlock();
		Location loc = new Location(placed.getWorld(),placed.getX(),placed.getY() + 1, placed.getZ());
		Block dispenser = loc.getBlock();

		if(blueTeam.getPlayerList().contains(Methods.getArena().getArenaPlayer(getOwner()))){
            leg.setType(Material.BLUE_WOOL);

		}
		if(redTeam.getPlayerList().contains(Methods.getArena().getArenaPlayer(getOwner()))){
			leg.setType(Material.RED_WOOL);

		}
		dispenser.setType(Material.DISPENSER);
		
		this.dispenser = dispenser.getLocation();
		getBlocks().add(leg);
		getBlocks().add(dispenser);
		
		setAlive(true); 
	}
	
	private Vector getArrowDirection(Location second_location, Location first_location){
		Vector from = new Vector(first_location.getX(), first_location.getY(), first_location.getZ());
		Vector to = new Vector(second_location.getX(), second_location.getY(), second_location.getZ());
		 
		Vector vector = from.subtract(to);
		vector.normalize();
		vector.multiply(2.5);
		return vector;
	}
	
	private void attack(Location loc){
		Entity e = loc.getWorld().spawn(new Location(dispenser.getWorld(), dispenser.getX() + 0.5, dispenser.getY() + 1.5, dispenser.getZ() + 0.5), Arrow.class);
		//Entity e = getDispenser().getBlockProjectileSource().launchProjectile(Arrow.class,
		//		getArrowDirection(getDispenser().getLocation(), loc));
	
		Projectile proj = (Projectile) e;
		proj.setVelocity(getArrowDirection(proj.getLocation(), loc));
		proj.setShooter(getOwner());
		EngineerListener.entities.add(proj.getUniqueId()); 
	//	proj.getVelocity().multiply(1);
		
	}
	
	private void playTargetSound(){
		placed.getWorld().playSound(placed, Sound.BLOCK_ANVIL_LAND, 1, 1);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Methods.getPlugin(), new Runnable(){

			public void run() {
				placed.getWorld().playSound(placed, Sound.BLOCK_ANVIL_LAND, 1, 2);
				
			}
			
		}, 5);
	}
	
	int taskId;
	public void start(){
		
		taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Methods.getPlugin(), new Runnable(){

			public void run() {
				if(!isAlive()){
					Bukkit.getScheduler().cancelTask(taskId);
					return;
				}
				
				
				for(Entity e : Methods.getNearbyEntities(dispenser, 15)){
					if(e instanceof Player){
						if(Methods.getPlugin().getArena().isOnSameTeam(getOwner(), (Player) e))
							continue;
						
						Player target = (Player) e;
						if(GameListener.respawnQueue.contains(target.getUniqueId()))
							continue;
						
						
						playTargetSound();
						attack(target.getEyeLocation()); 
						
						
						
					}
				}
			}
			
		}, 0, 38);
	}
	

	
	
	
}
