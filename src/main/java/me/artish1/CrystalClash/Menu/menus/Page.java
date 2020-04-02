package me.artish1.CrystalClash.Menu.menus;

import java.util.HashMap;
import java.util.HashSet;

import me.artish1.CrystalClash.Menu.items.MenuItem;

public class Page {
	
	private HashMap<MenuItem, Integer> indexes = new HashMap<>();
	private HashSet<MenuItem> items = new HashSet<>();
	
	
	public void addItem(int index, MenuItem item){
		if(!indexes.containsValue(index)){
			indexes.put(item, index);
			items.add(item);
		}
	}
	
	public HashMap<MenuItem, Integer> getIndexes() {
		return indexes;
	}
	
	public HashSet<MenuItem> getItems() {
		return items;
	}
	
}
