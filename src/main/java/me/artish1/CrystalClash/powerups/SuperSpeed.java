package me.artish1.CrystalClash.powerups;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.other.ClassInventories;

public class SuperSpeed extends PowerUp{

	public SuperSpeed() {
		super("Super Speed");
		setItemStack(Methods.createItem(Material.FEATHER, "Super Speed!!!", ClassInventories.createLore("Super Speed powerup"))); 
		PowerUp.getPowerups().add(this);
	}
	
	
	@Override
	public void activatePowerUp(Player p) {
		
		if(Methods.getArena().getArenaPlayer(p).getType() != ClassType.SCOUT){
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 15, 4), true);
		}
		super.activatePowerUp(p); 
	}
	
}
