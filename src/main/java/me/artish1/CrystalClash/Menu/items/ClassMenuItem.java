package me.artish1.CrystalClash.Menu.items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.artish1.CrystalClash.CrystalClash;
import me.artish1.CrystalClash.Arena.Arena;
import me.artish1.CrystalClash.Arena.ArenaManager;
import me.artish1.CrystalClash.Arena.GameState;
import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Listeners.GameListener;
import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Menu.menus.ClassMenu;
import me.artish1.CrystalClash.Menu.menus.ShopClassMenu;
import me.artish1.CrystalClash.Menu.menus.UpgradeMenu;
import me.artish1.CrystalClash.Util.MySQLUtil;
import me.artish1.CrystalClash.events.PlayerSwitchClassEvent;

public class ClassMenuItem extends MenuItem{	
	
	protected ClassType type;
	
	public ClassMenuItem(String name, ItemStack icon,ClassType type) {
		super(name, icon);
		this.type = type;
	}
	
	public ClassType getClassType() {
		return type;
	}
	
	public void setClassType(ClassType cType)
	{
		this.type = cType;
	}

	@Override
	public void onItemClicked(ItemClickedEvent e) {
		Player player = e.getPlayer();
		
		if(e.getMenu() instanceof ClassMenu){
			
			if(e.getMenu().hasParent())
			{
				e.setGoBack(true);
			}
			
			if(ArenaManager.isInArena(player)){
				if(type.isDonator())
				{
					if(!player.hasPermission(CrystalClash.DONATOR_PERMISSION))
					{
						player.sendMessage(ChatColor.RED + "You must be a donator to choose this class!");
						return;
					}
				}
				Arena arena = ArenaManager.getArena(player);
				PlayerSwitchClassEvent event = new PlayerSwitchClassEvent(player, arena, arena.getArenaPlayer(player).getType(), type);
				Bukkit.getPluginManager().callEvent(event);
				if(event.isCancelled())
					return;
				
				arena.getArenaPlayer(player).setType(type);
				if(!GameListener.respawnQueue.contains(e.getPlayer().getUniqueId()) && ArenaManager.getArena(e.getPlayer()).getState() == GameState.INGAME){
					arena.setInventory(e.getPlayer());
					e.setClose(false);
					}else{
						if(!e.getMenu().hasParent())
							e.setClose(true);
					}
				
			}
			super.onItemClicked(e);
		}else{
			if(e.getMenu() instanceof ShopClassMenu){
				if(type.isFree())
					return;
				
				if(MySQLUtil.hasClass(player.getUniqueId(), getClassType())){
					player.sendMessage(ChatColor.RED + "You already have this item!");  
					return;
				}
				
				if(type.isDonator())
				{
					if(!player.hasPermission(CrystalClash.DONATOR_PERMISSION))
					{
						
						player.sendMessage(ChatColor.RED + "You must be a donator to buy this class!");
						return;
					}
				}
				
				int points = MySQLUtil.getPoints(player.getUniqueId());
				if(points >= getClassType().getPrice()){
					 MySQLUtil.subtractPoints(player.getUniqueId(), getClassType().getPrice()); 
					
					MySQLUtil.addClass(player.getUniqueId(), getClassType()); 
					player.sendMessage(ChatColor.GREEN + "You have successfully purchased: " + ChatColor.BOLD + getClassType().getName()); 
					e.setUpdate(true); 
				}else{
					player.sendMessage(ChatColor.RED + "You don't have enough points to purchase this!"); 
				}
			}
			
			
			if(e.getMenu() instanceof UpgradeMenu)
			{
				if(type.getUpgradeMenu() != null){
					if(!type.isFree()){
						if(MySQLUtil.hasClass(player.getUniqueId(), type))
						{ 
							type.getUpgradeMenu().open(e.getPlayer());
						}else{
							player.sendMessage(ChatColor.RED + "You must first buy/own this class before buying upgrades for it!");
						}
					}else{
						if(type.isDonator())
						{
							if(!e.getPlayer().hasPermission(CrystalClash.DONATOR_PERMISSION))
							{
								player.sendMessage(ChatColor.RED + "You must be a donator to upgrade this class!");
								return;
							}
						}
						
						
						type.getUpgradeMenu().open(e.getPlayer());
					}
				}else{
					e.getPlayer().sendMessage(ChatColor.RED + "This upgrade menu is still undergoing maintenance.");
				}
				return;
			}
		}
	}
	
	

}
