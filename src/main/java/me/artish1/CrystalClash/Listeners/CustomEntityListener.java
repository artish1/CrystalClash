package me.artish1.CrystalClash.Listeners;

import me.artish1.CrystalClash.Arena.Arena;
import me.artish1.CrystalClash.Arena.ArenaManager;
import me.artish1.CrystalClash.Arena.GameState;
import me.artish1.CrystalClash.Classes.Abilities.BowChangeMode;
import me.artish1.CrystalClash.Classes.Abilities.FireRocket;
import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.CrystalClash;
import me.artish1.CrystalClash.Listeners.Classes.ArcherListener;
import me.artish1.CrystalClash.Menu.menus.Menus;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.Util.MySQLUtil;
import me.artish1.CrystalClash.other.ArrowStraightener;
import me.artish1.CrystalClash.other.ClassInventories;
import me.artish1.CrystalClash.other.GunningInfo;
import me.artish1.CrystalClash.other.ScheduleManager;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;


public class CustomEntityListener implements Listener{
	
	CrystalClash plugin;
	
	public static HashSet<UUID> items = new HashSet<UUID>();
	
	public CustomEntityListener(CrystalClash plugin) {
		this.plugin = plugin;
	}
	
	
	private static HashMap<UUID,ItemStack[]> armor = new HashMap<UUID,ItemStack[]>();
	private static HashMap<UUID,ItemStack[]> contents = new HashMap<UUID,ItemStack[]>();
	
	
	
	
	public static void saveInventory(Player p){
		armor.put(p.getUniqueId(), p.getInventory().getArmorContents());
		contents.put(p.getUniqueId(), p.getInventory().getContents());
	}
	
