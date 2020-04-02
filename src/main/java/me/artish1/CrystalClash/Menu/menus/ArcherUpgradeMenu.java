package me.artish1.CrystalClash.Menu.menus;

import java.util.HashMap;
import java.util.HashSet;

import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Menu.Menu;
import me.artish1.CrystalClash.Menu.MenuHolder;
import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Menu.items.ArcherFireBarrageUpgradeItem;
import me.artish1.CrystalClash.Menu.items.BackItem;
import me.artish1.CrystalClash.Menu.items.HelmetItem;
import me.artish1.CrystalClash.Menu.items.MenuItem;
import me.artish1.CrystalClash.Menu.items.UpgradeAmmoItem;
import me.artish1.CrystalClash.Menu.items.UpgradeBootsItem;
import me.artish1.CrystalClash.Menu.items.UpgradeChestplateItem;
import me.artish1.CrystalClash.Menu.items.UpgradeLeggingsItem;
import me.artish1.CrystalClash.Menu.items.UpgradeSwordItem;
import me.artish1.CrystalClash.Util.MySQLUtil;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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

public class ArcherUpgradeMenu implements Menu,Listener{
	private String name;
	private Menu parent;
	private int size;
	private MenuItem[] items;
	private ClassType type = ClassType.ARCHER;
	private HashSet<MenuItem> items1 = new HashSet<MenuItem>();
	private HashMap<MenuItem,Integer> indexes = new HashMap<>();
	
	public ArcherUpgradeMenu(String name,int size, JavaPlugin plugin,Menu parent) {
		this.name = name;
		this.parent = parent;
		this.size = size;
		items = new MenuItem[size];
	    Bukkit.getPluginManager().registerEvents(this, plugin);
	    
	    
	  
	    
	    
	}
	 
	
	public MenuItem[] getItems() {
		return items;
	}
	
	public ArcherUpgradeMenu addItem(int index, MenuItem item){
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
		items1.clear();
		Material boots = MySQLUtil.getBoots(p.getUniqueId(), type);
		Material leggings = MySQLUtil.getLeggings(p.getUniqueId(), type);
		Material sword = MySQLUtil.getItem(p.getUniqueId(), type, "sword");
		Material helmet = MySQLUtil.getHelmet(p.getUniqueId(), type);
		Material chestplate = MySQLUtil.getChestplate(p.getUniqueId(), type);
		Material ammo = Material.ARROW;
		
		addItem(13, new HelmetItem(helmet,type,p.getUniqueId()));
		addItem(13 + 9, new UpgradeChestplateItem(chestplate,type,p.getUniqueId()));
		addItem(13 + 18, new UpgradeLeggingsItem(leggings, type,p.getUniqueId()));
		
	    addItem(22 + 18, new UpgradeBootsItem(boots,type,p.getUniqueId()));
	    addItem(3 + 18, new UpgradeSwordItem(sword,type,p.getUniqueId()));  
	    addItem(1+18, new UpgradeAmmoItem(ammo,type,p.getUniqueId()));
	    
	    addItem(15, new ArcherFireBarrageUpgradeItem(p.getUniqueId())); 
	    
	    addItem(size-1, new BackItem());

		
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
		items = null;
	}
}
