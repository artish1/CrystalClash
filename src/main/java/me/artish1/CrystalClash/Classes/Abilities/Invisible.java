package me.artish1.CrystalClash.Classes.Abilities;

import me.artish1.CrystalClash.Listeners.GameListener;
import me.artish1.CrystalClash.Listeners.Classes.AssassinListener;
import me.artish1.CrystalClash.other.ClassInventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Invisible extends Ability{

	public Invisible(Player player) {
		super("Invisibility", plugin.kits.getInt("Assassin.Invisible.Cooldown"), ClassInventories.getAssassinInvis(), player);
	}

	public int getDuration(){
		return plugin.kits.getInt("Assassin.Invisible.Duration");
	}
	private int id;
	@Override
	public void onCast() {
		
		for(Player p:Bukkit.getOnlinePlayers()){
			if(!arena.isOnSameTeam(p, player)){
				p.hidePlayer(player);
			}
		}
		
		player.sendMessage(ChatColor.GRAY + "You are now " + ChatColor.GREEN + "Invisible" + ChatColor.GRAY + ".");
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, getDuration(),1,true,false));
		
		id = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			public void run() {
				if(GameListener.respawnQueue.contains(player.getUniqueId()))
					return;
				
				for(Player p : Bukkit.getOnlinePlayers()){
					if(!p.canSee(player)){
						p.showPlayer(player);
					}
				}
			
				player.sendMessage(ChatColor.RED + "Invisibility has run out!");
				AssassinListener.invis.remove(player.getUniqueId());
				for(PotionEffect type : player.getActivePotionEffects())
				{
					if(type.getType()== PotionEffectType.INVISIBILITY)
					{
						player.removePotionEffect(PotionEffectType.INVISIBILITY);
					}
				}
			}
			
		}, 20 * getDuration());
	}
	
	public void stopEarly(){
		Bukkit.getScheduler().cancelTask(id);
		
		for(Player p : Bukkit.getOnlinePlayers()){
			p.showPlayer(player);
		}
		
		player.sendMessage(ChatColor.GRAY + "You are visible again.");
		for(PotionEffect type : player.getActivePotionEffects())
		{
			if(type.getType()== PotionEffectType.INVISIBILITY)
			{
				player.removePotionEffect(PotionEffectType.INVISIBILITY);
			}
		}
		AssassinListener.invis.remove(player.getUniqueId());
	}
	
	
}
