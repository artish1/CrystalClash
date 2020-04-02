package me.artish1.CrystalClash.Classes.Abilities;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import me.artish1.CrystalClash.Util.MySQLUtil;
import me.artish1.CrystalClash.entities.CustomEntityType;
import me.artish1.CrystalClash.entities.SummonedZombie;
import me.artish1.CrystalClash.other.SummonManager;

public class SpawnZombie extends Ability{

	public SpawnZombie(Player player) {
		super("Spawn Zombie", getCooldown(player.getUniqueId()), player);
	}
	
	public static int getCooldown(UUID id)
	{
		int cooldown = 20;
		if(MySQLUtil.getBoolean(id, "necromancerinfo", "szbcd"))
		{
			cooldown-=7;
		}
		
		return cooldown;
	}

	@Override
	public void onCast() {
		Location loc = player.getTargetBlock( null, 40).getLocation().add(0, 1.2, 0);
		SummonedZombie skellie = (SummonedZombie) CustomEntityType.SUMMONED_ZOMBIE.spawn(loc);
		skellie.setOwner(player);
		
		//Effects-----
        loc.getWorld().spawnParticle(Particle.CRIT_MAGIC, loc, 10);
        loc.getWorld().spawnParticle(Particle.LAVA, loc, 40);
		//------------
		
		SummonManager.getManager(player).addSummoned(skellie);
		
		super.onCast();

	}
	
}
