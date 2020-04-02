package me.artish1.CrystalClash.Effect;

import java.util.List;

import org.bukkit.Location;

import me.artish1.CrystalClash.Util.Methods;
import org.bukkit.Particle;

public class LoopLocationsEffect extends Effect{
	private Particle particle;
	private List<Location> list;
	private int size;

	public LoopLocationsEffect(int delay, List<Location> list, Particle particle) {
		super(delay, list.size());
		this.list = list;
		this.particle = particle;
		size = list.size() -1;
	}
	
	
	public List<Location> getList() {
		return list;
	}
	
	
	@Override
	public void onRun() {
		Location loc = list.get(size);
		loc.getWorld().spawnParticle(particle, loc, 1);
		size--;
	}
	
	
}
