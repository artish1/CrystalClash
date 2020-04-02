package me.artish1.CrystalClash.Menu.items;

import me.artish1.CrystalClash.Arena.Arena;
import me.artish1.CrystalClash.Arena.GameState;
import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.events.PlayerJoinTeamQueueEvent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import static me.artish1.CrystalClash.Arena.Arena.blueTeam;
import static me.artish1.CrystalClash.Arena.Arena.redTeam;

public class BluePickItem extends MenuItem{
	
	public BluePickItem() {
		super(ChatColor.GRAY + "Pick " + ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + "Blue " + ChatColor.GRAY + "ArenaTeam",
				getPickItem());
		
		addLore("Click to join");
		addLore("the blue team!");
		addLore("Etc");
		
	}
	private static ItemStack getPickItem()
	{
		ItemStack item = new ItemStack(Material.BLUE_BANNER);
		BannerMeta meta = (BannerMeta)item.getItemMeta();
		meta.setBaseColor(DyeColor.BLUE);
		item.setItemMeta(meta);
		return item;
	}
	
	
	@Override
	public ItemStack getIcon() {
		if(Methods.getArena().getState() == GameState.LOBBY || Methods.getArena().getState() == GameState.STARTING){
			getLore().set(2, ChatColor.GRAY + "Queued Players: " + ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + 
					blueTeam.getQueue().size());
		}else{
			getLore().set(2, ChatColor.GRAY + "Players: " + ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + 
					blueTeam.getPlayerList().size());
		}
		build();
		return icon;
	}
	
	@Override
	public void onItemClicked(ItemClickedEvent e) {
		Arena arena = Methods.getArena();
		e.setUpdate(true);
		if(arena.getState() == GameState.INGAME)
		{
			if(blueTeam.getPlayerList().size() > redTeam.getPlayerList().size())
			{
				e.getPlayer().sendMessage(ChatColor.RED + "The blue team has more players than the red team! Try joining the red team or wait until"
						+ " the teams are more balanced out.");
				return;
			} 
			
			arena.addBlue(arena.getArenaPlayer(e.getPlayer()));
			arena.setInventory(e.getPlayer());
			arena.setPassivePotionEffect(e.getPlayer());
			e.getPlayer().teleport(arena.getBlueSpawn());
			return;
		}
		if(arena.getState() == GameState.LOBBY || arena.getState() == GameState.STARTING)
		{
			PlayerJoinTeamQueueEvent event = new PlayerJoinTeamQueueEvent(arena.getArenaPlayer(e.getPlayer()),arena,blueTeam);
			Bukkit.getPluginManager().callEvent(event);
			
			if(event.isCancelled())
				return;
			
			if(!blueTeam.getQueue().contains(arena.getArenaPlayer(e.getPlayer()))){
				blueTeam.getQueue().add(arena.getArenaPlayer(e.getPlayer()));
				e.getPlayer().sendMessage(ChatColor.GRAY + "You have been added to the " + ChatColor.BLUE  + "Blue" + ChatColor.GRAY + " Queue.");

			}else{
				e.getPlayer().sendMessage(ChatColor.GRAY+ "You are already in the Blue team's Queue!");
			}
			if(redTeam.getQueue().contains(arena.getArenaPlayer(e.getPlayer()))){
				redTeam.getQueue().remove(arena.getArenaPlayer(e.getPlayer()));
			}
			BannerMeta meta = (BannerMeta)e.getPlayer().getInventory().getItemInHand().getItemMeta();
			meta.setBaseColor(DyeColor.BLUE);
			e.getPlayer().getInventory().getItemInHand().setItemMeta(meta);
		}
	}
	
	
}
