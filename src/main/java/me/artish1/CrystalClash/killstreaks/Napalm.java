package me.artish1.CrystalClash.killstreaks;

import me.artish1.CrystalClash.Arena.Arena;
import me.artish1.CrystalClash.Arena.ArenaTeam;
import me.artish1.CrystalClash.Listeners.KillstreakListener;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.other.ClassInventories;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.List;

public class Napalm extends Killstreak{

	public Napalm() {
		super(ChatColor.RED + "Napalm");
		setKillsNeeded(8); 
		setItem(Methods.createItem(Material.FIRE_CHARGE, ChatColor.RED + "Napalm", ClassInventories.createLore("Click to" +
                " activate!")));
	}
	
	
	
	
	private int taskId;
	
	@SuppressWarnings("deprecation")
	@Override
	public void onActivate(final Player p) {
		Location targeted = p.getTargetBlock( null, 100).getLocation();
		final List<Location> locs =  Methods.circle(targeted, 6, 1, false, false, 70);
		final BlockFace face = Methods.yawToFace(p.getLocation().getYaw());

		taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Methods.getPlugin(), new Runnable(){

			public void run() { 
					Location loc = Methods.getRandomLocation(locs);
					FallingBlock fire = loc.getWorld().spawnFallingBlock(loc, Material.FIRE,(byte) 0);
					fire.setDropItem(false);
					fire.setVelocity(new Vector(face.getModX() / 2,0,face.getModZ() / 2));
					KillstreakListener.napalms.put(fire.getUniqueId(), (Methods.getArena().isBlue(p)) ? Arena.blueTeam : Arena.redTeam );
					
					
					
					locs.remove(loc);
					if(locs.size() <= 0){
						Bukkit.getScheduler().cancelTask(taskId);
					}
					
					
			}
			
		}, 0, 3);
		removeKillstreak(p);
		super.onActivate(p);
	}
	
	public static void removeSafeLocs(final Block b, final ArenaTeam type){
		Bukkit.getScheduler().scheduleSyncDelayedTask(Methods.getPlugin(), new Runnable(){

			public void run() {
				if(b.getType() == Material.FIRE){
					removeSafeLocs(b, type);
					return;
				}
				
				if(type == Arena.blueTeam){
					
					
						if(KillstreakListener.blueNapalmLocs.contains(b.getLocation())){
							KillstreakListener.blueNapalmLocs.remove(b.getLocation());
						}
					
					
					
				}else if(type == Arena.redTeam){
						if(KillstreakListener.redNapalmLocs.contains(b.getLocation())){
							KillstreakListener.redNapalmLocs.remove(b.getLocation());
						}
				}
				
			}
			
		}, 20 * 15);
	}
	
	
	

}
