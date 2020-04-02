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

public class RedPickItem extends MenuItem{

	public RedPickItem() {
		super(ChatColor.GRAY + "Pick " + ChatColor.RED.toString() + ChatColor.BOLD.toString() + "Red " + ChatColor.GRAY + "ArenaTeam",
				getPickItem());
		
		addLore("Click to join");
		addLore("the red team!");
		addLore("Etc.");
		
	}

	private static ItemStack getPickItem()
	{
		ItemStack item = new ItemStack(Material.RED_BANNER);
		BannerMeta meta = (BannerMeta)item.getItemMeta();
		meta.setBaseColor(DyeColor.RED);
		item.setItemMeta(meta);
		return item;
	}
	
	@Override
	public ItemStack getIcon() {
		if(Methods.getArena().getState() == GameState.LOBBY || Methods.getArena().getState() == GameState.STARTING){
			getLore().set(2, ChatColor.GRAY + "Queued Players: " + ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + 
					Arena.redTeam.getQueue().size());
		}else{
			getLore().set(2, ChatColor.GRAY + "Players: " + ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + 
					Arena.redTeam.getPlayerList().size());
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
			if(Arena.redTeam.getPlayerList().size() > Arena.blueTeam.getPlayerList().size())
			{
				e.getPlayer().sendMessage(ChatColor.RED + "The red team has more players than the blue team! Try joining the blue team or wait until"
						+ " the teams are more balanced out.");
				return;
			} 
			
			arena.addBlue(arena.getArenaPlayer(e.getPlayer()));
			arena.setInventory(e.getPlayer());
			arena.setPassivePotionEffect(e.getPlayer());
			e.getPlayer().teleport(arena.getRedSpawn());
			return;
		}
		
		if(arena.getState() == GameState.LOBBY || arena.getState() == GameState.STARTING)
		{
			PlayerJoinTeamQueueEvent event = new PlayerJoinTeamQueueEvent(arena.getArenaPlayer(e.getPlayer()),arena,Arena.redTeam);
			Bukkit.getPluginManager().callEvent(event);
			if(event.isCancelled())
				return;
			if(!Arena.redTeam.getQueue().contains(arena.getArenaPlayer(e.getPlayer()))){
				Arena.redTeam.getQueue().add(arena.getArenaPlayer(e.getPlayer()));
				e.getPlayer().sendMessage(ChatColor.GRAY + "You have been added to the " + ChatColor.RED  + "Red" + ChatColor.GRAY + " Queue.");
			}else{
				e.getPlayer().sendMessage(ChatColor.GRAY+ "You are already in the Red team's Queue!");
			}
			if(Arena.blueTeam.getQueue().contains(arena.getArenaPlayer(e.getPlayer()))){
				Arena.blueTeam.getQueue().remove(arena.getArenaPlayer(e.getPlayer()));
			}			BannerMeta meta = (BannerMeta)e.getPlayer().getInventory().getItemInHand().getItemMeta();
			meta.setBaseColor(DyeColor.RED); 
			e.getPlayer().getInventory().getItemInHand().setItemMeta(meta);
		}
		
	}
}
