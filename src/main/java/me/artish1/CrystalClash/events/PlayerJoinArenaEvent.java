package me.artish1.CrystalClash.events;

import me.artish1.CrystalClash.Arena.Arena;
import me.artish1.CrystalClash.Arena.ArenaPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerJoinArenaEvent extends Event implements Cancellable{
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	private ArenaPlayer arenaPlayer;
	private Arena arena;
	
	public PlayerJoinArenaEvent(ArenaPlayer player,Arena arena) {
		this.arenaPlayer = player;
		this.arena = arena;
	}
	
	public Arena getArena() {
		return arena;
	}
	
	public ArenaPlayer getArenaPlayer() {
		return arenaPlayer;
	}
	
	
	public Player getPlayer()
	{
		return arenaPlayer.getPlayer();
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean arg0) {
		cancelled = arg0;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList()
	{
		return handlers;
	}
	
}
