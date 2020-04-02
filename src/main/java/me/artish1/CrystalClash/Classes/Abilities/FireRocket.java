package me.artish1.CrystalClash.Classes.Abilities;

import me.artish1.CrystalClash.Util.Methods;

import org.bukkit.entity.Player;

public class FireRocket extends Ability{

	public FireRocket(Player player) {
		super("Rocket", 1, player);
		
	}
	
	
	@Override
	public void onCast() {
		
		Methods.fireRocket(player);
		
		super.onCast();
	}
	
}
