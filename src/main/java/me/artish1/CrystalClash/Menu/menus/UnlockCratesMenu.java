package me.artish1.CrystalClash.Menu.menus;

import java.util.HashMap;
import java.util.HashSet;

import me.artish1.CrystalClash.Menu.Menu;
import me.artish1.CrystalClash.Menu.MenuHolder;
import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Menu.items.ColoredPaneItem;
import me.artish1.CrystalClash.Menu.items.CrateItem;
import me.artish1.CrystalClash.Menu.items.MenuItem;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.crates.GoldCrate;
import me.artish1.CrystalClash.crates.StoneCrate;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
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

public class UnlockCratesMenu implements Menu,Listener{
	private String name;
	private Menu parent;
	private int size;
	private MenuItem[] items;
	private HashSet<MenuItem> items1 = new HashSet<MenuItem>();
	private HashMap<MenuItem,Integer> indexes = new HashMap<>();
	private DyeColor paneColor = DyeColor.BLUE;
	public UnlockCratesMenu() {
		this.name = ChatColor.DARK_BLUE + ChatColor.BOLD.toString() + "Your Crates";
		this.size = 4*9;
		items = new MenuItem[size];
	    Bukkit.getPluginManager().registerEvents(this, Methods.getPlugin());
	    
	    // 0, 1, 2, 3, 4, 5, 6, 7, 8
	    setBorderItem(0);
	    setBorderItem(1);
	    setBorderItem(2);
	    setBorderItem(3);
	    setBorderItem(4);
	    setBorderItem(5);
	    setBorderItem(6);
	    setBorderItem(7);
	    setBorderItem(8);

	    //9 ,10,11,12, 13 ,14,15,16,  17
	    
	    setBorderItem(9);
	    addItem(10, new CrateItem(new StoneCrate(),new ItemStack(Material.CHEST)));
	    addItem(11, new CrateItem(new GoldCrate(), new ItemStack(Material.CHEST)));
	    setBorderItem(17);
	    //13 THE MIDDLE
	    
	    //18-19,20,21,22,23,24,25,26,
	    
	    setBorderItem(18);
	    setBorderItem(26);
	    
	    
	    //27-28-29-30-31-32-33-34-35
	    setBorderItem(27);
	    setBorderItem(28);
	    setBorderItem(29);
	    setBorderItem(30);
	    setBorderItem(31);
	    setBorderItem(32);
	    setBorderItem(33);
	    setBorderItem(34);
	    setBorderItem(35);


	}
	 

	
	
	
	private void setBorderItem(int index)
	{
		addItem(index, new ColoredPaneItem(paneColor));
	}
	public MenuItem[] getItems() {
		return items;
	}
	
	public UnlockCratesMenu addItem(int index, MenuItem item){
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
		inventory.clear();
		
		
		
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
			
			if(clicked.getItemMeta().getDisplayName() == null)
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
