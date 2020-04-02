package me.artish1.CrystalClash.Listeners;

import me.artish1.CrystalClash.Arena.Arena;
import me.artish1.CrystalClash.Arena.ContestPoint;
import me.artish1.CrystalClash.CrystalClash;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.leaderboards.Leaderboard;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class SetupListener implements Listener{
	
	public static ItemStack wand;
	public static HashMap<UUID, SetupType> setupTypes = new HashMap<UUID,SetupType>();
	public static HashMap<ItemStack,Leaderboard> skullSetup = new HashMap<ItemStack, Leaderboard>();

	public static HashSet<ItemStack> filler = new HashSet<ItemStack>();
	
	public static SetupType getSetupType(Player player){
        if (!setupTypes.containsKey(player.getUniqueId())) {
            setupTypes.put(player.getUniqueId(), SetupType.ADD_IMPORTANT_BLOCKS);
        }
        return setupTypes.get(player.getUniqueId());

    }
	
	public static void setupWand(){
		wand = new ItemStack(Material.DIAMOND_SHOVEL);
		ItemMeta meta = wand.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "Setup Wand");  
		wand.setItemMeta(meta);
	}
	

	@EventHandler
	public void onPlace(BlockPlaceEvent e){
		Player player = e.getPlayer();
		if(e.getBlockPlaced().getState() instanceof Skull){
		if(e.getItemInHand().getType().getId() == 397){
			for(ItemStack item : filler){
				if(e.getItemInHand().equals(item)){
					Leaderboard type = skullSetup.get(item);
					type.setSkullLocation(e.getBlockPlaced().getLocation()); 
					player.sendMessage(ChatColor.GRAY + "You have set the head for: " + ChatColor.GREEN + type.getTitle()); 
					break;
				}
			}
		}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		if(!e.hasItem())
			return;
		 
		if(e.getItem().equals(wand)){
			SetupType setup = getSetupType(e.getPlayer());
			Location targetedLoc = e.getPlayer().getTargetBlock( null, 50).getLocation();
			if(setup == SetupType.ADD_IMPORTANT_BLOCKS){
				if(Methods.getImportantBlocks().contains(targetedLoc)){
					CrystalClash.sendMessage(e.getPlayer(), "This block is already flagged as " + ChatColor.GOLD + "Important!");
					return;
				}
				
				Methods.addMultiLocation("ImportantBlocks", targetedLoc);
				Methods.getImportantBlocks().add(targetedLoc);
				CrystalClash.sendMessage(e.getPlayer(), "You have added an " + ChatColor.GOLD + "Important Block");
				return;
			}
			if(setup == SetupType.ADD_BOUNCY_EXPLOSIONS_RED){
				Arena.redTeam.addBouncyExplosionLocation(targetedLoc);
				CrystalClash.sendMessage(e.getPlayer(), "You have added an " + ChatColor.GOLD + "Explosion location " + ChatColor.GRAY + "for Red team.");

				return;
			}
			if(setup == SetupType.ADD_BOUNCY_EXPLOSIONS_BLUE){
				Arena.blueTeam.addBouncyExplosionLocation(targetedLoc);
				CrystalClash.sendMessage(e.getPlayer(), "You have added an " + ChatColor.GOLD + "Explosion location " + ChatColor.GRAY + "for Blue team.");
				return;
			}
			if(setup == SetupType.ADD_DEC_BLOCK_POINT1){
				if(ContestPoint.POINT1.isDecBlock(targetedLoc))
				{
					CrystalClash.sendMessage(e.getPlayer(), "That is already a decorative block for Point 1!");
					return;
				}
				ContestPoint.POINT1.addDecBlock(targetedLoc);
				CrystalClash.sendMessage(e.getPlayer(),"Added a Decorative Changing Block for Point 1 at X: " + targetedLoc.getBlockX() + ", Y: " + targetedLoc.getBlockY() + ", Z: " + targetedLoc.getBlockZ() );
				return;
			}
			if(setup == SetupType.ADD_DEC_BLOCK_POINT2)
			{
				if(ContestPoint.POINT2.isDecBlock(targetedLoc))
				{
					CrystalClash.sendMessage(e.getPlayer(), "That is already a decorative block for Point 2!");
					return;
				}
				ContestPoint.POINT2.addDecBlock(targetedLoc);;
				CrystalClash.sendMessage(e.getPlayer(),"Added a Decorative Changing Block for Point 2 at X: " + targetedLoc.getBlockX() + ", Y: " + targetedLoc.getBlockY() + ", Z: " + targetedLoc.getBlockZ() );
				return;
			}	
			
			if(setup == SetupType.ADD_DEC_BLOCK_POINT3)
			{
				if(ContestPoint.POINT3.isDecBlock(targetedLoc))
				{

					CrystalClash.sendMessage(e.getPlayer(), "That is already a decorative block for Point 3!");
					return;
				}
				ContestPoint.POINT3.addDecBlock(targetedLoc);
				CrystalClash.sendMessage(e.getPlayer(),"Added a Decorative Changing Block for Point 3 at X: " + targetedLoc.getBlockX() + ", Y: " + targetedLoc.getBlockY() + ", Z: " + targetedLoc.getBlockZ() );
				return;
			}
			
			
			
		}
		
	}
	
	
}
