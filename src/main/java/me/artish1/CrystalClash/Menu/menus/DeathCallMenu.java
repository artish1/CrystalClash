package me.artish1.CrystalClash.Menu.menus;

import me.artish1.CrystalClash.Arena.ArenaPlayer;
import me.artish1.CrystalClash.Arena.ArenaTeam;
import me.artish1.CrystalClash.Listeners.GameListener;
import me.artish1.CrystalClash.Menu.Menu;
import me.artish1.CrystalClash.Menu.MenuHolder;
import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Menu.items.DeathCallItem;
import me.artish1.CrystalClash.Menu.items.MenuItem;
import me.artish1.CrystalClash.Menu.items.NextPageItem;
import me.artish1.CrystalClash.Menu.items.PreviousPageItem;
import me.artish1.CrystalClash.killstreaks.DeathCall;
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

import java.util.HashMap;
import java.util.HashSet;

import static me.artish1.CrystalClash.Arena.Arena.blueTeam;
import static me.artish1.CrystalClash.Arena.Arena.redTeam;

public class DeathCallMenu implements Menu,Listener{
	public static HashSet<DeathCallMenu> deathCallMenus = new HashSet<DeathCallMenu>();
	private String name;
	private Menu parent;
	private int size;
	private JavaPlugin plugin;
	private HashSet<MenuItem> items1 = new HashSet<MenuItem>();
	private HashMap<MenuItem,Integer> indexes = new HashMap<>();
	private HashMap<Integer,Page> pages = new HashMap<>();
	private DeathCall deathCall;
	private Player caller;
	public int currentPage = 1;
	public boolean fulfilled = false;
	
	
	public DeathCallMenu(int size,JavaPlugin plugin,DeathCall killstreak,Player caller) {
		this.name = ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "Death Call";
		this.size = size;
		this.plugin = plugin;
		this.parent = null;
		this.deathCall = killstreak;
		this.caller = caller;
		
		
		
	    Bukkit.getPluginManager().registerEvents(this, plugin);	
	    			
	}
	
	
	public Player getCaller() {
		return caller;
	}
	
	public DeathCallMenu addItem(int index, MenuItem item){
		items1.add(item);
		indexes.put(item, index);
		return this;
	}
	
	public static void updateDeathCallMenus(){
		for(DeathCallMenu menu: deathCallMenus){
			menu.update(menu.getCaller());
		} 
	}
	
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean hasParent() {
		return (parent != null);
	}
	
	public DeathCall getDeathCall() {
		return deathCall;
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
			ArenaTeam type;
			if(redTeam.isOnTeam(player)){
				type = blueTeam;
			}else{
				type = redTeam;
			}
			apply(inventory,player, type);
			player.updateInventory();
		}
	}
	
	@Override
	public void open(Player player) {
		Inventory inv = Bukkit.createInventory(new MenuHolder(this, Bukkit.createInventory(player, size)), size, name);
		ArenaTeam type;
		if(redTeam.isOnTeam(player)){
			type = blueTeam;
		}else{
			type = redTeam;
		}
		
		apply(inv,player, type);
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
	
	public void setItemInPage(MenuItem item, int index, int page)
	{
		
	}
	
	
	
	public void apply(Inventory inventory,Player p,ArenaTeam type){
		inventory.clear();
		items1.clear();
		int indexCounter = 0;
		int players = 0; // 50;//0
		
		int pageCounter = 1;
		
		
		for(ArenaPlayer ap : type.getPlayerList()){
			if(GameListener.respawnQueue.contains(ap.getPlayer().getUniqueId()))
				continue;
			players++;
			
		}
		
		int pages = players/size + 1;
		for(int x = pages;x > 0; x--){
			this.pages.put(x, new Page());
		}
		
		/*
		for(int x = players; x>0 ; x--)
		{
			if(pageCounter <= pages){
				
				if(pageCounter > 1)
				{
					this.pages.get(pageCounter).addItem(getSize() - 9,new PreviousPageItem());  
					if(indexCounter == getSize() - 9)
					{
						indexCounter++;
					}else{
						this.pages.get(pageCounter).addItem(indexCounter,new ScoutItem());
						indexCounter++;
					}
				}else{
				
					this.pages.get(pageCounter).addItem(indexCounter,new ScoutItem());
					indexCounter++;
				}
			if(players != getSize()){
				if(indexCounter == getSize() -1 ){
				this.pages.get(pageCounter).addItem(indexCounter , new NextPageItem());
				pageCounter++;
				indexCounter=0;
			}
			}
			
			
			
			}
		}
		*/
		
		for(ArenaPlayer ap : type.getPlayerList()){
			if(GameListener.respawnQueue.contains(ap.getPlayer().getUniqueId()))
				continue;
			
			if(pageCounter <= pages){
				
				if(pageCounter > 1)
				{
					this.pages.get(pageCounter).addItem(getSize() - 9,new PreviousPageItem());  
					if(indexCounter == getSize() - 9)
					{
						indexCounter++;
					}else{
						this.pages.get(pageCounter).addItem(indexCounter,new DeathCallItem(ap.getPlayer()));
						indexCounter++;
					}
				}else{
				
					this.pages.get(pageCounter).addItem(indexCounter,new DeathCallItem(ap.getPlayer())); 
					indexCounter++;
				}
			if(players != getSize()){
				if(indexCounter == getSize() -1 ){
					this.pages.get(pageCounter).addItem(indexCounter , new NextPageItem());
					pageCounter++;
					indexCounter=0;
				}
			}
			
			
			
			}
		}
		
		
		
		for(MenuItem item : this.pages.get(currentPage).getItems()){
			Page page = this.pages.get(currentPage);
				inventory.setItem(page.getIndexes().get(item), item.getIcon());
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
			
			
			for(MenuItem item : this.pages.get(currentPage).getItems()){ 
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

								@Override
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
	
	@Override
	public void destroy(){
		HandlerList.unregisterAll(this);
		plugin = null;
		items1.clear();
	}
}
