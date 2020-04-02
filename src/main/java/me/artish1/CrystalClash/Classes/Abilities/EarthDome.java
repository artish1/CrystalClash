package me.artish1.CrystalClash.Classes.Abilities;

import java.util.List;

import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.other.BlockManager;
import me.artish1.CrystalClash.other.ClassInventories;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class EarthDome extends Ability{
	
	
	public EarthDome(Player player) {
		super("Earth Dome",
				Methods.getPlugin().kits.getInt("Earth.EarthDome.Cooldown"),
				ClassInventories.getEarthDomeItem(), player);
	}
	
	
	
	@Override
	public void onCast() {
		
		List<Block> blocks = Methods.getNearbyCircleBlocks(player.getLocation(), Methods.getPlugin().kits.getInt("Earth.EarthDome.Size"), 0,
				true, true, 0);
		
		new BlockManager(blocks, Methods.getPlugin().kits.getInt("Earth.EarthDome.Duration"), Material.getMaterial(Methods.getPlugin().kits.getString("Earth.EarthDome.Material")));
		
		Methods.setNearbyCircleBlocks(player.getLocation(), Methods.getPlugin().kits.getInt("Earth.EarthDome.Size"), 0, true, true, 0,
				Material.getMaterial(Methods.getPlugin().kits.getString("Earth.EarthDome.Material")));
		
		
	}
	
	
	
	
	
}
