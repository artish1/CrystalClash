package me.artish1.CrystalClash.Arena;

import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.killstreaks.Killstreak;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArenaPlayer {
	private UUID player;
	private String name;
	private ClassType type;
	private int money = 0;
	private int killStreak = 0;
	private ArenaTeam team;
	private int kills = 0;
	private int cpoints =0;
	
	private List<Killstreak> avaliable = new ArrayList<Killstreak>();
	
	
	private boolean zoom = false; 
	private int zoomCounter = 0;
	
	private List<ItemStack> bought = new ArrayList<ItemStack>();
	
	public ArenaPlayer(Player player) {
		this.player = player.getUniqueId();
		this.name = player.getName();
		type = ClassType.SCOUT;
	}
	public UUID getUUID(){
		return player;
	}
	
	public void setTeam(ArenaTeam team) {
		this.team = team;
	}
	
	
	public boolean isRed()
	{
		return team.getName().equalsIgnoreCase("Red");
	}
	
	public boolean isBlue()
	{
		return team.getName().equalsIgnoreCase("Blue");
	}
	
	public ArenaTeam getTeam() {
		return team;
	}
	
	public int getKills() {
		return kills;
	}
	
	public int getAwardPoints(){
		int points = 0;
		points += (kills/2);
		points += (cpoints);
		return points; 
	}
	
	
	public int getCpoints() {
		return cpoints;
	}
	
	public void addKill(){
		kills++;
	}
	public void addCPoint(){
		cpoints++;
	}
	
	
	
	public void increaseKillstreak(){
		killStreak++;
		
		for(Killstreak ks : Killstreak.getKillstreaks()){
			if(ks.getKillsNeeded() == killStreak){
				if(avaliable.contains(ks))
					return;
				avaliable.add(ks);
				bought.add(ks.getItem());
				getPlayer().getInventory().addItem(ks.getItem());
				getPlayer().sendMessage(ChatColor.GRAY + "You have just activated a secret Killstreak: " + ks.getName());
				
			}
		}
	}
	
	public void resetKillstreak(){
		killStreak = 0;
	}
	
	public int getKillStreak() {
		return killStreak;
	}
	
	public List<Killstreak> getAvaliable() {
		return avaliable;
	}
	
	
	
	public boolean isZoomed() {
		return zoom;
	}
	
	public void toggleZoom(){
		if(zoom){
			getPlayer().removePotionEffect(PotionEffectType.SLOW);
			
			zoom = false;
		}else{
			
			getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999999, 20), true); 
			
			zoom = true;
		}
		
	}
	
	public void zoomIn(){
		getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999999, 99), true); 
		//getPlayer().setWalkSpeed(-0.15F);
		zoom = true;
	}
	
	public void zoomOut(){
		getPlayer().removePotionEffect(PotionEffectType.SLOW);
		//getPlayer().setWalkSpeed(0.2f);
		zoom = false;
	}

	
	public void upZoom(){
		zoomCounter++;
		zoom = true;
		switch(zoomCounter){
		case 1:
			getPlayer().removePotionEffect(PotionEffectType.SLOW);
			 
			if(zoom){
				zoom = false;
			}
			//normal 
			break;
		case 2:
			getPlayer().removePotionEffect(PotionEffectType.SLOW);
			getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 999999), true); 
			
			
			//small
			break;
		case 3:
			getPlayer().removePotionEffect(PotionEffectType.SLOW);
			getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 999999), true); 
			
			
			//middle
			break;
		case 4:
			getPlayer().removePotionEffect(PotionEffectType.SLOW);
			getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 999999), true); 
			
			zoomCounter = 0;
			//max
			break;
		}
		
	}
	
	
	
	public void downZoom(){
		
	}
	
	public void sendMessage(String message){
		getPlayer().sendMessage(ChatColor.GRAY + message);
	}
	
	public String getName() {
		return name;
	}
	
	
	public List<ItemStack> getBought() {
		return bought;
	}
	
	
	
	public int getMoney() {
		return money;
	}
	
	public void addMoney(int amount){
		money += amount;
		getPlayer().setLevel(money);
		Methods.sendActionBar(getPlayer(),ChatColor.GRAY + "You have earned " + ChatColor.GOLD + amount + ChatColor.GRAY + " gold.");
	//	getPlayer().sendMessage(ChatColor.GRAY + "You have earned " + ChatColor.GOLD + amount);

	}
	
	public void subtractMoney(int amount){
		money -= amount;
		getPlayer().setLevel(getMoney());
	}
	
	public boolean hasEnough(int amount){
		return getMoney() >= amount;
	}
	
	
	
	public Player getPlayer() {
		return Bukkit.getPlayer(player);
	}
	
	public ClassType getType() {
		return type;
	}
	
	public void setType(ClassType type) {
		this.type = type;
	}
	
	
}
