package me.artish1.CrystalClash.Listeners.Classes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.artish1.CrystalClash.Arena.Arena;
import me.artish1.CrystalClash.Arena.ArenaManager;
import me.artish1.CrystalClash.Arena.ArenaPlayer;
import me.artish1.CrystalClash.Arena.GameState;
import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Classes.Abilities.Invisible;
import me.artish1.CrystalClash.Cooldown.Cooldown;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.other.ClassInventories;

public class AssassinListener implements Listener{
	
	public static HashMap<UUID,Invisible> invis = new HashMap<>();
	
	public static HashSet<UUID> canPoison = new HashSet<UUID>();
	
	@EventHandler
	public void onAccidentPlace(BlockPlaceEvent e){
		Player player = e.getPlayer();
		if(!ArenaManager.isInArena(player))
			return;
		Arena arena = ArenaManager.getArena(player);
		if(arena.getState() != GameState.INGAME)
			return;
		
		if(arena.getArenaPlayer(player).getType() != ClassType.ASSASSIN)
			return;
		
		
		if(e.getItemInHand().equals(ClassInventories.getAssassinInvis())){
			e.setCancelled(true);
			player.updateInventory();
		}
	}
	
	@EventHandler
	public void onInvisibility(PlayerInteractEvent e){
		if(!e.hasItem())
			return;
		
		Player player = e.getPlayer();
		if(!ArenaManager.isInArena(player))
			return;
		Arena arena = ArenaManager.getArena(player);
		if(arena.getState() != GameState.INGAME)
			return;
		
		if(arena.getArenaPlayer(player).getType() != ClassType.ASSASSIN)
			return;
		
		
		if(e.getItem().equals(ClassInventories.getAssassinInvis())){
			if(invis.containsKey(player.getUniqueId())){
				Invisible inv = invis.get(player.getUniqueId());
				inv.stopEarly();
			}else{
				if(Cooldown.isCooling(player.getUniqueId(), "Invisibility")){
					Cooldown.coolDurMessage(player, "Invisibility");
					return;
				}
				Invisible inv = new Invisible(player);
				inv.cast();
				invis.put(player.getUniqueId(), inv);
			}
			e.setCancelled(true);
		}
	}
	
	public static void loadAssassinStuff()
	{
		try {
			Statement statement = Methods.getPlugin().c.createStatement();
			ResultSet res = statement.executeQuery("SELECT * FROM assassininfo");
			while(res.next())
			{
				
				if(res.getBoolean("poisonedsword"))
				{
					canPoison.add(UUID.fromString(res.getString("uuid")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	@EventHandler
	public void onPoisonDamage(EntityDamageByEntityEvent e)
	{
		if(Methods.getArena().getState() != GameState.INGAME)
			return;
		
		if(!(e.getDamager() instanceof Player))
			return;
		
		if(!(e.getEntity() instanceof Player))
			return;
			
		Player damager = (Player)e.getDamager();
		Player player = (Player)e.getEntity();
		
		if(Methods.getArena().getArenaPlayer(damager) != null)
		{
			if(Methods.getArena().getArenaPlayer(player) == null)
				return;
			
			ArenaPlayer ap = Methods.getArena().getArenaPlayer(damager);
			if(ap.getType() == ClassType.ASSASSIN)
			{
				if(damager.getInventory().getItemInHand().getType() == Material.DIAMOND_SWORD)
				{
					/*if(MySQLUtil.getBoolean(damager.getUniqueId(), "assassininfo", "poisonedsword"))
					{
						player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,20 * 3,0,true));
					} */
					
					if(canPoison.contains(damager.getUniqueId()))
					{
						player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,20 * 3,0,true));
					}
					
					
				}
			}
		}
		
		
	}
	
	
	
}
