 package me.artish1.CrystalClash.Listeners;

 import com.gmail.filoghost.holographicdisplays.api.Hologram;
 import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
 import com.gmail.filoghost.holographicdisplays.api.handler.PickupHandler;
 import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
 import me.artish1.CrystalClash.Arena.Arena;
 import me.artish1.CrystalClash.Arena.ArenaManager;
 import me.artish1.CrystalClash.Arena.GameState;
 import me.artish1.CrystalClash.Arena.Point;
 import me.artish1.CrystalClash.Classes.ClassType;
 import me.artish1.CrystalClash.CrystalClash;
 import me.artish1.CrystalClash.Listeners.Classes.ArcherListener;
 import me.artish1.CrystalClash.Listeners.Classes.ExplosivesListener;
 import me.artish1.CrystalClash.Menu.menus.DeathCallMenu;
 import me.artish1.CrystalClash.Util.Methods;
 import me.artish1.CrystalClash.Util.MySQLUtil;
 import me.artish1.CrystalClash.crates.Crate;
 import me.artish1.CrystalClash.crates.Crates;
 import me.artish1.CrystalClash.leaderboards.Leaderboard;
 import me.artish1.CrystalClash.other.BlockPlaceManager;
 import me.artish1.CrystalClash.other.ClaymoreInfo;
 import net.minecraft.server.v1_15_R1.EntityArrow;
 import net.minecraft.server.v1_15_R1.PacketPlayInClientCommand;
 import org.bukkit.*;
 import org.bukkit.block.Block;
 import org.bukkit.craftbukkit.v1_15_R1.entity.CraftArrow;
 import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
 import org.bukkit.entity.Arrow;
 import org.bukkit.entity.Player;
 import org.bukkit.entity.Projectile;
 import org.bukkit.event.EventHandler;
 import org.bukkit.event.EventPriority;
 import org.bukkit.event.Listener;
 import org.bukkit.event.block.BlockBreakEvent;
 import org.bukkit.event.block.BlockBurnEvent;
 import org.bukkit.event.block.BlockIgniteEvent;
 import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
 import org.bukkit.event.block.BlockPlaceEvent;
 import org.bukkit.event.entity.*;
 import org.bukkit.event.inventory.InventoryClickEvent;
 import org.bukkit.event.inventory.InventoryType.SlotType;
 import org.bukkit.event.player.*;
 import org.bukkit.event.player.PlayerLoginEvent.Result;
 import org.bukkit.event.server.ServerListPingEvent;
 import org.bukkit.inventory.ItemStack;

 import java.util.HashMap;
 import java.util.HashSet;
 import java.util.UUID;

public class GameListener implements Listener{
	
	CrystalClash plugin;
	
