package me.artish1.CrystalClash.Arena;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.CrystalClash;
import me.artish1.CrystalClash.Effect.PlingWarningEffect;
import me.artish1.CrystalClash.Listeners.Classes.ArcherListener;
import me.artish1.CrystalClash.Listeners.Classes.ExplosivesListener;
import me.artish1.CrystalClash.Listeners.GameListener;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.Util.MySQLUtil;
import me.artish1.CrystalClash.Util.SimpleScoreboard;
import me.artish1.CrystalClash.events.GameStateChangeEvent;
import me.artish1.CrystalClash.leaderboards.Leaderboard;
import me.artish1.CrystalClash.objects.BuildingManager;
import me.artish1.CrystalClash.other.BlockPlaceManager;
import me.artish1.CrystalClash.other.TipManager;
import net.minecraft.server.v1_15_R1.PacketPlayOutTitle;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;


public class Arena {
	private String name;

	
	private HashSet<Entity> guns = new HashSet<Entity>();
	private HashSet<Entity> merchants = new HashSet<Entity>();
	
	public  HashSet<UUID> redGuns = new HashSet<UUID>();
	public HashSet<UUID> blueGuns = new HashSet<UUID>();
	
	private HashSet<ArenaPlayer> allPlayers = new HashSet<ArenaPlayer>();
	private HashSet<ArenaPlayer> lobbyPlayers = new HashSet<ArenaPlayer>();
	private HashSet<ArenaPlayer> spectators = new HashSet<ArenaPlayer>();
	private List<Location> powerUps = new ArrayList<Location>();
    public HashMap<String, Team> tags = new HashMap<String,Team>();
    public static ArenaTeam blueTeam = new ArenaTeam("Blue",ChatColor.BLUE,DyeColor.BLUE,Point.BLUE,Material.BLUE_WOOL);
    public static ArenaTeam redTeam = new ArenaTeam("Red",ChatColor.RED,DyeColor.RED,Point.RED, Material.RED_WOOL);

    private Scoreboard scoreboard;
	private ArenaTeam winner = null;
	private GameState gstate = GameState.LOBBY;
	
	private CrystalClash plugin;
	
	private int countdownId;
	
	private int counter;
	
	private int crystalId;
	
	private Point point1 = Point.NEUTRAL;
	private Point point2 = Point.NEUTRAL;
	private Point point3 = Point.NEUTRAL;
	

	
	public Arena(String name, CrystalClash plugin) {
		this.name = name;
		this.plugin = plugin;
		ArenaManager.addArena(this);
	}
	
	public int getAutoStartPlayers(){
		return plugin.getConfig().getInt("AutoStartPlayers");
	}
	
	public boolean isRed(Player player){
		if(redTeam.getPlayerList().contains(getArenaPlayer(player))){
			return true;
		}
		return false;
	}
	
	public int getPointsGoal(){
		/*
		if(!plugin.getConfig().contains(getName() + ".PointsGoal")){
			plugin.getConfig().addDefault(getName() + ".PointsGoal", 300);
		}
		*/
		return 300;
	}
	
	
	public List<Location> getPowerUps() {
		return powerUps;
	}
	
	
	public void addPowerUpLocation(Location loc){
		getPowerUps().add(loc);
		Methods.addMultiLocation("PowerUps", loc);
	}
	
	public Location getRandomPowerUpLocation(){
		return Methods.getRandomLocation(getPowerUps());
	}
	

	public Point getPoint1() {
		return point1;
	}
	public Point getPoint2() {
		return point2;
	}
	public Point getPoint3() {
		return point3;
	}
	
	public boolean isAutoCapture(){
		return Methods.getPlugin().getConfig().getBoolean("AutoCapture");
	}

	private String getCapturedPrefix(int point) {
        return "Your team has captured" + ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + " Point " + point;
    }
    private String getLostPrefix(int point) {
        return "Your team has lost control of" + ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + " Point " + point;
    }


	public void setPoint1(Point point1 ) {
		if(this.point1 == point1){
			return;
		}
		
		
		
		if(point1 == Point.RED){
			sendRedMessage(getCapturedPrefix(1));
			if(!isAutoCapture())
			setPoint1DecBlocks(DyeColor.RED);
		}
		if(point1 == Point.BLUE){
			if(!isAutoCapture())
				setPoint1DecBlocks(DyeColor.BLUE);

			sendBlueMessage(getCapturedPrefix(1));
		}
		
		if(this.point1 == Point.RED){
		    sendRedAlert(getLostPrefix(1));
		}
		
			if(this.point1 == Point.BLUE){
                sendRedAlert(getLostPrefix(1));
            }


		int placeholder = scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(
				ChatColor.GOLD.toString()+ChatColor.BOLD.toString() + "P1: " + this.point1.toString()).getScore();
		scoreboard.resetScores(ChatColor.GOLD.toString() +ChatColor.BOLD.toString() + "P1: " + this.point1.toString());
		
		this.point1 = point1;



		scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(ChatColor.GOLD.toString() +ChatColor.BOLD.toString() + "P1: " + this.point1.toString()).setScore(placeholder);
		
		
	}
	
	public void setPoint2(Point point2) {
		if(this.point2 == point2){
			return;
		}
		
		if(point2 == Point.RED){
            sendRedMessage(getCapturedPrefix(2));
			if(!isAutoCapture())
			setPoint2DecBlocks(DyeColor.RED);
		}else if(point2 == Point.BLUE){
			if(!isAutoCapture())
			setPoint2DecBlocks(DyeColor.BLUE);
            sendBlueMessage(getCapturedPrefix(2));
		}
		if(this.point2 == Point.RED){
            sendRedAlert(getLostPrefix(2));
		}
			
		if(this.point2 == Point.BLUE){
                sendRedAlert(getLostPrefix(2));
			}
		
		
		int placeholder = scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(
				ChatColor.GOLD.toString()+ChatColor.BOLD.toString() + "P2: " + this.point2.toString()).getScore();
		scoreboard.resetScores(ChatColor.GOLD.toString() +ChatColor.BOLD.toString() + "P2: " + this.point2.toString());
		
		this.point2 = point2;
		
		scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(ChatColor.GOLD.toString() +ChatColor.BOLD.toString() + "P2: " + this.point2.toString()).setScore(placeholder);
		
		
		
		
		
	}
	
	public void setPoint3(Point point3) {
		if(this.point3 == point3){
			return;
		}
		
		if(point3 == Point.RED){
			if(!isAutoCapture())
			setPoint3DecBlocks(DyeColor.RED);
            sendRedMessage(getCapturedPrefix(3));
		}else if(point3 == Point.BLUE){
			if(!isAutoCapture())
			setPoint3DecBlocks(DyeColor.BLUE);
		    sendBlueMessage(getCapturedPrefix(3));
		}
		
		if(this.point3 == Point.RED){
            sendRedAlert(getLostPrefix(3));
		}
			if(this.point3 == Point.BLUE){
                sendBlueAlert(getLostPrefix(3));
			}
		
		
		
		
		int placeholder = scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(
				ChatColor.GOLD.toString()+ChatColor.BOLD.toString() + "P3: " + this.point3.toString()).getScore();
		scoreboard.resetScores(ChatColor.GOLD.toString() +ChatColor.BOLD.toString() + "P3: " + this.point3.toString());
		
		this.point3 = point3;
		
		scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(ChatColor.GOLD.toString() +ChatColor.BOLD.toString() + "P3: " + this.point3.toString()).setScore(placeholder);
		
		
	}
	
	
	
	

