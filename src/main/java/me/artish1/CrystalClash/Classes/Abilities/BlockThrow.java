package me.artish1.CrystalClash.Classes.Abilities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import me.artish1.CrystalClash.Cooldown.Cooldown;
import me.artish1.CrystalClash.Listeners.Classes.EarthListener;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.other.RockThrowInfo;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class BlockThrow extends Ability {
	
	
	
	public static HashMap<UUID,RockThrowInfo> map = new HashMap<UUID,RockThrowInfo>();
	
	public BlockThrow(Player player) {
		super("Block Throw",
				Methods.getPlugin().kits.getInt("Earth.BlockThrow.Cooldown"),
				Methods.getPlugin().kits.getInt("Earth.BlockThrow.Hotkey"),player);
	}
	
	
	public static List<Integer> getThrowableBlocks(){
		return Methods.getPlugin().kits.getIntegerList("Earth.BlockThrow.ThrowableBlocks");
	}
	
	
	
	public static boolean isBlockThrowable(int id){
		if(getThrowableBlocks().contains(id)){
			return true;
		}
		return false;
	
	}
	
	private Vector getVelocity(Location first_location, Location second_location){
		double dX = first_location.getX() - second_location.getX();
		double dY = first_location.getY() - second_location.getY();
		double dZ = first_location.getZ() - second_location.getZ();
		
		double yaw = Math.atan2(dZ, dX);
		double pitch = Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + Math.PI;
		
		double X = Math.sin(pitch) * Math.cos(yaw);
		double Y = Math.sin(pitch) * Math.sin(yaw);
		double Z = Math.cos(pitch);
		 
		 
		return new Vector(X, Z, Y);
		
	}

	public void secondCast(Player player){
		if(map.containsKey(player.getUniqueId())){
			RockThrowInfo info = map.get(player.getUniqueId());
			if(!info.hitSecond()){
			Location loc = player.getTargetBlock(null, 50).getLocation();
			info.getFb().setVelocity(getVelocity(info.getFb().getLocation(),loc).multiply(2.5));
			info.setSecondHit(true);
			EarthListener.ids.add(info.getFb().getUniqueId());
			EarthListener.owners.put(info.getFb().getUniqueId(), player.getUniqueId());
			Cooldown.add(player.getUniqueId(), getName(), getCooldown(), System.currentTimeMillis());
			}
		}
	}

	@Override
	public void onCast() {
		
		
		final Block block = player.getTargetBlock(null, 24);
		
		if(!isBlockThrowable(block.getType().getId()))
			return;
		
		FallingBlock fb = block.getWorld().spawnFallingBlock(block.getLocation().add(0,0.5,0), block.getType(), block.getData());
		fb.setVelocity(new Vector(0,1,0));
		fb.setDropItem(false);
		EarthListener.firstCasts.add(fb.getUniqueId());
		map.put(player.getUniqueId(), new RockThrowInfo(fb,player,Methods.getPlugin()));
		Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable(){

			public void run() {
				block.getState().update(true);
			}
			
		}, 3);
	}
	
	
	
	@Override
	public void cast() {
		
		if(Cooldown.isCooling(player.getUniqueId(), getName())){
			Cooldown.coolDurMessage(player, getName());
			return;
		}
		
		if(map.containsKey(player.getUniqueId())){
		
			secondCast(player);
			return;
		}
		
		
		onCast();
	}
	
	

}
