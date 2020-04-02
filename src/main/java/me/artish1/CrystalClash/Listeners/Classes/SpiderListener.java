package me.artish1.CrystalClash.Listeners.Classes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Classes.Abilities.WebThrow;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.other.ClassInventories;

public class SpiderListener implements Listener{
	
	
	private boolean canPoison(){
		Random r = new Random();
		int rnum = r.nextInt(100) + 1;

        return rnum > 30;
		
	}
	
	@EventHandler
	public void onPoison(EntityDamageByEntityEvent e){
		if(e.getDamager() instanceof Player){
			Player attacker = (Player) e.getDamager();
			
			if(Methods.getPlugin().getArena().getArenaPlayer(attacker).getType() == ClassType.SPIDER){
				
				if(canPoison()){
					if(e.getEntity() instanceof LivingEntity){
						LivingEntity le = (LivingEntity) e.getEntity();
						
						if(le instanceof Player)
						{
							Player damaged = (Player) le;
							if(Methods.getArena().isOnSameTeam(attacker, damaged))
								return;
						}
						
						le.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 80, 1));
						
					}
					
					
				}
				
				
			}
			
			
		}
	}
	
	public static HashSet<UUID> wallClimbers = new HashSet<UUID>();
	
	public static void loadSpiderStuff()
	{
		try {
			Statement statement = Methods.getPlugin().c.createStatement();
			ResultSet res = statement.executeQuery("SELECT * FROM spiderinfo");
			
			while(res.next())
			{
				if(res.getBoolean("wallclimb"))
				{
					wallClimbers.add(UUID.fromString(res.getString("uuid")));
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	@EventHandler
	public void onAbility(PlayerInteractEvent e){
		
		if(!e.hasItem())
			return;
		
		if(Methods.getPlugin().getArena().getArenaPlayer(e.getPlayer()).getType() != ClassType.SPIDER)
			return;
		
		Player player = e.getPlayer();
		Methods.debug(player, "We are spider class");
		
		if(e.getItem().equals(ClassInventories.getSpiderCobwebItem())){
			if(e.getAction() == Action.RIGHT_CLICK_AIR ||
					e.getAction() == Action.RIGHT_CLICK_BLOCK ||
					e.getAction() == Action.LEFT_CLICK_AIR){
				
				new WebThrow(player).cast();
				return;
				
			}
			
			
		}
		
		if(e.getItem().getType() == Material.IRON_SWORD || e.getItem().getType() == Material.DIAMOND_SWORD)
		{
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				
				/*if(MySQLUtil.getBoolean(player.getUniqueId(), "spiderinfo", "wallclimb"))
				{
					Vector vel = e.getPlayer().getVelocity();
					vel.setY(0.7);
					e.getPlayer().setVelocity(vel);
					Methods.debug(player, "Climbing...");
				}
				*/
				if(wallClimbers.contains(player.getUniqueId()))
				{
					Vector vel = e.getPlayer().getVelocity();
					vel.setY(0.7);
					e.getPlayer().setVelocity(vel);
					Methods.debug(player, "Climbing...");
				}
				
				
				
			}
		}
		
	}
	
}
