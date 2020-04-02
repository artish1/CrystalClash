package me.artish1.CrystalClash.events;

import me.artish1.CrystalClash.Arena.Arena;
import me.artish1.CrystalClash.Arena.ArenaPlayer;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLeaveArenaEvent extends Event implements Cancellable{
	private static final HandlerList handlers = new HandlerList();
	
	private ArenaPlayer player;
	private Arena arena;
	
	
	
	public PlayerLeaveArenaEvent(ArenaPlayer player,Arena arena) {
		this.player = player;
		this.arena = arena;
	}
	
	public ArenaPlayer getPlayer() {
		return player;
	}
	
	public Arena getArena() {
		return arena;
	}

	private boolean cancelled;

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
