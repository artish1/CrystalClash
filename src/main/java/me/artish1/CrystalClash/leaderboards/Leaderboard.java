package me.artish1.CrystalClash.leaderboards;

import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.Util.MySQLUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;

import java.util.List;
import java.util.UUID;

public enum Leaderboard {
	MOST_KILLS("Most Kills", "Kills","kills"), MOST_DEATHS("Most Deaths", "Deaths","deaths"), MOST_POINTS_TAKEN("Most Pts. Taken", "Points Captured","points_captured"),
	MOST_CRYSTAL_BREAKS("Most C. Breaks", "Crystal Breaks","crystal_breaks"), BEST_KDR("Best KDR", "KDR", "kdr"),
	MOST_WINS("Most Wins","Wins", "wins"), MOST_LOSSES("Most Losses","Losses","losses");
	
	
	private String title;
	private String path;
	private Location signLocation;
	private Location skullLocation;
	private String shortTitle;
	private String MySQLColumn;
	
	
	private Leaderboard(String title, String shortTitle,String mysqlColumn) {
		this.title = title;
		path = title;
		this.shortTitle = shortTitle;
		path.replaceAll("\\s","");
		this.MySQLColumn = mysqlColumn;
	}
	
	
	public String getMySQLColumn() {
		return MySQLColumn;
	}
	
	public void init(){
		signLocation = Methods.getLocation(path + ".SignLocation");
		skullLocation = Methods.getLocation(path + ".SkullLocation");
	}
	
	
	public String getShortTitle() {
		return shortTitle;
	}
	
	
	public void setSkullLocation(Location loc){
		Methods.addLocation(path + ".SkullLocation", loc);
		skullLocation = loc;
	}
	
	
	public Skull getSkull(){
		if(isSkullLocation(getSkullLocation())){
			BlockState bs = getSkullLocation().getBlock().getState();
			Skull skull = (Skull) bs;
			return skull;
		}
		return null;
	}
	
	private boolean isSkullLocation(Location loc){
		if(getSkullLocation() == null)
			return false;
		
		if(getSkullLocation().getBlock().getLocation().equals(loc.getBlock().getLocation())){
            return loc.getBlock().getType() == Material.PLAYER_WALL_HEAD || loc.getBlock().getType() == Material.PLAYER_HEAD;
        }
		return false;
	}
	
	public Location getSkullLocation(){
		return skullLocation;
	}
	
	public void setSignLocation(Location loc){
		Methods.addLocation(path + ".SignLocation", loc); 
		signLocation = loc;
	}
	
	public static void updateAllSigns(){
		for(Leaderboard board : Leaderboard.values()){
			board.updateSign();
		}
	}
	
	public Location getSignLocation() {
		return signLocation;
	}
	
	
	public void removeSignLocation(){
		Methods.removeLocation(path + ".SignLocation"); 
		signLocation = null;
	}
	
	
	public boolean isLeaderboardSign(Location loc){
		
		if(loc.getBlock().getState() instanceof Sign){
				if(loc.getBlock().getLocation().equals(getSignLocation().getBlock().getLocation()))
				return true;
			
			
		}
		
		
		return false;
	}
	
	
	public UUID getTopPlayer(){
		List<String> uuids = MySQLUtil.getUUIDList();
		UUID topPlayer = UUID.fromString("c3ce322d-5c9e-4a6b-9fa5-67d34d7ed5fa");
		int topScore = 0;
		
		for(String s : uuids){
			double score = MySQLUtil.getScore(UUID.fromString(s), this);
			if(score > topScore){
				topScore = (int) score; 
				topPlayer = UUID.fromString(s);
			}
		}
		
		return topPlayer;
	}
	
	public void updateSign(){
		if(getSignLocation() == null){
			return;
		}
		
		
		if(isLeaderboardSign(getSignLocation())){
			Sign sign = (Sign) getSignLocation().getBlock().getState();
			
			sign.setLine(0, title);
			OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(getTopPlayer());
			sign.setLine(1, oPlayer.getName());
			if(this == Leaderboard.BEST_KDR){
				sign.setLine(2, "Score: "  + "Unfinished");  
			}else{
				sign.setLine(2, "Score: "  + MySQLUtil.getScore(oPlayer.getUniqueId(), this));  
			}
			sign.update(true);
			
			Skull skull = getSkull();
			if(skull == null){
				return;
			}
			skull.setOwner(oPlayer.getName());
			skull.update(true);
			
		}
		
		
		
	}
	
	
	public String getTitle() {
		return title;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	
	public int getScore(UUID p){
		FileConfiguration players = Methods.getPlugin().players;
		if(players.contains(path + "." + p + ".Score")){
			return players.getInt(path + "." + p + ".Score");
		}else{
			return 0;
		}
	}
	public int getScore(Player p){
		FileConfiguration players = Methods.getPlugin().players;
		if(players.contains(path + "." + p.getUniqueId() + ".Score")){
			return players.getInt(path + "." + p.getUniqueId() + ".Score");
		}else{
			return 0;
		}
	}
	
	
	public double getScoreDouble(UUID p){
		FileConfiguration players = Methods.getPlugin().players;
		if(players.contains(path + "." + p+ ".Score")){
			return players.getDouble(path + "." + p + ".Score");
			
		}else{
			return 0;
		}
	}
	
	private void addEntry(UUID id){
		if(getEntries() != null){
			List<String> lsit = getEntries();
			lsit.add(id.toString());
			Methods.getPlugin().players.set(path + ".Entries", lsit);
		}else{
			List<String> list = new ArrayList<String>();
			list.add(id.toString());
			Methods.getPlugin().players.set(path + ".Entries", list); 
		}
		Methods.saveYamls();
	}
	
	public List<String> getEntries(){
			return (List<String>) Methods.getPlugin().players.getStringList(path +".Entries");
	}
	
	private void inputValue(Player p, int score){ 
		FileConfiguration players = Methods.getPlugin().players;
		if(players.contains(path + "." + p.getUniqueId()) || players.contains(path + "." + p.getUniqueId() + ".Score")){
			players.set(path + "." + p.getUniqueId() + ".Score", score); 
			if(players.getString(path + "." + p.getUniqueId() + ".Name") != p.getName()){
				players.set(path + "." + p.getUniqueId() + ".Name", p.getName());
			}
		}else{
			players.set(path + "." + p.getUniqueId() + ".Score", score);  
			players.set(path + "." + p.getUniqueId() + ".Name", p.getName()); 
			addEntry(p.getUniqueId());
		}
		Methods.saveYamls();
	}
	private void inputValue(UUID p, double score){ 
		FileConfiguration players = Methods.getPlugin().players;
		if(players.contains(path + "." + p) || players.contains(path + "." + p + ".Score")){
			players.set(path + "." + p + ".Score", score); 
			if(players.getString(path + "." + p + ".Name") != Bukkit.getOfflinePlayer(p).getName()){
				players.set(path + "." + p + ".Name", Bukkit.getOfflinePlayer(p).getName());
			}
		}else{
			players.set(path + "." + p+ ".Score", score);  
			players.set(path + "." + p + ".Name", Bukkit.getOfflinePlayer(p).getName()); 
			addEntry(p);
		}
		Methods.saveYamls();
	}
	public void setScore(UUID p, double score){
		inputValue(p, score);
	}
	
	
	public void addScore(Player p){
		inputValue(p, getScore(p.getUniqueId()) + 1);
	}
	*/
	
}
