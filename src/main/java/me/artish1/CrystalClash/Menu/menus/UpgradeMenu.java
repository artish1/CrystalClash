package me.artish1.CrystalClash.Menu.menus;

import java.util.HashMap;
import java.util.HashSet;

import me.artish1.CrystalClash.Menu.Menu;
import me.artish1.CrystalClash.Menu.MenuHolder;
import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Menu.items.ArcherItem;
import me.artish1.CrystalClash.Menu.items.AssassinItem;
import me.artish1.CrystalClash.Menu.items.BackItem;
import me.artish1.CrystalClash.Menu.items.EarthItem;
import me.artish1.CrystalClash.Menu.items.EnderItem;
import me.artish1.CrystalClash.Menu.items.EngineerItem;
import me.artish1.CrystalClash.Menu.items.ExplosivesItem;
import me.artish1.CrystalClash.Menu.items.GuardianItem;
import me.artish1.CrystalClash.Menu.items.MageItem;
import me.artish1.CrystalClash.Menu.items.MenuItem;
import me.artish1.CrystalClash.Menu.items.NecromancerItem;
import me.artish1.CrystalClash.Menu.items.ScoutItem;
import me.artish1.CrystalClash.Menu.items.SniperItem;
import me.artish1.CrystalClash.Menu.items.SpiderItem;
import me.artish1.CrystalClash.Menu.items.TankItem;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class UpgradeMenu implements Menu,Listener{
	private String name;
	private Menu parent;
	private int size;
	private MenuItem[] items;
	private HashSet<MenuItem> items1 = new HashSet<MenuItem>();
	private HashMap<MenuItem,Integer> indexes = new HashMap<>();
	//private JavaPlugin plugin;
	
	public UpgradeMenu(String name,int size, JavaPlugin plugin,Menu parent) {
		this.name = name;
		this.size = size;
		this.parent = parent;
		//this.plugin = plugin;
		items = new MenuItem[size];
	    Bukkit.getPluginManager().registerEvents(this, plugin);
	    
	    addItem(0, new ScoutItem()); 
	    addItem(1, new ArcherItem());
	    addItem(2, new SniperItem());
	    addItem(3, new AssassinItem());
	    addItem(4, new EngineerItem());
	    addItem(5, new ExplosivesItem());
	    addItem(6, new SpiderItem());
	    addItem(7, new TankItem());
	    addItem(8, new MageItem()); 
	    addItem(9, new EnderItem());
	    addItem(10, new EarthItem());
	    addItem(11, new GuardianItem(this));
	    addItem(12, new NecromancerItem(this));
	    addItem(size - 1, new BackItem());
	    
	}
	 
	
	public MenuItem[] getItems() {
		return items;
	}
	
	public UpgradeMenu addItem(int index, MenuItem item){
		items[index] = item;
		items1.add(item);
		indexes.put(item, index);
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
			Inventory inv = player.getOpenInventory().getTopInventory();
			if(inv.getHolder() instanceof MenuHolder && ((MenuHolder) inv.getHolder()).getMenu().equals(this));
				apply(inv,player);
				player.updateInventory();
			}
	}
	@Override
	public void open(Player player) {
        Inventory inventory = Bukkit.createInventory(new MenuHolder(this, Bukkit.createInventory(player, size)), size, name);
        apply(inventory,player); 
        player.openInventory(inventory);
	}
	
	public void apply(Inventory inventory,Player p){
		
		for(MenuItem item : items1){
			inventory.setItem(indexes.get(item), item.getIcon());
		}
		
		p.updateInventory();
		
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onInvClick(InventoryClickEvent e){
		if(!(e.getWhoClicked() instanceof Player) || !(e.getInventory().getHolder() instanceof MenuHolder) || !((MenuHolder) e.getInventory().getHolder()).getMenu().equals(this))
			return;
		e.setCancelled(true);
		if(e.getClick() == ClickType.LEFT){
		
			final Player player = (Player) e.getWhoClicked();

			
			ItemStack clicked = e.getCurrentItem();
			if(clicked == null)
				return;
			
			if(clicked.getItemMeta() == null)
				return;
			
			if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase("") || clicked.getItemMeta().getDisplayName() == null)
				return;
			
			for(MenuItem item : items1){
				
				
				if(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(item.getIcon().getItemMeta().getDisplayName()))){
					
					
					ItemClickedEvent clickedEvent = new ItemClickedEvent(player, item,this);
					item.onItemClicked(clickedEvent);
					if(clickedEvent.isUpdate()){
						update(player);
					}else{
						player.updateInventory();
						if(clickedEvent.isClose()){
							player.closeInventory();
						}
						
						if(clickedEvent.isGoBack() && hasParent()){
							/*Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

								@Override
								public void run() {
									parent.open(player);
								}
								
							}, 2);*/
							parent.open(player);
						}
						
						
						
					}
					
					
					break;
				}
			}
			
			
		}
	}
	
	@Override
	public void destroy(){
		HandlerList.unregisterAll(this);
		//plugin = null;
		items = null;
	}
}
