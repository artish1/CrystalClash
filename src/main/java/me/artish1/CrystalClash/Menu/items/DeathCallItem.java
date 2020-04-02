package me.artish1.CrystalClash.Menu.items;

import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.Menu.menus.DeathCallMenu;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.entities.CustomEntityType;
import me.artish1.CrystalClash.entities.EntityDeath;
import me.artish1.CrystalClash.killstreaks.DeathCall;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class DeathCallItem extends MenuItem{
	private Player player;
	public DeathCallItem(Player player) {
		super(ChatColor.AQUA + "Call death upon " +ChatColor.YELLOW +  ChatColor.BOLD + player.getName(), Methods.getPlayerHead(player)); 
		this.player = player;
	}
	
	
	@Override
	public void onItemClicked(ItemClickedEvent e) {
		if(e.getMenu() instanceof DeathCallMenu){
			DeathCallMenu menu = (DeathCallMenu) e.getMenu();
			EntityDeath death = (EntityDeath) CustomEntityType.ENTITY_DEATH.spawn(player.getLocation());
			death.setOwner(e.getPlayer());
			death.target = ((CraftPlayer) player).getHandle();
			
			menu.getDeathCall().removeKillstreak(e.getPlayer()); 
			menu.fulfilled = true;
			e.setClose(true);
			DeathCall.sendDeathMessage(e.getPlayer(), "On my way...");
			DeathCall.sendDeathMessage(player, "PREPARE FOR THE GRAVE " + ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString()  +player.getName());
			player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1f, 1f);

			player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
		}
	}
	
	
	
}
