package me.artish1.CrystalClash.Arena;

import org.bukkit.ChatColor;

public enum GameState {
	LOBBY(ChatColor.YELLOW + "Waiting"),
	STARTING(ChatColor.AQUA + "Starting"),
	INGAME(ChatColor.GREEN + "Ingame"),
	STOPPING(ChatColor.DARK_RED + "Ending"), RESTARTING(ChatColor.RED + "Restarting...");
	private String rep;
	private GameState(String stuff)
	{
		this.rep = stuff;
	}
	
	public String getRep() {
		return rep;
	}
	
}
