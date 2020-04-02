package me.artish1.CrystalClash.Menu;

import org.bukkit.entity.Player;

public interface Menu {
	
	String getName();
	
	boolean hasParent();
	
	void setParent(Menu parent);
	
	Menu getParent();
	
	int getSize();
	
	void update(Player player);
	
	void open(Player player);
	
	void destroy();
	
}
