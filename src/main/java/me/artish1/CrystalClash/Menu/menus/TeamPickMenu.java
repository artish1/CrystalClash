package me.artish1.CrystalClash.Menu.menus;

import java.util.HashMap;
import java.util.HashSet;

import me.artish1.CrystalClash.Menu.Menu;
import me.artish1.CrystalClash.Menu.MenuHolder;
import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Menu.items.BluePickItem;
import me.artish1.CrystalClash.Menu.items.ChangeClassItem;
import me.artish1.CrystalClash.Menu.items.ColoredPaneItem;
import me.artish1.CrystalClash.Menu.items.MenuItem;
import me.artish1.CrystalClash.Menu.items.RedPickItem;
import me.artish1.CrystalClash.Util.Methods;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
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

public class TeamPickMenu implements Menu, Listener{

	private String name;
	private Menu parent;
	private int size;
	private MenuItem[] items;
	private HashSet<MenuItem> items1 = new HashSet<MenuItem>();
	private HashMap<MenuItem,Integer> indexes = new HashMap<>();
	
	public TeamPickMenu(String name,int size, JavaPlugin plugin) {
		this.name = name;
		this.size = size;
		items = new MenuItem[size];
	    Bukkit.getPluginManager().registerEvents(this, plugin);
	    
	    DyeColor color = DyeColor.BLACK;
	    
	    fillBorders(5,color);
	    setColoredPane(13, color);
	    setColoredPane(22,color);
	    setColoredPane(31, color);
	    
	    setColoredPane(10, DyeColor.RED);
	    setColoredPane(11, DyeColor.RED);
	    setColoredPane(12, DyeColor.RED);
	    setColoredPane(19, DyeColor.RED);
	    setColoredPane(21, DyeColor.RED);
	    setColoredPane(28, DyeColor.RED);
	    setColoredPane(29, DyeColor.RED);
	    setColoredPane(30, DyeColor.RED);

	    setColoredPane(14, DyeColor.BLUE);
	    setColoredPane(15, DyeColor.BLUE);
	    setColoredPane(16, DyeColor.BLUE);
	    setColoredPane(23, DyeColor.BLUE);
	    setColoredPane(25, DyeColor.BLUE);
	    setColoredPane(32, DyeColor.BLUE);
	    setColoredPane(33, DyeColor.BLUE);
	    setColoredPane(34, DyeColor.BLUE);
	    
	    addItem(20, new RedPickItem()); 
	    addItem(24, new BluePickItem());	
	    //addItem(22, SpectatorPickItem());
	    addItem(40, new ChangeClassItem(this));
	    
	}
	 
	
	
	private void fillBorders(int rows,DyeColor color)
	{
		rows--;
		for(int y = 0; y<=rows;y++)
	    {
	    	for(int x =1; x <= 9 ;x++)
		    {
	    		if(y==0 || y == rows)
	    		{
	    			if((x + (y*9)) - 1 == 40){
    					continue;
    				}
	    			addItem((x + (y * 9)) - 1, new ColoredPaneItem(color));
	    		}else
	    		{	
	    			if(x== 1 || x == 9)
	    			{
		    			addItem((x + (y*9)) - 1, new ColoredPaneItem(color));	
	    			}
	    		}
		   	}
	    }
	}
	
	
	private void setColoredPane(int slot, DyeColor color){
		
		addItem(slot, new ColoredPaneItem(color));
	}
	
	public MenuItem[] getItems() {
		return items;
	}
	
	public TeamPickMenu addItem(int index, MenuItem item){
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
			if(!(item instanceof ChangeClassItem)){
				inventory.setItem(indexes.get(item), item.getIcon());
			}else{
				inventory.setItem(indexes.get(item), ((ChangeClassItem)item).getIcon(Methods.getArena().getArenaPlayer(p)));
			}
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
