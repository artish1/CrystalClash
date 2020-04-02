package me.artish1.CrystalClash.crates;

import java.util.ArrayList;
import java.util.List;

import me.artish1.CrystalClash.Util.Methods;

public class Crates {
	
	private static List<Crate> crates = new ArrayList<Crate>();
	
	public static void init()
	{
		
		addCrate(new StoneCrate());
		addCrate(new GoldCrate());
		
	}
	
	public static Crate getRandomCrate()
	{
		int rNum = Methods.getRandom().nextInt(crates.size());
		return crates.get(rNum);
	}
	
	public static void addCrate(Crate crate)
	{
		crates.add(crate);
	}
	
	public static List<Crate> getCrates() {
		return crates;
	}
	
}
