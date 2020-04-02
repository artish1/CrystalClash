package me.artish1.CrystalClash.Classes.Abilities;

import java.util.HashSet;

import me.artish1.CrystalClash.Listeners.Classes.EndermanListener;
import me.artish1.CrystalClash.other.ClassInventories;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class EnderTeleport extends Ability{

	public EnderTeleport(Player player) {
		super("Teleport", 6, ClassInventories.getEndermanTeleportItem(), player);
	}
	
	
	

	@Override
	public void onCast() {
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
		player.getWorld().playEffect(player.getLocation(), Effect.ENDER_SIGNAL, 1);
		Location loc = player.getTargetBlock(null, 24).getLocation();
		loc.setPitch(player.getLocation().getPitch());
		loc.setYaw(player.getLocation().getYaw());
		player.teleport(loc,TeleportCause.PLUGIN);
		player.getWorld().playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
		player.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, 1);
		EndermanListener.uuids.add(player.getUniqueId());
		autoRemove();
	}
	
	
	private void autoRemove(){
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			public void run() {
				if(EndermanListener.uuids.contains(player.getUniqueId())){
					EndermanListener.uuids.remove(player.getUniqueId());
				}
			}
			
		}, 20 * 2 + 10);
	}

}