	public static HashSet<UUID> respawnQueue = new HashSet<UUID>();
	public static HashSet<UUID> chances = new HashSet<UUID>();
	public static HashSet<UUID> bouncyTNT = new HashSet<UUID>();
	public static HashMap<UUID,UUID> rockets = new HashMap<UUID,UUID>();
	public final int CRATE_DROP_CHANCE = 10; //Percentage wise.
	
	
	
	
	public GameListener(CrystalClash plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onBlockIgnite(BlockIgniteEvent e){
		if(e.getCause() == IgniteCause.LIGHTNING || e.getCause() == IgniteCause.SPREAD){
			e.setCancelled(true);
		}
		
	}
	
	
	
	
	@EventHandler
	public void onBlockBurn(BlockBurnEvent e){
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockForm(EntityChangeBlockEvent e){
		if(Methods.cancelFallBlocks.contains(e.getEntity().getUniqueId())){
			e.setCancelled(true);
		}
	}
	
	
	@EventHandler
	public void onRocketHit(ProjectileHitEvent e){
		if(rockets.containsKey(e.getEntity().getUniqueId())){
			Methods.createTeamExplosion(Bukkit.getPlayer(rockets.get(e.getEntity().getUniqueId())), e.getEntity().getLocation(), 6, 10);
			rockets.remove(e.getEntity().getUniqueId());
			Methods.displayParticleEffect(Particle.EXPLOSION_LARGE, true, e.getEntity().getLocation(),0,0,0,10,20);

		}
	}
	
	@EventHandler
	public void onServerListPing(ServerListPingEvent e){
		if(plugin.getArena().getState() == GameState.INGAME){
			e.setMotd(ChatColor.GREEN + "Ingame");
			return;
		}
		
		if(plugin.getArena().getState() == GameState.LOBBY){
			e.setMotd(ChatColor.YELLOW + "Waiting...");
			return;
		}
		
		if(plugin.getArena().getState() == GameState.STARTING){
			e.setMotd(ChatColor.AQUA + "Starting...");
			return;
		}
		
		if(plugin.getArena().getState() == GameState.STOPPING){
			e.setMotd(ChatColor.RED + "Ending...");
		}
		
	}
	
	
	@EventHandler
	public void onFoodLevel(FoodLevelChangeEvent e){
		if(e.getEntity() instanceof Player){
			if(e.getFoodLevel() < 20){
				e.setFoodLevel(20);
			}
		}	
	}
	
	@EventHandler
	public void onLogin(PlayerLoginEvent e){
		if(plugin.getArena().getState() == GameState.STOPPING){
			e.disallow(Result.KICK_OTHER, ChatColor.RED + "Game is stopping...Please wait up to 20 seconds before trying again");
		}else{
			
		}
	}
	
	
	@EventHandler
	public void onBouncyExplode(EntityExplodeEvent e){
		
		if(bouncyTNT.contains(e.getEntity().getUniqueId())){
			for(Block b : e.blockList()){
				if(Methods.getImportantBlocks().contains(b.getLocation())){
					
					continue;
				}
				
				Methods.bounceBlock(b);
			}
		}
		e.blockList().clear();
	}
	
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBreak(BlockBreakEvent e){
		Player player = e.getPlayer();
		if(plugin.getArena().getState() != GameState.INGAME){
			
			if(plugin.getArena().getState() == GameState.STOPPING){
				e.setCancelled(true);
				return;
			}
			
			if(player.hasPermission("cp.admin") || player.hasPermission("cp.break"))
				return;
			
			
			
			e.setCancelled(true);
			return;
		}else{ 
			if(e.getBlock().getType() != Material.COBWEB){
				if(!ArcherListener.debuggers.contains(player.getUniqueId())){
					e.setCancelled(true);
				}else{
					if(e.getBlock().getLocation().equals(Methods.getArena().getRedCrystal()) 
							|| e.getBlock().getLocation().equals(Methods.getArena().getBlueCrystal())){
						e.setCancelled(true);
					}
					
					
				}
				
				
			}else{
				e.setCancelled(true);
				e.getBlock().setType(Material.AIR);
			}
		}
		if(respawnQueue.contains(player.getUniqueId())){
			return;
		}
		
		
		Location loc = e.getBlock().getLocation();
		
		
		if(loc.equals(plugin.getArena().getRedCrystal())){
			e.setCancelled(true);
			
			if(plugin.getArena().isRed(player)){
				player.sendMessage(ChatColor.GRAY + "You cannot damage your own team's Crystal!");
				return;
			}
			
			plugin.getArena().subtractRedCrystalHealth();
			plugin.getArena().warnRedPlayers();
			MySQLUtil.incrementScore(player.getUniqueId(), Leaderboard.MOST_CRYSTAL_BREAKS);
			return;
		}
		
		if(loc.equals(plugin.getArena().getBlueCrystal())){
			e.setCancelled(true);

			if(plugin.getArena().isBlue(player)){
				player.sendMessage(ChatColor.GRAY + "You cannot damage your own team's Crystal!");
				return;
			}
			
			plugin.getArena().subtractBlueCrystalHealth();
			plugin.getArena().warnBluePlayers();
			MySQLUtil.incrementScore(player.getUniqueId(), Leaderboard.MOST_CRYSTAL_BREAKS);
			return;
		}
		
		if(loc.equals(plugin.getArena().getPoint1Spawn())){
			e.setCancelled(true);
			if(plugin.getArena().isAutoCapture())
				return;
			
			
			if(plugin.getArena().isRed(player)){
				e.getBlock().setType(Material.RED_WOOL);
				if(plugin.getArena().getPoint1() != Point.RED){
					plugin.getArena().getArenaPlayer(player).addMoney(15);
				}
				
				plugin.getArena().setPoint1(Point.RED);
				
				return;
			}
			if(plugin.getArena().isBlue(player)){
				e.getBlock().setType(Material.BLUE_WOOL);
				if(plugin.getArena().getPoint1() != Point.BLUE){
					plugin.getArena().getArenaPlayer(player).addMoney(15);
					}
				plugin.getArena().setPoint1(Point.BLUE);
				
				return;
			}
			
			return;
		}
		
		if(loc.equals(plugin.getArena().getPoint2Spawn())){
			e.setCancelled(true);
			if(plugin.getArena().isAutoCapture())
				return;
			
			
			if(plugin.getArena().isRed(player)){
				e.getBlock().setType(Material.RED_WOOL);
				if(plugin.getArena().getPoint2() != Point.RED){
					plugin.getArena().getArenaPlayer(player).addMoney(15);
				}
				plugin.getArena().setPoint2(Point.RED);
				
				return;
			}
			if(plugin.getArena().isBlue(player)){
				e.getBlock().setType(Material.BLUE_WOOL);
				if(plugin.getArena().getPoint2() != Point.BLUE){
					plugin.getArena().getArenaPlayer(player).addMoney(15);
					}
				plugin.getArena().setPoint2(Point.BLUE);
				
				return;
			}
			
			return;
		}
		
		if(loc.equals(plugin.getArena().getPoint3Spawn())){
			e.setCancelled(true);
			if(plugin.getArena().isAutoCapture())
				return;
			
			
			if(plugin.getArena().isRed(player)){
				e.getBlock().setType(Material.RED_WOOL);
				if(plugin.getArena().getPoint3() != Point.RED){
					plugin.getArena().getArenaPlayer(player).addMoney(15);
					}	
				plugin.getArena().setPoint3(Point.RED);
				return;
			}
			if(plugin.getArena().isBlue(player)){
				e.getBlock().setType(Material.BLUE_WOOL);
				if(plugin.getArena().getPoint3() != Point.BLUE){
					plugin.getArena().getArenaPlayer(player).addMoney(15);
					}	
				plugin.getArena().setPoint3(Point.BLUE);
			}

		}
		
	}
	
	
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onC4Break(BlockBreakEvent e){
		if(plugin.getArena().getState() != GameState.INGAME)
			return;
		
		if(e.getBlock().getType() == Material.SPONGE){
			Player player = e.getPlayer();
			if(BlockPlaceManager.isC4(e.getBlock())){
				Player owner = Bukkit.getPlayer(BlockPlaceManager.getOwner(e.getBlock()).getPlayer());
				
				if(owner.getUniqueId().equals(player.getUniqueId())){
					e.setCancelled(true);
					return;
				}
				
				if(plugin.getArena().isOnSameTeam(player, owner)){
					player.sendMessage(ChatColor.RED + "You cannot destroy your own teammates C4!");
					e.setCancelled(true);
					return;
				}
				
				BlockPlaceManager.getOwner(e.getBlock()).removeBlock(e.getBlock());
				e.getBlock().setType(Material.AIR);
				player.sendMessage(ChatColor.GRAY + "You have disabled " + ChatColor.GOLD + owner.getName() + "'s" + ChatColor.GRAY + " C4!");
				owner.sendMessage(ChatColor.RED + player.getName() +ChatColor.GRAY + " has destroyed one of your C4!");
			}else{
			}
		}
		
		
		
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e){
		if(respawnQueue.contains(e.getPlayer().getUniqueId())){
			return;
		}
		
		if(plugin.getArena().getState() != GameState.INGAME){
			Player player = e.getPlayer();
			if(player.hasPermission("cp.admin") || player.hasPermission("cp.place"))
				return;
			e.setCancelled(true);
		}
	}
	
	
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		
		if(!MySQLUtil.hasRow(e.getPlayer().getUniqueId()))
			MySQLUtil.insertNew(e.getPlayer().getUniqueId()); 
		
		MySQLUtil.checkNames(e.getPlayer().getUniqueId()); 
		
		if(!MySQLUtil.hasNewCrates(e.getPlayer().getUniqueId()))
		{
			MySQLUtil.insertNewCrates(e.getPlayer().getUniqueId());
		}
		for(ClassType type : ClassType.values())
		{
			if(type.getUpgradeMenu() == null)
				continue;
			if(!MySQLUtil.hasClassTypeRow(e.getPlayer().getUniqueId(), type))
			{
				MySQLUtil.insertNewClassTypeInfo(e.getPlayer().getUniqueId(), type);
			}
		}
		
		final Player player = e.getPlayer();
			Bukkit.getScheduler().scheduleSyncDelayedTask(Methods.getPlugin(), new Runnable(){

				@Override
				public void run() {
					showPlayer(player);
				}
				
			}, 10);
		if(plugin.getArena().getState() == GameState.LOBBY || plugin.getArena().getState() == GameState.STARTING){
			//Methods.updateLobbyScoreboard();

			Methods.createLobbyScoreboard(player);


		}
			
			
		e.setJoinMessage("");
		plugin.getArena().addPlayer(player);
		player.setGameMode(GameMode.SURVIVAL);
		
		if(!player.hasPermission("cp.admin")){
			player.setAllowFlight(false);
			player.setFlying(false);
		}
	}
	
	
	@EventHandler //For rangers/arrows, so not much will be displayed.
	public void onArrowHit(final ProjectileHitEvent e){
		if(!(e.getEntity() instanceof Arrow))
			return;
		
		if(ExplosivesListener.c4Arrows.contains(e.getEntity().getUniqueId()))
			return;
		CraftArrow cArrow = (CraftArrow) ((Arrow) e.getEntity());
		cArrow.getHandle().fromPlayer = EntityArrow.PickupStatus.DISALLOWED;
		Bukkit.getScheduler().scheduleSyncDelayedTask(Methods.getPlugin(), new Runnable(){

			@Override
			public void run() {
				e.getEntity().remove();
			}
			
		}, 20 * 3);
		
	}
	
	private void hidePlayer(Player p){
		for(Player pl: Bukkit.getOnlinePlayers()){
			pl.hidePlayer(p);
		}
	}
	
	private void showPlayer(Player p){
		for(Player pl: Bukkit.getOnlinePlayers()){
			pl.showPlayer(p);
		}
	}
	
	private void giveSwitchChance(final Player p){
		chances.add(p.getUniqueId());
		p.getInventory().addItem(Methods.getClassMenuItem());
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			@Override
			public void run() {
				if(p.getInventory().contains(Methods.getClassMenuItem())){
					p.getInventory().remove(Methods.getClassMenuItem()); 
				}
				chances.remove(p.getUniqueId());
				p.closeInventory();
				p.updateInventory();
				Methods.removePotionEffects(p);
				plugin.getArena().setPassivePotionEffect(p);
			}
			
		}, 20 * 6);
		
	}
	
	public void startRespawnDelay(final Player player){
		hidePlayer(player);
		final Arena arena = ArenaManager.getArena(player);
		player.setAllowFlight(true);
		player.setFlying(true);
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			@Override
			public void run() {
				respawnQueue.remove(player.getUniqueId());
				
				if(arena.getState() != GameState.INGAME)
					return;
				
				if(!player.isOnline()){
					return;
				}
				
				if(arena.isRed(player))
				{
					player.teleport(arena.getRedSpawn());
				}
				if(arena.isBlue(player))
				{
					player.teleport(arena.getBlueSpawn());
				}
				
				player.getInventory().clear();
				arena.setInventory(player);
				player.setAllowFlight(false);
				player.setFlying(false);
				giveSwitchChance(player);
				showPlayer(player);
				player.setNoDamageTicks(20 * 4 + 10);
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(Methods.getPlugin(), new Runnable(){

					@Override
					public void run() {
						arena.setPassivePotionEffect(player);
					}
					
				}, 2);
				
			}
			
		}, 20 * 7);
	}
	
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e){
		final Player player = e.getEntity();
		if(!ArenaManager.isInArena(player))
			return;
		
		if(player.isInsideVehicle()){
			player.leaveVehicle();
		}
		
		e.setDeathMessage("");
		Arena arena = ArenaManager.getArena(player);
		e.getDrops().clear();
		e.setDroppedExp(0);
		
		if(arena.getState() == GameState.INGAME){
			
			
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(Methods.getPlugin(), new Runnable()
		      {
		        public void run()
		        {
		          PacketPlayInClientCommand packet = new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN);

		          ((CraftPlayer)player).getHandle().playerConnection.a(packet);
		        }
		      }, 1);
			
			
			if(ClaymoreInfo.hasClaymore(player.getUniqueId())){
				ClaymoreInfo.clearClaymores(player.getUniqueId()); 
			}
			plugin.getArena().getArenaPlayer(player).resetKillstreak();
			
			MySQLUtil.incrementScore(player.getUniqueId(), Leaderboard.MOST_DEATHS);
			if(player.getKiller() != null){
				player.getKiller().sendMessage(ChatColor.GRAY + "You have killed " + ChatColor.GREEN + player.getName() + ".");
				player.sendMessage(ChatColor.GRAY + "You have been killed by " + ChatColor.RED + player.getKiller().getName() + ChatColor.GRAY + ".");
				MySQLUtil.incrementScore(player.getKiller().getUniqueId(), Leaderboard.MOST_KILLS);
				plugin.getArena().getArenaPlayer(player.getKiller()).addKill();
				plugin.getArena().getArenaPlayer(player.getKiller()).addMoney(10);
				plugin.getArena().getArenaPlayer(player.getKiller()).increaseKillstreak();
				crateDropChance(player,player.getKiller());
			}
		}
	}
	
	
	private void crateDropChance(Player player,Player killer)
	{
		int chance = Methods.getRandom().nextInt(100) + 1;
		if(chance > CRATE_DROP_CHANCE)
			return;
		
		final Hologram hg = HologramsAPI.createHologram(plugin, player.getLocation().add(0,1.1,0));
		hg.getVisibilityManager().setVisibleByDefault(false);
		hg.getVisibilityManager().showTo(killer);
		for(Player pla: Bukkit.getOnlinePlayers())
		{
			if(pla.getUniqueId() != killer.getUniqueId())
				hg.getVisibilityManager().hideTo(pla);
		}
		killer.sendMessage(ChatColor.GRAY + "A crate has been dropped for you to pick up. Hurry before it goes away!");
		final Crate crate = Crates.getRandomCrate();
		hg.appendTextLine(crate.getName() + " for " + killer.getName());
		ItemLine itemLine = hg.appendItemLine(new ItemStack(Material.CHEST)); 
		itemLine.setPickupHandler(new PickupHandler(){ 

			@Override
			public void onPickup(Player p) {
				MySQLUtil.addCrateAmount(p.getUniqueId(), crate.getType(), 1);
				p.sendMessage(ChatColor.GRAY + "You have picked up " + crate.getName()); 
				p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 2f);
				hg.delete();
			}
			
		});
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Methods.getPlugin(), new Runnable(){

			@Override
			public void run() {
				if(!hg.isDeleted())
					hg.delete();
			}
			
		}, 20 * 25);
		
	}
	
	@EventHandler
	public void onInventoryClick (InventoryClickEvent event) {
		 
	    final Player player = (Player)event.getWhoClicked();
	    
	    if (event.getSlotType() == SlotType.ARMOR) {
	    	event.setCancelled(true);
	    	Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

				@Override
				public void run() {
			    	player.updateInventory();
					
				}
	    		
	    	}, 10);
	    }
	}
	
	
	
	@EventHandler	(priority = EventPriority.HIGHEST)
	public void onRespawn(PlayerRespawnEvent e){
		if(!ArenaManager.isInArena(e.getPlayer()))
			return;
		
		final Player player = e.getPlayer();
		final Arena arena = ArenaManager.getArena(player);
		
		if(arena.getState() == GameState.INGAME){
			e.setRespawnLocation(arena.getPoint1Spawn().add(0, 1, 0));
			respawnQueue.add(player.getUniqueId());
			DeathCallMenu.updateDeathCallMenus();
			player.getInventory().addItem(Methods.getClassMenuItem());
			player.updateInventory();
			player.sendMessage(ChatColor.GRAY + "You will respawn in 7 seconds. You may change your class with the iron ingot.");
			startRespawnDelay(player);
			
			return;
			
		}else{
			e.setRespawnLocation(Methods.getLobby());
		}
		
		
	}
	
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		Player player = e.getPlayer();
		
		
		
		e.setQuitMessage("");
		if(!ArenaManager.isInArena(player))
			return;
		e.setQuitMessage(ChatColor.RED + player.getName() + ChatColor.GRAY + " has left.");
		Arena arena = ArenaManager.getArena(player);
		arena.removePlayer(player);
		
	}
	
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player player = (Player) e.getEntity();
			
			
			if(plugin.getArena().getState() != GameState.INGAME){
				e.setCancelled(true);
				return;
			}
			
			if(respawnQueue.contains(player.getUniqueId())){
				e.setCancelled(true);
			}
			
			
			if(e.getDamage() >= player.getHealth()){
				player.leaveVehicle();
			}
			
		}
		
		
	}
	
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof Player){
			Player player = (Player) e.getEntity();
			
			if(e.getDamager() instanceof Player){
				Player damager = (Player) e.getDamager();
				
				if(respawnQueue.contains(damager.getUniqueId())){
					e.setCancelled(true);
				}
				
				if(ArenaManager.isInArena(player)){
					Arena arena = ArenaManager.getArena(player);
					if(arena.isOnSameTeam(player, damager)){
						e.setCancelled(true);
					}
				}
			}
			
			if(e.getDamager() instanceof Projectile){
				Projectile arrow = (Projectile) e.getDamager();
				if(arrow.getShooter() instanceof Player){
					Player shooter = (Player) arrow.getShooter();
					
					if(ArenaManager.isInArena(player)){
						if(ArenaManager.getArena(player).isOnSameTeam(player, shooter)){
							e.setCancelled(true);
							e.setDamage(0.0); 
							Methods.debug(shooter, "Should be canceled"); 
						}
					}
					
				}
			}
			
		}
		
		
		
	}
	
	
	
	@EventHandler
	public void onDropItem(PlayerDropItemEvent e){
		final Player player = e.getPlayer();
			
		e.setCancelled(true);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Methods.getPlugin(), new Runnable(){

			@Override
			public void run() {
				player.updateInventory();
			}
			
		}, 1);
		
	}
	
	
}
