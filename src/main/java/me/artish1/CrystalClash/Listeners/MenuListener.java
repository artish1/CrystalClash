package me.artish1.CrystalClash.Listeners;

import me.artish1.CrystalClash.Menu.MenuHolder;
import me.artish1.CrystalClash.Menu.menus.DeathCallMenu;
import me.artish1.CrystalClash.Menu.menus.Menus;
import me.artish1.CrystalClash.Menu.menus.UnlockCratesMenu;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.killstreaks.DeathCall;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class MenuListener implements Listener{
	
	@EventHandler
	public void onOpenMenu(PlayerInteractEvent e){
		if(!e.hasItem())
			return;
		
		Player player = e.getPlayer();
		
		
		if(e.getItem().equals(Methods.getShopMenuItem())){
			Menus.getShopMenu().open(player);
		}
		
		if(e.getItem().equals(Methods.getClassMenuItem())){
			Menus.getClassMenu().open(player);
		}
		
		if(e.getItem().equals(Methods.getOpenCratesMenuItem()))
		{
			new UnlockCratesMenu().open(player);
			e.setUseItemInHand(Result.DENY);
			e.setUseInteractedBlock(Result.DENY);
			e.setCancelled(true);
		}
		
		if(e.getItem().hasItemMeta()){
			if(e.getItem().getItemMeta().getDisplayName()!= null && e.getItem().getItemMeta().getDisplayName()!= "" )
			
				if(ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName()).equals(ChatColor.stripColor(Methods.getTeamPickMenuItem().getItemMeta().getDisplayName()))){
					Menus.getTeamPickMenu().open(player);
					e.setCancelled(true);
					e.setUseInteractedBlock(Result.DENY); 
					e.setUseItemInHand(Result.DENY); 
			}
			
		}
		
	}
	
	@EventHandler
	public void onInvClose(InventoryCloseEvent e){
		if(!(e.getInventory().getHolder() instanceof MenuHolder)){
			return;
		}
		
		if( ( (MenuHolder) e.getInventory().getHolder()).getMenu() instanceof DeathCallMenu ){
			DeathCallMenu menu = (DeathCallMenu) ((MenuHolder)e.getInventory().getHolder()).getMenu();
			if(DeathCallMenu.deathCallMenus.contains(menu)){
				DeathCallMenu.deathCallMenus.remove(menu);
			}
			
			if(e.getPlayer() instanceof Player){
			Player player = (Player) e.getPlayer();
			if(!menu.fulfilled)
			DeathCall.sendDeathMessage(player, "Call me when you need me...");
			}
		}
		
		
	}
	
}
