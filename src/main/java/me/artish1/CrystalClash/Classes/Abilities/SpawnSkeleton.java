package me.artish1.CrystalClash.Classes.Abilities;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.Util.MySQLUtil;
import me.artish1.CrystalClash.entities.CustomEntityType;
import me.artish1.CrystalClash.entities.SummonedSkeleton;
import me.artish1.CrystalClash.other.SummonManager;

public class SpawnSkeleton extends Ability{

	public SpawnSkeleton(Player player) {
		super("Summon Skeleton", 25, player);
	}
	
	public static int getCooldown(UUID id)
	{
		int cooldown = 25;
		
		if(MySQLUtil.getBoolean(id, "necromancerinfo", "sskcd"))
		{
			cooldown-=7;
		}
		
		return cooldown;
	}

	@Override
	public void onCast() {
		
		
		
		
		Location loc = player.getTargetBlock( null, 40).getLocation().add(0,1.2,0);
		SummonedSkeleton skellie = (SummonedSkeleton) CustomEntityType.SUMMONED_SKELETON.spawn(loc);
		skellie.setOwner(player);
		
	

			//Effects-----
			loc.getWorld().spawnParticle(Particle.CRIT_MAGIC, loc, 10);
			loc.getWorld().spawnParticle(Particle.LAVA, loc, 40);
			//------------
			

		SummonManager.getManager(player).addSummoned(skellie);
		
		super.onCast();
	}
	
	
}
