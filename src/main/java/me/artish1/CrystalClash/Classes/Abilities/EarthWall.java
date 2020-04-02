package me.artish1.CrystalClash.Classes.Abilities;

import java.util.HashSet;
import java.util.List;

import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.other.BlockManager;
import me.artish1.CrystalClash.other.ClassInventories;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class EarthWall extends Ability{

	public EarthWall(Player player) {
		super("EarthWall", getPlugin().kits.getInt("Earth.EarthWall.Cooldown"), ClassInventories.getEarthWallItem(),player);
	}
	
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCast() {
		
		List<Block> blocks = Methods.getWallBlocks(player, player.getTargetBlock(null, 3),
				plugin.kits.getInt("Earth.EarthWall.Height"),
				plugin.kits.getInt("Earth.EarthWall.Width"));
		
		new BlockManager(blocks, plugin.kits.getInt("Earth.EarthWall.Duration"),
				Material.DIRT);
		
		Methods.setWallBlocks(player, player.getTargetBlock(null, 3),
				plugin.kits.getInt("Earth.EarthWall.Height"), plugin.kits.getInt("Earth.EarthWall.Width"),
				Material.DIRT);
		
	}
	
	
	
}
