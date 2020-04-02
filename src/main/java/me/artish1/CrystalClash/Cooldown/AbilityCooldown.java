package me.artish1.CrystalClash.Cooldown;

import java.util.HashMap;
import java.util.UUID;

public class AbilityCooldown {
	 public String ability = "";
	 public UUID player;
	 public long seconds;
	 public long systime;
	 
	 public HashMap<String, AbilityCooldown> cooldownMap = new HashMap<String, AbilityCooldown>();

	 
	 public AbilityCooldown(UUID player, long seconds, long systime) {
	        this.player = player;
	        this.seconds = seconds;
	        this.systime = systime;
	 	}
	 
	 public AbilityCooldown(UUID player) {
	        this.player = player;
	    }
	 
}
