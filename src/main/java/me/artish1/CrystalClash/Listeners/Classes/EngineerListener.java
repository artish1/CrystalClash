package me.artish1.CrystalClash.Listeners.Classes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.artish1.CrystalClash.CrystalClash;
import me.artish1.CrystalClash.Arena.GameState;
import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Classes.Abilities.EngineerFireRocketLauncher;
import me.artish1.CrystalClash.Cooldown.Cooldown;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.objects.BuildingManager;
import me.artish1.CrystalClash.objects.Turret;
import me.artish1.CrystalClash.other.ClassInventories;

public class EngineerListener implements Listener{
	
	CrystalClash plugin = Methods.getPlugin();
	public static HashSet<UUID> entities = new HashSet<UUID>(); 
	public static HashSet<UUID> rockets = new HashSet<UUID>();
	private HashMap<UUID,Turret> playerTurrets = new HashMap<UUID,Turret>();
	
	
	@EventHandler
	public void onTurretPlace(BlockPlaceEvent e){
		Player player = e.getPlayer();
		if(plugin.getArena().getArenaPlayer(player).getType() != ClassType.ENGINEER)
			return;
		
		
		Methods.debug(player, "we are enginneer");
		
		if(e.getBlockPlaced().getType() == Material.DISPENSER){
			Methods.debug(player, "It is a dispenser we placed");
			
			if(playerTurrets.containsKey(player.getUniqueId()))
			{
				if(Cooldown.isCooling(player.getUniqueId(), "turretplace"))
				{
					player.sendMessage(ChatColor.RED + "You must wait " + Cooldown.getRemaining(player.getUniqueId(), "turretplace") + " seconds before placing down "
							+ "another turret!");
					e.setCancelled(true);
					return;
				}
				
				Turret turr = playerTurrets.get(player.getUniqueId());
				if(turr.isAlive())
				{
					player.sendMessage(ChatColor.RED + "You may only have 1 turret down at a time.");
					e.setCancelled(true);
					return;
				}else{
					Turret turret = new Turret(player, e.getBlockPlaced().getLocation());
					turret.start();
					playerTurrets.put(player.getUniqueId(), turret);
					player.sendMessage(ChatColor.GREEN + "Turret is up and running!"); 
					player.getInventory().addItem(ClassInventories.getTurretItem());
					player.updateInventory();
				}
			}else{
			
				Turret turret = new Turret(player, e.getBlockPlaced().getLocation());
				turret.start();
				playerTurrets.put(player.getUniqueId(), turret);
				player.sendMessage(ChatColor.GREEN + "Turret is up and running!"); 
				player.getInventory().addItem(ClassInventories.getTurretItem());
				player.updateInventory();
			}
			 
		}
	}
	
	
	
	@EventHandler
	public void onRocketHit(ProjectileHitEvent e)
	{
		if(!rockets.contains(e.getEntity().getUniqueId()))
			return;
		Projectile proj = (Projectile)e.getEntity();
		
		if(proj.getShooter() instanceof Player){
			Player player = (Player) proj.getShooter();
			Methods.createTeamFireworkExplosion(player, e.getEntity().getLocation(), 7, 9.5);
			
		}
	}
	
	
	@EventHandler
	public void onArrowDamage(EntityDamageByEntityEvent e){
		if(entities.contains(e.getDamager().getUniqueId())){
			e.setDamage(4.0); 
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e){
		Player player = e.getEntity();
		
		if(plugin.getArena().getArenaPlayer(player).getType() != ClassType.ENGINEER)
			return;
		
		Methods.debug(player, "We are Engineer! We died!");
		
		if(BuildingManager.ownsTurret(player)){
			Methods.debug(player, "You own a turret!");
			BuildingManager.getTurret(player).remove();
			Methods.debug(player, "removed turret");  
		}
		
		
	}
	
	@EventHandler
	public void onRocketLauncherShoot(PlayerInteractEvent e)
	{
		if(!e.hasItem())
			return;
		if(Methods.getArena().getState() != GameState.INGAME)
			return;
		
		if(Methods.getArena().getArenaPlayer(e.getPlayer()) == null)
			return;
		
		if(Methods.getArena().getArenaPlayer(e.getPlayer()).getType() == ClassType.ENGINEER)
		{
			
			if(e.getItem().equals(ClassInventories.getEngineerRocketLauncher()))
			{
				new EngineerFireRocketLauncher(e.getPlayer()).cast();
			}
			
		}
		
	}
	
	@EventHandler
	public void onTurretBreak(BlockBreakEvent e){
		Player player = e.getPlayer();
		
		if(plugin.getArena().getState() != GameState.INGAME)
			return;
		Methods.debug(player, "We are ingame, checking before if the it is the turret break point!");
		if(BuildingManager.isTurretBreakPoint(e.getBlock().getLocation())){
			Methods.debug(player, "It is a turret break point");
			Turret turret = BuildingManager.getTurretFromBreakPoint(e.getBlock().getLocation());
			if(plugin.getArena().isOnSameTeam(player, turret.getOwner())){
				player.sendMessage(ChatColor.RED + "You cannot break your own teammate's Turrets!");
				e.setCancelled(true);
				return; 
			}else{
				 
				Methods.debug(player, "Starting to remove turret!");
				turret.remove();
				player.sendMessage(ChatColor.GRAY + "You have broken " + ChatColor.GOLD + turret.getOwner().getName() + "'s " + ChatColor.GRAY + " turret!");
				turret.getOwner().sendMessage(ChatColor.RED + player.getName() + ChatColor.GRAY + " has broken your turret!");
				Cooldown.add(turret.getOwner().getUniqueId(), "turretplace",8, System.currentTimeMillis());
			}
			
		}
		
	}
	
	
}
