package me.artish1.CrystalClash.Listeners.Classes;


import me.artish1.CrystalClash.Arena.Arena;
import me.artish1.CrystalClash.Arena.ArenaManager;
import me.artish1.CrystalClash.Arena.GameState;
import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.Util.MySQLUtil;
import me.artish1.CrystalClash.other.*;
import net.minecraft.server.v1_15_R1.EntityArrow;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftArrow;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.*;

public class ExplosivesListener implements Listener{
	private HashSet<UUID> grenades = new HashSet<>();
	
	private HashMap<UUID,MineManager> mines = new HashMap<UUID,MineManager>();	
	public static List<MineManager> managers = new ArrayList<MineManager>();
	public static HashSet<UUID> c4Arrows = new HashSet<UUID>();
	private HashMap<UUID,EntityListManager> c4ArrowManager = new HashMap<UUID,EntityListManager>();
	
	private HashMap<UUID,AmmoReplenisher> nadeReplen = new HashMap<UUID,AmmoReplenisher>();
	
	
	public static boolean hasManager(Player p){
		for(MineManager mm : managers){
			if(mm == null || p == null || mm.getOwner() == null)
				continue;
			
			if(mm.getOwner().getUniqueId() == p.getUniqueId())
				return true;
		}
		return false;
	}
	
