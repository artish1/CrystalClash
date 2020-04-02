package me.artish1.CrystalClash.Listeners;

import me.artish1.CrystalClash.Arena.GameState;
import me.artish1.CrystalClash.CrystalClash;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static me.artish1.CrystalClash.Arena.Arena.blueTeam;
import static me.artish1.CrystalClash.Arena.Arena.redTeam;

public class ChatListener implements Listener{
		
	CrystalClash plugin;
	
	public final String TEAM_PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "TeamChat" + ChatColor.DARK_GRAY + "] ";
	
	public ChatListener(CrystalClash plugin) {
		this.plugin = plugin;
	}
	
	private void sendAll(String message){
		for(Player p : Bukkit.getOnlinePlayers()){
			p.sendMessage(message);
		}
	}
	private HashSet<UUID> filteredPlayers;
	@EventHandler
	public void testChat(AsyncPlayerChatEvent e)
	{
		List<String> list = plugin.getConfig().getStringList("List");
		String original = e.getMessage();
		boolean bad = false;
		for(String word : original.split(" "))
		{
			if(list.contains(word))
			{
				//Bad word detected.
				bad = true;
				break;
			}
		}
		List<Player> toRemove = new ArrayList<Player>();
		if(bad)
		{
			for(Player p : e.getRecipients())
			{
				//Acquiring all players who need their chat filtered in the toRemove list.
				if(filteredPlayers.contains(p.getUniqueId()))
				{
					//A man who just wants peace and quiet.
					toRemove.add(p);
					
				}
			}
			for(String badword : list)
			{
				if(original.contains(badword))
				{
					String filteredWORD= "";
					for(int i = badword.length(); i>0; i--)
					{
						filteredWORD += "*";
					}
					original.replaceAll(badword, filteredWORD);
				}
			}
			for(Player p : toRemove)
			{
				e.getRecipients().remove(p);
				String formattedMessage = String.format(e.getFormat(), new Object[] { e.getPlayer().getDisplayName(), original});
				p.sendMessage(formattedMessage);
			}
			
		}
		
	}
	
	
	private void sendRegularMessage(Player p,String message){
		String total =  ChatColor.GRAY + p.getName()+ ": " + message;
		sendAll(total);
	}
	
	private void sendWinnerMessage(Player p,String message){
		String total = ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "Winner" + ChatColor.DARK_GRAY + "] " + ChatColor.GREEN + p.getName() + ChatColor.WHITE + ": " +message;
		sendAll(total);
	}
	
	
	private void sendLoserMessage(Player p, String message){
		String total = ChatColor.DARK_GRAY + "[" + ChatColor.RED + "Loser" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + p.getName() + ChatColor.GRAY +": " + message;
		sendAll(total);
	}
	
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		Player player = e.getPlayer();
		if(plugin.getArena().getState() == GameState.INGAME){
			e.setCancelled(true);
			if(plugin.getArena().isBlue(player)){
				plugin.getArena().sendBlueMessage(TEAM_PREFIX + ChatColor.BLUE + player.getName() +ChatColor.DARK_GRAY +  ":" + ChatColor.GRAY + " " +  e.getMessage());
			}
			
			if(plugin.getArena().isRed(player)){
				plugin.getArena().sendRedMessage(TEAM_PREFIX + ChatColor.RED + player.getName() +ChatColor.DARK_GRAY +   ":" + ChatColor.GRAY + " " +  e.getMessage());
			}
		}else{
			if(plugin.getArena().getState() == GameState.STOPPING){
				
				
				e.setCancelled(true);
				
				if(plugin.getArena().getWinner() == blueTeam){
					if(plugin.getArena().isBlue(player)){
						sendWinnerMessage(player, e.getMessage());
					}else{
						sendLoserMessage(player, e.getMessage());
					}
					
				}
				
				
				if(plugin.getArena().getWinner() == redTeam){
					if(plugin.getArena().isRed(player)){
						sendWinnerMessage(player, e.getMessage());
					}else{
						sendLoserMessage(player, e.getMessage());
					}
				}
				
				
			}else{
				if(plugin.getArena().getState() == GameState.LOBBY || plugin.getArena().getState() == GameState.STARTING){
					sendRegularMessage(player, e.getMessage());
					e.setCancelled(true);
				}
			}
		}
		
	}
	
	
	
}