	public static boolean hasInventory(Player p){
		return armor.containsKey(p.getUniqueId());
	}
	
	
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e){
		if(Methods.getRedSummons().contains(e.getEntity().getUniqueId())){
			if(e.getDamager() instanceof Player){
				Player player = (Player) e.getDamager();
				if(Methods.getPlugin().getArena().isRed(player)){
					e.setCancelled(true);
					return;
				}
			}
			
			if(e.getDamager() instanceof Projectile){
				Projectile proj = (Projectile) e.getDamager();
				if(proj.getShooter() instanceof Player){
					Player player = (Player) proj.getShooter();
					if(Methods.getPlugin().getArena().isRed(player)){
						e.setCancelled(true);
					}
					
				}
			}
			
		}
		if(Methods.getBlueSummons().contains(e.getEntity().getUniqueId())){
			if(e.getDamager() instanceof Player){
				Player player = (Player) e.getDamager();
				if(Methods.getPlugin().getArena().isBlue(player)){
					e.setCancelled(true);
					return;
				}
			}
			
			if(e.getDamager() instanceof Projectile){
				Projectile proj = (Projectile) e.getDamager();
				if(proj.getShooter() instanceof Player){
					Player player = (Player) proj.getShooter();
					if(Methods.getPlugin().getArena().isBlue(player)){
						e.setCancelled(true);
					}
					
				}
			}
		}
		
	}
	
	@EventHandler
	public void onMountTanks(PlayerInteractEntityEvent e){
		Player p = e.getPlayer();
		
		if(e.getRightClicked() instanceof LivingEntity){
			LivingEntity ent = (LivingEntity) e.getRightClicked();
			if(ent.getCustomName() == null){
				return;
			}
			if(ChatColor.stripColor(ent.getCustomName()).equalsIgnoreCase("Red Tank")){
				if(Methods.getArena().isRed(p)){
					ent.setPassenger(p);
					
				}else{
					p.sendMessage(ChatColor.RED + "You are not on the red team! You can not ride this vehicle!");
				}
				
			}else{
				if(ChatColor.stripColor(ent.getCustomName()).equalsIgnoreCase("Blue Tank")){
					if(Methods.getArena().isBlue(p)){
						ent.setPassenger(p);
						
					}else{
						p.sendMessage(ChatColor.RED + "You are not on the blue team! You can not ride this vehicle!");
					}
				}
			}
			
		}
		
	}
	
	
	public static void loadInventory(Player p){
		p.getInventory().clear();
		if(armor.containsKey(p.getUniqueId())){
			p.getInventory().setArmorContents(armor.get(p.getUniqueId()));
			armor.remove(p.getUniqueId());
		}
		
		if(contents.containsKey(p.getUniqueId())){
			p.getInventory().setContents(contents.get(p.getUniqueId()));
			contents.remove(p.getUniqueId());
		}
		p.updateInventory();
		
	}
	
	
	
	@EventHandler
	public void onTeamPick(PlayerInteractEntityEvent e){
		Player p = e.getPlayer();
		if(Methods.getArena().getState() != GameState.STARTING && Methods.getArena().getState() != GameState.LOBBY)
			return;
		
		if(!(e.getRightClicked() instanceof LivingEntity))
			return;
		LivingEntity ent = (LivingEntity) e.getRightClicked();
		
		if(ent.getCustomName() == null || ent.getCustomName() == "")
			return;
		
		if(Methods.redPick != null)
		if(Methods.redPick.getCustomName().getString().equalsIgnoreCase(ent.getCustomName())){
			//Methods.getPlugin().getArena().addRedQueue(Methods.getArena().getArenaPlayer(p));
			//Methods.redPick.updateName(redTeam);
			p.sendMessage(ChatColor.GOLD + "This is undergoing maintenance. Sorry!");

		}
		
		if(Methods.bluePick != null)
		if(Methods.bluePick.getCustomName().getString().equalsIgnoreCase(ent.getCustomName())){

			//Methods.getPlugin().getArena().addBlueQueue(Methods.getArena().getArenaPlayer(p));
			//Methods.bluePick.updateName(blueTeam);
			p.sendMessage(ChatColor.GOLD + "This is undergoing maintenance. Sorry!");
		}
		
		
	}
	
	
	@EventHandler
	public void onMerchantInteract(PlayerInteractEntityEvent e){
		if(!ArenaManager.isInArena(e.getPlayer()))
			return;
		
		Player player = e.getPlayer();
		Methods.debug(player, "interacting with an entity, we are in an Arena!");
		if(e.getRightClicked() instanceof Villager && Methods.isMerchant(e.getRightClicked())){
			Methods.debug(player, "It is a merchant by uuid");
				e.setCancelled(true);
				Menus.getIngameMenu().open(player);
				player.sendMessage(ChatColor.AQUA + "Welcome to the ingame shop!");
			
			
		}else{
			Methods.debug(player, "Entity is not a Merchant");
		}
	}
	

	@EventHandler
	public void onChopperGunner(PlayerInteractEvent e){
		Player p = e.getPlayer();
		if(!e.hasItem())
			return;
		
		if(p.getVehicle() == null)
			return;
		
		if(Methods.getChoppergunnners().contains(p.getVehicle().getUniqueId())){
			if(e.getItem().equals(getGunnerZoom())){
				
				plugin.getArena().getArenaPlayer(p).toggleZoom();
				e.setCancelled(true);
				return;
			}
			 
			if(e.getItem().equals(getChopperGunnerMachineGun())){
				if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
				final Arrow arrow = p.launchProjectile(Arrow.class);
				arrow.setVelocity(p.getLocation().getDirection().multiply(4.0f));
				arrow.setShooter(p);
				new ArrowStraightener(arrow, arrow.getVelocity()).start();;
				removeArrow(arrow); 
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_VILLAGER_HURT, 1f, 1.2f);
				shootMore(p);
				e.setCancelled(true);
				return; 
				}else{
					if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction()== Action.LEFT_CLICK_BLOCK){
						new FireRocket(p).cast();
						e.setCancelled(true);
					}
				}
			}
			if(e.getItem().equals(getChopperGunnerRocketItem())){
				
				new FireRocket(p).cast();
				e.setCancelled(true);
			}
			
			
		}
	}
	
	
	
	
	public static ItemStack getChopperGunnerRocketItem(){
		return Methods.createItem(Material.REDSTONE, ChatColor.GREEN + "Rockets", ClassInventories.createLore("Click to activate!"));
	} 
	
	public static ItemStack getChopperGunnerMachineGun(){
		return Methods.createItem(Material.BLAZE_ROD, ChatColor.RED + "Control Stick", ClassInventories.createLore("Right click to shoot!", "Left click to shoot rockets!"));
	} 
	
	
	@EventHandler
	public void onGunSheepInteract(PlayerInteractEntityEvent e){
		if(!ArenaManager.isInArena(e.getPlayer()))
			return;
		
		Player player = e.getPlayer();
		Arena arena = ArenaManager.getArena(player);
		
		if(arena.getState() != GameState.INGAME)
			return;
		
		
		if(e.getRightClicked() instanceof Sheep && Methods.isGunSheep((Sheep) e.getRightClicked()) ){
			Sheep sheep = (Sheep) e.getRightClicked();
			e.setCancelled(true);
				
			if(sheep.getColor() == DyeColor.BLUE)
			{
				if(!arena.getArenaPlayer(player).isBlue())
				{
					
					player.sendMessage(ChatColor.GRAY + "You must be on the " +ChatColor.BLUE +  "Blue" +" team to mount on their Machine guns!");
					return;
				}
			}
			
			if(sheep.getColor() == DyeColor.RED)
			{
				if(!arena.getArenaPlayer(player).isRed())
				{
					
					player.sendMessage(ChatColor.GRAY + "You must be on the " +ChatColor.RED +  "Red" +" team to mount on their Machine guns!");
					return;
				}
			}
			
			if(sheep.getPassenger() != null){
					player.sendMessage(ChatColor.RED + "Another player is on that station!");
					return;
				}
				
				saveInventory(player);
				player.getInventory().clear();
				
				sheep.setPassenger(player);
				player.getInventory().addItem(getGunnerZoom());
				player.getInventory().setItemInHand(Methods.getGunnerTrigger());
				
				if(arena.getArenaPlayer(player).getType() == ClassType.ARCHER){
					if(ArcherListener.map.containsKey(player.getUniqueId())){
						BowChangeMode bow = ArcherListener.map.get(player.getUniqueId());
						if(bow.getMode() != 1){
							bow.setMode(1);
						} 
					}
					
					
				}
				
				player.setAllowFlight(true);
				
				new ScheduleManager(player,5,"ride");
				
				player.sendMessage(ChatColor.GRAY + "You have mounted onto a " + ChatColor.GOLD + "Gunning Station" + ChatColor.GRAY + "!");
				Methods.sendActionBar(player, ChatColor.GRAY + "You have mounted onto a " + ChatColor.GOLD + "Gunning Station" + ChatColor.GRAY + "!");
			
		}	
	}
	
	
	
	
	public static void removeArrow(final Entity arrow){
		Bukkit.getScheduler().scheduleSyncDelayedTask(Methods.getPlugin(), new Runnable(){

			@Override
			public void run() {
				arrow.remove();
			}
			
		}, 20 * 5);
	}
	
	
	public static ItemStack getGunnerZoom(){
		ItemStack item = new ItemStack(Material.SLIME_BALL);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.BLUE + "Toggle Zoom");
		item.setItemMeta(meta);
		return item;
	}
	
	
	
	@EventHandler
	public void onInteractModels(PlayerInteractEntityEvent e){
		Player player = e.getPlayer();
		
		
		if(e.getRightClicked() instanceof LivingEntity){
			LivingEntity le = (LivingEntity) e.getRightClicked();
			
			
			for(ClassType type : ClassType.values()){
					if(type.getModelUUID() != null)
					if(type.getModelUUID().equals(le.getUniqueId())){
						if(type.isFree()){
							player.sendMessage(ChatColor.GRAY + "You have picked " + type.getName());
							Methods.sendActionBar(player, ChatColor.GRAY + "You have picked " + type.getName());
							plugin.getArena().getArenaPlayer(player).setType(type);
						}else{
							if(MySQLUtil.hasClass(player.getUniqueId(), type)){
								player.sendMessage(ChatColor.GRAY + "You have picked " + type.getName());
								Methods.sendActionBar(player, ChatColor.GRAY + "You have picked " + type.getName());
								plugin.getArena().getArenaPlayer(player).setType(type);
							}else{
								player.sendMessage(ChatColor.RED + "You must first buy this class at the shop!");
							}
						}
						
						
						break;
					}
				
			}
			
		}
		
	}
	

	private HashMap<UUID,Integer> shootDelay = new HashMap<UUID,Integer>();
	
	private HashMap<UUID,GunningInfo> gunInfo = new HashMap<UUID,GunningInfo>();
	
	@EventHandler
	public void onInteractOnEntity(PlayerInteractEvent e){
		if(!e.getPlayer().isInsideVehicle())
			return;
		
		if(!ArenaManager.isInArena(e.getPlayer()))
			return;
		 
		final Player player = e.getPlayer();
		Arena arena = ArenaManager.getArena(player);
		if(arena.getState() != GameState.INGAME)
			return;
		Entity ent = (Entity) e.getPlayer().getVehicle();
		if(ent instanceof Sheep){
			Sheep sheep = (Sheep) ent;
			if(e.hasItem()){
			if(Methods.isGunSheep(sheep)){
				if(e.getAction()== Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR){
						if(e.getItem().equals(Methods.getGunnerTrigger())){ 
							
							if(shootDelay.containsKey(player.getUniqueId())){
								//TODO cooldowns/later use and stuffcahsd asdn
							}
							GunningInfo info;
							if(gunInfo.containsKey(player.getUniqueId()))
							{
								info = gunInfo.get(player.getUniqueId());
							}else{
								info = new GunningInfo(player);
								gunInfo.put(player.getUniqueId(), info);
							}
							if(!info.isCanShoot())
							{
								return;
							}else{
								info.minusShot();
							}
							
							final Arrow arrow = player.launchProjectile(Arrow.class);
							arrow.setVelocity(player.getLocation().getDirection().multiply(4.0f));
							
							
							player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_HURT, 1f, 1.2f);

							arrow.setShooter(player);
							new ArrowStraightener(arrow, arrow.getVelocity()).start();;
							removeArrow(arrow); 
							//shootMore(player);
							
							
						}
				}
				if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK)
				{
					if(e.getItem().equals(getGunnerZoom())){
						
						plugin.getArena().getArenaPlayer(player).toggleZoom();
						
					}
				}
			
			}else{
			}
		}
		}
	}
	
	
	public void shootMore(final Player player){
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			@Override
			public void run() {
				final Arrow arrow = player.launchProjectile(Arrow.class);
				arrow.setVelocity(player.getLocation().getDirection().multiply(4.0f));
				

				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_HURT, 1f, 1.2f);

				arrow.setShooter(player);
				new ArrowStraightener(arrow, arrow.getVelocity()).start();;
				removeArrow(arrow); 	
			}
			
		}, 3);
		
		
	}
	
	
}