	public static MineManager getManager(Player p){
		for(MineManager mm : managers){
			if(mm.getOwner().getUniqueId() == p.getUniqueId())
				return mm;
		}
		return null;
	}

	
	@EventHandler
	public void onShoot(ProjectileLaunchEvent e)
	{
		if(e.getEntity().getShooter() != null && e.getEntity().getShooter() instanceof Player)
		{
			Player player = (Player)e.getEntity().getShooter();
			
			if(Methods.getArena().getArenaPlayer(player) != null)
			{
				if(Methods.getArena().getArenaPlayer(player).getType() == ClassType.EXPLOSIVES)
				{
					if(player.getInventory().getItemInHand().hasItemMeta())
					{
						if(ChatColor.stripColor(player.getInventory().getItemInHand().getItemMeta().getDisplayName()).equalsIgnoreCase("High Frequency Bow"))
						{
							
							if(c4ArrowManager.containsKey(player.getUniqueId()))
							{
								c4ArrowManager.get(player.getUniqueId()).getIds().add(e.getEntity());
							}else{
								EntityListManager man = new EntityListManager(player.getUniqueId());
								man.getIds().add(e.getEntity());
								c4ArrowManager.put(player.getUniqueId(), man);
							}
							
							c4Arrows.add(e.getEntity().getUniqueId());
							e.getEntity().setBounce(true);

							if(e.getEntity() instanceof Arrow)
							{
								Arrow arrow = (Arrow)e.getEntity();
								CraftArrow cArrow = (CraftArrow)arrow;
								cArrow.getHandle().fromPlayer = EntityArrow.PickupStatus.DISALLOWED;



							}
							e.getEntity().setCustomName(ChatColor.GREEN + "C4 Tipped Arrow"); 
						}
						
					}
				}
			}
		}
	}
	

	
	
	
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e){
		Player player = e.getEntity();
		if(Methods.getPlugin().getArena().getArenaPlayer(player).getType() != ClassType.EXPLOSIVES)
			return;
		 
		if(MySQLUtil.getBoolean(player.getUniqueId(), "explosivesinfo", "deathexplode"))
			Methods.createTeamExplosion(player, player.getLocation(), 6, 9); 
		
		if(BlockPlaceManager.hasBlockPlaceManager(player)){
			for(Block b : BlockPlaceManager.getManager(player).getBlocks()){
				b.setType(Material.AIR);	
			}
			BlockPlaceManager.removeManager(player);
			
		}
		
		if(hasManager(player)){
			MineManager manager = getManager(player);
			manager.clearMines();
			
		}
	}
	
	@EventHandler
	public void onC4Place(BlockPlaceEvent e){
		Player player = e.getPlayer();
		if(!ArenaManager.isInArena(player))
			return;
		
		Arena arena = ArenaManager.getArena(player);
		
		if(arena.getState() != GameState.INGAME)
			return;
		
		if(arena.getArenaPlayer(player).getType() != ClassType.EXPLOSIVES)
			return;
		
		if(e.getBlockPlaced().getType() == Material.SPONGE){
			if(BlockPlaceManager.hasBlockPlaceManager(player)){
				BlockPlaceManager.getManager(player).addBlock(e.getBlockPlaced());
			}else{
				new BlockPlaceManager(player,e.getBlockPlaced());
			}
			player.sendMessage(ChatColor.GRAY + "C4 placed.");
			return;
		}
		
		
		if(e.getBlockPlaced().getType() == Material.STONE_PRESSURE_PLATE){
			
			
			if(mines.containsKey(player.getUniqueId())){
				MineManager manager = mines.get(player.getUniqueId());
				manager.addLocation(e.getBlockPlaced().getLocation());
				if(!managers.contains(manager)){
					managers.add(manager);
				}
				
				
			}else{
				MineManager manager = new MineManager(player);
				manager.addLocation(e.getBlockPlaced().getLocation());
				managers.add(manager);
				mines.put(player.getUniqueId(), manager);
			}
			player.sendMessage(ChatColor.GRAY + "Land Mine placed!");
			
		}
		
	}
	
	
	@EventHandler
	public void onMineStep( PlayerInteractEvent e){
		if(e.getAction() != Action.PHYSICAL)
			return;
		
		if(!ArenaManager.isInArena(e.getPlayer()))
			return;
		
		Player player = e.getPlayer();
		Arena arena = ArenaManager.getArena(player);
		if(arena.getState() != GameState.INGAME)
			return;
		
		final Location stepped = e.getClickedBlock().getLocation();
		
		if(getManager(stepped) == null)
			return;
		
		MineManager man = getManager(stepped);
		
		if(arena.isOnSameTeam(player, man.getOwner()))
			return;
		Bukkit.getScheduler().scheduleSyncDelayedTask(Methods.getPlugin(), new Runnable(){

			@Override
			public void run() {
				stepped.getBlock().setType(Material.AIR);

			}
			
		}, 20 * 2);
		
		//stepped.getWorld().createExplosion(stepped, 2f);
		Methods.createTeamExplosion(player, stepped, 3);
		player.damage(6.0, man.getOwner());
		man.removeLocation(stepped);
		
		man.getOwner().sendMessage(ChatColor.RED + player.getName() + ChatColor.GRAY + " has stepped on your mine!");
		player.sendMessage(ChatColor.GRAY + "You have stepped on one of " + ChatColor.GREEN + man.getOwner().getName() + "'s" + ChatColor.GRAY + " land mines.");
		
		
		
		
	}
	
	
	@EventHandler
	public void onMineBreak(BlockBreakEvent e){
		if(!ArenaManager.isInArena(e.getPlayer()))
			return;
		Player player = e.getPlayer();
		Arena arena = ArenaManager.getArena(player);
		if(arena.getState() != GameState.INGAME)
			return;
		
		Location broke = e.getBlock().getLocation();
		if(getManager(broke) == null)
			return;
		
		MineManager manager = getManager(broke);
		if(arena.isOnSameTeam(player, manager.getOwner())){
			player.sendMessage(ChatColor.GRAY + "This land mine is owned by your teammate "
		+ ChatColor.GOLD + manager.getOwner().getName());
			e.setCancelled(true);
			return;
		}
		e.getBlock().getDrops().clear();
		manager.removeLocation(broke);
		e.getBlock().setType(Material.AIR);
		player.sendMessage(ChatColor.GRAY + "You broke " + ChatColor.GOLD + manager.getOwner().getName() + "'s land mine");
		manager.getOwner().sendMessage(ChatColor.GOLD + player.getName() + ChatColor.GRAY + " Broke one of your land mines!");
		
		
	}
	
	
	@EventHandler
	public void onProjectileThrow(ProjectileLaunchEvent e){
		if(e.getEntity() instanceof Egg){
			if(!(((Egg)e.getEntity()).getShooter() instanceof Player))
				return;
			
			Player shooter = (Player) e.getEntity().getShooter();
			if(!ArenaManager.isInArena(shooter))
				return;
			Arena arena = ArenaManager.getArena(shooter);
			
			if(arena.getState() != GameState.INGAME)
				return;
			
			if(arena.getArenaPlayer(shooter).getType() != ClassType.EXPLOSIVES)
				return;
			
			grenades.add(e.getEntity().getUniqueId());
			
			if(!nadeReplen.containsKey(shooter.getUniqueId()))
			{
				AmmoReplenisher replen = new AmmoReplenisher(shooter,ClassInventories.getExplosiveGrenades(1));
				replen.setDelay(42); 
				replen.start();
				nadeReplen.put(shooter.getUniqueId(), replen);
			}
			
		}
	}
	
	@EventHandler
	public void onGrenadeHit(ProjectileHitEvent e){
		if(!grenades.contains(e.getEntity().getUniqueId()))
			return;
		if(!(e.getEntity().getShooter() instanceof Player))
			return;
		
		Player player = (Player) e.getEntity().getShooter();
		
		//e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), 2f);
		Methods.createTeamExplosion(player, e.getEntity().getLocation(), 3);
		
		grenades.remove(e.getEntity().getUniqueId());
		
		
	}
	
	
	@EventHandler
	public void onC4Explode(PlayerInteractEvent e){
		Player player = e.getPlayer();
		if(!ArenaManager.isInArena(player))
			return;
		
		Arena arena = ArenaManager.getArena(player);
		
		if(arena.getState() != GameState.INGAME)
			return;
		
		if(arena.getArenaPlayer(player).getType() != ClassType.EXPLOSIVES)
			return;
		
		if(e.hasItem()){
			if(e.getItem().equals(ClassInventories.getExplosiveDetonator())){
				if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
					if(BlockPlaceManager.hasBlockPlaceManager(player)){
						if(BlockPlaceManager.getManager(player).getBlocks().size() != 0){
						List<Block> c4 = BlockPlaceManager.getManager(player).getBlocks();
						
						for(Block b : c4){
							Methods.setBlockFast(b.getLocation(), Material.AIR);
							//b.getWorld().createExplosion(b.getLocation(), 2f);
							Methods.createTeamExplosion(player, b.getLocation(), 5, 15);
						}
						
						BlockPlaceManager.getManager(player).getBlocks().clear();;
						player.sendMessage(ChatColor.GRAY + "C4 has been detonated");
						BlockPlaceManager.getList().remove(BlockPlaceManager.getManager(player));
						BlockPlaceManager.removeManager(player);

						}else{
							player.sendMessage(ChatColor.RED + "You have no C4 to blow up!");
						}
					}else{
						player.sendMessage(ChatColor.RED + "You have no C4 to blow up!");
					}
					
				}
			}
			
			if(e.getItem().hasItemMeta())
			{
				if(!e.getItem().getItemMeta().getDisplayName().isEmpty())
				if(ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName()).equalsIgnoreCase("High Frequency Bow"))
				{
					if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK)
					{
						if(c4ArrowManager.containsKey(player.getUniqueId()))
						{
							EntityListManager manager = c4ArrowManager.get(player.getUniqueId());
							if(manager.getIds().size() <= 0)
							{
								e.getPlayer().sendMessage(ChatColor.RED + "You don't have any C4 Arrows to blow up! Shoot some first.");
								return;
							}
							for(Entity ent : manager.getIds())
							{
								if(!ent.isValid())
									continue;
								
								Methods.createTeamFireworkExplosion(player, ent.getLocation(), 5, 8.0); 
								ent.remove();
							}
							manager.getIds().clear();
							
						}else{
							e.getPlayer().sendMessage(ChatColor.RED + "You don't have any C4 Arrows to blow up! Shoot some first.");
						}
					}
				}
			}
			
		}
	}
	
	@EventHandler
	public void stopChickSpawning(CreatureSpawnEvent e)
	{
		if(e.getSpawnReason() == SpawnReason.EGG)
		{
			if(e.getEntityType() == EntityType.CHICKEN)
			{
				e.setCancelled(true);
			}
		}
	}
	
	
	private MineManager getManager(Location loc){
		for(MineManager m:managers){
			if(m.isMine(loc))
				return m;
		}
		return null;
	}
	
	
	
}
