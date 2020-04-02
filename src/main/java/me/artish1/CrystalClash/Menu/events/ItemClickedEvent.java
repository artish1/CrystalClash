package me.artish1.CrystalClash.Menu.events;

import me.artish1.CrystalClash.Menu.Menu;
import me.artish1.CrystalClash.Menu.items.MenuItem;

import org.bukkit.entity.Player;

public class ItemClickedEvent {
	private Player player;
	private boolean goBack;
	private boolean close;
	private boolean update;
	private MenuItem item;
	private Menu menu;
	
	public ItemClickedEvent(Player player, MenuItem item, Menu menu) {
		this.player = player;
		this.item = item;
		this.menu = menu;
	}
	
	public Menu getMenu() {
		return menu;
	}
	
	
	public Player getPlayer() {
		return player;
	}
	
	public MenuItem getItem() {
		return item;
	}
	
	public void setClose(boolean close) {
		this.close = close;
		if(close){
			update = false;
			goBack = false;
		}
		
	}
	public void setGoBack(boolean goBack) {
		this.goBack = goBack;
		if(goBack){
			close = false;
			update = false;
		}
	}
	public void setUpdate(boolean update) {
		this.update = update;
		if(update){
			goBack = false;
			close = false;
		}
		
	}
	
	public boolean isUpdate() {
		return update;
	}
	public boolean isClose() {
		return close;
	}
	
	public boolean isGoBack() {
		return goBack;
	}
	
	
	
	
	
}
