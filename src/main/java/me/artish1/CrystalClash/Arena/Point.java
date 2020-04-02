package me.artish1.CrystalClash.Arena;


import org.bukkit.ChatColor;

public enum Point{
	RED(ChatColor.RED + "Red"),
	BLUE(ChatColor.BLUE + "Blue"),
	NEUTRAL(ChatColor.WHITE +"None");
	
	private String name;

	private Point(String name) {
		this.name = name;

	}
	

	
	 public String getName() {
		return name;
	}
	 
	 @Override
	public String toString() {
		return name;
	}
	 
	 
	 
	 
}