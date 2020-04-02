package me.artish1.CrystalClash.Classes.Abilities;

import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.CrystalClash;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.other.ClassInventories;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.UUID;

public class BowChangeMode extends Ability implements Listener{
	
	public int mode = 1;
		
	public BowChangeMode(Player player) {
		super("BowChangeMode", 0, ClassInventories.getArcherBow(), player);
	
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	private HashSet<UUID> explosive = new HashSet<UUID>();
	private HashSet<UUID> lightning = new HashSet<UUID>();
	private HashSet<UUID> poison = new HashSet<UUID>();


	
	public int getMode() {
		return mode;
	}
	
	public void setMode(int mode) {
		this.mode = mode;
	}
	@Override
	public void onCast() {
		if(mode < 5){ //amount of max modes
			mode++;
		}else{
			mode = 1;
		}
		switch(mode){
		case 1:
			player.sendMessage(ChatColor.GRAY + "You are now using the " + ChatColor.GREEN + "Normal" + ChatColor.GRAY + " bow.");
			break;
			
		case 2:
			player.sendMessage(ChatColor.GRAY + "You are now using the " + ChatColor.GREEN + "Explosive" + ChatColor.GRAY + " bow.");
			break;
			
		case 3:
			player.sendMessage(ChatColor.GRAY + "You are now using the " + ChatColor.GREEN + "Lightning" + ChatColor.GRAY + " bow.");
			break;
			
		case 4: 
			player.sendMessage(ChatColor.GRAY + "You are now using the " + ChatColor.GREEN + "Poison" + ChatColor.GRAY + " bow.");
			break;
			
		case 5:
			
			if(!player.hasPermission(CrystalClash.DONATOR_PERMISSION)){
				mode = 1;
				player.sendMessage(ChatColor.GRAY + "You are now using the " + ChatColor.GREEN + "Normal" + ChatColor.GRAY + " bow.");
				break;
			}else{
			
				player.sendMessage(ChatColor.GRAY + "You are now using the " + ChatColor.GREEN + "Volley" + ChatColor.GRAY + " bow.");
				break;
			}
		}
		
		
	}
	/** Shoots a volley of arrows following the initial arrow.
	 * 
	 * @param initial The initial Arrow in the middle of the volley.
	 * @param amount The amount of Volley arrows (not including the initial)
	 * @param wideness The wideness/narrowness of the volley, any number farther from 0 will be wider, any number close 0 will be more narrow.
	 */
	private void shootVolley(Arrow initial, int amount, float wideness)
	{
		float adjustedYaw = player.getLocation().getYaw();
		float toAdjust = wideness;
		
		adjustedYaw = adjustedYaw - ((amount/2) * toAdjust);
		for(; amount >=0; amount--)
		{
			Location loc = initial.getLocation();
			loc.setYaw(adjustedYaw);
			loc.setPitch(player.getLocation().getPitch());
			adjustedYaw += toAdjust;
			Vector vector = loc.getDirection().normalize();
			Arrow arrow = player.getWorld().spawn(loc, Arrow.class);
			arrow.setVelocity(vector);
			setSameSpeed(initial, arrow);
			arrow.setShooter(player);
			if(initial.isCritical())
			{
				arrow.setCritical(true);
			}
		}
		
	}
	
	private void setSameSpeed(Arrow initial, Arrow other)
	{
		double multiplier = initial.getVelocity().length();
		Vector direction = other.getVelocity().clone();
		direction.normalize().multiply(multiplier);
		other.setVelocity(direction);
		
	}
	
	/*
	 ***********(X,Y,Z)
	 * West is (-1,0,0)
	 * East is (1,0,0)
	 * North is (0,0,-1)
	 * South is (0,0,1)
	 * 
	 * 
	 */
	
	
	
	
	
	@EventHandler
	public void onBowShoot(ProjectileLaunchEvent e){
		
		
		if(!(e.getEntity() instanceof Arrow))
			return;
		if(!(e.getEntity().getShooter() instanceof Player))
			return;
		Player player = (Player) e.getEntity().getShooter();
		if(!player.equals(this.player))
			return;
		if(arena.getArenaPlayer(player).getType() != ClassType.ARCHER)
			return;
		
		if(player.getInventory().getItemInHand().getType() != Material.BOW)
			return;
		
		
		switch(mode){
		case 2:
			explosive.add(e.getEntity().getUniqueId());
			return;
		case 3:
			lightning.add(e.getEntity().getUniqueId());
			return;
		case 4:
			poison.add(e.getEntity().getUniqueId());
			return;
		case 5:
			shootVolley((Arrow)e.getEntity(),5,3.5f);

			break;
		default:
			return;
		}
		

		
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Arrow){
			Player hit = (Player) e.getEntity();
			Arrow damager = (Arrow) e.getDamager();
		
			if(poison.contains(damager.getUniqueId())){
				if(!arena.isOnSameTeam(player, hit)){
					hit.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20* 5,1,true),true);
					poison.remove(damager.getUniqueId());
				}
			}
		}
	}
	
	@EventHandler
	public void onHit(ProjectileHitEvent e){
		if(!(e.getEntity() instanceof Arrow))
			return;
		if(!(e.getEntity().getShooter() instanceof Player) && !((Player)e.getEntity().getShooter()).equals(player))
			return;
		
		if(explosive.contains(e.getEntity().getUniqueId())){
			//explosion
			//e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), 2f);
			Methods.createTeamExplosion(player, e.getEntity().getLocation(), 3);
			
			explosive.remove(e.getEntity().getUniqueId());

			return;
		}
		
		
		
		if(lightning.contains(e.getEntity().getUniqueId())){
			//lightning
			e.getEntity().getWorld().spigot().strikeLightningEffect(e.getEntity().getLocation(), true);
			Methods.createTeamExplosion(player, e.getEntity().getLocation(), 2, 6.0);
			lightning.remove(e.getEntity().getUniqueId());
			return;
		}
		
	}
	
	
	/** CODE FOR MULTIPLE ARROW TYPES
	 * switch(mode){
		case 2:
			if(player.getInventory().containsAtLeast(ClassInventories.getExplosiveArrow(1), 1)){
			player.getInventory().removeItem(ClassInventories.getExplosiveArrow(1));
			explosive.add(e.getEntity().getUniqueId());
			}else{
				player.sendMessage(ChatColor.RED + "You do not have any more Explosive Arrows!");
				e.setCancelled(true);
			}
			ClassInventories.addArrow(player);
			
			return;
		case 3:
			if(player.getInventory().containsAtLeast(ClassInventories.getLightningArrow(1), 1)){
				player.getInventory().removeItem(ClassInventories.getLightningArrow(1));
				lightning.add(e.getEntity().getUniqueId());
				}else{
					player.sendMessage(ChatColor.RED + "You do not have any more Lightning Arrows!");
					e.setCancelled(true);
				}
			ClassInventories.addArrow(player);
			return;
		case 4: 
			if(player.getInventory().containsAtLeast(ClassInventories.getTNTArrow(1), 1)){
				player.getInventory().removeItem(ClassInventories.getTNTArrow(1));
				tnt.add(e.getEntity().getUniqueId());
				}else{
					player.sendMessage(ChatColor.RED + "You do not have any more TNT Arrows!");
					e.setCancelled(true);
				}
			ClassInventories.addArrow(player);
				
			return;
		case 5:
			poison.add(e.getEntity().getUniqueId());
			return;
			
		case 6:
			break;
		}
	 * 
	 */
	
	
}
