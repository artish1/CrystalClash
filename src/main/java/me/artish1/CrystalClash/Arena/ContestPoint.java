package me.artish1.CrystalClash.Arena;

import me.artish1.CrystalClash.Listeners.GameListener;
import me.artish1.CrystalClash.Util.Cuboid;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.Util.MySQLUtil;
import me.artish1.CrystalClash.leaderboards.Leaderboard;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public enum ContestPoint {
	POINT1("Point1",1),POINT2("Point2",2),POINT3("Point3",3);
	
	
	private String name;
	private int value;
	private int secondsToChange = 8;
    private Point point;
    private List<Location> decBlocks = new ArrayList<Location>();

	private ContestPoint(String name,int value) {
		this.name = name;
		this.value = value;
		this.point = Point.NEUTRAL;
	}




	
	public int getValue() {
		return value;
	}
	
	private Random r = new Random();

	public void setup(){
		decBlocks = getMultiLocation(Integer.toString(getValue()) + ".Dec");
	}
	 
	public void addDecBlock(Location loc){
		addMultiLocation(Integer.toString(getValue()) + ".Dec", loc);
		decBlocks.add(loc);
	}
	
	public boolean isDecBlock(Location loc){
		if(decBlocks.contains(loc))
			return true;
		return false;
		
	}
	
	public List<Location> getDecBlocks(){
		return decBlocks;
	}
	

	
	public Point getPoint() {
		return point;
	}
	
	public void setPoint(Point point){
		this.point = point;
	}
	
	public void setPointBlock(Location loc){
		addSingleLocation(Integer.toString(getValue()),loc);
		
	}

	

	private void turnBlocksTo(ArenaTeam type, HashSet<ArenaPlayer> list){
		List<Location> toChange = new ArrayList<Location>();
		for(Location loc : getDecBlocks()){
		    if(type.getName().equalsIgnoreCase("Blue")){
                if(loc.getBlock().getType() != Material.BLUE_WOOL){
                    toChange.add(loc);
                }
            } else {
                if(type.getName().equalsIgnoreCase("Red")){
                    if(loc.getBlock().getType() != Material.RED_WOOL){
                        toChange.add(loc);
                    }
                }
            }

		}



        if(toChange.size() <= 0){
            if(point== type.getPoint())
                return;


            point = type.getPoint();

            getPointBlock().getBlock().setType(type.getMaterialType());
            for(ArenaPlayer ap : list){
                MySQLUtil.incrementScore(ap.getPlayer().getUniqueId(), Leaderboard.MOST_POINTS_TAKEN);
                ap.addCPoint();
            }
            //updateScoreboard();
            type.awardAllPoints(15);


            if(this == ContestPoint.POINT1){



                Methods.getArena().setPoint1(point);
            }
            if(this == ContestPoint.POINT2){
                Methods.getArena().setPoint2(point);
            }
            if(this == ContestPoint.POINT3){
                Methods.getArena().setPoint3(point);
            }




        }


        int multiplier = getDecBlocks().size() / secondsToChange;

		for(double x = multiplier; x > 0; x--){

		    if(toChange.isEmpty())
		        break;

			int randomIndex = r.nextInt(toChange.size());



			toChange.get(randomIndex).getBlock().setType(type.getMaterialType());
			toChange.remove(randomIndex);
		}
		


	}
	
	
	private boolean hasOvercome(int defenderPlayers, int attackPlayers){
		if(defenderPlayers >= attackPlayers)
			return false;
		return true;
	}
	
	public void stopTask(){
		Bukkit.getScheduler().cancelTask(id);
	}
	/*
	private void updateScoreboard(){
		int placeholder = Methods.getArena().getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(
				ChatColor.GOLD.toString()+ChatColor.BOLD.toString() + "P"+Integer.toString(getValue()) + ": " + point.toString()).getScore();
		Methods.getArena().getScoreboard().resetScores(ChatColor.GOLD.toString() +ChatColor.BOLD.toString() + "P"+Integer.toString(getValue())+": " +point.toString());
		
		
		
		Methods.getArena().getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(ChatColor.GOLD.toString() +ChatColor.BOLD.toString() + "P"+Integer.toString(getValue())+": " + point.toString()).setScore(placeholder);
		
	}
	*/
	private int id;
	public void startTask(){
		if(!Methods.getPlugin().getConfig().getBoolean("AutoCapture"))
			return;

		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Methods.getPlugin(), new Runnable(){

			public void run() {

                HashSet<ArenaPlayer> redPlayers = new HashSet<ArenaPlayer>();
                HashSet<ArenaPlayer> bluePlayers = new HashSet<ArenaPlayer>();

				switch(getPoint()){
				case BLUE:
					for(ArenaPlayer ap : Methods.getPlugin().getArena().getRedPlayers()){
						if(!getCaptureArea().contains(ap.getPlayer().getLocation()))
							continue;
						
						if(GameListener.respawnQueue.contains(ap.getPlayer().getUniqueId()))
							continue;
						
						
						redPlayers.add(ap);
					}
					
					for(ArenaPlayer ap : Methods.getPlugin().getArena().getBluePlayers()){
						if(!getCaptureArea().contains(ap.getPlayer().getLocation()))
							continue;
						
						if(GameListener.respawnQueue.contains(ap.getPlayer().getUniqueId()))
							continue;
						
						bluePlayers.add(ap);
					}
					
					
					//count blue players IF there is anyone contesting
					if(redPlayers.size() > 0 ){
						if(hasOvercome(bluePlayers.size(), redPlayers.size())){
							 
							turnBlocksTo(Arena.redTeam, redPlayers);
							
						}
					}
					if(bluePlayers.size() > redPlayers.size()){
						
						turnBlocksTo(Arena.blueTeam, bluePlayers);
					}

					
					
					redPlayers.clear();
					bluePlayers.clear();
					
					break;
				case NEUTRAL:
					for(ArenaPlayer ap : Methods.getPlugin().getArena().getBluePlayers()){
						if(!getCaptureArea().contains(ap.getPlayer().getLocation()))
							continue;
						
						if(GameListener.respawnQueue.contains(ap.getPlayer().getUniqueId()))
							continue;
						
							bluePlayers.add(ap);
					}

					for(ArenaPlayer apr : Methods.getPlugin().getArena().getRedPlayers()){
						if(!getCaptureArea().contains(apr.getPlayer().getLocation()))
							continue;
						
						if(GameListener.respawnQueue.contains(apr.getPlayer().getUniqueId()))
							continue;
						
							redPlayers.add(apr);
					}


					//We now have all the players in the game detected in the points if they are in the cuboid regions



                    //Don't do anything if it's a stalemate
					if(redPlayers.size() == bluePlayers.size()){
						break;
					}



					if(redPlayers.size() > bluePlayers.size()){
						turnBlocksTo(Arena.redTeam,redPlayers);
					}else{
						turnBlocksTo(Arena.blueTeam,bluePlayers);

                    }

					redPlayers.clear();
					bluePlayers.clear();

                    break;
				case RED:

                    for(ArenaPlayer ap : Methods.getPlugin().getArena().getBluePlayers()){
						if(!getCaptureArea().contains(ap.getPlayer().getLocation()))
							continue;
						
						if(GameListener.respawnQueue.contains(ap.getPlayer().getUniqueId()))
							continue;
						
						bluePlayers.add(ap);
					}
					
					for(ArenaPlayer ap : Methods.getPlugin().getArena().getRedPlayers()){
						if(!getCaptureArea().contains(ap.getPlayer().getLocation()))
							continue;
						if(GameListener.respawnQueue.contains(ap.getPlayer().getUniqueId()))
							continue;
						
						redPlayers.add(ap);
					}
					
					if(bluePlayers.size() > 0 ){
						
						
						
						if(hasOvercome(redPlayers.size(), bluePlayers.size())){
							turnBlocksTo(Arena.blueTeam,bluePlayers);
						}
						
						
					} 
					
					if(redPlayers.size() > 0){
							turnBlocksTo(Arena.redTeam,redPlayers);
					}
					
					bluePlayers.clear();
					redPlayers.clear();
					break;
				}
				
			}
			
		}, 0, 10);
	}
	
	
	public Location getPointBlock(){
		return getSingleLocation(Integer.toString(getValue()));
	}
	
	
	public void setCaptureArea(Cuboid captureArea){
		Methods.addLocation(getName() + "." + "CapturePoint1", captureArea.getPoint1());
		Methods.addLocation(getName() + "." + "CapturePoint2", captureArea.getPoint2());
		
	}
	
	
	public void setCapturePoint1(Location loc){
		Methods.addLocation(getName() + "." + "CapturePoint1", loc);
	}
	
	public void setCapturePoint2(Location loc){
		Methods.addLocation(getName() + "." + "CapturePoint2", loc);
	}
	
	public Cuboid getCaptureArea(){
		return new Cuboid(Methods.getLocation(getName() + "." + "CapturePoint1"), Methods.getLocation(getName() + "." + "CapturePoint2"));
	}
	
	
	public String getName() {
		return name;
	}
	
	private Location getSingleLocation(String path){
		 if (Methods.getPlugin().arenas.contains("Points" + "." + path + ".World"))
		    {
		      Location loc = new Location(Bukkit.getWorld(Methods.getPlugin().arenas.getString("Points" + "." + path + ".World")), 
		        Methods.getPlugin().arenas.getDouble("Points" + "." + path  + ".X"), 
		        Methods.getPlugin().arenas.getDouble("Points" + "." + path + ".Y"), 
		        Methods.getPlugin().arenas.getDouble("Points" + "." + path  + ".Z"));
		      loc.setPitch((float)Methods.getPlugin().arenas.getDouble("Points" + "." + path  + ".Pitch"));
		      loc.setYaw((float)Methods.getPlugin().arenas.getDouble("Points" + "." + path  + ".Yaw"));
		      return loc;
		    }
		    return null;
	}
	
	
	private void addSingleLocation(String path, Location loc){
		  if (!Methods.getPlugin().arenas.contains("Points" + "." + path))
		    {
		      Methods.getPlugin().arenas.addDefault("Points" + "." + path + ".X", loc.getX());
		      Methods.getPlugin().arenas.addDefault("Points" + "." + path + ".Y", loc.getY());
		      Methods.getPlugin().arenas.addDefault("Points" + "." + path + ".Z", loc.getZ());
		      Methods.getPlugin().arenas.addDefault("Points" + "." + path + ".World", loc.getWorld().getName());
		      Methods.getPlugin().arenas.addDefault("Points" + "." + path+ ".Pitch", loc.getPitch());
		      Methods.getPlugin().arenas.addDefault("Points" + "." + path + ".Yaw", loc.getYaw());
		    }
		    else
		    {
		      Methods.getPlugin().arenas.set("Points" + "." + path + ".X", loc.getX());
		      Methods.getPlugin().arenas.set("Points" + "." + path + ".Y", loc.getY());
		      Methods.getPlugin().arenas.set("Points" + "." + path+ ".Z", loc.getZ());
		      Methods.getPlugin().arenas.set("Points" + "." + path + ".World", loc.getWorld().getName());
		      Methods.getPlugin().arenas.set("Points" + "." + path + ".Pitch", loc.getPitch());
		      Methods.getPlugin().arenas.set("Points" + "." + path + ".Yaw", loc.getYaw());
		    }
		    Methods.saveYamls();
	}
	
	
	private List<Location> getMultiLocation(String path){
		 List<Location> locs = new ArrayList<Location>();
		  for(int i = 1;i <= Methods.getPlugin().arenas.getInt("Points" + "." + path + ".Counter"); i++ ){			  
			 Location loc = new Location(
					 Bukkit.getWorld(Methods.getPlugin().arenas.getString("Points" + "." + path + "." + i + ".World")),
					 Methods.getPlugin().arenas.getDouble("Points" + "." + path + "."  + i + ".X"),
					 Methods.getPlugin().arenas.getDouble("Points" + "." + path + "."  + i + ".Y"),
					 Methods.getPlugin().arenas.getDouble("Points" + "." + path + "." + i + ".Z"));
			 loc.setPitch((float) Methods.getPlugin().arenas.getInt("Points" + "." + path + "."  + i + ".Pitch"));
			 loc.setYaw((float) Methods.getPlugin().arenas.getDouble("Points" + "." + path + "." + i + ".Yaw"));
			 if(loc != null){
			 locs.add(loc);
			 }
		  }
		  
		  
		  return locs;
		
	}
	
	private void addMultiLocation(String path, Location loc){
		if (!Methods.getPlugin().arenas.contains("Points" + "." + path +".Counter"))
	    {
	      int counter = 1;
		  Methods.getPlugin().arenas.addDefault("Points" + "." + path + "." + counter +  ".X", loc.getX());
	      Methods.getPlugin().arenas.addDefault("Points" + "." + path + "." +counter + ".Y", loc.getY());
	      Methods.getPlugin().arenas.addDefault("Points" + "." + path + "."  +counter+ ".Z", loc.getZ());
	      Methods.getPlugin().arenas.addDefault("Points" + "." + path + "." +counter+ ".World", loc.getWorld().getName());
	      Methods.getPlugin().arenas.addDefault("Points" + "." + path + "."  +counter+ ".Pitch", loc.getPitch());
	      Methods.getPlugin().arenas.addDefault("Points" + "." + path + "."  +counter+ ".Yaw", loc.getYaw());
	      Methods.getPlugin().arenas.addDefault("Points" + "." + path + ".Counter", counter);

	    }
	    else
	    {
	    	int counter = Methods.getPlugin().arenas.getInt("Points" + "." + path + ".Counter");
	    	counter++;
	    	Methods.getPlugin().arenas.set("Points" + "." + path + "."  +counter+ ".X", loc.getX());
	      Methods.getPlugin().arenas.set("Points" + "." + path + "."  +counter+ ".Y", loc.getY());
	      Methods.getPlugin().arenas.set("Points" + "." + path + "."  +counter+ ".Z", loc.getZ());
	      Methods.getPlugin().arenas.set("Points" + "." + path + "."  +counter+ ".World", loc.getWorld().getName());
	      Methods.getPlugin().arenas.set("Points" + "." + path + "."  +counter+ ".Pitch", loc.getPitch());
	      Methods.getPlugin().arenas.set("Points" + "." + path + "."  +counter+ ".Yaw", loc.getYaw());
	      Methods.getPlugin().arenas.set("Points" + "." + path + ".Counter" , counter);

	    }
	    Methods.saveYamls();
	}
	
	
	
}
