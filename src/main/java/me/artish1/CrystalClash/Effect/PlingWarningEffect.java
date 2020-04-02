package me.artish1.CrystalClash.Effect;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PlingWarningEffect extends Effect{
	
	Player player;
	
	public PlingWarningEffect(Player player) {
		super(7, 3);
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	@Override
	public void onRun() {
		player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
	}
	

}
