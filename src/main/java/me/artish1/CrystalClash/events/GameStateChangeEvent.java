package me.artish1.CrystalClash.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.artish1.CrystalClash.Arena.GameState;

public class GameStateChangeEvent extends Event{
	private static final HandlerList handlers = new HandlerList();

	private GameState changedTo;
	private GameState changedFrom;
	public GameStateChangeEvent(GameState changedTo, GameState changedFrom) {
		this.changedFrom = changedFrom;
		this.changedTo = changedTo;
	}
	
	public GameState getChangedFrom() {
		return changedFrom;
	}
	
	public GameState getChangedTo() {
		return changedTo;
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
