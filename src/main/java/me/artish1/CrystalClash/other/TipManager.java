package me.artish1.CrystalClash.other;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.artish1.CrystalClash.CrystalClash;
import me.artish1.CrystalClash.Arena.ArenaPlayer;
import me.artish1.CrystalClash.Arena.GameState;
import me.artish1.CrystalClash.Classes.ClassType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class TipManager {
	
	static CrystalClash plugin;
	
	private static List<String> sniperTips = new ArrayList<String>();
	private static List<String> archerTips = new ArrayList<String>();
	private static List<String> scoutTips = new ArrayList<String>();
	private static List<String> mageTips = new ArrayList<String>();
	private static List<String> enderTips = new ArrayList<String>();
	private static List<String> tankTips = new ArrayList<String>();
	private static List<String> assassinTips = new ArrayList<String>();
	private static List<String> earthTips = new ArrayList<String>();
	private static List<String> explosiveTips = new ArrayList<String>();
	private static List<String> engineerTips = new ArrayList<String>();
	private static List<String> guardianTips = new ArrayList<String>();
	private static List<String> necromancerTips = new ArrayList<String>();
	private static List<String> spiderTips = new ArrayList<String>();

	
	private static List<String> gameTips = new ArrayList<String>();
	
	public static List<String> getEngineerTips() {
		return engineerTips;
	}
	
	public TipManager(CrystalClash plugin) {
		TipManager.plugin = plugin;
		
		
		addTips();
		
	}
	public static List<String> getGuardianTips(){
		return guardianTips;
	}
	
	public static List<String> getSpiderTips() {
		return spiderTips;
	}
	
	public static List<String> getGameTips() {
		return gameTips;
	}
	public static List<String> getArcherTips() {
		return archerTips;
	}
	public static List<String> getAssassinTips() {
		return assassinTips;
	}
	public static List<String> getEarthTips() {
		return earthTips;
	}
	public static List<String> getEnderTips() {
		return enderTips;
	}
	public static List<String> getExplosiveTips() {
		return explosiveTips;
	}
	public static List<String> getMageTips() {
		return mageTips;
	}
	public static CrystalClash getPlugin() {
		return plugin;
	}
	public static List<String> getScoutTips() {
		return scoutTips;
	}
	
	public static List<String> getSniperTips() {
		return sniperTips;
	}
	public static List<String> getTankTips() {
		return tankTips;
	}
	
	private static void addTips(){
		sniperTips.add("Have your sniper rifle in your hand and crouch to zoom in!");
		sniperTips.add("Place down a claymore that will blow up if an enemy is in range. Damaging them for a hefty amount.");
		scoutTips.add("Scouts are immune to fall damage! This counters the tank's ability, 'Ground Smash', where it sends you flying up to die when you fall down!");
		scoutTips.add("Right click your sword to 'Dash'!");
		scoutTips.add("It's faster to stay on the ground and run then to jump and run for scouts.");
		archerTips.add("Left click your bow to cycle through different modes!");
		mageTips.add("The Meteor Shower spell takes along time for the meteors to reach their destination! Think"
				+ " carefully when casting it!");
		enderTips.add("After casting teleport, you are immune to fall damage for 2.5 Seconds. Use it wisely (Tank's 'Ground Smash' Ability).");
		tankTips.add("When casting ground smash, note that only players that are completely on ground will get sent flying up.");
		assassinTips.add("Interact with your gold clock to toggle invisibility!");
		earthTips.add("Certain blocks cannot be picked up and used for the ability, 'Block Throw'. Only earth-type blocks.");
		explosiveTips.add("Place C4(Sponge) and use the detonator(Emerald) to blow them up!");
		explosiveTips.add("Placing more C4 together won't increase its damage, spread them out."); 
		engineerTips.add("After dieing, your turret will be removed and you'll need to place down another one.");
		
		necromancerTips.add("You can only have 2 summons at a time.");
		necromancerTips.add("Careful, your summons will despawn if you die!");  
		guardianTips.add("Right click your sword to cast 'Holy Smite'!");
		guardianTips.add("You automatically Regenerate your health faster than other classes");
		
		//-----------------------------
		
		gameTips.add("Your team gets 2 points for each AreaPoint captured every 10 seconds.");
		gameTips.add("Break the enemy crystal multiple times to deplete their CrystalHP, once down to 0, they lose.");
		gameTips.add("Stay in a capture point to help capture it for your team!"); 
		
		
	}
	
	static int taskId;
	
	
	public static List<String> getNecromancerTips() {
		return necromancerTips;
	}
	
	public static String getRandomTip(List<String> list){
		Random r = new Random();
		int rnum = r.nextInt(list.size());
		return list.get(rnum);
	}
	
	public static void sendTip(Player player, String tip){
		String total = ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "Tip" + ChatColor.DARK_GRAY + "] " +ChatColor.GRAY + tip;
		player.sendMessage(total);
	}
	
	public static void sendClassTip(String tip, ClassType type){
		for(ArenaPlayer ap : plugin.getArena().getRedPlayers()){
			if(ap.getType() != type)
				continue;
			
			sendTip(ap.getPlayer(), tip);
		}
		for(ArenaPlayer ap : plugin.getArena().getBluePlayers()){
			if(ap.getType() != type)
				continue;
			
			sendTip(ap.getPlayer(), tip);
		}
	}
	

	static int allTaskId;
	public static void startAllBroadcast(){
		allTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){

			@Override
			public void run() {
				if(plugin.getArena().getState() != GameState.INGAME){
					Bukkit.getScheduler().cancelTask(allTaskId);
					return;
				}
				
				for(Player p : Bukkit.getOnlinePlayers()){
					sendTip(p, getRandomTip(gameTips));
				}
			}
			
		}, 20 * 20, 20 * 60 * 2);
	}
	
	public static void startAutomaticBroadcast(){
		taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){

			@Override
			public void run() {
				if(plugin.getArena().getState() != GameState.INGAME){
					Bukkit.getScheduler().cancelTask(taskId);
					return;
				}
				
				for(ClassType ct : ClassType.values()){
					if(ct.getTipsList().size() > 0)
					sendClassTip(getRandomTip(ct.getTipsList()), ct);
				}
				
			}
			
		}, 20 * 40, 20 * 60);
	}
	
	
	
	
}