	/*
	public void updateAllTabHashSets(){
		
		for(Player p :Bukkit.getOnlinePlayers()){
			updateTabHashSet(p); 
		}
		
	}
	*/
	/*
	public void createTabHashSet(Player player){
		switch(getState()){
		case INGAME:
			break;
		case LOBBY:
			TabHashSet HashSet = TabAPI.createTabHashSetForPlayer(player);
			int currentSlot = 0;
			HashSet.setSlot(1, "-------");
			HashSet.setSlot(2, ChatColor.GREEN + "CrystalClash");
			HashSet.setSlot(3, "-------");
			currentSlot = 3;
			
			for(Player p : Bukkit.getOnlinePlayers()){
				currentSlot++;
				HashSet.setSlot(currentSlot,ChatColor.GRAY +  p.getName());
			}
				HashSet.send();
			Bukkit.broadcastMessage("Created");
			break;
		case STARTING:
			break;
		case STOPPING:
			break;
		default:
			break;
		}
		
	}
	*/
	
	
	private int pointTaskId;
	private void startPointTask(){
		pointTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){

			public void run() {
				if(getState() != GameState.INGAME){
					Bukkit.getScheduler().cancelTask(pointTaskId);
					return;
				}


				if(getPoint1() == Point.BLUE)
					 blueTeam.addTeamPoints(2);
                else
                    redTeam.addTeamPoints(2);

                if(getPoint2() == Point.BLUE)
                    blueTeam.addTeamPoints(2);
                else
                    redTeam.addTeamPoints(2);


                if(getPoint3() == Point.BLUE)
                    blueTeam.addTeamPoints(2);
                else
                    redTeam.addTeamPoints(2);

            }
			
		}, 100, 20 * 10);
	}
	
	
	public void subtractBlueCrystalHealth(){
		int placeholderScore = scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(ChatColor.BLUE.toString() 
				+ "Crystal HP: " +/* blueCrystalHealth*/ blueTeam.getCrystalHealth()).getScore();
		
		scoreboard.resetScores(ChatColor.BLUE.toString()+ "Crystal HP: " + blueTeam.getCrystalHealth());
		//blueCrystalHealth--;
		blueTeam.subtractCrystalHealth(1);
		scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(ChatColor.BLUE.toString() + "Crystal HP: " + blueTeam.getCrystalHealth()).setScore(placeholderScore);
		if(blueTeam.getCrystalHealth() <= 0){
			Bukkit.broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "Red Team"
					 + ChatColor.GREEN + " destroyed the " + ChatColor.BLUE.toString()  + ChatColor.BOLD.toString() + "Blue Team's" + ChatColor.GREEN + " Crystal, and won!");
			
			setWinner(redTeam);
			stop(StopReason.GAME_END);
		}
		
	}
	public void subtractRedCrystalHealth(){
		int placeholderScore = scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(ChatColor.RED.toString() 
				+ "Crystal HP: " + redTeam.getCrystalHealth()).getScore();
		
		scoreboard.resetScores(ChatColor.RED.toString()+ "Crystal HP: " + redTeam.getCrystalHealth());
		//redCrystalHealth--;
		redTeam.subtractCrystalHealth(1);
		scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(ChatColor.RED.toString() + "Crystal HP: " + redTeam.getCrystalHealth()).setScore(placeholderScore);
		
		
		if(redTeam.getCrystalHealth() <= 0){
			Bukkit.broadcastMessage(ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + "Blue Team"
					 + ChatColor.GREEN + " destroyed the " + ChatColor.RED.toString()  + ChatColor.BOLD.toString() + "Red Team's" + ChatColor.GRAY + " Crystal, and won!");
			
			setWinner(blueTeam);
			stop(StopReason.GAME_END);
		}
		
	}
	
	
	
	

	private void createScoreboard(){
		SimpleScoreboard board = new SimpleScoreboard(ChatColor.GOLD + ChatColor.BOLD.toString() + "CrystalClash");
		board.add(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "RED:");
		board.add(ChatColor.RED.toString() + "Crystal HP: " + redTeam.getCrystalHealth());
		board.add(ChatColor.RED.toString() + "Points: 0");
		board.add("");
		board.add("");
		board.add(ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + "BLUE:");
		board.add(ChatColor.BLUE.toString() + "Crystal HP: " + blueTeam.getCrystalHealth());
		board.add(ChatColor.BLUE.toString() + "Points: 0");
		board.add("");
		board.add("");
		board.add(ChatColor.GOLD.toString() + ChatColor.BOLD.toString() +"P1: " +point3.toString());
		board.add(ChatColor.GOLD.toString() +ChatColor.BOLD.toString() + "P2: " +point3.toString());
		board.add(ChatColor.GOLD.toString()+ ChatColor.BOLD.toString() + "P3: " + point3.toString());
		board.build();
		Team blue = board.getScoreboard().registerNewTeam("Blue");
		Team red = board.getScoreboard().registerNewTeam("Red");
		Team creatorRed = board.getScoreboard().registerNewTeam("CreatorRed");
		Team creatorBlue = board.getScoreboard().registerNewTeam("CreatorBlue");
		Team spectators = board.getScoreboard().registerNewTeam("Spectator");
		
		
		tags.put("Spectator", spectators);
		tags.put("Red", red);
		tags.put("CreatorRed", creatorRed);
		tags.put("CreatorBlue", creatorBlue);
		tags.put("Blue", blue);
		
		
		spectators.setPrefix(ChatColor.DARK_GRAY + "[" + ChatColor.WHITE + "Spec" + ChatColor.DARK_GRAY + "] " + ChatColor.WHITE);
		spectators.setAllowFriendlyFire(false);
		spectators.setCanSeeFriendlyInvisibles(true);
		creatorRed.setPrefix(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "Red" + ChatColor.DARK_GRAY + "] " + ChatColor.RED );
		red.setPrefix(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "Red" + ChatColor.DARK_GRAY + "] " + ChatColor.RED );
		creatorBlue.setPrefix(ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "Blue" + ChatColor.DARK_GRAY + "] " + ChatColor.BLUE);
		blue.setPrefix(ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "Blue" + ChatColor.DARK_GRAY + "] " + ChatColor.BLUE);
		blue.setAllowFriendlyFire(false);
		red.setAllowFriendlyFire(false);
		blue.setCanSeeFriendlyInvisibles(true);
		red.setCanSeeFriendlyInvisibles(true);
		creatorRed.setSuffix(" " + ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "Dev" + ChatColor.DARK_GRAY + "]");
		creatorBlue.setSuffix(" " + ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "Dev" + ChatColor.DARK_GRAY + "]");

		
		for(ArenaPlayer ap : redTeam.getPlayerList()){
			if(!ap.getPlayer().getUniqueId().equals(UUID.fromString("c3ce322d-5c9e-4a6b-9fa5-67d34d7ed5fa"))){
				red.addPlayer(Bukkit.getOfflinePlayer(ap.getName()));
			}else{
				creatorRed.addPlayer(Bukkit.getOfflinePlayer(ap.getName())); 
			}
			
			board.send(ap.getPlayer());
		}
		
		for(ArenaPlayer ap: blueTeam.getPlayerList()){
			if(!ap.getPlayer().getUniqueId().equals(UUID.fromString("c3ce322d-5c9e-4a6b-9fa5-67d34d7ed5fa"))){
				blue.addPlayer(Bukkit.getOfflinePlayer(ap.getName()));
			}else{
				creatorBlue.addPlayer(Bukkit.getOfflinePlayer(ap.getName())); 
			}
			board.send(ap.getPlayer());
		}
		
		
		
		scoreboard = board.getScoreboard();
		
	}
	
	public boolean isBlue(Player player){
        return blueTeam.getPlayerList().contains(getArenaPlayer(player));
    }
	
	public boolean isOnSameTeam(Player player, Player teammate){
		if(isRed(player) && isRed(teammate)){
			return true;
		}
        return isBlue(player) && isBlue(teammate);
    }
	
	public boolean isOn(){
		return (getState() == GameState.INGAME || getState() == GameState.STARTING || getState() == GameState.STOPPING);
	}
	
	public boolean canStart(){
		return lobbyPlayers.size() >= getAutoStartPlayers() && getState() == GameState.LOBBY;
			
	}
	
	
	private void balanceRest(){
		for(ArenaPlayer ap : lobbyPlayers){
			if(redTeam.getPlayerList().contains(ap) || blueTeam.getPlayerList().contains(ap))
				continue;

			balance(ap);
			
			
		}
		
	}
	
	
	private void setTeams(){
		calculateBalanceQueues();
		balanceRest();
	}
	
	
	
	public void addBlue(final ArenaPlayer player){
		blueTeam.getPlayerList().add(player);
		player.setTeam(blueTeam);
		//player.getPlayer().setPlayerListName(ChatColor.BLUE + player.getPlayer().getName());
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){ 

			public void run() {
				player.getPlayer().setCompassTarget(getRedCrystal());
				
			}
			
		}, 20);
		
		hideSpectatorsFrom(player.getPlayer());
	}
	
	public void addRed(final ArenaPlayer player){
		redTeam.getPlayerList().add(player);
		player.setTeam(redTeam);
		//player.getPlayer().setPlayerListName(ChatColor.RED +player.getPlayer().getName());
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			public void run() {
				player.getPlayer().setCompassTarget(getBlueCrystal());
				
			}
			
		}, 20);
		hideSpectatorsFrom(player.getPlayer());
	}
	
	
	public void addSpectator(ArenaPlayer player)
	{
		spectators.add(player);

		blueTeam.getPlayerList().remove(player);
		redTeam.getPlayerList().remove(player);
		lobbyPlayers.remove(player);
		player.getPlayer().teleport(ContestPoint.POINT1.getPointBlock().add(0, 3, 0));
		player.getPlayer().setScoreboard(getScoreboard());
		tags.get("Spectator").addEntry(player.getName());
		player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,Integer.MAX_VALUE,1,true,false));
		
		hideSpectator(player.getPlayer());
	}
	
	public static void hideSpectator(Player player)
	{

			for(ArenaPlayer ap : redTeam.getPlayerList())
			{
				if(!Methods.getArena().spectators.contains(ap))
				ap.getPlayer().hidePlayer(player);
			}
        for(ArenaPlayer ap : blueTeam.getPlayerList())
        {
            if(!Methods.getArena().spectators.contains(ap))
                ap.getPlayer().hidePlayer(player);
        }

	}
	public static void hideSpectators()
	{
		for(ArenaPlayer spectator : Methods.getArena().spectators)
		{
			hideSpectator(spectator.getPlayer());
		}
	}
	
	public void addLobbyPlayer(ArenaPlayer player)
	{
		spectators.remove(player);

		redTeam.getPlayerList().remove(player);
        blueTeam.getPlayerList().remove(player);

        player.setTeam(null);
		lobbyPlayers.add(player);
		Methods.setLobbyInventory(player.getPlayer()); 
		Methods.createLobbyScoreboard(player.getPlayer());
		player.getPlayer().teleport(Methods.getLobby());
	}
	
	
	private void balance(final ArenaPlayer player){
		if(redTeam.getPlayerList().size() == blueTeam.getPlayerList().size()){
			Random r = Methods.getRandom();
			int rnum = r.nextInt(2) + 1;
			switch(rnum){
			case 1:
				addRed(player);
				player.getPlayer().sendMessage(ChatColor.GRAY + "You have been set to the " + ChatColor.RED + "Red" + ChatColor.GRAY + " team");
				return;
			case 2:
				addBlue(player);
				player.getPlayer().sendMessage(ChatColor.GRAY + "You have been set to the " + ChatColor.BLUE + "Blue" + ChatColor.GRAY + " team");
				return;
			}
		}
		
		
		if(redTeam.getPlayerList().size() > blueTeam.getPlayerList().size()){
			addBlue(player); 
			player.getPlayer().sendMessage(ChatColor.GRAY + "You have been set to the " + ChatColor.BLUE + "Blue" + ChatColor.GRAY + " team");
			return;
		}
		
		if(blueTeam.getPlayerList().size() > redTeam.getPlayerList().size()){
			addRed(player);
			player.getPlayer().sendMessage(ChatColor.GRAY + "You have been set to the " + ChatColor.RED + "Red" + ChatColor.GRAY + " team");
			return;	
		}
		
		
		
	}
	
	public static void hideSpectatorsFrom(Player player)
	{
		for(ArenaPlayer ap : Methods.getArena().spectators)
		{
			player.hidePlayer(ap.getPlayer());
		}
	}
	/*
	public HashSet<ArenaPlayer> getLobbyPlayers() {
		return lobbyPlayers;
	}*/
	
	private Object getRandomObjectFromList(List<?> list)
	{
		int rNum = Methods.getRandom().nextInt(list.size());
		return list.get(rNum); 
	}
	
	public void calculateBalanceQueues()
	{
		int reds = redTeam.getQueue().size();
		int blues = blueTeam.getQueue().size();
		
		if(reds > blues)
		{
			reds-= blues;
			if(reds > 1)
			{
				reds--;
				
				for(;reds>0;reds--)
				{
					ArenaPlayer toTransfer = (ArenaPlayer) getRandomObjectFromList(redTeam.getQueue());
					blueTeam.getQueue().add(toTransfer);
					redTeam.getQueue().remove(toTransfer);
					toTransfer.sendMessage("You have been moved to a different team due to auto-balancing"); 
					
				}
				
			}
		}
		
		if(blues > reds)
		{
			blues-= reds;
			if(blues > 1)
			{
				blues--;
				
				
				for(;blues>0;blues--)
				{
					ArenaPlayer toTransfer = (ArenaPlayer) getRandomObjectFromList(blueTeam.getQueue());
					redTeam.getQueue().add(toTransfer);
					blueTeam.getQueue().remove(toTransfer);
					toTransfer.sendMessage("You have been moved to a different team due to auto-balancing"); 
					
				}
				
			}
		}
		
		
		for(ArenaPlayer player : redTeam.getQueue())
		{
			addRed(player);
		}
		
		
		for(ArenaPlayer player : blueTeam.getQueue())
		{
			addBlue(player);
		}
		
		blueTeam.getQueue().clear();
		redTeam.getQueue().clear();
		
	}
	
	
	
	
	
	public void setTabHashSetColor(final Player player){
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			public void run() {
				//player.setPlayerListName(ChatColor.GRAY + player.getName()); 
				if(player.hasPermission("cc.tab.darkred")){
					player.setPlayerListName(ChatColor.DARK_RED + player.getName()); 
				}
				if(player.hasPermission("cc.tab.aqua")){
					player.setPlayerListName(ChatColor.AQUA + player.getName()); 
				}
				if(player.hasPermission("cc.tab.darkblue")){
					player.setPlayerListName(ChatColor.DARK_BLUE + player.getName()); 
				}
				if(player.hasPermission("cc.tab.black")){
					player.setPlayerListName(ChatColor.BLACK + player.getName()); 
				}
				if(player.hasPermission("cc.tab.darkaqua")){
					player.setPlayerListName(ChatColor.DARK_AQUA + player.getName()); 
				}
				if(player.hasPermission("cc.tab.yellow")){
					player.setPlayerListName(ChatColor.YELLOW + player.getName()); 
				}
				if(player.hasPermission("cc.tab.white")){
					player.setPlayerListName(ChatColor.WHITE + player.getName()); 
				}
				if(player.hasPermission("cc.tab.lightpurple")){
					player.setPlayerListName(ChatColor.LIGHT_PURPLE + player.getName()); 
				}
				if(player.hasPermission("cc.tab.darkpurple")){
					player.setPlayerListName(ChatColor.DARK_PURPLE + player.getName()); 
				}
				if(player.hasPermission("cc.tab.green")){
					player.setPlayerListName(ChatColor.GREEN + player.getName()); 
				}
				if(player.hasPermission("cc.tab.gold")){
					player.setPlayerListName(ChatColor.GOLD + player.getName()); 
				}
				if(player.hasPermission("cc.tab.darkgreen")){
					player.setPlayerListName(ChatColor.DARK_GREEN + player.getName()); 
				}
				if(player.hasPermission("cc.tab.darkgray")){
					player.setPlayerListName(ChatColor.DARK_GRAY + player.getName()); 
				}
				if(player.hasPermission("cc.tab.red")){
					player.setPlayerListName(ChatColor.RED + player.getName()); 
				}
				if(player.hasPermission("cc.tab.blue")){
					player.setPlayerListName(ChatColor.BLUE + player.getName()); 
				}
				if(player.getName().equalsIgnoreCase(ChatColor.stripColor("artish1"))){
					player.setPlayerListName(ChatColor.BOLD.toString() + ChatColor.AQUA.toString() + "||Artish1||");
				}
			}
			
		}, 2);
	}
	
	

	public void addPlayer(Player player){
		if(player == null)
		{
			Bukkit.broadcastMessage("Player is null, at Arena:764");
		}
		
		setTabHashSetColor(player);
		
		Methods.removePotionEffects(player);
		
		Methods.sendHeaderAndFooter(player, ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Header")),ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Footer")));
	
		
		ArenaPlayer ap = (getArenaPlayer(player) != null ? getArenaPlayer(player) : new ArenaPlayer(player));
		allPlayers.add(ap);
		 
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.updateInventory();
		
		player.setHealth(20.0);
		
		ArenaManager.addArena(player, this);
		
		
		if(getState() == GameState.INGAME){
			balance(ap);
			if(redTeam.getPlayerList().contains(ap)){
				ap.getPlayer().teleport(getRedSpawn());
				tags.get("Red").addPlayer(Bukkit.getOfflinePlayer(ap.getName()));
			}
			if(blueTeam.getPlayerList().contains(ap)){
				ap.getPlayer().teleport(getBlueSpawn());
				tags.get("Blue").addPlayer(Bukkit.getOfflinePlayer(ap.getName()));
			}
			refreshScoreboard();
			setInventory(player);
			setPassivePotionEffect(player);
			ap.getPlayer().setScoreboard(scoreboard);
			
			sendAll(ChatColor.GREEN +player.getName() + ChatColor.GRAY + " has joined the game.");
			return;
		}
		
		lobbyPlayers.add(ap);
		if(getState() != GameState.STARTING){
			sendAll(ChatColor.GREEN +player.getName() + ChatColor.GRAY + " has joined the game. " + ChatColor.DARK_GRAY + "[" +
		ChatColor.AQUA + Bukkit.getOnlinePlayers().size() + "/" + getAutoStartPlayers() + ChatColor.DARK_GRAY + "] ");
		}else{
			sendAll(ChatColor.GREEN +player.getName() + ChatColor.GRAY + " has joined the game.");
		}
		if(canStart()){
			start();
		}

		if(getLobbySpawn() != null){
			player.teleport(getLobbySpawn());
		}else{
			player.sendMessage(ChatColor.GREEN + "It seems the arena is not setup. If you have the sufficient permission to do so, please do.");
			player.sendMessage(ChatColor.GREEN + "If you happened to join the server and you aren't an admin (or have sufficient permission), please leave and tell an admin.");
		}
		Methods.setLobbyInventory(player);
	}
	
	public void clearInventory(Player player){
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.updateInventory();
	}
	
	
	private void clearAll(){
		for(ArenaPlayer ap: redTeam.getPlayerList()){
			clearInventory(ap.getPlayer());
		}
		for(ArenaPlayer ap: blueTeam.getPlayerList()){
			clearInventory(ap.getPlayer());
		}
		
	}
	

	public void removePlayer(Player player){
		ArenaPlayer arenaPlayer = getArenaPlayer(player);
		
		
        lobbyPlayers.remove(arenaPlayer);
        spectators.remove(arenaPlayer);

        if (redTeam.getQueue().contains(arenaPlayer)) {
            redTeam.getQueue().remove(arenaPlayer);
        }

        if (blueTeam.getQueue().contains(arenaPlayer)) {
            blueTeam.getQueue().remove(arenaPlayer);
        }


        if(redTeam.getPlayerList().contains(arenaPlayer)){
			redTeam.getPlayerList().remove(arenaPlayer);
			tags.get("Red").removePlayer(Bukkit.getOfflinePlayer(arenaPlayer.getName()));
		} 
		
		if(blueTeam.getPlayerList().contains(arenaPlayer)){
			blueTeam.getPlayerList().remove(arenaPlayer);
			tags.get("Blue").removePlayer(Bukkit.getOfflinePlayer(arenaPlayer.getName()));
		}
		
		player.setPlayerListName(ChatColor.GRAY + player.getName());
		player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		refreshScoreboard();
		for(PotionEffect effect : player.getActivePotionEffects()){
			player.removePotionEffect(effect.getType());
		}
		
		clearInventory(player);
		ArenaManager.removeArena(player);
		
		
		if(getState() == GameState.STARTING){
			if(lobbyPlayers.size() <= 0){
				stop(StopReason.NO_PLAYERS);
			}
			
		}
		
		
		
		if(BuildingManager.ownsTurret(player)){
			BuildingManager.getTurret(player).remove();
		}
		
		if(BlockPlaceManager.hasBlockPlaceManager(player)){
			for(Block b : BlockPlaceManager.getManager(player).getBlocks()){
				b.setType(Material.AIR);
			}
			BlockPlaceManager.removeManager(player);
		}
		
		if(ExplosivesListener.hasManager(player)){
			for(Location loc : ExplosivesListener.getManager(player).getLocs()){
				loc.getBlock().setType(Material.AIR);
			}
			ExplosivesListener.managers.remove(ExplosivesListener.getManager(player));
		}
		
		
		if(getState() == GameState.INGAME){
			if(redTeam.getPlayerList().size() == 0 && blueTeam.getPlayerList().size() == 0){
				stop(StopReason.NO_PLAYERS);
			}
		}

	//	updateSigns();
		
	}
	

	
	public void setInventory(Player player){
		if(player == null)
			return;
		ClassType type = getArenaPlayer(player).getType();
		Methods.removePotionEffects(player);
		
		type.setInventory(player); 
		giveCompass(player); 
		
		
		for(ItemStack item: getArenaPlayer(player).getBought()){
			player.getInventory().addItem(item);
		}
		
		
		
		
		player.setLevel(getArenaPlayer(player).getMoney());
		 
		if(GameListener.chances.contains(player.getUniqueId())){
			player.getInventory().addItem(Methods.getClassMenuItem());
		}
		
	}
	
	public void setPassivePotionEffect(Player player){
		if(player == null)
			return;
		if(getArenaPlayer(player) == null)
			return;
		
		ClassType type = getArenaPlayer(player).getType();
		
		
		if(type == ClassType.EARTH){
			player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999,1,true),true);
			return;
		}
		
		
		
		if(type == ClassType.SCOUT){
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999,2,true), true);
			
		}
		
		if(type == ClassType.GUARDIAN){
			player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 999999,0,true),true);
		}
		
		
		if(type == ClassType.ASSASSIN){
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999,0,true), true);
		}
		
		
	}
	
	
	
	
	
	private void healAll(){
		for(ArenaPlayer ap: redTeam.getPlayerList()){
			ap.getPlayer().setHealth(20.0);
		}
		for(ArenaPlayer ap: blueTeam.getPlayerList()){
			ap.getPlayer().setHealth(20.0);
		}
	}
	
	public ArenaPlayer getArenaPlayer(Player player){
		
		for(ArenaPlayer aPlayer : allPlayers)
		{
			if(player.getUniqueId().equals(aPlayer.getUUID()))
			{
				return aPlayer;
			}
		}
		
		/*
		for(ArenaPlayer aplayer: redTeam.getPlayerList()){
			if(player.getUniqueId().equals( aplayer.getUUID())){
				return aplayer;
			}
		}
		
		for(ArenaPlayer aplayer: blueTeam.getPlayerList()){
			if(player.getUniqueId().equals( aplayer.getUUID())){
				return aplayer;
			}
		}
		
		for(ArenaPlayer aPlayer: lobbyPlayers){
			if(player.getUniqueId().equals( aPlayer.getUUID())){
				return aPlayer;
			}
		}
		
		for(ArenaPlayer aPlayer : spectators)
		{
			if(player.getUniqueId().equals(aPlayer.getUUID())){
				return aPlayer;
			}
		}
		
		*/
		
		return null;
		
	}
	
	public void sendAll(String message){
		sendRedMessage(message);
		sendBlueMessage(message);
		
		if(getState() != GameState.INGAME){
			for(ArenaPlayer play : lobbyPlayers){
				play.sendMessage(message);
			}
		}
	}
	
	public void sendRedMessage(String message){
		for(ArenaPlayer player: redTeam.getPlayerList()){
			player.sendMessage(message);
		}
	}
	
	public void sendBlueMessage(String message){
		for(ArenaPlayer player: blueTeam.getPlayerList()){
			player.sendMessage(message);
		}
	}
	
	
	private void spawnPlayers(){
		
		for(ArenaPlayer player: redTeam.getPlayerList())
		{
			player.getPlayer().teleport(getRedSpawn());
		}
		for(ArenaPlayer player: blueTeam.getPlayerList())
		{
			player.getPlayer().teleport(getBlueSpawn());
		}
		
	}
	
	
	public void setRedMerchantSpawn(Location loc){
		Methods.addLocation("Merchants.Red", loc);
	}
	
	public void setBlueMerchantSpawn(Location loc){
		Methods.addLocation("Merchants.Blue", loc);
	}
	
	
	public Location getRedMerchantSpawn(){
		return Methods.getLocation("Merchants.Red");
	}
	
	public Location getBlueMerchantSpawn(){
		return Methods.getLocation("Merchants.Blue");
	}
	
	
	public void setLobbySpawn(Location loc){
		
		
		  if (!this.plugin.arenas.contains("Arenas." + getName() + ".Lobby.Spawn"))
		    {
		      this.plugin.arenas.addDefault("Lobby.Spawn" + ".X", loc.getX());
		      this.plugin.arenas.addDefault("Lobby.Spawn" + ".Y", loc.getY());
		      this.plugin.arenas.addDefault("Lobby.Spawn" + ".Z", loc.getZ());
		      this.plugin.arenas.addDefault("Lobby.Spawn" + ".World", loc.getWorld().getName());
		      this.plugin.arenas.addDefault("Lobby.Spawn" + ".Pitch", loc.getPitch());
		      this.plugin.arenas.addDefault("Lobby.Spawn" + ".Yaw", loc.getYaw());
		    }
		    else
		    {
		      this.plugin.arenas.set("Lobby.Spawn" + ".X", loc.getX());
		      this.plugin.arenas.set("Lobby.Spawn" + ".Y", loc.getY());
		      this.plugin.arenas.set("Lobby.Spawn" + ".Z", loc.getZ());
		      this.plugin.arenas.set("Lobby.Spawn" + ".World", loc.getWorld().getName());
		      this.plugin.arenas.set("Lobby.Spawn" + ".Pitch", loc.getPitch());
		      this.plugin.arenas.set("Lobby.Spawn" + ".Yaw", loc.getYaw());
		    }
		    Methods.saveYamls();
	  }
	  	
	public void setPoint1Spawn(Location loc){
		  if (!this.plugin.arenas.contains("Points.1.X"))
		    {
		      this.plugin.arenas.addDefault("Points.1" + ".X", loc.getX());
		      this.plugin.arenas.addDefault("Points.1" + ".Y", loc.getY());
		      this.plugin.arenas.addDefault("Points.1" + ".Z", loc.getZ());
		      this.plugin.arenas.addDefault("Points.1" + ".World", loc.getWorld().getName());
		      this.plugin.arenas.addDefault("Points.1" + ".Pitch", loc.getPitch());
		      this.plugin.arenas.addDefault("Points.1" + ".Yaw", loc.getYaw());
		    }
		    else
		    {
		      this.plugin.arenas.set("Points.1" + ".X", loc.getX());
		      this.plugin.arenas.set("Points.1" + ".Y", loc.getY());
		      this.plugin.arenas.set("Points.1" + ".Z", loc.getZ());
		      this.plugin.arenas.set("Points.1" + ".World", loc.getWorld().getName());
		      this.plugin.arenas.set("Points.1" + ".Pitch", loc.getPitch());
		      this.plugin.arenas.set("Points.1" + ".Yaw", loc.getYaw());
		    }
		    Methods.saveYamls();
	  }
	
	public void setPoint2Spawn(Location loc){
		  if (!this.plugin.arenas.contains("Points.2.X"))
		    {
		      this.plugin.arenas.addDefault("Points.2" + ".X", loc.getX());
		      this.plugin.arenas.addDefault("Points.2" + ".Y", loc.getY());
		      this.plugin.arenas.addDefault("Points.2" + ".Z", loc.getZ());
		      this.plugin.arenas.addDefault("Points.2" + ".World", loc.getWorld().getName());
		      this.plugin.arenas.addDefault("Points.2" + ".Pitch", loc.getPitch());
		      this.plugin.arenas.addDefault("Points.2" + ".Yaw", loc.getYaw());
		    }
		    else
		    {
		      this.plugin.arenas.set("Points.2" + ".X", loc.getX());
		      this.plugin.arenas.set("Points.2" + ".Y", loc.getY());
		      this.plugin.arenas.set("Points.2" + ".Z", loc.getZ());
		      this.plugin.arenas.set("Points.2" + ".World", loc.getWorld().getName());
		      this.plugin.arenas.set("Points.2" + ".Pitch", loc.getPitch());
		      this.plugin.arenas.set("Points.2" + ".Yaw", loc.getYaw());
		    }
		    Methods.saveYamls();
	  }
	
	public void setPoint3Spawn(Location loc){
		  if (!this.plugin.arenas.contains("Points.3.X"))
		    {
		      this.plugin.arenas.addDefault("Points.3" + ".X", loc.getX());
		      this.plugin.arenas.addDefault("Points.3" + ".Y", loc.getY());
		      this.plugin.arenas.addDefault("Points.3" + ".Z", loc.getZ());
		      this.plugin.arenas.addDefault("Points.3" + ".World", loc.getWorld().getName());
		      this.plugin.arenas.addDefault("Points.3" + ".Pitch", loc.getPitch());
		      this.plugin.arenas.addDefault("Points.3" + ".Yaw", loc.getYaw());
		    }
		    else
		    {
		      this.plugin.arenas.set("Points.3" + ".X", loc.getX());
		      this.plugin.arenas.set("Points.3" + ".Y", loc.getY());
		      this.plugin.arenas.set("Points.3" + ".Z", loc.getZ());
		      this.plugin.arenas.set("Points.3" + ".World", loc.getWorld().getName());
		      this.plugin.arenas.set("Points.3" + ".Pitch", loc.getPitch());
		      this.plugin.arenas.set("Points.3" + ".Yaw", loc.getYaw());
		    }
		    Methods.saveYamls();
	  }
		
	public Location getPoint3Spawn(){
		  if (this.plugin.arenas.contains("Points.3" + ".World"))
		    {
		      Location loc = new Location(Bukkit.getWorld(this.plugin.arenas.getString("Points.3" + ".World")), 
		        this.plugin.arenas.getDouble("Points.3" + ".X"), 
		        this.plugin.arenas.getDouble("Points.3" + ".Y"), 
		        this.plugin.arenas.getDouble("Points.3" + ".Z"));
		      loc.setPitch((float)this.plugin.arenas.getDouble("Points.3" + ".Pitch"));
		      loc.setYaw((float)this.plugin.arenas.getDouble("Points.3" + ".Yaw"));
		      return loc;
		    }
		    return null;
	  }
	
	
	public Location getPoint2Spawn(){
		  if (this.plugin.arenas.contains("Points.2" + ".World"))
		    {
		      Location loc = new Location(Bukkit.getWorld(this.plugin.arenas.getString("Points.2" + ".World")), 
		        this.plugin.arenas.getDouble("Points.2" + ".X"), 
		        this.plugin.arenas.getDouble("Points.2" + ".Y"), 
		        this.plugin.arenas.getDouble("Points.2" + ".Z"));
		      loc.setPitch((float)this.plugin.arenas.getDouble("Points.2" + ".Pitch"));
		      loc.setYaw((float)this.plugin.arenas.getDouble("Points.2" + ".Yaw"));
		      return loc;
		    }
		    return null;
	  }
	
	
	public Location getPoint1Spawn(){
		  if (this.plugin.arenas.contains("Points.1" + ".World"))
		    {
		      Location loc = new Location(Bukkit.getWorld(this.plugin.arenas.getString("Points.1" + ".World")), 
		        this.plugin.arenas.getDouble("Points.1" + ".X"), 
		        this.plugin.arenas.getDouble("Points.1" + ".Y"), 
		        this.plugin.arenas.getDouble("Points.1" + ".Z"));
		      loc.setPitch((float)this.plugin.arenas.getDouble("Points.1" + ".Pitch"));
		      loc.setYaw((float)this.plugin.arenas.getDouble("Points.1" + ".Yaw"));
		      return loc;
		    }
		    return null;
	  }
	
    private void setBlocksColor(Location l, DyeColor color) {
        if(color == DyeColor.RED){
            Methods.setBlockFast(l, Material.RED_WOOL);
        } else {
            if (color == DyeColor.BLUE) {
                Methods.setBlockFast(l, Material.BLUE_WOOL);
            }
        }
    }
	public void setPoint1DecBlocks(DyeColor color){
		for(Location l : getPoint1DecLocs()){
		    //Quick fix to update to 1.15.X, will eventually rework
           setBlocksColor(l, color);

		}
	}

	public void setPoint2DecBlocks(DyeColor color){
		for(Location l : getPoint2DecLocs()){
            setBlocksColor(l, color);

        }
	}
	

	public void setPoint3DecBlocks(DyeColor color){
		for(Location l : getPoint3DecLocs()){
            setBlocksColor(l, color);


        }
	}
	
	
	public Location getLobbySpawn(){
		  
		  if(this.plugin == null){
				Bukkit.broadcastMessage("Plugin is null....");
				this.plugin = Methods.getPlugin();
			}
		  
		  
		  
		  if (this.plugin.arenas.contains("Lobby.Spawn" + ".World"))
		    {
			
			  
			  World world = Bukkit.getWorld(this.plugin.arenas.getString("Lobby.Spawn.World"));
			  double x = this.plugin.arenas.getDouble("Lobby.Spawn.X");
			  double y = this.plugin.arenas.getDouble("Lobby.Spawn.Y");
			  double z = this.plugin.arenas.getDouble("Lobby.Spawn.Z");
			  
			  
			  
			  Location loc = new Location(world, 
		       x, 
		       y, 
		       z);
		      loc.setPitch((float)this.plugin.arenas.getDouble( "Lobby.Spawn" + ".Pitch"));
		      loc.setYaw((float)this.plugin.arenas.getDouble("Lobby.Spawn" + ".Yaw"));
		     
		      return loc;
		    }
		 
		    return null;
	  }
	  
    public HashSet<Location> getRedGunSpawns(){
      HashSet<Location> locs = new HashSet<Location>();
      for(int i = 1;i <= plugin.arenas.getInt("Red.GunSpawn.Counter"); i++ ){
         Location loc = new Location(
                 Bukkit.getWorld(plugin.arenas.getString("Red.GunSpawn." + i + ".World")),
                 plugin.arenas.getDouble("Red.GunSpawn." + i + ".X"),
                 plugin.arenas.getDouble("Red.GunSpawn." + i + ".Y"),
                 plugin.arenas.getDouble("Red.GunSpawn." + i + ".Z"));
         loc.setPitch((float) plugin.arenas.getInt("Red.GunSpawn." + i + ".Pitch"));
         loc.setYaw((float) plugin.arenas.getDouble("Red.GunSpawn." + i + ".Yaw"));

         locs.add(loc);
      }


      return locs;
}
	  
	  
	  
	  public HashSet<Location> getPoint3DecLocs(){
		  HashSet<Location> locs = new HashSet<Location>();
		  for(int i = 3;i <= plugin.arenas.getInt("Points.3.Dec.Counter"); i++ ){
			 Location loc = new Location(
					 Bukkit.getWorld(plugin.arenas.getString("Points.3.Dec." + i + ".World")),
					 plugin.arenas.getDouble("Points.3.Dec." + i + ".X"),
					 plugin.arenas.getDouble("Points.3.Dec." + i + ".Y"),
					 plugin.arenas.getDouble("Points.3.Dec." + i + ".Z"));
			 loc.setPitch((float) plugin.arenas.getInt("Points.3.Dec." + i + ".Pitch"));
			 loc.setYaw((float) plugin.arenas.getDouble("Points.3.Dec." + i + ".Yaw"));
			 if(loc != null){
			 locs.add(loc);
			 }
		  }
		  
		  
		  return locs;
	  }
	  
	  
	  public HashSet<Location> getPoint2DecLocs(){
		  HashSet<Location> locs = new HashSet<Location>();
		  for(int i = 2;i <= plugin.arenas.getInt("Points.2.Dec.Counter"); i++ ){
			 Location loc = new Location(
					 Bukkit.getWorld(plugin.arenas.getString("Points.2.Dec." + i + ".World")),
					 plugin.arenas.getDouble("Points.2.Dec." + i + ".X"),
					 plugin.arenas.getDouble("Points.2.Dec." + i + ".Y"),
					 plugin.arenas.getDouble("Points.2.Dec." + i + ".Z"));
			 loc.setPitch((float) plugin.arenas.getInt("Points.2.Dec." + i + ".Pitch"));
			 loc.setYaw((float) plugin.arenas.getDouble("Points.2.Dec." + i + ".Yaw"));
			 if(loc != null){
			 locs.add(loc);
			 }
		  }
		  
		  
		  return locs;
	  }
	  
	  
	  public HashSet<Location> getPoint1DecLocs(){
		  HashSet<Location> locs = new HashSet<Location>();
		  for(int i = 1;i <= plugin.arenas.getInt("Points.1.Dec.Counter"); i++ ){
			 Location loc = new Location(
					 Bukkit.getWorld(plugin.arenas.getString("Points.1.Dec." + i + ".World")),
					 plugin.arenas.getDouble("Points.1.Dec." + i + ".X"),
					 plugin.arenas.getDouble("Points.1.Dec." + i + ".Y"),
					 plugin.arenas.getDouble("Points.1.Dec." + i + ".Z"));
			 loc.setPitch((float) plugin.arenas.getInt("Points.1.Dec." + i + ".Pitch"));
			 loc.setYaw((float) plugin.arenas.getDouble("Points.1.Dec." + i + ".Yaw"));
			 if(loc != null){
			 locs.add(loc);
			 }
		  }
		  
		  
		  return locs;
	  }
	  
	  public static void setup(){
		  
		  //Firework Locations--------------
		  for(int i = 1;i <= Methods.getPlugin().arenas.getInt("Fireworks.Counter"); i++ ){
				 Location loc = new Location(
						 Bukkit.getWorld(Methods.getPlugin().arenas.getString("Fireworks." + i + ".World")),
						 Methods.getPlugin().arenas.getDouble("Fireworks." + i + ".X"),
						 Methods.getPlugin().arenas.getDouble("Fireworks." + i + ".Y"),
						 Methods.getPlugin().arenas.getDouble("Fireworks." + i + ".Z"));
				 loc.setPitch((float) Methods.getPlugin().arenas.getInt("Fireworks." + i + ".Pitch"));
				 loc.setYaw((float) Methods.getPlugin().arenas.getDouble("Fireworks." + i + ".Yaw"));
				 if(loc != null){
					 Methods.getArena().getFireworkLocs().add(loc);
				 }
			  }
		  
		  
		  //Dec blocks--------------------
		  
		  for(ContestPoint point : ContestPoint.values()){
			 point.setup();
		  }
		  
		  //power up locs
		  
		  Methods.getArena().powerUps = Methods.getMultiLocation("PowerUps");
		  
		  
		
		  
		  
	  }
	  
	  
	  
	  
	  private int fwTask;
	  private void playFireworks(){
		  final Random r = new Random();
		  
		  fwTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){

			public void run() {
				if(getState() != GameState.STOPPING){
					Bukkit.getScheduler().cancelTask(fwTask);
					return;
				}
				
				
				List<Location> locs = getFireworkLocs();
				int rindex = r.nextInt(locs.size());
				
				try {
				//	FireworkEffectPlayer.playFirework(locs.get(rindex), Methods.getRandomFireworkEffect());
					Methods.playFireworkRandomColor(locs.get(rindex));
				
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
			}
			  
		  }, 0, 4);
	  }
	  private List<Location> fireworkLocs = new ArrayList<Location>();
	  
	  public List<Location> getFireworkLocs(){
		  List<Location> locs = new ArrayList<Location>();
		  for(int i = 1;i <= plugin.arenas.getInt("Fireworks.Counter"); i++ ){
			 Location loc = new Location(
					 Bukkit.getWorld(plugin.arenas.getString("Fireworks." + i + ".World")),
					 plugin.arenas.getDouble("Fireworks." + i + ".X"),
					 plugin.arenas.getDouble("Fireworks." + i + ".Y"),
					 plugin.arenas.getDouble("Fireworks." + i + ".Z"));
			 loc.setPitch((float) plugin.arenas.getInt("Fireworks." + i + ".Pitch"));
			 loc.setYaw((float) plugin.arenas.getDouble("Fireworks." + i + ".Yaw"));
			 if(loc != null){
			 locs.add(loc);
			 }
		  }
		  
		  
		  //return locs;
		  
		  return fireworkLocs;
	  }
	  
	  
	  public void addFireworkLocation(Location loc){
		  if (!this.plugin.arenas.contains("Fireworks.Counter"))
		    {
		      int counter = 1;
			  this.plugin.arenas.addDefault("Fireworks." +counter +  ".X", loc.getX());
		      this.plugin.arenas.addDefault("Fireworks." +counter + ".Y", loc.getY());
		      this.plugin.arenas.addDefault("Fireworks." +counter+ ".Z", loc.getZ());
		      this.plugin.arenas.addDefault("Fireworks." +counter+ ".World", loc.getWorld().getName());
		      this.plugin.arenas.addDefault("Fireworks." +counter+ ".Pitch", loc.getPitch());
		      this.plugin.arenas.addDefault("Fireworks." +counter+ ".Yaw", loc.getYaw());
		      this.plugin.arenas.addDefault("Fireworks.Counter", counter);

		    }
		    else
		    {
		    	int counter = plugin.arenas.getInt("Fireworks.Counter");
		    	counter++;
		    	this.plugin.arenas.set("Fireworks." +counter+ ".X", loc.getX());
		      this.plugin.arenas.set("Fireworks." +counter+ ".Y", loc.getY());
		      this.plugin.arenas.set("Fireworks." +counter+ ".Z", loc.getZ());
		      this.plugin.arenas.set("Fireworks." +counter+ ".World", loc.getWorld().getName());
		      this.plugin.arenas.set("Fireworks." +counter+ ".Pitch", loc.getPitch());
		      this.plugin.arenas.set("Fireworks." +counter+ ".Yaw", loc.getYaw());
		      this.plugin.arenas.set("Fireworks.Counter", counter);

		    }
		    Methods.saveYamls();
		    fireworkLocs.add(loc);
	  }
	  
	  
	  public void addRedExplosion(Location loc){
		  if (!this.plugin.arenas.contains("Red.Explosions.Counter"))
		    {
		      int counter = 1;
			  this.plugin.arenas.addDefault("Red.Explosions." +counter +  ".X", loc.getX());
		      this.plugin.arenas.addDefault("Red.Explosions." +counter + ".Y", loc.getY());
		      this.plugin.arenas.addDefault("Red.Explosions." +counter+ ".Z", loc.getZ());
		      this.plugin.arenas.addDefault("Red.Explosions." +counter+ ".World", loc.getWorld().getName());
		      this.plugin.arenas.addDefault("Red.Explosions." +counter+ ".Pitch", loc.getPitch());
		      this.plugin.arenas.addDefault("Red.Explosions." +counter+ ".Yaw", loc.getYaw());
		      this.plugin.arenas.addDefault("Red.Explosions.Counter", counter);

		    }
		    else
		    {
		    	int counter = plugin.arenas.getInt("Red.Explosions.Counter");
		    	counter++;
		    	this.plugin.arenas.set("Red.Explosions." +counter+ ".X", loc.getX());
		      this.plugin.arenas.set("Red.Explosions." +counter+ ".Y", loc.getY());
		      this.plugin.arenas.set("Red.Explosions." +counter+ ".Z", loc.getZ());
		      this.plugin.arenas.set("Red.Explosions." +counter+ ".World", loc.getWorld().getName());
		      this.plugin.arenas.set("Red.Explosions." +counter+ ".Pitch", loc.getPitch());
		      this.plugin.arenas.set("Red.Explosions." +counter+ ".Yaw", loc.getYaw());
		      this.plugin.arenas.set("Red.Explosions.Counter", counter);

		    }
		    Methods.saveYamls();
		    
	  }
	  
	  
	  public void addPoint3Dec(Location loc){
		  if (!this.plugin.arenas.contains("Points.3.Dec.Counter"))
		    {
		      int counter = 1;
			  this.plugin.arenas.addDefault("Points.3.Dec." +counter +  ".X", loc.getX());
		      this.plugin.arenas.addDefault("Points.3.Dec." +counter + ".Y", loc.getY());
		      this.plugin.arenas.addDefault("Points.3.Dec." +counter+ ".Z", loc.getZ());
		      this.plugin.arenas.addDefault("Points.3.Dec." +counter+ ".World", loc.getWorld().getName());
		      this.plugin.arenas.addDefault("Points.3.Dec." +counter+ ".Pitch", loc.getPitch());
		      this.plugin.arenas.addDefault("Points.3.Dec." +counter+ ".Yaw", loc.getYaw());
		      this.plugin.arenas.addDefault("Points.3.Dec.Counter", counter);

		    }
		    else
		    {
		    	int counter = plugin.arenas.getInt("Points.3.Dec.Counter");
		    	counter++;
		    	this.plugin.arenas.set("Points.3.Dec." +counter+ ".X", loc.getX());
		      this.plugin.arenas.set("Points.3.Dec." +counter+ ".Y", loc.getY());
		      this.plugin.arenas.set("Points.3.Dec." +counter+ ".Z", loc.getZ());
		      this.plugin.arenas.set("Points.3.Dec." +counter+ ".World", loc.getWorld().getName());
		      this.plugin.arenas.set("Points.3.Dec." +counter+ ".Pitch", loc.getPitch());
		      this.plugin.arenas.set("Points.3.Dec." +counter+ ".Yaw", loc.getYaw());
		      this.plugin.arenas.set("Points.3.Dec.Counter", counter);

		    }
		    Methods.saveYamls();
		    
	  }
	  
	  public void addPoint2Dec(Location loc){
		  if (!this.plugin.arenas.contains("Points.2.Dec.Counter"))
		    {
		      int counter = 1;
			  this.plugin.arenas.addDefault("Points.2.Dec." +counter +  ".X", loc.getX());
		      this.plugin.arenas.addDefault("Points.2.Dec." +counter + ".Y", loc.getY());
		      this.plugin.arenas.addDefault("Points.2.Dec." +counter+ ".Z", loc.getZ());
		      this.plugin.arenas.addDefault("Points.2.Dec." +counter+ ".World", loc.getWorld().getName());
		      this.plugin.arenas.addDefault("Points.2.Dec." +counter+ ".Pitch", loc.getPitch());
		      this.plugin.arenas.addDefault("Points.2.Dec." +counter+ ".Yaw", loc.getYaw());
		      this.plugin.arenas.addDefault("Points.2.Dec.Counter", counter);

		    }
		    else
		    {
		    	int counter = plugin.arenas.getInt("Points.2.Dec.Counter");
		    	counter++;
		    	this.plugin.arenas.set("Points.2.Dec." +counter+ ".X", loc.getX());
		      this.plugin.arenas.set("Points.2.Dec." +counter+ ".Y", loc.getY());
		      this.plugin.arenas.set("Points.2.Dec." +counter+ ".Z", loc.getZ());
		      this.plugin.arenas.set("Points.2.Dec." +counter+ ".World", loc.getWorld().getName());
		      this.plugin.arenas.set("Points.2.Dec." +counter+ ".Pitch", loc.getPitch());
		      this.plugin.arenas.set("Points.2.Dec." +counter+ ".Yaw", loc.getYaw());
		      this.plugin.arenas.set("Points.2.Dec.Counter", counter);

		    }
		    Methods.saveYamls();
		    
	  }
	  
	  public void addPoint1Dec(Location loc){
		  if (!this.plugin.arenas.contains("Points.1.Dec.1.X"))
		    {
		      int counter = 1;
			  this.plugin.arenas.addDefault("Points.1.Dec." +counter +  ".X", loc.getX());
		      this.plugin.arenas.addDefault("Points.1.Dec." +counter + ".Y", loc.getY());
		      this.plugin.arenas.addDefault("Points.1.Dec." +counter+ ".Z", loc.getZ());
		      this.plugin.arenas.addDefault("Points.1.Dec." +counter+ ".World", loc.getWorld().getName());
		      this.plugin.arenas.addDefault("Points.1.Dec." +counter+ ".Pitch", loc.getPitch());
		      this.plugin.arenas.addDefault("Points.1.Dec." +counter+ ".Yaw", loc.getYaw());
		      this.plugin.arenas.addDefault("Points.1.Dec.Counter", counter);

		    }
		    else
		    {
		    	int counter = plugin.arenas.getInt("Points.1.Dec.Counter");
		    	counter++;
		    	this.plugin.arenas.set("Points.1.Dec." +counter+ ".X", loc.getX());
		      this.plugin.arenas.set("Points.1.Dec." +counter+ ".Y", loc.getY());
		      this.plugin.arenas.set("Points.1.Dec." +counter+ ".Z", loc.getZ());
		      this.plugin.arenas.set("Points.1.Dec." +counter+ ".World", loc.getWorld().getName());
		      this.plugin.arenas.set("Points.1.Dec." +counter+ ".Pitch", loc.getPitch());
		      this.plugin.arenas.set("Points.1.Dec." +counter+ ".Yaw", loc.getYaw());
		      this.plugin.arenas.set("Points.1.Dec.Counter", counter);

		    }
		    Methods.saveYamls();
		    
	  }
	  
	  public void addRedGunSpawn(Location loc){
		  if (!this.plugin.arenas.contains("Red.GunSpawn.Counter"))
		    {
		      int counter = 1;
			  this.plugin.arenas.set("Red.GunSpawn." +counter +  ".X", loc.getX());
		      this.plugin.arenas.set("Red.GunSpawn." +counter + ".Y", loc.getY());
		      this.plugin.arenas.set("Red.GunSpawn." +counter+ ".Z", loc.getZ());
		      this.plugin.arenas.set("Red.GunSpawn." +counter+ ".World", loc.getWorld().getName());
		      this.plugin.arenas.set("Red.GunSpawn." +counter+ ".Pitch", loc.getPitch());
		      this.plugin.arenas.set("Red.GunSpawn." +counter+ ".Yaw", loc.getYaw());
		      this.plugin.arenas.set("Red.GunSpawn.Counter", counter);

		    }
		    else
		    {
		    	int counter = plugin.arenas.getInt("Red.GunSpawn.Counter");
		    	counter++;
		    	this.plugin.arenas.set("Red.GunSpawn." +counter+ ".X", loc.getX());
		      this.plugin.arenas.set("Red.GunSpawn." +counter+ ".Y", loc.getY());
		      this.plugin.arenas.set("Red.GunSpawn." +counter+ ".Z", loc.getZ());
		      this.plugin.arenas.set("Red.GunSpawn." +counter+ ".World", loc.getWorld().getName());
		      this.plugin.arenas.set("Red.GunSpawn." +counter+ ".Pitch", loc.getPitch());
		      this.plugin.arenas.set("Red.GunSpawn." +counter+ ".Yaw", loc.getYaw());
		      this.plugin.arenas.set("Red.GunSpawn.Counter", counter);

		    }
		    Methods.saveYamls();
		    
	  }
	  
	  
	  
	  public HashSet<Location> getBlueGunSpawns(){
		  HashSet<Location> locs = new HashSet<Location>();
		  for(int i = 1;i <= plugin.arenas.getInt("Blue.GunSpawn.Counter"); i++ ){
			 Location loc = new Location(
					 Bukkit.getWorld(plugin.arenas.getString("Blue.GunSpawn." + i + ".World")),
					 plugin.arenas.getDouble("Blue.GunSpawn." + i + ".X"),
					 plugin.arenas.getDouble("Blue.GunSpawn." + i + ".Y"),
					 plugin.arenas.getDouble("Blue.GunSpawn." + i + ".Z"));
			 loc.setPitch((float) plugin.arenas.getInt("Blue.GunSpawn." + i + ".Pitch"));
			 loc.setYaw((float) plugin.arenas.getDouble("Blue.GunSpawn." + i + ".Yaw"));
			 if(loc != null){
			 locs.add(loc);
			 }
		  }
		  
		  
		  return locs;
	  }
	  
	  public void addBlueGunSpawn(Location loc){
		  if (!this.plugin.arenas.contains("Blue.GunSpawn.Counter"))
		    {
		      int counter = 1;
			  this.plugin.arenas.set("Blue.GunSpawn." +counter +  ".X", loc.getX());
		      this.plugin.arenas.set("Blue.GunSpawn." +counter + ".Y", loc.getY());
		      this.plugin.arenas.set("Blue.GunSpawn." +counter+ ".Z", loc.getZ());
		      this.plugin.arenas.set("Blue.GunSpawn." +counter+ ".World", loc.getWorld().getName());
		      this.plugin.arenas.set("Blue.GunSpawn." +counter+ ".Pitch", loc.getPitch());
		      this.plugin.arenas.set("Blue.GunSpawn." +counter+ ".Yaw", loc.getYaw());
		      this.plugin.arenas.set("Blue.GunSpawn.Counter", counter);

		    }
		    else
		    {
		    	int counter = plugin.arenas.getInt("Blue.GunSpawn.Counter");
		    	counter++;
		    	this.plugin.arenas.set("Blue.GunSpawn." +counter+ ".X", loc.getX());
		      this.plugin.arenas.set("Blue.GunSpawn." +counter+ ".Y", loc.getY());
		      this.plugin.arenas.set("Blue.GunSpawn." +counter+ ".Z", loc.getZ());
		      this.plugin.arenas.set("Blue.GunSpawn." +counter+ ".World", loc.getWorld().getName());
		      this.plugin.arenas.set("Blue.GunSpawn." +counter+ ".Pitch", loc.getPitch());
		      this.plugin.arenas.set("Blue.GunSpawn." +counter+ ".Yaw", loc.getYaw());
		      this.plugin.arenas.set("Blue.GunSpawn.Counter", counter);

		    }
		    Methods.saveYamls();
		    
	  }
	  
	  
	
	public void setBlueSpawn(Location loc){
		  if (!this.plugin.arenas.contains("Blue.Spawn"))
		    {
		      this.plugin.arenas.addDefault("Blue.Spawn" + ".X", loc.getX());
		      this.plugin.arenas.addDefault("Blue.Spawn" + ".Y", loc.getY());
		      this.plugin.arenas.addDefault("Blue.Spawn" + ".Z", loc.getZ());
		      this.plugin.arenas.addDefault("Blue.Spawn" + ".World", loc.getWorld().getName());
		      this.plugin.arenas.addDefault("Blue.Spawn" + ".Pitch", loc.getPitch());
		      this.plugin.arenas.addDefault("Blue.Spawn" + ".Yaw", loc.getYaw());
		    }
		    else
		    {
		      this.plugin.arenas.set("Blue.Spawn" + ".X", loc.getX());
		      this.plugin.arenas.set("Blue.Spawn" + ".Y", loc.getY());
		      this.plugin.arenas.set("Blue.Spawn" + ".Z", loc.getZ());
		      this.plugin.arenas.set("Blue.Spawn" + ".World", loc.getWorld().getName());
		      this.plugin.arenas.set("Blue.Spawn" + ".Pitch", loc.getPitch());
		      this.plugin.arenas.set("Blue.Spawn" + ".Yaw", loc.getYaw());
		    }
		    Methods.saveYamls();
		    
	  }
	  
	  
	  public Location getBlueSpawn(){
		  if (this.plugin.arenas.contains("Blue.Spawn" + ".World"))
		    {
		      Location loc = new Location(Bukkit.getWorld(this.plugin.arenas.getString("Blue.Spawn" + ".World")), 
		        this.plugin.arenas.getDouble("Blue.Spawn" + ".X"), 
		        this.plugin.arenas.getDouble("Blue.Spawn" + ".Y"), 
		        this.plugin.arenas.getDouble("Blue.Spawn" + ".Z"));
		      loc.setPitch((float)this.plugin.arenas.getDouble("Blue.Spawn" + ".Pitch"));
		      loc.setYaw((float)this.plugin.arenas.getDouble("Blue.Spawn" + ".Yaw"));
		      return loc;
		    }
		    return null;
	  }
	
	
	
	public void setRedSpawn(Location loc){
		  if (!this.plugin.arenas.contains("Red.Spawn"))
		    {
		      this.plugin.arenas.addDefault("Red.Spawn" + ".X", loc.getX());
		      this.plugin.arenas.addDefault("Red.Spawn" + ".Y", loc.getY());
		      this.plugin.arenas.addDefault("Red.Spawn" + ".Z", loc.getZ());
		      this.plugin.arenas.addDefault("Red.Spawn" + ".World", loc.getWorld().getName());
		      this.plugin.arenas.addDefault("Red.Spawn" + ".Pitch", loc.getPitch());
		      this.plugin.arenas.addDefault("Red.Spawn" + ".Yaw", loc.getYaw());
		    }
		    else
		    {
		      this.plugin.arenas.set("Red.Spawn" + ".X", loc.getX());
		      this.plugin.arenas.set("Red.Spawn" + ".Y", loc.getY());
		      this.plugin.arenas.set("Red.Spawn" + ".Z", loc.getZ());
		      this.plugin.arenas.set("Red.Spawn" + ".World", loc.getWorld().getName());
		      this.plugin.arenas.set("Red.Spawn" + ".Pitch", loc.getPitch());
		      this.plugin.arenas.set("Red.Spawn" + ".Yaw", loc.getYaw());
		    }
		    Methods.saveYamls();
	  }
	  
	  
	  public Location getRedSpawn(){
		  if (this.plugin.arenas.contains("Red.Spawn" + ".World"))
		    {
		      Location loc = new Location(Bukkit.getWorld(this.plugin.arenas.getString("Red.Spawn" + ".World")), 
		        this.plugin.arenas.getDouble("Red.Spawn" + ".X"), 
		        this.plugin.arenas.getDouble("Red.Spawn" + ".Y"), 
		        this.plugin.arenas.getDouble("Red.Spawn" + ".Z"));
		      loc.setPitch((float)this.plugin.arenas.getDouble("Red.Spawn" + ".Pitch"));
		      loc.setYaw((float)this.plugin.arenas.getDouble("Red.Spawn" + ".Yaw"));
		      return loc;
		    }
		    return null;
	  }
	
	  public void refreshScoreboard(){
		  for(ArenaPlayer ap : redTeam.getPlayerList()){
			  ap.getPlayer().setScoreboard(getScoreboard());
		  }
		  for(ArenaPlayer ap : blueTeam.getPlayerList()){
			  ap.getPlayer().setScoreboard(getScoreboard());
		  }
	  
	  }
	  
	  private void spawnGuns(){
		  for(Location loc: getRedGunSpawns()){
			  Entity e = Methods.spawnEntityGunSheep(loc, DyeColor.RED).getBukkitEntity();
			  guns.add(e);
			  redGuns.add(e.getUniqueId());
			  if(CrystalClash.useHolograms){
					 Hologram hg = HologramsAPI.createHologram(Methods.getPlugin(), loc.clone().add(0, 5.8, 0));
				 	hg.appendTextLine(ChatColor.RED + "Machine Gun");
				 	hg.appendItemLine(new ItemStack(Material.ARROW));
				 	hg.appendTextLine(ChatColor.RED + "|");
				 	hg.appendTextLine(ChatColor.RED + "V");
				} 
		  }
		  
		  for(Location loc: getBlueGunSpawns()){
			 Entity e = Methods.spawnEntityGunSheep(loc, DyeColor.BLUE).getBukkitEntity();
			 guns.add(e);
			 blueGuns.add(e.getUniqueId());
			 if(CrystalClash.useHolograms){
				 Hologram hg = HologramsAPI.createHologram(Methods.getPlugin(), loc.clone().add(0, 5.8, 0));
			 	hg.appendTextLine(ChatColor.BLUE + "Machine Gun");
			 	hg.appendItemLine(new ItemStack(Material.ARROW));
			 	hg.appendTextLine(ChatColor.BLUE + "|");
			 	hg.appendTextLine(ChatColor.BLUE + "V");
			 } 
		  }
	  }
	  
	  private void spawnMerchants(){
		  Entity e = Methods.spawnVillagerMerchant(getRedMerchantSpawn()).getBukkitEntity();
		  Entity e1 = Methods.spawnVillagerMerchant(getBlueMerchantSpawn()).getBukkitEntity();
		  merchants.add(e1);
		  merchants.add(e);
		  if(CrystalClash.useHolograms){
		  Hologram hg = HologramsAPI.createHologram(Methods.getPlugin(), getRedMerchantSpawn().add(0, 2.8, 0));
		  hg.appendTextLine(ChatColor.AQUA + "Merchant");
		  
		  Hologram hg1 = HologramsAPI.createHologram(Methods.getPlugin(), getBlueMerchantSpawn().add(0,2.8,0)); 
		  hg1.appendTextLine(ChatColor.AQUA + "Merchant");
		  }
	}
	  
	  public void setBlueCrystal(Location loc){
		  if (!this.plugin.arenas.contains("Blue.Crystal"))
		    {
		      this.plugin.arenas.addDefault("Blue.Crystal" + ".X", loc.getX());
		      this.plugin.arenas.addDefault("Blue.Crystal" + ".Y", loc.getY());
		      this.plugin.arenas.addDefault("Blue.Crystal" + ".Z", loc.getZ());
		      this.plugin.arenas.addDefault("Blue.Crystal" + ".World", loc.getWorld().getName());
		      this.plugin.arenas.addDefault("Blue.Crystal" + ".Pitch", loc.getPitch());
		      this.plugin.arenas.addDefault("Blue.Crystal" + ".Yaw", loc.getYaw());
		    }
		    else
		    {
		      this.plugin.arenas.set("Blue.Crystal" + ".X", loc.getX());
		      this.plugin.arenas.set("Blue.Crystal" + ".Y", loc.getY());
		      this.plugin.arenas.set("Blue.Crystal" + ".Z", loc.getZ());
		      this.plugin.arenas.set("Blue.Crystal" + ".World", loc.getWorld().getName());
		      this.plugin.arenas.set("Blue.Crystal" + ".Pitch", loc.getPitch());
		      this.plugin.arenas.set("Blue.Crystal" + ".Yaw", loc.getYaw());
		    }
		    Methods.saveYamls();
	  }
	  
	  public void setRedCrystal(Location loc){
		  if (!this.plugin.arenas.contains("Red.Crystal"))
		    {
		      this.plugin.arenas.addDefault("Red.Crystal" + ".X", loc.getX());
		      this.plugin.arenas.addDefault("Red.Crystal" + ".Y", loc.getY());
		      this.plugin.arenas.addDefault("Red.Crystal" + ".Z", loc.getZ());
		      this.plugin.arenas.addDefault("Red.Crystal" + ".World", loc.getWorld().getName());
		      this.plugin.arenas.addDefault("Red.Crystal" + ".Pitch", loc.getPitch());
		      this.plugin.arenas.addDefault("Red.Crystal" + ".Yaw", loc.getYaw());
		      
		    }
		    else
		    {
		      this.plugin.arenas.set("Red.Crystal" + ".X", loc.getX());
		      this.plugin.arenas.set("Red.Crystal" + ".Y", loc.getY());
		      this.plugin.arenas.set("Red.Crystal" + ".Z", loc.getZ());
		      this.plugin.arenas.set("Red.Crystal" + ".World", loc.getWorld().getName());
		      this.plugin.arenas.set("Red.Crystal" + ".Pitch", loc.getPitch());
		      this.plugin.arenas.set("Red.Crystal" + ".Yaw", loc.getYaw());
		    }
		    Methods.saveYamls();
	  }
	  
	  
	  public void giveCompass(Player player){
		  ItemStack compass = new ItemStack(Material.COMPASS);
		  ItemMeta meta = compass.getItemMeta();
		  if(isBlue(player)){
			  meta.setDisplayName(ChatColor.RED + "Red Crystal's" + ChatColor.GRAY + " Location");
			  player.setCompassTarget(getRedCrystal());
		  }
		  if(isRed(player)){
			  meta.setDisplayName(ChatColor.BLUE + "Blue Crystal's" + ChatColor.GRAY + " Location");
			  player.setCompassTarget(getBlueCrystal());
		  }
		  compass.setItemMeta(meta);
		  player.getInventory().addItem(compass);
	  }
	  
	  public Location getBlueCrystal(){
		  if (this.plugin.arenas.contains("Blue.Crystal" + ".World"))
		    {
		      Location loc = new Location(Bukkit.getWorld(this.plugin.arenas.getString("Blue.Crystal" + ".World")), 
		        this.plugin.arenas.getDouble("Blue.Crystal" + ".X"), 
		        this.plugin.arenas.getDouble("Blue.Crystal" + ".Y"), 
		        this.plugin.arenas.getDouble("Blue.Crystal" + ".Z"));
		      loc.setPitch((float)this.plugin.arenas.getDouble("Blue.Crystal" + ".Pitch"));
		      loc.setYaw((float)this.plugin.arenas.getDouble("Blue.Crystal" + ".Yaw"));
		      return loc;
		    }
		    return null;
	  }
	  
	  public Location getRedCrystal(){
		  if (this.plugin.arenas.contains("Red.Crystal" + ".World"))
		    {
		      Location loc = new Location(Bukkit.getWorld(this.plugin.arenas.getString("Red.Crystal" + ".World")), 
		        this.plugin.arenas.getDouble("Red.Crystal" + ".X"), 
		        this.plugin.arenas.getDouble("Red.Crystal" + ".Y"), 
		        this.plugin.arenas.getDouble("Red.Crystal" + ".Z"));
		      loc.setPitch((float)this.plugin.arenas.getDouble("Red.Crystal" + ".Pitch"));
		      loc.setYaw((float)this.plugin.arenas.getDouble("Red.Crystal" + ".Yaw"));
		      return loc;
		    }
		    return null;
	  }
	  
	  
	  
	  public void playStartingSound(){
		  for(ArenaPlayer ap : lobbyPlayers){
			  ap.getPlayer().playSound(ap.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
		  }
	  }
	  
	  
	  
	  
	  private void startCrystalTasks(){
		  
		  crystalId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){

			public void run() {
				Location loc = getBlueCrystal().clone();
				Location loc1 = getRedCrystal().clone();
				loc.getWorld().spawnParticle(Particle.PORTAL,loc.add(0.5,0.5,0.5),20 );
                loc.getWorld().spawnParticle(Particle.PORTAL,loc1.add(0.5,0.5,0.5),20 );
            }
			  
		  }, 0, 10);
	  
	  }
	  
	  public void stopCrystalTasks(){
		  Bukkit.getScheduler().cancelTask(crystalId);
	  }
	 /* 
	  private void refreshTags(){
		  for(Player p : Bukkit.getOnlinePlayers()){
			  TagAPI.refreshPlayer(p);
		  }
	  }
	  */
	  
	  public void sendAllActionBarMessage(String message){
		  for(Player p : Bukkit.getOnlinePlayers()){
			  Methods.sendActionBar(p, message);
		  }
	  }
	  
	  public void start(){
		if(getState() != GameState.LOBBY)
			return;
		
		setState(GameState.STARTING);
		
		counter = plugin.getConfig().getInt("Countdown");
		
		final Thread t = new Thread(new Runnable()
		{

			public void run() {
				CrystalClash.loadGameData();
			}
	
		});
		
		t.start();
		
		countdownId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){

			public void run() {
				if(counter > 0){
					if(counter == 45){
						sendAll(ChatColor.GREEN.toString() + counter + ChatColor.GRAY + " seconds left.");
						sendAllActionBarMessage(ChatColor.GREEN.toString() + counter + ChatColor.GRAY + " seconds left.");
						playStartingSound();
					}
					
					if(counter == 30){
						sendAll(ChatColor.GREEN.toString() + counter + ChatColor.GRAY + " seconds left.");
						sendAllActionBarMessage(ChatColor.GREEN.toString() + counter + ChatColor.GRAY + " seconds left.");
						playStartingSound();
					}
					
					if(counter == 15){
						sendAll(ChatColor.GREEN.toString() + counter + ChatColor.GRAY + " seconds left.");
						sendAllActionBarMessage(ChatColor.GREEN.toString() + counter + ChatColor.GRAY + " seconds left.");
						playStartingSound();
					}
					
					if(counter <= 10){
						if(counter == 1){
							sendAll(ChatColor.GREEN.toString() + counter + ChatColor.GRAY + " second left.");
							sendAllActionBarMessage(ChatColor.GREEN.toString() + counter + ChatColor.GRAY + " seconds left.");
						
						}else{
							sendAll(ChatColor.GREEN.toString() + counter + ChatColor.GRAY + " seconds left.");
							sendAllActionBarMessage(ChatColor.GREEN.toString() + counter + ChatColor.GRAY + " second left.");

						}
						playStartingSound();
					}
					
					
					counter--;
				}else{
					Bukkit.getScheduler().cancelTask(countdownId);
					onStart(t);
				}
			}
			
		}, 0, 20);
	}
	  
	  private void onStart(Thread t)
	  {
		  setState(GameState.INGAME);
			clearWorldEntities();
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(!CrystalClash.FFA)
			{
				setTeams();
				startCrystalTasks(); 
			}

			spawnGuns();
			spawnMerchants();
			setClasses();
			spawnPlayers();
			createScoreboard();
			healAll();
			startPointTask();
			setNeutralBlocks();
			TipManager.startAutomaticBroadcast();
			TipManager.startAllBroadcast();
			for(ContestPoint points : ContestPoint.values()){
			    points.startTask();
            }

			if(CrystalClash.useHolograms){
				addExtraHolograms();
			}

			sendStartGameTitle();
			sendAll(ChatColor.GREEN + "The game has begun!");
			
			
	  }
	  
	  
	  
	  
	  private void clearWorldEntities()
	  {
		  for(Entity e : getRedSpawn().getWorld().getEntities())
		  {
			  e.remove();
		  }
	  }
	  
	
	 private void addExtraHolograms(){

	      Hologram rhg = HologramsAPI.createHologram(Methods.getPlugin(), redTeam.getCrystal().clone().add(0.5, 1.5, 0.5));
	      rhg.appendTextLine(redTeam.getColoredName() + " Crystal");

         Hologram bhg = HologramsAPI.createHologram(Methods.getPlugin(), blueTeam.getCrystal().clone().add(0.5, 1.5, 0.5));
         bhg.appendTextLine(blueTeam.getColoredName() + " Crystal");




		 
		 for(ContestPoint point : ContestPoint.values()){
			 Hologram hg = HologramsAPI.createHologram(Methods.getPlugin(), point.getPointBlock().clone().add(0.5, 1.7, 0.5));
			 hg.appendTextLine(ChatColor.GRAY + "Capture Point: " + ChatColor.GREEN + point.getValue());
			 
		 }
		 
		 
	 }
	 
	  
	
	  
	 

	private void showEndGameTitle(){
		  String title = "Winners: " + getWinner();


	  	for(Player p : Bukkit.getOnlinePlayers()){
			  Methods.sendTitle(p,title,ChatColor.GREEN, PacketPlayOutTitle.EnumTitleAction.TITLE);
			  p.sendMessage(ChatColor.DARK_AQUA + "End game scores:"); 

            String prefix = ChatColor.GRAY + "[" + redTeam.getChatColor() + redTeam.getName() + ChatColor.GRAY + "] ";
            p.sendMessage(prefix + redTeam.getName() + " Points: " + redTeam.getChatColor() + redTeam.getTeamPoints() + ", " + ChatColor.GRAY + "CrystalHealth: " + redTeam.getChatColor() + redTeam.getCrystalHealth());

            String bprefix = ChatColor.GRAY + "[" + blueTeam.getChatColor() + blueTeam.getName() + ChatColor.GRAY + "] ";
            p.sendMessage(bprefix + blueTeam.getName() + " Points: " + blueTeam.getChatColor() + blueTeam.getTeamPoints() + ", " + ChatColor.GRAY + "CrystalHealth: " + blueTeam.getChatColor() + blueTeam.getCrystalHealth());


        }
	  }
	  

	private void sendStartGameTitle(){
		for(ArenaPlayer ap : redTeam.getPlayerList()){
			Methods.sendTitle(ap.getPlayer(),"Game has begun!",ChatColor.GREEN, PacketPlayOutTitle.EnumTitleAction.TITLE);
			Methods.sendTitle(ap.getPlayer(),"You are on team Red", ChatColor.RED, PacketPlayOutTitle.EnumTitleAction.SUBTITLE);
		}
		for(ArenaPlayer ap : blueTeam.getPlayerList()){
			Methods.sendTitle(ap.getPlayer(),"Game has begun!",ChatColor.GREEN, PacketPlayOutTitle.EnumTitleAction.TITLE);
			Methods.sendTitle(ap.getPlayer(),"You are on team Red", ChatColor.BLUE, PacketPlayOutTitle.EnumTitleAction.SUBTITLE);
			
		}
		
		
	}
	  
	private void despawnGuns(){
		for(Entity e : guns){
			if(!e.isValid())
				continue;
			e.remove();
		}
		guns.clear();
	}
	
	private void despawnMerchants(){
		for(Entity e : merchants){
			if(!e.isValid())
				continue;
			e.remove();
		}
		merchants.clear();
	}
	
	private void despawnAll(){
		for(Entity e : getRedSpawn().getWorld().getEntities()){
			if(e instanceof Player)
				continue;
			
			e.remove();
		}
	}
	
	private void setClasses(){
		for(ArenaPlayer ap: redTeam.getPlayerList()){
			setInventory(ap.getPlayer());
			setPassivePotionEffect(ap.getPlayer());
		}
		for(ArenaPlayer ap: blueTeam.getPlayerList()){
			setInventory(ap.getPlayer());
			setPassivePotionEffect(ap.getPlayer());
		}
	}
	
	
	
	
	public Scoreboard getScoreboard(){
		return scoreboard;
	}
	

	
	private void removePlayerSetup(){
		for(ArenaPlayer player : redTeam.getPlayerList()){
			if(player.getPlayer().isInsideVehicle()){
				player.getPlayer().leaveVehicle();
			}
			for(PotionEffect effect : player.getPlayer().getActivePotionEffects()){
				player.getPlayer().removePotionEffect(effect.getType());
			}
			if(BuildingManager.ownsTurret(player.getPlayer())){
				BuildingManager.getTurret(player.getPlayer()).remove();
			}
			if(ExplosivesListener.hasManager(player.getPlayer())){
				ExplosivesListener.getManager(player.getPlayer()).clearMines();
			}
			if(BlockPlaceManager.hasBlockPlaceManager(player.getPlayer())){
				for(Block b : BlockPlaceManager.getManager(player.getPlayer()).getBlocks()){
					b.setType(Material.AIR);
				}
			}
			player.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			ArenaManager.removeArena(player.getPlayer());
			player.getPlayer().setHealth(20.0);

		}
		for(ArenaPlayer player : blueTeam.getPlayerList()){
			if(player.getPlayer().isInsideVehicle()){
				player.getPlayer().leaveVehicle();
			}
			
			for(PotionEffect effect : player.getPlayer().getActivePotionEffects()){
				player.getPlayer().removePotionEffect(effect.getType());
			}
			if(BuildingManager.ownsTurret(player.getPlayer())){
				BuildingManager.getTurret(player.getPlayer()).remove();
			}
			if(ExplosivesListener.hasManager(player.getPlayer())){
				ExplosivesListener.getManager(player.getPlayer()).clearMines();
			}
			if(BlockPlaceManager.hasBlockPlaceManager(player.getPlayer())){
				for(Block b : BlockPlaceManager.getManager(player.getPlayer()).getBlocks()){
					b.setType(Material.AIR);
				}
			}
			player.getPlayer().setHealth(20.0);
			player.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			ArenaManager.removeArena(player.getPlayer());
		}
		if(lobbyPlayers.size() > 0){
			for(ArenaPlayer player : lobbyPlayers){
				ArenaManager.removeArena(player.getPlayer());
			}
		}
	}

	private void setNeutralBlocks(){
		
		for(Location l : getPoint1DecLocs()){Methods.setBlockFast(l, Material.WHITE_WOOL);}
		for(Location l : getPoint2DecLocs()){Methods.setBlockFast(l, Material.WHITE_WOOL);}
		for(Location l : getPoint3DecLocs()){Methods.setBlockFast(l, Material.WHITE_WOOL);}

        Methods.setBlockFast(getPoint1Spawn(), Material.WHITE_WOOL);
        Methods.setBlockFast(getPoint2Spawn(), Material.WHITE_WOOL);
        Methods.setBlockFast(getPoint3Spawn(), Material.WHITE_WOOL);


    }
	
	private void kickAll(){
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		  out.writeUTF("Connect");
		  out.writeUTF(plugin.getConfig().getString("BackServer"));
		 
		
		for(Player p : Bukkit.getOnlinePlayers()){
		//	p.getPlayer().sendPluginMessage(Methods.getPlugin(), "BungeeCord", out.toByteArray()); //TODO
			p.kickPlayer("Game is restarting..");;
		}
	}
	

	private void waitTime(){
		
		for(ContestPoint points : ContestPoint.values()){
			points.stopTask();
		}
		
		Bukkit.broadcastMessage(ChatColor.RED + "Game is restarting in 20 seconds...");
		
		playFireworks();
		showEndGameTitle();
		
		for(ArenaPlayer aPlayer : allPlayers)
		{
			if(aPlayer.getPlayer() != null){
			aPlayer.getPlayer().getInventory().clear();
			aPlayer.getPlayer().setAllowFlight(true);;
			aPlayer.getPlayer().setFlying(true);
			}
			int pointsEarned = aPlayer.getAwardPoints();
			if(aPlayer.getTeam() == getWinner())
			{
				pointsEarned += 7;
				if(aPlayer.getPlayer() != null)
				aPlayer.getPlayer().getInventory().addItem(Methods.getWinnerItem());
				MySQLUtil.incrementScore(aPlayer.getUUID(), Leaderboard.MOST_WINS); 
			}else{
				pointsEarned += 3;
				if(aPlayer.getPlayer() != null)
				aPlayer.getPlayer().getInventory().addItem(Methods.getLoserItem());
				MySQLUtil.incrementScore(aPlayer.getUUID(), Leaderboard.MOST_LOSSES); 
			}
			MySQLUtil.addPoints(aPlayer.getUUID(), pointsEarned);
			if(aPlayer.getPlayer() != null){
				aPlayer.sendMessage(ChatColor.GRAY + "You have been awarded " + ChatColor.GOLD.toString() + ChatColor.BOLD.toString()  + pointsEarned + " points" + ChatColor.GRAY +
						" based on your gameplay performance.");
			}
		}
	
		if(plugin == null){
			plugin = Methods.getPlugin();
		}
		
		//Leaderboard.updateAllSigns();
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			public void run() {
				kickAll();
				Bukkit.setWhitelist(true);
				setState(GameState.RESTARTING); 
			}
			
		}, 20 * 18);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			public void run() {
				Bukkit.shutdown();
			}
			
		}, 20 * 20);
		
	}
	
	
	private boolean canWarnRed = true;
	private boolean canWarnBlue = true;
	
	private void startRedWarnCooldown(){
		canWarnRed = false;
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			public void run() {
				canWarnRed = true;
			}
			
		}, 20 * 10);
	}
	
	private void startBlueWarnCooldown(){
		canWarnBlue = false;
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			public void run() {
				canWarnBlue = true;
			}
			
		}, 20 * 10);
	}
	
	public void sendAlertMessage(Player player, String message){
		player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW.toString() + "Alert" + ChatColor.DARK_GRAY + "] "
				+ChatColor.GRAY + message);
	}
	
	public void sendAllAlertMessage(String message){
		sendBlueAlertMessage(message);
		sendRedAlertMessage(message);
	}
	
	public void sendRedAlertMessage(String message){
		for(ArenaPlayer ap :redTeam.getPlayerList()){
			sendAlertMessage(ap.getPlayer(), message);
		}
	}
	
	public void sendBlueAlertMessage(String message){
		for(ArenaPlayer ap: blueTeam.getPlayerList()){
			sendAlertMessage(ap.getPlayer(),message);
		}
	}

	public void warnRedPlayers(){
		
		if(!canWarnRed)
			return;
		
		for(ArenaPlayer ap : redTeam.getPlayerList()){
			sendAlertMessage(ap.getPlayer(), "Your crystal is being attacked!");
			Methods.sendTitle(ap.getPlayer(),"Your Crystal!",ChatColor.YELLOW, PacketPlayOutTitle.EnumTitleAction.TITLE);
			Methods.sendTitle(ap.getPlayer(),"Your crystal is being attacked!",ChatColor.RED, PacketPlayOutTitle.EnumTitleAction.TITLE);
			new PlingWarningEffect(ap.getPlayer()).start();
		}
		
		startRedWarnCooldown();
		
	}
	
	public void sendRedAlert(String message){
		sendRedAlertMessage(message);
		sendRedAlertPling();
	}
	
	public void sendBlueAlert(String message){
		sendBlueAlertMessage(message);
		sendBlueAlertPling();
	}
	
	public void sendBlueAlertPling(){
		for(ArenaPlayer ap: blueTeam.getPlayerList()){
			new PlingWarningEffect(ap.getPlayer()).start();
		}
	}
	
	public void sendRedAlertPling(){
		for(ArenaPlayer ap: redTeam.getPlayerList()){
			new PlingWarningEffect(ap.getPlayer()).start();
		}
	}
	
	
	
	
	public void sendAlertPling(Player player){
		new PlingWarningEffect(player).start();
	}
	
	public void warnBluePlayers(){
		if(!canWarnBlue)
			return;
		
		for(ArenaPlayer ap: blueTeam.getPlayerList()){
			sendAlertMessage(ap.getPlayer(), "Your crystal is being attacked!");
			new PlingWarningEffect(ap.getPlayer()).start();
		}
		startBlueWarnCooldown();
	}
	
	/*
	public void stop(){
		if(state == GameState.STARTING){
			Bukkit.getScheduler().cancelTask(countdownId);
		}
		state = GameState.STOPPING;
		
		despawnGuns();
		despawnMerchants();
		despawnAll();
		
		
		
		for(Player p : Bukkit.getOnlinePlayers()){
			if(p == null)
				continue;
			
			if(BuildingManager.ownsTurret(p)){
				BuildingManager.getTurret(p).remove();
			}
			if(ExplosivesListener.hasManager(p)){
				ExplosivesListener.getManager(p).clearMines();
			}
			if(BlockPlaceManager.hasBlockPlaceManager(p)){
				for(Block b : BlockPlaceManager.getManager(p).getBlocks()){
					b.setType(Material.AIR);
				}
			}
			
			p.kickPlayer("CrystalClash reloading...");
		}
	
		
		
		
		lobbyPlayers.clear();
		//redTeam.getPlayerList().clear();
		//bluePlayers.clear();
		guns.clear();
		//redCrystalHealth = 50;
		//blueCrystalHealth = 50;
		bluePoints = 0;
		redPoints = 0;
		ArcherListener.map.clear();
		
		this.point1 = Point.NEUTRAL;
		this.point2 = Point.NEUTRAL;
		this.point3 = Point.NEUTRAL;
		setNeutralBlocks();
		state = GameState.LOBBY;
		
		
		
		Bukkit.shutdown(); 

		
	}
	*/
	
	public void stop(StopReason reason){
		if(getState() == GameState.STARTING){
			Bukkit.getScheduler().cancelTask(countdownId);
		}
		setState(GameState.STOPPING);
		despawnGuns();
		despawnAll();
		despawnMerchants();
		
		for(Player p : Bukkit.getOnlinePlayers()){
			if(p == null)
				continue;
			
			if(BuildingManager.ownsTurret(p)){
				BuildingManager.getTurret(p).remove();
			}
			if(ExplosivesListener.hasManager(p)){
				ExplosivesListener.getManager(p).clearMines();
			}
			if(BlockPlaceManager.hasBlockPlaceManager(p)){
				for(Block b : BlockPlaceManager.getManager(p).getBlocks()){
					b.setType(Material.AIR);
				}
			}
		}
		
		clearAll();
		Bukkit.getLogger().info("Despawned Guns!");
		removePlayerSetup(); 
		stopCrystalTasks();
		
		if(reason == StopReason.GAME_END){
			waitTime();
		}
		
		
		
		lobbyPlayers.clear();
		guns.clear();
		this.point1 = Point.NEUTRAL;
		this.point2 = Point.NEUTRAL;
		this.point3 = Point.NEUTRAL;
		ArcherListener.map.clear();
		setNeutralBlocks();
		
		if(reason != StopReason.GAME_END){
			setState(GameState.LOBBY);
			
			
		}
	}
	
	
	public int getTotalPlayers(){
		
		if(getState() == GameState.INGAME){
			return (blueTeam.getPlayerList().size() + redTeam.getPlayerList().size());
		}else{
			return lobbyPlayers.size();
		}
		
	}
	
	public String getName() {
		return name;
	}
	
	public HashSet<ArenaPlayer> getRedPlayers() {
		return redTeam.getPlayerList();
	}
	public HashSet<ArenaPlayer> getBluePlayers() {
		return blueTeam.getPlayerList();
	}
	
	public boolean hasPlayer(Player player){
		
		for(ArenaPlayer aplayer: redTeam.getPlayerList()){
			if(player.getName() == aplayer.getName()){
				return true;
			}
		}
		
		for(ArenaPlayer aplayer: blueTeam.getPlayerList()){
			if(player.getName() == aplayer.getName()){
				return true;
			}
		}
		for(ArenaPlayer aplayer: lobbyPlayers){
			if(player.getName() == aplayer.getName()){
				return true;
			}
		}
		
		return false;
		
	}
	
	public int getMaxPlayers(){
		return plugin.getConfig().getInt("MaxPlayers");
	}
	
	public GameState getState() {
		return gstate;
	}
	public void setState(GameState state) {
		GameStateChangeEvent event = new GameStateChangeEvent(state,this.gstate);
		this.gstate = state;
		Bukkit.getPluginManager().callEvent(event);

	}
	
	public void setWinner(ArenaTeam winner) {
		this.winner = winner;
		
	}
	
	public ArenaTeam getWinner() {
		return winner;
	}
	
	
}
