package me.artish1.CrystalClash.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.artish1.CrystalClash.Arena.Arena;
import me.artish1.CrystalClash.Classes.ClassType;

public class PlayerSwitchClassEvent extends Event implements Cancellable{
	private static final HandlerList handlers = new HandlerList();
	
	private Player p;
	private Arena arena;
	private ClassType before;
	private ClassType to;
	private boolean cancel = false;
	public PlayerSwitchClassEvent(Player p, Arena arena, ClassType before, ClassType to) {
		this.p = p;
		this.arena = arena;
		this.before = before;
		this.to = to;
	}
	
	public Arena getArena() {
		return arena;
	}
	
	public ClassType getBefore() {
		return before;
	}
	
	public ClassType getTo() {
		return to;
	}
	
	public Player getPlayer() {
		return p;
	}
	
	
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList()
	{
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
	

}
