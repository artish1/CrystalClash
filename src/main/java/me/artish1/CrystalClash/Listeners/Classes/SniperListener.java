package me.artish1.CrystalClash.Listeners.Classes;

import me.artish1.CrystalClash.Arena.ArenaManager;
import me.artish1.CrystalClash.Classes.Abilities.SniperShoot;
import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.CrystalClash;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.other.ClassInventories;
import me.artish1.CrystalClash.other.Claymore;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.UUID;

public class SniperListener implements Listener{
	
	public static HashSet<UUID> ids = new HashSet<>();
	
	public static HashSet<UUID> claymoreIds = new HashSet<>();
	
	CrystalClash plugin;
	
	public SniperListener(CrystalClash plugin) {
		this.plugin = plugin;
	}
	
	//private ItemStack reticle = new ItemStack(Material.PUMPKIN);
	
	
	

	
	@EventHandler
	public void onClaymorePlace(BlockPlaceEvent e){
		Player player = e.getPlayer();
		
		if(!ArenaManager.isInArena(player))
			return;
		
		
		if(plugin.getArena().getArenaPlayer(player).getType() != ClassType.SNIPER)
			return;
		
		if(e.getBlock().getType() == Material.REDSTONE_LAMP){
			final Location loc = e.getBlock().getLocation().add(0.5, 1, 0.5);
			final Item item = e.getBlock().getWorld().dropItem(loc,
					ClassInventories.getClaymoreItem());;
			item.setVelocity(new Vector(0,0,0));
			claymoreIds.add(item.getUniqueId());
			Claymore claymore = new Claymore(plugin,item,player);
			claymore.startDetection();
			player.sendMessage(ChatColor.GRAY + "You have placed a claymore.");
			e.getBlock().setType(Material.AIR);
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

				@Override
				public void run() {
					item.teleport(loc);
				}
				
			}, 1);
			
			return;
		}
		
		
	}
	
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent e){
		Player player = e.getPlayer();
		if(!ArenaManager.isInArena(player))
			return;
		
		if(claymoreIds.contains(e.getItem().getUniqueId())){
			
			e.setCancelled(true);
		}
		
		
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		if(!e.hasItem())
			return;
		
		Player p = e.getPlayer();
		if(!ArenaManager.isInArena(p))
			return;
			
		if(plugin.getArena().getArenaPlayer(p).getType() != ClassType.SNIPER)
			return;
		
		
		if(e.getItem().getType() == Material.SNOWBALL){
			e.setCancelled(true);
			p.sendMessage(ChatColor.RED + "You cannot throw this type of Ammo!");
			return;
		}
		
		
			
	}
	
	//private HashMap<UUID, ItemStack> heads = new HashMap<UUID,ItemStack>();
	
	@EventHandler
	public void onSneak(PlayerToggleSneakEvent e){
		Player player = e.getPlayer();
		
		if(!ArenaManager.isInArena(player))
			return;
		
		
		if(ArenaManager.getArena(player).getArenaPlayer(player).getType() != ClassType.SNIPER)
			return;
		
		Methods.debug(player, "We are sniper, and we toggled sneaking");
		
		if(!player.getInventory().getItemInHand().equals(ClassInventories.getSniperItem()))
			return;
		
		
		
		
		if(e.isSneaking()){
			plugin.getArena().getArenaPlayer(player).zoomIn();
			Methods.debug(player, "Zooming in.");
			/*
			if(player.getInventory().getHelmet() != null){
				heads.put(player.getUniqueId(), player.getInventory().getHelmet());
			}
			
			player.getInventory().setHelmet(reticle);
			*/
			
		}else{ 
			plugin.getArena().getArenaPlayer(player).zoomOut();
			Methods.debug(player, "Zooming out.");
			/*
			if(heads.containsKey(player.getUniqueId()))
			{
				player.getInventory().setHelmet(heads.get(player.getUniqueId()));
			}else{
				player.getInventory().setHelmet(null);
			}
			*/
		}
	}
	@EventHandler
	public void changeItemSlot(PlayerItemHeldEvent e){
		Player player = e.getPlayer();
		
		if(Methods.getArena().getArenaPlayer(player).getType() != ClassType.SNIPER)
			return;
		
		PlayerInventory inv = player.getInventory();
		if(inv == null || inv.getItem(e.getPreviousSlot()) == null)
			return;
		
		
		
		if(inv.getItem(e.getPreviousSlot()).equals(ClassInventories.getSniperItem())){
			if(Methods.getArena().getArenaPlayer(player).isZoomed())
			{
				Methods.getArena().getArenaPlayer(player).zoomOut();
				
			}
			
			
		}
		
		
	}
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e){
	
		
		
		if(ids.contains(e.getDamager().getUniqueId())){
			e.setDamage(20.0);
			Projectile proj = (Projectile)e.getDamager();
			if(proj.getShooter() instanceof Player)
			{ 
				Player player = (Player) proj.getShooter();
				player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_HURT, 1.0f, 1.0f);
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e){
		Player p = e.getPlayer();
		
		if(!e.hasItem())
			return;
		
		if(!ArenaManager.isInArena(p))
			return;
		
		
		
		if(plugin.getArena().getArenaPlayer(p).getType() != ClassType.SNIPER)
			return;
		if(ArcherListener.debuggers.contains(p.getUniqueId())){
			p.sendMessage("You are sniper.");
		}
		
		if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
			Methods.debug(p, "Right/left clickking");
			
			if(e.getItem().equals(ClassInventories.getSniperItem())){
				Methods.debug(p, "It's equal to sniper item");
				new SniperShoot(p).cast();
				
			}
		}else{
			Methods.debug(p, "not right/leftclicking");
		}
		
		
	}
	
}
