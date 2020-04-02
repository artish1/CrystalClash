package me.artish1.CrystalClash.Menu.menus;

import java.util.HashMap;
import java.util.HashSet;

import me.artish1.CrystalClash.Menu.Menu;
import me.artish1.CrystalClash.Menu.MenuHolder;
import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Menu.items.ArcherItem;
import me.artish1.CrystalClash.Menu.items.AssassinItem;
import me.artish1.CrystalClash.Menu.items.ClassMenuItem;
import me.artish1.CrystalClash.Menu.items.CloseItem;
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
import me.artish1.CrystalClash.Util.MySQLUtil;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ClassMenu implements Menu,Listener{
	
	private String name;
	private Menu parent;
	private int size;
	private MenuItem[] items;
	private JavaPlugin plugin;
	private HashSet<MenuItem> items1 = new HashSet<MenuItem>();
	private HashMap<MenuItem,Integer> indexes = new HashMap<MenuItem,Integer>();
	
	public ClassMenu(String name, int size,JavaPlugin plugin,Menu parent ) {
		this.name = name;
		this.size = size;
		this.plugin = plugin;
		this.parent = parent;
		items = new MenuItem[size];
		addItem(size -1, new CloseItem());
		addItem(0, new ScoutItem());
		addItem(1, new AssassinItem());
		addItem(2, new EarthItem());
		addItem(3, new ArcherItem());
		addItem(4, new ExplosivesItem());
		addItem(5, new TankItem());
		addItem(6, new EnderItem());
		addItem(7, new SniperItem());
		addItem(8, new MageItem());
		addItem(9, new EngineerItem());
		addItem(10, new SpiderItem());
		addItem(11, new GuardianItem(this));
		addItem(12, new NecromancerItem(this));
					
		
		
		
	    Bukkit.getPluginManager().registerEvents(this, plugin);	
	    			
	}
	
	public ClassMenu addItem(int index, MenuItem item){
		items[index] = item;
		items1.add(item);
		indexes.put(item, index);
		return this;
	}

	public String getName() {
		return name;
	}

	public boolean hasParent() {
		return (parent != null);
	}

	public void setParent(Menu parent) {
		this.parent = parent;
	}

	public Menu getParent() {
		return parent;
	}

	public int getSize() {
		return size;
	}

	public void update(Player player) {
		if(player.getInventory() != null){
			Inventory inventory = player.getOpenInventory().getTopInventory();
			apply(inventory,player);
			player.updateInventory();
		}
	}

	public void open(Player player) {
		Inventory inv = Bukkit.createInventory(new MenuHolder(this, Bukkit.createInventory(player, size)), size, name);
		apply(inv,player);
		player.openInventory(inv);
	}
	/*
	public void apply(Inventory inv,Player p){
		for(int i = 0; i < items.length; i++){
			if(items[i] != null){
				
				if(items[i] instanceof ClassMenuItem){
					ClassMenuItem classItem = (ClassMenuItem) items[i];
					if(classItem.getClassType().isFree()){
						inv.setItem(i, items[i].getIcon());
						continue;
					}else{
						if(MySQLUtil.hasClass(p.getUniqueId(), classItem.getClassType())){
							inv.setItem(i, items[i].getIcon());
							continue;
						}
					}
				}else{
					inv.setItem(i, items[i].getIcon());
				}
			}
		}
	}
	*/
	
	public void apply(Inventory inventory,Player p){
		
		for(MenuItem item : items1){
			if(item instanceof ClassMenuItem){
				ClassMenuItem classItem = (ClassMenuItem) item ;
				if(classItem.getClassType().isFree()){
					inventory.setItem(indexes.get(item), item.getIcon());
				}else{
					if(MySQLUtil.hasClass(p.getUniqueId(), classItem.getClassType()))
						inventory.setItem(indexes.get(item), item.getIcon());

				}
			}else
				inventory.setItem(indexes.get(item), item.getIcon());

		}
		
		p.updateInventory();
		
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){
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
			
			for(MenuItem item : items1){
				
				if(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(item.getIcon().getItemMeta().getDisplayName()))){
					
					
					ItemClickedEvent clickedEvent = new ItemClickedEvent(player, item,this);
					item.onItemClicked(clickedEvent);
					if(clickedEvent.isUpdate()){
						update(player);
					}else{
						player.updateInventory();
						if(clickedEvent.isClose() || clickedEvent.isGoBack()){
							player.closeInventory();
						}
						
						if(clickedEvent.isGoBack() && hasParent()){
							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

								public void run() {
									parent.open(player);
								}
								
							}, 2);
						}
					}
					
					
					break;
				}
			}
			
		}
	}

	public void destroy(){
		HandlerList.unregisterAll(this);
		plugin = null;
		items = null;
	}
	
}	
