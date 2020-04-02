package me.artish1.CrystalClash.Listeners;

import me.artish1.CrystalClash.Arena.ArenaManager;
import me.artish1.CrystalClash.Menu.items.FlashBombItem;
import me.artish1.CrystalClash.Menu.items.GrenadeItem;
import me.artish1.CrystalClash.Menu.items.LifeDrainItem;
import me.artish1.CrystalClash.Util.Methods;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class ItemListener implements Listener{
	
	private static HashSet<UUID> grenades = new HashSet<UUID>();
	private static HashSet<UUID> flashbombs = new HashSet<UUID>();

	
	@EventHandler
	public void onVampireEye(EntityDamageByEntityEvent e){

		if(e.getDamager() instanceof Player){
			if(ArenaManager.isInArena((Player) e.getDamager())){
				Player player = (Player) e.getDamager();
				Methods.debug(player, "checking if it's lifedrainitem");
				if(player.getInventory().contains(LifeDrainItem.getVampireEye())){
					double toHeal = e.getDamage() * 0.20;
					double total = toHeal + player.getHealth();
					if(toHeal <= 20.0){
						if(total > 20.0){
							total = 20.0;
						}
						Methods.debug(player, "Healing you for: " + toHeal);

						player.setHealth(total); 
					}
				}
			}
		}else{
			if(e.getDamager() instanceof Projectile){
				Projectile proj = (Projectile) e.getDamager();
				if(proj.getShooter() != null && proj.getShooter() instanceof Player){
					Player player = (Player) proj.getShooter();
					Methods.debug(player, "checking if it's lifedrainitem");

					if(player.getInventory().contains(LifeDrainItem.getVampireEye())){
						double toHeal = e.getDamage() * 0.20;
						double total = toHeal + player.getHealth();
						if(toHeal <= 20.0){
							if(total > 20.0){
								total = 20.0;
							}
							Methods.debug(player, "Healing you for: " + toHeal);

							player.setHealth(total); 
						}
					}
				}
			}
		}
	}
	
	
	@EventHandler
	public void onThrowables(ProjectileHitEvent e){
		if(grenades.contains(e.getEntity().getUniqueId())){
			Projectile proj = e.getEntity();
			Methods.createTeamExplosion((Player) proj.getShooter(), e.getEntity().getLocation(), 6, 6);
		}
		
		if(flashbombs.contains(e.getEntity().getUniqueId())){
			Projectile proj = e.getEntity();
			Player shooter = (Player) proj.getShooter();
			 List<Entity> entities = proj.getNearbyEntities(5.0D, 5.0D, 5.0D);
			 Methods.createTeamExplosion(shooter, e.getEntity().getLocation(), 0, 0);
	          for (Entity entity : entities) {
	            if ((entity instanceof Player))
	            {
	            	Player player = (Player) entity;
	            	if(ArenaManager.getArena(shooter).isOnSameTeam(player, shooter))
	            		continue;
	            	
	              ((Player)entity).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 4, 1), true);
	              ((Player)entity).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 4, 1), true);
	            }
	          }
		}
		
	}
	
	public static void launchGrenade(Player p){
		grenades.add(p.launchProjectile(Egg.class).getUniqueId());
	}
	
	public static void launchFlashBomb(Player p){
		flashbombs.add(p.launchProjectile(Snowball.class).getUniqueId());
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		if(e.hasItem()){
			ItemStack itemg = GrenadeItem.getGrenadeItem();
			itemg.setAmount(e.getItem().getAmount());
			if(e.getItem().equals(itemg)){
				e.setCancelled(true);
				
				launchGrenade(e.getPlayer());
				if(ArenaManager.getArena(e.getPlayer()).getArenaPlayer(e.getPlayer()).getBought().contains(GrenadeItem.getGrenadeItem())){
					ArenaManager.getArena(e.getPlayer()).getArenaPlayer(e.getPlayer()).getBought().remove(GrenadeItem.getGrenadeItem());
				}
				

					if(e.getPlayer().getInventory().getItemInHand().getAmount() <= 1){
						e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR));
						Methods.debug(e.getPlayer(), "Setting hand to air.");
					}else{
						e.getPlayer().getInventory().getItemInHand().setAmount(e.getPlayer().getInventory().getItemInHand().getAmount() -1);
					}

				e.getPlayer().updateInventory();


			}
			ItemStack flashItem = FlashBombItem.getFlashBomb();
			flashItem.setAmount(e.getItem().getAmount());
			
			if(e.getItem().equals(flashItem)){
				e.setCancelled(true); 
				launchFlashBomb(e.getPlayer());
				
				if(ArenaManager.getArena(e.getPlayer()).getArenaPlayer(e.getPlayer()).getBought().contains(FlashBombItem.getFlashBomb())){
					ArenaManager.getArena(e.getPlayer()).getArenaPlayer(e.getPlayer()).getBought().remove(FlashBombItem.getFlashBomb());
				}

					if(e.getPlayer().getInventory().getItemInHand().getAmount() <= 1){
						e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR));
						Methods.debug(e.getPlayer(), "Setting hand to air.");
					}else{
						e.getPlayer().getInventory().getItemInHand().setAmount(e.getPlayer().getInventory().getItemInHand().getAmount() -1);
					}

				
				
				e.getPlayer().updateInventory();
			}
			
			
		}
	}
	
}
