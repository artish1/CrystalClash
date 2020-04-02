package me.artish1.CrystalClash.Menu.menus;

import me.artish1.CrystalClash.Menu.Menu;
import me.artish1.CrystalClash.Menu.MenuHolder;
import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Menu.items.CloseItem;
import me.artish1.CrystalClash.Menu.items.FlashBombItem;
import me.artish1.CrystalClash.Menu.items.GrenadeItem;
import me.artish1.CrystalClash.Menu.items.LifeDrainItem;
import me.artish1.CrystalClash.Menu.items.MenuItem;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class IngameMenu implements Menu,Listener {

	private String name;
	private Menu parent;
	private int size;
	private MenuItem[] items;
	private JavaPlugin plugin;
	
	public IngameMenu(String name, int size,JavaPlugin plugin,Menu parent ) {
		this.name = name;
		this.size = size;
		this.plugin = plugin;
		this.parent = parent;
		items = new MenuItem[size];
		addItem(size -1, new CloseItem());
		addItem(0, new LifeDrainItem()); 
		addItem(1, new GrenadeItem());
		addItem(2, new FlashBombItem());
		 
	    Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	public IngameMenu addItem(int index, MenuItem item){
		items[index] = item;
		return this;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean hasParent() {
		return (parent != null);
	}

	@Override
	public void setParent(Menu parent) {
		this.parent = parent;
	}

	@Override
	public Menu getParent() {
		return parent;
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public void update(Player player) {
		if(player.getInventory() != null){
			Inventory inventory = player.getOpenInventory().getTopInventory();
			apply(inventory);
			player.updateInventory();
		}
	}

	@Override
	public void open(Player player) {
		Inventory inv = Bukkit.createInventory(new MenuHolder(this, Bukkit.createInventory(player, size)), size, name);
		apply(inv);
		player.openInventory(inv);
	}
	
	public void apply(Inventory inv){
		for(int i = 0; i < items.length; i++){
			if(items[i] != null){
				inv.setItem(i, items[i].getIcon());
			}
		}
	}
	
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){
		if(!(e.getWhoClicked() instanceof Player) || !(e.getInventory().getHolder() instanceof MenuHolder) || !((MenuHolder) e.getInventory().getHolder()).getMenu().equals(this))
			return;
		
		e.setCancelled(true);
		
		if(e.getClick() == ClickType.LEFT){
			int slot = e.getRawSlot();
			
			if(!(slot >= 0) || !(slot < items.length) || items[slot] == null){
				return;
			}
			
			
				final Player player = (Player) e.getWhoClicked();
				ItemClickedEvent clickedEvent = new ItemClickedEvent(player,items[slot],this);
				items[slot].onItemClicked(clickedEvent);
				if(clickedEvent.isUpdate()){
					update(player);
				}else{
					if(clickedEvent.isClose() || clickedEvent.isGoBack()){
						player.closeInventory();
					}
					
					if(clickedEvent.isGoBack() && hasParent()){
						
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

							@Override
							public void run() {
								parent.open(player);
							}
							
						}, 2);
						
					}
				}
			
		}
	}
	
	@Override
	public void destroy(){
		HandlerList.unregisterAll(this);
		plugin = null;
		items = null;
	}

}
