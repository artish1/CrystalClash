package me.artish1.CrystalClash.Listeners.Classes;

import me.artish1.CrystalClash.Arena.GameState;
import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Classes.Abilities.HeavenlyHeal;
import me.artish1.CrystalClash.Classes.Abilities.HolySmite;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.Util.MySQLUtil;
import me.artish1.CrystalClash.other.ClassInventories;

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

public class GuardianListener implements Listener{
	
	
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		Player player = e.getPlayer();
		if(Methods.getArena().getState() != GameState.INGAME)
			return;
			
			if(!e.hasItem())
				return;
			if(Methods.getArena().getArenaPlayer(player).getType() != ClassType.GUARDIAN)
				return;
			
				if(e.getItem().getType() == Material.GOLDEN_SWORD){
					if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
						new HolySmite(player).cast();
						return;
					}
				}
				
				if(e.getItem().equals(ClassInventories.getGuardianHealItem())){
					if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR){
						new HeavenlyHeal(player).cast();
						return;
					}
				}
				
				
				
	}
	
	@EventHandler
	public void onHitSlow(EntityDamageByEntityEvent e)
	{
		if(e.getDamager() instanceof Player)
		{
			
			if(e.getEntity() instanceof LivingEntity)
			{
				LivingEntity le = (LivingEntity)e.getEntity();
				Player player = (Player)e.getDamager();
				if(Methods.getArena().getArenaPlayer(player) != null && Methods.getArena().getArenaPlayer(player).getType() == ClassType.GUARDIAN)
				{
					if(MySQLUtil.getBoolean(player.getUniqueId(), "guardianinfo", "hitslow"))
					{
						le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20 * 4,1),true);
					}
					
					
				}
			}
			
		}
		
		
	}
	
	
}
