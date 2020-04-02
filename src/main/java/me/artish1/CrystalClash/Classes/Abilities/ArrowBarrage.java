package me.artish1.CrystalClash.Classes.Abilities;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

import me.artish1.CrystalClash.Listeners.Classes.ArcherListener;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.Util.MySQLUtil;
import me.artish1.CrystalClash.other.ClassInventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ArrowBarrage extends Ability{

	public ArrowBarrage(Player player) {
		super("Arrow Barrage", plugin.kits.getInt("Archer.ArrowBarrage.Cooldown"), ClassInventories.getArcherArrowBarrage(), player);
		abilities.add(this);
	}
	private int id;
	
	int waves = 5;
	int arrowsPerWave = 25;
	int arrows = arrowsPerWave;
	@Override
	public void onCast() {

		Location loc = player.getTargetBlock(null, 40).getLocation();
		player.sendMessage(ChatColor.GRAY + "Arrow Barrage incoming!");
		
		final List<Location> locs = Methods.circle(loc, 8, 80, false, false, 70);
		
		final Random r = new Random();
		final BowChangeMode bowChange;
		if(ArcherListener.map.containsKey(player.getUniqueId())){
			bowChange = ArcherListener.map.get(player.getUniqueId());
		}else{
			bowChange = new BowChangeMode(player);
			ArcherListener.map.put(player.getUniqueId(), bowChange);
		}
		
		
		
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){

			public void run() {
				if(waves <= 0){
					Bukkit.getScheduler().cancelTask(id);
					waves = 5;
					arrows = arrowsPerWave;
				}
				
				if(arrows <= 0){
					waves--;
					arrows = arrowsPerWave;
				}
				
				int rindex = r.nextInt(locs.size());
				
				Location toSpawn = locs.get(rindex);
				int currentMode = bowChange.getMode();
				bowChange.setMode(1);
				Arrow arrow = toSpawn.getWorld().spawnArrow(toSpawn, new Vector(0,-0.1,0), 2, 1);
				arrow.setShooter(player);
				if(MySQLUtil.getBoolean(player.getUniqueId(), "archerinfo", "firebarrage")){
					arrow.setFireTicks(20 * 10); 
				}
				arrow.setCritical(true);
				bowChange.setMode(currentMode);
				
				
				arrows--;
				
				
			}
			
		}, 0, 1);
		
	}
	

}
