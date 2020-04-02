package me.artish1.CrystalClash;

import me.artish1.CrystalClash.Arena.*;
import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Cooldown.Cooldown;
import me.artish1.CrystalClash.Listeners.*;
import me.artish1.CrystalClash.Listeners.Classes.*;
import me.artish1.CrystalClash.Menu.menus.Menus;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.Util.MySQLUtil;
import me.artish1.CrystalClash.Util.UnzipUtility;
import me.artish1.CrystalClash.crates.Crates;
import me.artish1.CrystalClash.entities.CustomEntityType;
import me.artish1.CrystalClash.killstreaks.Killstreak;
import me.artish1.CrystalClash.leaderboards.Leaderboard;
import me.artish1.CrystalClash.other.ClassInventories;
import me.artish1.CrystalClash.other.TipManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class CrystalClash extends JavaPlugin{
	public Methods m;
	
	public final Logger logger = Logger.getLogger("Minecraft");
	
	public static boolean FFA = false;
	public me.artish1.CrystalClash.Util.Config mysql = new me.artish1.CrystalClash.Util.Config(this, "mysql");
	public static String DONATOR_PERMISSION = "crystalclash.donator";
	
	public Arena mainArena; 
	
	public Arena getArena(){
		
		return mainArena;

	}
	
	
	public code.husky.mysql.MySQL MySQL;
	public Connection c = null; 
	
	public File kitsFile;
	public FileConfiguration kits;
	public File playersFile;
	public FileConfiguration players;
	public File arenasFile;
	public FileConfiguration arenas;
	public static boolean useHolograms;	
	/*
	public void calculateKDRS(){
		for(String s : Leaderboard.MOST_KILLS.getEntries()){
			OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(UUID.fromString(s));
			double kills = Leaderboard.MOST_KILLS.getScore(oPlayer.getUniqueId());
			double deaths = Leaderboard.MOST_DEATHS.getScore(oPlayer.getUniqueId());
			
			double kdr = kills/deaths;
			
			Leaderboard.BEST_KDR.setScore(UUID.fromString(s), kdr); 
			
		}
	}
	
	/*
	 * Creating Table: 
	 * CREATE TABLE CrazyTest1(
    name VARCHAR(60) NOT NULL,
    UUID VARCHAR(60) NOT NULL,
    points SMALLINT UNSIGNED NOT NULL DEFAULT '0',
     kills SMALLINT UNSIGNED NOT NULL DEFAULT '0',
      deaths SMALLINT UNSIGNED NOT NULL DEFAULT '0',
       crystal_breaks SMALLINT UNSIGNED NOT NULL DEFAULT '0',
        points_captured SMALLINT UNSIGNED NOT NULL DEFAULT '0',
         wins SMALLINT UNSIGNED NOT NULL DEFAULT '0',
          losses SMALLINT UNSIGNED NOT NULL DEFAULT '0',
           kdr SMALLINT UNSIGNED NOT NULL DEFAULT '0',
    guardian TINYINT(1) NOT NULL DEFAULT '0',
    PRIMARY KEY(UUID)
    )
	 * 
	 * C
	 * 
	 * 
	 * Inputting rows:
	 * INSERT INTO CrazyTest1 VALUES('name','uuid',points,0)
	 * 
	 * updating:
	 * UPDATE crazytest1 SET guardian=1 WHERE uuid = 'c3ce322d-5c9e-4a6b-9fa5-67d34d7ed5fa'
	 * 
	 * 
	 * 
	 * 
	 */
	



	private void mysqlSetup()
	{
		
		
		//Upgrade Tables
		try {
			Statement statement = c.createStatement();
			statement.executeUpdate("CREATE TABLE scoutinfo(uuid VARCHAR(60) NOT NULL, dashcd SMALLINT UNSIGNED NOT NULL DEFAULT '6', helmet VARCHAR(40) NOT NULL DEFAULT 'CHAINMAIL_HELMET', "
					+ "chestplate VARCHAR(40) NOT NULL DEFAULT 'air', leggings VARCHAR(40) NOT NULL DEFAULT 'CHAINMAIL_LEGGINGS', boots VARCHAR(40) NOT NULL DEFAULT 'CHAINMAIL_BOOTS', sword VARCHAR(40) NOT NULL DEFAULT 'IRON_SWORD',"
					+ "level SMALLINT UNSIGNED NOT NULL DEFAULT '1', experience SMALLINT UNSIGNED NOT NULL DEFAULT '0')" ); 
			
			
			
			
		} catch (SQLException e) {
			getLogger().info("Table: 'scoutinfo' already created!"); 
		}
		
		try {
			Statement statement = c.createStatement();
			statement.executeUpdate("CREATE TABLE archerinfo(uuid VARCHAR(60) NOT NULL, helmet VARCHAR(40) NOT NULL DEFAULT 'LEATHER_HELMET', "
					+ "chestplate VARCHAR(40) NOT NULL DEFAULT 'LEATHER_CHESTPLATE', leggings VARCHAR(40) NOT NULL DEFAULT 'LEATHER_LEGGINGS', boots VARCHAR(40) NOT NULL DEFAULT 'LEATHER_BOOTS', "
					+ "sword VARCHAR(40) NOT NULL DEFAULT 'STONE_SWORD', "
					+ "level SMALLINT UNSIGNED NOT NULL DEFAULT '1', experience SMALLINT UNSIGNED NOT NULL DEFAULT '0',"
					+ "ammo SMALLINT UNSIGNED NOT NULL DEFAULT '64', firebarrage TINYINT(1) NOT NULL DEFAULT '0')" ); 
			
			
			
			
		} catch (SQLException e) {
			getLogger().info("Table: 'archerinfo' already created!"); 
			
			try {
				Statement statement = c.createStatement();
				statement.executeUpdate("ALTER TABLE archerinfo ADD COLUMN firebarrage TINYINT(1) NOT NULL DEFAULT '0'");	
			} catch (SQLException e1) {
				
			}
		}
		
		
		try {
			Statement statement;
			statement = c.createStatement();
			statement.executeUpdate("CREATE TABLE sniperinfo(uuid VARCHAR(60) NOT NULL, helmet VARCHAR(40) NOT NULL DEFAULT 'AIR', "
					+ "chestplate VARCHAR(40) NOT NULL DEFAULT 'AIR', leggings VARCHAR(40) NOT NULL DEFAULT 'AIR', boots VARCHAR(40) NOT NULL DEFAULT 'AIR',"
					+ "level SMALLINT UNSIGNED NOT NULL DEFAULT '1', experience SMALLINT UNSIGNED NOT NULL DEFAULT '0',"
					+ "ammo SMALLINT UNSIGNED NOT NULL DEFAULT '64', claymore SMALLINT UNSIGNED NOT NULL DEFAULT '2')" ); 
		} catch (SQLException e) {
			getLogger().info("Table: 'sniperinfo' already created!");
		}
		
		try {
			Statement statement;
			statement = c.createStatement();
			statement.executeUpdate("CREATE TABLE assassininfo(uuid VARCHAR(60) NOT NULL, "
					+ "helmet VARCHAR(40) NOT NULL DEFAULT 'DIAMOND_HELMET', "
					+ "chestplate VARCHAR(40) NOT NULL DEFAULT 'AIR', "
					+ "leggings VARCHAR(40) NOT NULL DEFAULT 'AIR', "
					+ "boots VARCHAR(40) NOT NULL DEFAULT 'AIR', "
					+ "level SMALLINT UNSIGNED NOT NULL DEFAULT '1', "
					+ "experience SMALLINT UNSIGNED NOT NULL DEFAULT '0', "
					+ "poisonedsword TINYINT(1) NOT NULL DEFAULT '0', "
					+ "PRIMARY KEY(uuid))"); 
			getLogger().info("Created assassininfo!");
		} catch (SQLException e) {
			getLogger().info("Creating table assassininfo: " + e.getMessage());
		}
		
		try {
			Statement statement;
			statement = c.createStatement();
			statement.executeUpdate("CREATE TABLE engineerinfo(uuid VARCHAR(60) NOT NULL, "
					+ "helmet VARCHAR(40) NOT NULL DEFAULT 'LEATHER_HELMET', "
					+ "chestplate VARCHAR(40) NOT NULL DEFAULT 'LEATHER_CHESTPLATE', "
					+ "leggings VARCHAR(40) NOT NULL DEFAULT 'LEATHER_LEGGINGS', "
					+ "boots VARCHAR(40) NOT NULL DEFAULT 'LEATHER_BOOTS', "
					+ "sword VARCHAR(40) NOT NULL DEFAULT 'IRON_SWORD', "
					+ "level SMALLINT UNSIGNED NOT NULL DEFAULT '1', "
					+ "experience SMALLINT UNSIGNED NOT NULL DEFAULT '0', "
					+ "rocketlauncher TINYINT(1) NOT NULL DEFAULT '0', "
					+ "PRIMARY KEY(uuid))"); 
			getLogger().info("Created engineerinfo table!");
		} catch (SQLException e) {
			getLogger().info("Creating table engineerinfo: " + e.getMessage());
		}
		
		try {
			Statement statement;
			statement = c.createStatement();
			statement.executeUpdate("CREATE TABLE spiderinfo(uuid VARCHAR(60) NOT NULL, "
					+ "helmet VARCHAR(40) NOT NULL DEFAULT 'LEATHER_HELMET', "
					+ "chestplate VARCHAR(40) NOT NULL DEFAULT 'LEATHER_CHESTPLATE', "
					+ "leggings VARCHAR(40) NOT NULL DEFAULT 'LEATHER_LEGGINGS', "
					+ "boots VARCHAR(40) NOT NULL DEFAULT 'LEATHER_BOOTS', "
					+ "sword VARCHAR(40) NOT NULL DEFAULT 'IRON_SWORD', "
					+ "level SMALLINT UNSIGNED NOT NULL DEFAULT '1', "
					+ "experience SMALLINT UNSIGNED NOT NULL DEFAULT '0', "
					+ "wallclimb TINYINT(1) NOT NULL DEFAULT '0', "
					+ "PRIMARY KEY(uuid))"); 
			getLogger().info("Created spiderinfo table!");
		} catch (SQLException e) {
			getLogger().info("Creating table spiderinfo: " + e.getMessage());
		}
		
		try {
			Statement statement;
			statement = c.createStatement();
			statement.executeUpdate("CREATE TABLE mageinfo(uuid VARCHAR(60) NOT NULL, "
					+ "helmet VARCHAR(40) NOT NULL DEFAULT 'AIR', "
					+ "chestplate VARCHAR(40) NOT NULL DEFAULT 'LEATHER_CHESTPLATE', "
					+ "leggings VARCHAR(40) NOT NULL DEFAULT 'AIR', "
					+ "boots VARCHAR(40) NOT NULL DEFAULT 'LEATHER_BOOTS', "
					+ "sword VARCHAR(40) NOT NULL DEFAULT 'WOOD_SWORD', "
					+ "level SMALLINT UNSIGNED NOT NULL DEFAULT '1', "
					+ "experience SMALLINT UNSIGNED NOT NULL DEFAULT '0', "
					+ "lstormcd TINYINT(1) NOT NULL DEFAULT '0', "
					+ "mshowercd TINYINT(1) NOT NULL DEFAULT '0', "
					+ "PRIMARY KEY(uuid))"); 
			getLogger().info("Created mageinfo table!");
		} catch (SQLException e) {
			getLogger().info("Creating table mageinfo: " + e.getMessage());
		}
		
		try {
			Statement statement;
			statement = c.createStatement();
			
			statement.executeUpdate("CREATE TABLE guardianinfo(uuid VARCHAR(60) NOT NULL, "
					+ "helmet VARCHAR(40) NOT NULL DEFAULT 'GOLD_HELMET', "
					+ "chestplate VARCHAR(40) NOT NULL DEFAULT 'GOLD_CHESTPLATE', "
					+ "leggings VARCHAR(40) NOT NULL DEFAULT 'GOLD_LEGGINGS', "
					+ "boots VARCHAR(40) NOT NULL DEFAULT 'GOLD_BOOTS', "
					+ "sword VARCHAR(40) NOT NULL DEFAULT 'GOLD_SWORD', "
					+ "level SMALLINT UNSIGNED NOT NULL DEFAULT '1', "
					+ "experience SMALLINT UNSIGNED NOT NULL DEFAULT '0', "
					+ "hitslow TINYINT(1) NOT NULL DEFAULT '0', "
					+ "PRIMARY KEY(uuid))"); 
			getLogger().info("Created guardianinfo table!");
		} catch (SQLException e) {
			getLogger().info("Creating table guardianinfo: " + e.getMessage());
		}
		
		
		try {
			Statement statement;
			statement = c.createStatement();
			statement.executeUpdate("CREATE TABLE tankinfo(uuid VARCHAR(60) NOT NULL, "
					+ "helmet VARCHAR(40) NOT NULL DEFAULT 'DIAMOND_HELMET', "
					+ "chestplate VARCHAR(40) NOT NULL DEFAULT 'DIAMOND_CHESTPLATE', "
					+ "leggings VARCHAR(40) NOT NULL DEFAULT 'DIAMOND_LEGGINGS', "
					+ "boots VARCHAR(40) NOT NULL DEFAULT 'DIAMOND_BOOTS', "
					+ "sword VARCHAR(40) NOT NULL DEFAULT 'STONE_SWORD', "
					+ "level SMALLINT UNSIGNED NOT NULL DEFAULT '1', "
					+ "experience SMALLINT UNSIGNED NOT NULL DEFAULT '0', "
					+ "helmetench TINYINT(1) NOT NULL DEFAULT '0', "
					+ "chestench TINYINT(1) NOT NULL DEFAULT '0', "
					+ "leggingsench TINYINT(1) NOT NULL DEFAULT '0', "
					+ "bootsench TINYINT(1) NOT NULL DEFAULT '0', "
					+ "PRIMARY KEY(uuid))"); 
			getLogger().info("Created tankinfo table!");
		} catch (SQLException e) {
			getLogger().info("Creating table tankinfo: " + e.getMessage());
		}
		
		try {
			Statement statement;
			statement = c.createStatement();
			statement.executeUpdate("CREATE TABLE earthinfo(uuid VARCHAR(60) NOT NULL, "
					+ "helmet VARCHAR(40) NOT NULL DEFAULT 'LEATHER_HELMET', "
					+ "chestplate VARCHAR(40) NOT NULL DEFAULT 'CHAINMAIL_CHESTPLATE', "
					+ "leggings VARCHAR(40) NOT NULL DEFAULT 'AIR', "
					+ "boots VARCHAR(40) NOT NULL DEFAULT 'AIR', "
					+ "sword VARCHAR(40) NOT NULL DEFAULT 'STONE_SWORD', "
					+ "level SMALLINT UNSIGNED NOT NULL DEFAULT '1', "
					+ "experience SMALLINT UNSIGNED NOT NULL DEFAULT '0', "
					+ "helmetench TINYINT(1) NOT NULL DEFAULT '0', "
					+ "chestench TINYINT(1) NOT NULL DEFAULT '0', "
					+ "PRIMARY KEY(uuid))"); 
			getLogger().info("Created earthinfo table!");
		} catch (SQLException e) {
			getLogger().info("Creating table earthinfo: " + e.getMessage());
		}
		
		
		try {
			Statement statement;
			statement = c.createStatement();
			statement.executeUpdate("CREATE TABLE enderinfo(uuid VARCHAR(60) NOT NULL, "
					+ "helmet VARCHAR(40) NOT NULL DEFAULT 'LEATHER_HELMET', "
					+ "chestplate VARCHAR(40) NOT NULL DEFAULT 'LEATHER_CHESTPLATE', "
					+ "leggings VARCHAR(40) NOT NULL DEFAULT 'LEATHER_LEGGINGS', "
					+ "boots VARCHAR(40) NOT NULL DEFAULT 'LEATHER_BOOTS', "
					+ "sword VARCHAR(40) NOT NULL DEFAULT 'IRON_SWORD', "
					+ "level SMALLINT UNSIGNED NOT NULL DEFAULT '1', "
					+ "experience SMALLINT UNSIGNED NOT NULL DEFAULT '0', "
					+ "helmetench TINYINT(1) NOT NULL DEFAULT '0', "
					+ "chestench TINYINT(1) NOT NULL DEFAULT '0', "
					+ "leggingsench TINYINT(1) NOT NULL DEFAULT '0', "
					+ "bootsench TINYINT(1) NOT NULL DEFAULT '0', "
					+ "swordench TINYINT(1) NOT NULL DEFAULT '0', "
					+ "PRIMARY KEY(uuid))"); 
			getLogger().info("Created enderinfo table!");
		} catch (SQLException e) {
			getLogger().info("Creating table enderinfo: " + e.getMessage());
		}
		try {
			Statement statement;
			statement = c.createStatement();
			statement.executeUpdate("CREATE TABLE necromancerinfo(uuid VARCHAR(60) NOT NULL, "
					+ "level SMALLINT UNSIGNED NOT NULL DEFAULT '1', "
					+ "experience SMALLINT UNSIGNED NOT NULL DEFAULT '0', "
					+ "sskcd TINYINT(1) NOT NULL DEFAULT '0', "
					+ "szbcd TINYINT(1) NOT NULL DEFAULT '0', "
					+ "PRIMARY KEY(uuid))"); 
			getLogger().info("Created necromancerinfo table!");
		} catch (SQLException e) {
			getLogger().info("Creating table necromancerinfo: " + e.getMessage());
		}
		
		try {
			Statement statement;
			statement = c.createStatement();
			statement.executeUpdate("CREATE TABLE explosivesinfo(uuid VARCHAR(60) NOT NULL, "
					+ "helmet VARCHAR(40) NOT NULL DEFAULT 'LEATHER_HELMET', "
					+ "chestplate VARCHAR(40) NOT NULL DEFAULT 'AIR', "
					+ "leggings VARCHAR(40) NOT NULL DEFAULT 'LEATHER_LEGGINGS', "
					+ "boots VARCHAR(40) NOT NULL DEFAULT 'LEATHER_BOOTS', "
					+ "sword VARCHAR(40) NOT NULL DEFAULT 'STONE_SWORD', "
					+ "level SMALLINT UNSIGNED NOT NULL DEFAULT '1', "
					+ "experience SMALLINT UNSIGNED NOT NULL DEFAULT '0', "
					+ "deathexplode TINYINT(1) NOT NULL DEFAULT '0', "
					+ "grenades SMALLINT UNSIGNED NOT NULL DEFAULT '32', "
					+ "c4amount SMALLINT UNSIGNED NOT NULL DEFAULT '32', "
					+ "mineamount SMALLINT UNSIGNED NOT NULL DEFAULT '5', "
					+ "c4bow TINYINT(1) NOT NULL DEFAULT '0', "
					+ "c4arrows SMALLINT UNSIGNED NOT NULL DEFAULT '16', "
					+ "PRIMARY KEY(uuid) )"); 
			getLogger().info("Created explosivesinfo table!");
		} catch (SQLException e) {
			getLogger().info("Creating table explosivesinfo: " + e.getMessage());
			
			
		}
		
		
		
		//crates
		try{
			Statement statement = c.createStatement();
			statement.executeUpdate("CREATE TABLE crates(uuid VARCHAR(60) NOT NULL, "
					+ "stone_crate SMALLINT UNSIGNED NOT NULL DEFAULT '0', "
					+ "iron_crate SMALLINT UNSIGNED NOT NULL DEFAULT '0', "
					+ "gold_crate SMALLINT UNSIGNED NOT NULL DEFAULT '0',"
					+ "redstone_crate SMALLINT UNSIGNED NOT NULL DEFAULT '0', "
					+ "diamond_crate SMALLINT UNSIGNED NOT NULL DEFAULT '0', "
					+ "ender_crate SMALLINT UNSIGNED NOT NULL DEFAULT '0', "
					+ "stone_crate_keys SMALLINT UNSIGNED NOT NULL DEFAULT '0', "
					+ "iron_crate_keys SMALLINT UNSIGNED NOT NULL DEFAULT '0', "
					+ "gold_crate_keys SMALLINT UNSIGNED NOT NULL DEFAULT '0', "
					+ "redstone_crate_keys SMALLINT UNSIGNED NOT NULL DEFAULT '0', "
					+ "diamond_crate_keys SMALLINT UNSIGNED NOT NULL DEFAULT '0', "
					+ "ender_crate_keys SMALLINT UNSIGNED NOT NULL DEFAULT '0', "
					+ "PRIMARY KEY(uuid))");
			getLogger().info("Created crates table!");
		}catch(SQLException e)
		{
			getLogger().info("Creating crate tables: " + e.getMessage());
		}
		
		
	}
	
	
	@Override
	public void onEnable() {
		  //Loading arena world
	    WorldCreator wc = new WorldCreator("Arena");
	    getServer().createWorld(wc);
	    
	    
	    System.out.println("Loaded Arena World successfully.");
	    //
		
		m = new Methods(this);
		
		//LOADING CONFIG FILES ****************************
		getLogger().info("Loading YML files!");
	    this.playersFile = new File(getDataFolder(), "players.yml");
	    this.arenasFile = new File(getDataFolder(), "arenas.yml");
	    this.kitsFile = new File(getDataFolder(), "kits.yml");
	    
	    kits = new YamlConfiguration();
	    this.arenas = new YamlConfiguration();
	    players = new YamlConfiguration();
	    Killstreak.loadKillstreaks();
	    Methods.loadYamls();
	    
	    this.arenas.options().copyDefaults(true);
	    players.options().copyDefaults(true);
	    kits.options().copyDefaults(true);
	    
	    getConfig().options().copyDefaults(true);
	    getLogger().info("Loaded YML files Successfully!");
	    
	    
	    //Initialize Arena
	    
	    mainArena = new Arena("CrystalClash",this);
	    
	    //
	    
	    
	    //********************CONFIG FILES***********************
	     ClassType.init();
		Menus.initMenus();
		
		mysql.options().copyDefaults(true);
		mysql.addDefault("User", "root");
		mysql.addDefault("Pass", "");
		mysql.addDefault("Host", "127.0.0.1");
		mysql.addDefault("Port", "3306");
		mysql.addDefault("Database", "crystalclash");
		mysql.save();
		
		MySQL = new code.husky.mysql.MySQL(this, mysql.getString("Host"), mysql.getString("Port"), mysql.getString("Database"), mysql.getString("User"), mysql.getString("Pass"));
		 
		try {
			c = MySQL.openConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
	        public void run(){
	            try {
					c = MySQL.openConnection();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
	        }
	    }, 350000, 350000);
		
	    
		if(c != null){
		
		try {
			Statement statement = c.createStatement();
			
			statement.executeUpdate("CREATE TABLE crazytest1(name VARCHAR(60) NOT NULL,UUID VARCHAR(60) NOT NULL,"+
 "points SMALLINT UNSIGNED NOT NULL DEFAULT '0',"+
     "kills SMALLINT UNSIGNED NOT NULL DEFAULT '0',"+
      "deaths SMALLINT UNSIGNED NOT NULL DEFAULT '0',"+
       "crystal_breaks SMALLINT UNSIGNED NOT NULL DEFAULT '0',"+
        "points_captured SMALLINT UNSIGNED NOT NULL DEFAULT '0',"+
         "wins SMALLINT UNSIGNED NOT NULL DEFAULT '0',"+
          "losses SMALLINT UNSIGNED NOT NULL DEFAULT '0',"+
           "kdr SMALLINT UNSIGNED NOT NULL DEFAULT '0',"+
    "guardian TINYINT(1) NOT NULL DEFAULT '0',"+
    "necromancer TINYINT(1) NOT NULL DEFAULT '0'," + 
    "PRIMARY KEY(UUID) )");
			
			getLogger().info("Created table: crazytest1");
			
		} catch (SQLException e) {
			getLogger().info("Table already exists: crazytest1"); 
			getLogger().info("Silecned MySQL Error for testing(Ignore unless ABSOLUTELY NEEDED): " + e.getMessage());

			for(ClassType type : ClassType.values()){
				if(type.isFree())
					continue;
				
				
				try {
					Statement statement = c.createStatement();
					statement.executeUpdate("ALTER TABLE crazytest1 ADD COLUMN " + type.toString().toLowerCase() + " TINYINT(1) NOT NULL DEFAULT '0'");
					getLogger().info("Adding new shop class Column: " + type.toString().toLowerCase()); 
				} catch (SQLException e1) {
					getLogger().info("Class Column already added: " + type.toString()); 
				}
				
			}
			getLogger().info("Finished going through ClassTypes"); 
		}
		
		
		
		mysqlSetup();
		
		}else{
			getLogger().info("Connection is Null!!!!");
		}
		new TipManager(this);
	  
	    
	    
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

	    CustomEntityType.registerEntities();
	    
	    
	    getConfig().addDefault("BackServer", "hub");
	    saveConfig();
	    //+++++++++CONFIGURATION DEFAULTS++++++++++++++
	    List<Integer> ids = new ArrayList<Integer>();
	    ids.add(1); //stone
	    ids.add(2);//grass
	    ids.add(3);//dirt
	    ids.add(12);//sand
	    ids.add(13);//gravel
	    kits.addDefault("Earth.BlockThrow.ThrowableBlocks", ids);
	    
	    List<Integer> allowedBreak = new ArrayList<Integer>();
	    allowedBreak.add(1);
	    allowedBreak.add(2);
	    allowedBreak.add(3);
	    allowedBreak.add(4);
	    allowedBreak.add(12);
	    allowedBreak.add(13);
	    kits.addDefault("Earth.BlockThrow.AllowBreakOnExplosion", allowedBreak);
	    kits.addDefault("Earth.BlockThrow.Cooldown", 3);
	    kits.addDefault("Earth.BlockThrow.Hotkey", 3);
	    
	    
	    kits.addDefault("Earth.EarthDome.Cooldown", 15);
	    kits.addDefault("Earth.EarthDome.Hotkey", 5);
	    kits.addDefault("Earth.EarthDome.Duration", 4);
	    kits.addDefault("Earth.EarthDome.Size", 6);
	    kits.addDefault("Earth.EarthDome.Material", Material.DIRT.toString());
	    
	    kits.addDefault("Earth.EarthWall.Cooldown", 6);
	    kits.addDefault("Earth.EarthWall.Hotkey", 4);
	    kits.addDefault("Earth.EarthWall.Duration", 3);
	  //kits.addDefault("Earth.EarthWall.Material", "DIRT");
	    kits.addDefault("Earth.EarthWall.Height", 4);
	    kits.addDefault("Earth.EarthWall.Width", 4);
	    
	    
	    kits.addDefault("Scout.Dash.Cooldown", 3);
	   // kits.addDefault("Scout.Dash.Hotkey", 1);
	    kits.addDefault("Archer.ArrowBarrage.Cooldown", 20);
	    
	    kits.addDefault("Assassin.Invisible.Material", Material.CLOCK.toString());
	    kits.addDefault("Assassin.Invisible.Cooldown", 15);
	    kits.addDefault("Assassin.Invisible.Duration", 5);
	    
	    getConfig().addDefault("Countdown", 30);
	    getConfig().addDefault("FFA", false);
	    getConfig().addDefault("MaxPlayers", 50);
	    getConfig().addDefault("AutoCapture", true); 
	    getConfig().addDefault("ForceUnzip", false); 
	    getConfig().addDefault("MapName", "SampleMap");
	    getConfig().addDefault("AutoStartPlayers", 12);
	    getConfig().addDefault("DebugMessages", true);
 	    getConfig().addDefault("Header", "&bWelcome to Crystal Clash!");
	    getConfig().addDefault("Footer", "&bHave fun!");
	  //  getConfig().addDefault("BungeeServerName", "CrystalClash1");
	    saveConfig();
	    Methods.saveYamls();
	    //++++++++++++++++++++++++++++++++++++++++++++++
	  //OUR COOLDOWN HANDLER
	    
	    CrystalClash.FFA = getConfig().getBoolean("FFA");
	    
	  		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
	  		     
	              public void run() {
	                  Cooldown.handleCooldowns();
	              }
	          }, 1L, 2L);
	  	//
	    getServer().getPluginManager().registerEvents(new EarthListener(this), this); 
	    getServer().getPluginManager().registerEvents(new ScoutListener(), this); 
	    getServer().getPluginManager().registerEvents(new ArcherListener(this), this); 
	    getServer().getPluginManager().registerEvents(new TankListener(), this); 
	    getServer().getPluginManager().registerEvents(new EndermanListener(), this);
	    getServer().getPluginManager().registerEvents(new EngineerListener(), this);
	    getServer().getPluginManager().registerEvents(new ItemListener(), this);
	    getServer().getPluginManager().registerEvents(new MageListener(this), this);
	    getServer().getPluginManager().registerEvents(new GuardianListener(), this);
	    getServer().getPluginManager().registerEvents(new KillstreakListener(), this);
	    getServer().getPluginManager().registerEvents(new PowerUpListener(), this); 
	    getServer().getPluginManager().registerEvents(new NecromancerListener(), this);

	    getServer().getPluginManager().registerEvents(new SniperListener(this), this);
	    getServer().getPluginManager().registerEvents(new ChatListener(this), this);
	    getServer().getPluginManager().registerEvents(new SpiderListener(), this);
	    getServer().getPluginManager().registerEvents(new SetupListener(), this);
	    

	    getServer().getPluginManager().registerEvents(new GameListener(this), this);
	    getServer().getPluginManager().registerEvents(new TestListener(), this);
	    getServer().getPluginManager().registerEvents(new ExplosivesListener(), this); 
	    getServer().getPluginManager().registerEvents(new AssassinListener(), this);
	    getServer().getPluginManager().registerEvents(new MenuListener(), this);
	    getServer().getPluginManager().registerEvents(new CustomEntityListener(this), this);
	    useHolograms = Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays"); 

	    if(useHolograms){
	    	getLogger().info("HolographicDisplays has been recognized!!!!");
	    }else{
	    	getLogger().warning("HolographicDisplays has NOT BEEN RECOGNIZED! It's okay though. ~Artish");
	    }
	    
	    
	    Methods.spawnModels(); 
	    Methods.setupImportantBlocks();
	    SetupListener.setupWand();
	    Arena.setup();
	    Crates.init();
	    
	    Bukkit.setWhitelist(false);
	    
	    for(Leaderboard board : Leaderboard.values()){
	    	board.init();
	    	board.updateSign();
	    }
	    Arena.redTeam.init();
	    Arena.blueTeam.init();
	    
		    try
		    {
		      this.m.firstRun();
		    }

		    catch (Exception localException1) {
		        localException1.printStackTrace();
            }

		    Methods.loadYamls();
		super.onEnable();
	}
	
	
	public static void loadGameData()
	{
		AssassinListener.loadAssassinStuff();
		SpiderListener.loadSpiderStuff();
		try {
			ClassInventories.loadClassInventories();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	@Override
	public void onDisable() {
		CustomEntityType.unregisterEntities();
		Methods.clearModels();
		try {
			MySQL.closeConnection();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try{
			getArena().stop(StopReason.ON_DISABLE); 
		}catch(Exception e){
			e.printStackTrace();
		}
		
		 mainArena.setState(GameState.RESTARTING);

		mainArena = null;


		//Unzip original Map to reset map.

        unzipMap();


		super.onDisable();
	}
	

	private void unzipMap()
    {
        String zipFilePath = getDataFolder().getPath() + "\\Map.zip";
        String destDirectory = ".\\Arena";
        logger.info("Unzipping map from: " + zipFilePath + "      To: " + destDirectory);

        UnzipUtility unzipper = new UnzipUtility();
        try{
            unzipper.unzip(zipFilePath,destDirectory);
            logger.info("Unzipped map!");
        }catch(Exception ex){
            ex.printStackTrace();
        }


    }





	

	@SuppressWarnings("deprecation")
	@Override
		public boolean onCommand(CommandSender sender, Command command,
				String label, String[] args) {
			
		if(label.equalsIgnoreCase("a") || label.equalsIgnoreCase("all")){
			if(sender instanceof Player){
				Player player = (Player) sender;
				
					if(getArena().getState() != GameState.INGAME)
						return true;
					
					if(args.length == 0){
						return true;
					}
					
					String message = "";
					for(int i = 0; i < args.length; i++){
						message = message + " " +  args[i];
					}
					
					
					
					if(getArena().isRed(player))
						getArena().sendAll(RED_PREFIX  + ChatColor.RED + player.getName() +ChatColor.DARK_GRAY + ":" + ChatColor.WHITE + message);
					
					if(getArena().isBlue(player))
						getArena().sendAll(BLUE_PREFIX + ChatColor.BLUE + player.getName() +ChatColor.DARK_GRAY + ":" + ChatColor.WHITE +  message);
					
					
				
				
			}
		}
		if(label.equalsIgnoreCase("score")){
			if(sender instanceof Player){
				
			Player player = (Player) sender;
				
			if(args.length == 0){
				player.sendMessage(ChatColor.GOLD + "Showing scores for: " + ChatColor.BOLD.toString() + player.getName());
			
				/*
				for(Leaderboard board : Leaderboard.values()){
				if(board != Leaderboard.BEST_KDR){
					player.sendMessage(ChatColor.GRAY + board.getShortTitle() + ChatColor.GOLD + " " + board.getScore(player)); 
				}else{
					player.sendMessage(ChatColor.GRAY + board.getShortTitle() + ChatColor.GOLD + " " + board.getScoreDouble(player.getUniqueId())); 
					
				}
			}
			*/
				for(Leaderboard board : Leaderboard.values()){
					if(board != Leaderboard.BEST_KDR){
						player.sendMessage(ChatColor.GRAY + board.getShortTitle() + ChatColor.GOLD + " " + MySQLUtil.getScore(player.getUniqueId(), board)); 
					}else{
						player.sendMessage(ChatColor.GRAY + board.getShortTitle() + ChatColor.GOLD + " Not Finished" ); 
						
					}
				}	
				
				
			
			}
			
			if(args.length == 1){
				OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
				if(target != null){
					player.sendMessage(ChatColor.GOLD + "Showing scores for: " + ChatColor.BOLD.toString() + target.getName());
				
					
					/*
					for(Leaderboard board : Leaderboard.values()){
						if(board != Leaderboard.BEST_KDR){
							player.sendMessage(ChatColor.GRAY + board.getShortTitle() + ChatColor.GOLD + " " + board.getScore(target.getUniqueId())); 
						}else{
							player.sendMessage(ChatColor.GRAY + board.getShortTitle() + ChatColor.GOLD + " " + board.getScoreDouble(target.getUniqueId())); 

						}
					} */
					for(Leaderboard board : Leaderboard.values()){
						if(board != Leaderboard.BEST_KDR){
							player.sendMessage(ChatColor.GRAY + board.getShortTitle() + ChatColor.GOLD + " " + MySQLUtil.getScore(target.getUniqueId(), board)); 
						}else{
							player.sendMessage(ChatColor.GRAY + board.getShortTitle() + ChatColor.GOLD + " Not Finished" ); 
							
						}
					}	
					
					
					
				}
				
			}
			
			}
		}


		
		if(label.equalsIgnoreCase("cc") || label.equalsIgnoreCase("crystalclash") || label.equalsIgnoreCase("crystal")){
			if(sender instanceof Player){
				Player player = (Player) sender;
				
				if(!player.hasPermission("cp.admin") && !player.hasPermission("cc.admin"))
					return true;
				
				if(args.length == 0){
					if(player.hasPermission("cp.admin") || player.hasPermission("cc.admin")){
						sender.sendMessage("");
						sender.sendMessage("");
						sender.sendMessage("");
						sender.sendMessage("");
						sender.sendMessage("");
						sender.sendMessage("");
						sender.sendMessage(ChatColor.GRAY + "++++++++++++ [" + ChatColor.GOLD + "CrystalClash" + ChatColor.GRAY + "]++++++++++++");
						sender.sendMessage(ChatColor.GRAY + "Aliases to /crystalclash: /cc, /crystal");
						sender.sendMessage(ChatColor.GOLD + "Author: " + ChatColor.DARK_GRAY + "Artish1");
						sender.sendMessage(ChatColor.GOLD + "/crystalclash version" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Shows version description");
						sender.sendMessage(ChatColor.GOLD + "/crystalclash start" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Starts the Arena");
						sender.sendMessage(ChatColor.GOLD + "/crystalclash stop" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Stops the Arena");
						sender.sendMessage(ChatColor.GOLD + "/crystalclash setredspawn" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Sets the red team's spawn (Should be in Arena world)");
						sender.sendMessage(ChatColor.GOLD + "/crystalclash setbluespawn" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Sets the blue team's spawn (Should be in Arena world)");
						sender.sendMessage(ChatColor.GOLD + "/crystalclash setlobby" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Sets the lobby (Should be in Main world.)");
						sender.sendMessage(ChatColor.GOLD + "/crystalclash addgun [Blue/Red]" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Adds a gun spawn for specified team");
						sender.sendMessage(ChatColor.GOLD + "/crystalclash spawnclassmodel [Class]" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Spawns the Class Model where player is");
						sender.sendMessage(ChatColor.GOLD + "/crystalclash spawnTeampick [Blue/Red]" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Spawns the Sheep to pick teams with where the player is");
					//	sender.sendMessage(ChatColor.GOLD + "/crystalclash removemodel" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Gives an Item to remove models with");
						sender.sendMessage(ChatColor.GOLD + "/crystalclash setmerchant <Blue/Red>" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Sets the Spawns for merchants with specified team.");
						sender.sendMessage(ChatColor.GOLD + "/crystalclash setcrystal <Blue/Red> " + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Sets the Target block as the teams crystal");
						sender.sendMessage(ChatColor.GOLD + "/crystalclash setpoint [1/2/3]" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Sets the target block as specified Point");
						sender.sendMessage(ChatColor.GOLD + "/crystalclash addFirework " + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Adds a firework location to play at the end of the game");
						sender.sendMessage(ChatColor.GOLD + "/crystalclash addDec <1/2/3> " + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Adds a Decorative Block location to the specified Point owner (1,2,3)");
						sender.sendMessage(ChatColor.GOLD + "/crystalclash addExplosionTo <Red/Blue>" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Adds an explosion location to the targeted block for the specified team.");
						sender.sendMessage(ChatColor.GOLD + "/crystalclash addExplosionFrom <Red/Blue>" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Adds an explosion location to where you're standing currently for the specified team.");
						sender.sendMessage(ChatColor.GOLD + "/crystalclash setup <SetupType>" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Sets the setup type when interacting with the setup wand.");
						sender.sendMessage(ChatColor.GOLD + "/crystalclash setupTypes" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Shows a list of setup types.");
						sender.sendMessage(ChatColor.GOLD + "/crystalclash wand" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Gives the Setup Wand needed for /cc setup <setuptype>");
						sender.sendMessage(ChatColor.GOLD + "/crystalclash setLeaderboardSign <LeaderboardType>" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Sets the sign location for specified Leaderboard");
						sender.sendMessage(ChatColor.GOLD + "/crystalclash setLeaderboardHead <LeaderboardType>" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Sets the head location for specified Leaderboard");
						sender.sendMessage(ChatColor.GOLD + "/crystalclash addchoppergunnerloc " + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Adds a location for the chopper gunner path.");

						sender.sendMessage(ChatColor.GOLD + "/crystalclash leaderboardTypes" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Shows a list of leaderboard types."); 
						sender.sendMessage(ChatColor.GOLD + "/crystalclash setpoints <Player> [amount]" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Sets the players points");
						sender.sendMessage(ChatColor.GOLD + "/crystalclash addpoints <Player> [amount]" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Adds points onto the current players points");
						sender.sendMessage(ChatColor.GOLD + "/crystalclash givepoints <Player> [amount]" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Same as /cc addpoints <Player> [Amount]");

						
						return true;
					}
				}
				
				if(args.length == 1){
					if(args[0].equalsIgnoreCase("setbluespawn")){
						getArena().setBlueSpawn(player.getLocation());
						sendMessage(player, "You have set the " + ChatColor.BLUE +"Blue" + ChatColor.GRAY + " spawn");
						return true;
					}
					if(args[0].equalsIgnoreCase("leaderboardTypes")){
						String thingy = ChatColor.GRAY + "Leaderboard Types: " + ChatColor.GREEN;
						for(Leaderboard types : Leaderboard.values()){
							thingy = thingy + types.name() +", ";
						}
						
						sendMessage(player, thingy);
					}

					if(args[0].equalsIgnoreCase("ss")){
					    World world = Bukkit.getWorld("world");
					    Location loc = new Location(world,0,60,0);
					    player.teleport(loc);
                    }

					if(args[0].equalsIgnoreCase("addchopperloc")){
						Methods.addChopperGunnerLoc(player.getLocation());
						sendMessage(player, "Added a Chopper Gunner location!"); 
					}
					
					if(args[0].equalsIgnoreCase("updatesigns")){
						Leaderboard.updateAllSigns();
						sendMessage(player, "Signs updated!");
					}
					
					if(args[0].equalsIgnoreCase("gotoworld")){
						Location loc = new Location(Bukkit.getWorld("world"), 0,100,0);
						Location loc2 = loc.clone();
						loc2.subtract(0, 5, 0);
						loc2.getBlock().setType(Material.GRASS);
						player.teleport(loc);
					}
					if(args[0].equalsIgnoreCase("wand")){
						player.getInventory().addItem(SetupListener.wand);
						sendMessage(player, "You are currently on mode: " + ChatColor.GREEN + SetupListener.getSetupType(player).getName());
					}
					if(args[0].equalsIgnoreCase("setuptypes")){
						String thingy = ChatColor.GRAY + "Setup Types: " + ChatColor.GREEN ;
						for(SetupType types : SetupType.values()){
							thingy = thingy + types.name() +", ";
						}
						
						sendMessage(player, thingy);
					}
					
					if(args[0].equalsIgnoreCase("setredspawn")){
						getArena().setRedSpawn(player.getLocation());
						sendMessage(player, "You have set the " + ChatColor.RED +"Red" + ChatColor.GRAY + " spawn");
						return true;
					}
					if(args[0].equalsIgnoreCase("lobby")){
						player.teleport(getArena().getLobbySpawn());
					}
					if(args[0].equalsIgnoreCase("debug")){
						
						if(ArcherListener.debuggers.contains(player.getUniqueId())){
							ArcherListener.debuggers.remove(player.getUniqueId());
							player.sendMessage("Debug mode deactivated.");
						}else{
							ArcherListener.debuggers.add(player.getUniqueId());
							player.sendMessage("Debug mode activated.");
						}
					}
					
					if(args[0].equalsIgnoreCase("setlobby")){
						getArena().setLobbySpawn(player.getLocation());
						sendMessage(player, "You have set the " + ChatColor.AQUA +"Lobby" + ChatColor.GRAY + " spawn");
						return true;
					}
					
					if(args[0].equalsIgnoreCase("version")){
						sender.sendMessage(ChatColor.GOLD + "CrystalClash");
						sender.sendMessage(ChatColor.GOLD + "Version: " + ChatColor.AQUA + getDescription().getVersion());
						sender.sendMessage(ChatColor.AQUA + "Author: " + ChatColor.DARK_AQUA + "Artish1");
						return true;
					}
					
					if(args[0].equalsIgnoreCase("addFirework")){
						getArena().addFireworkLocation(player.getLocation());
						sendMessage(player, "You have added a Location for a firework to spawn at the end of the game!");
					}
					
					if(args[0].equalsIgnoreCase("reload")){
						reloadConfig();
						Methods.loadYamls();
						player.sendMessage(ChatColor.GRAY + "CrystalClash configs reloaded!");
						
					}
					
					if(args[0].equalsIgnoreCase("start")){
						if(getArena().getState() != GameState.LOBBY){
							sendMessage(player, "Arena is already on, or starting");
							return true;
						}
						
						getArena().start();
						getArena().sendAll(ChatColor.RED + player.getName() + ChatColor.GRAY + " has manually started the game!");
						return true;
					}
					
					if(args[0].equalsIgnoreCase("stop")){
						getArena().sendAll(ChatColor.RED + player.getName() + ChatColor.GRAY + " has manually stopped the game!");
						getArena().stop(StopReason.PLAYER_COMMAND);
						return true;
					}
					if(args[0].equalsIgnoreCase("setleaderboardheads")){
						for(Leaderboard board : Leaderboard.values()){
							ItemStack item = new ItemStack(Material.PLAYER_HEAD,1);
							ItemMeta meta = item.getItemMeta();
							meta.setDisplayName(ChatColor.GRAY + "Head: " + board.getTitle()); 
							item.setItemMeta(meta);
							
							player.getInventory().addItem(item);
							SetupListener.skullSetup.put(item, board);
							SetupListener.filler.add(item);
						}
						
					}
					if(args[0].equalsIgnoreCase("givepoints")){
						getArena().getArenaPlayer(player).addMoney(500);
					} 
					
					if(args[0].equalsIgnoreCase("end")){
						getArena().setWinner(Arena.redTeam);
						getArena().stop(StopReason.GAME_END);
						return true;
					}
					
					if(args[0].equalsIgnoreCase("removemodel")){
						player.getInventory().addItem(Methods.getModelRemoveItem());
						player.sendMessage(ChatColor.LIGHT_PURPLE + "To remove a model (Works with any entity), click on them with this item");
					}
					
				}
				
				if(args.length == 2){
					 
					if(args[0].equalsIgnoreCase("test")){
						
						return true;
					}
					
					if(args[0].equalsIgnoreCase("setup")){
						for(SetupType type : SetupType.values()){
							
							if(args[1].equalsIgnoreCase(type.name())){
								SetupListener.setupTypes.put(player.getUniqueId(), type);
								player.sendMessage(ChatColor.GREEN + "SetupType mode: " + type.getName() + ", or " + type.toString()); 
							}
						}
					}
					
					if(args[0].equalsIgnoreCase("setffaspawn"))
					{
						if(args[1].equalsIgnoreCase("1") || args[1].equalsIgnoreCase("2")|| args[1].equalsIgnoreCase("3")|| args[1].equalsIgnoreCase("4") )
						{
							int team = Integer.parseInt(args[1]);	
							
							Methods.addLocation("FFA.Spawns." + team, player.getLocation());
							player.sendMessage(ChatColor.GRAY + "Set FFA spawn for: " + ChatColor.RED + team);
							
						}else{
							player.sendMessage(ChatColor.RED + "Must be 1/2/3/4");
						}
						
						
					}
					
					if(args[0].equalsIgnoreCase("setleaderboardsign")){
						for(Leaderboard board : Leaderboard.values()){
							if(args[1].equalsIgnoreCase(board.name())){
								Location targetedLoc = player.getTargetBlock(null, 50).getLocation();
								board.setSignLocation(targetedLoc);
								sendMessage(player, "You have set the sign location for: " + ChatColor.GREEN + board.getTitle()); 
							}
						}
					}
					
					if(args[0].equalsIgnoreCase("setleaderboardhead")){
						for(Leaderboard board : Leaderboard.values()){
							if(args[1].equalsIgnoreCase(board.name())){
								Location targetedLoc = player.getTargetBlock( null, 50).getLocation();
								board.setSkullLocation(targetedLoc);
								sendMessage(player, "You have set the head location for: " + ChatColor.GREEN + board.getTitle()); 
							}
						}
					}
					
					
					
					
					if(args[0].equalsIgnoreCase("spawnteampick")){
						if(args[1].equalsIgnoreCase("red")){
							Methods.spawnModelSheep(player.getLocation(), Arena.redTeam);
							sendMessage(player, "You have set the ArenaTeam Pick entity for " + ChatColor.RED + "Red");
							
						}else{
							if(args[1].equalsIgnoreCase("blue")){
								Methods.spawnModelSheep(player.getLocation(), Arena.blueTeam);
								sendMessage(player, "You have set the ArenaTeam Pick entity for " + ChatColor.BLUE + "Blue");
							}else{
								sendMessage(player, "Wrong teamtype, do: /cc spawnteampick <Red/Blue>");
							}
							
						}
						return true;

					}
					
					if(args[0].equalsIgnoreCase("setmerchant")){
						if(args[1].equalsIgnoreCase("red")){
							getArena().setRedMerchantSpawn(player.getLocation());
							sendMessage(player, "You have set the " + ChatColor.RED + "Red " + ChatColor.GRAY + "merchant's spawn");

							return true;
						}
						if(args[1].equalsIgnoreCase("blue")){
							getArena().setBlueMerchantSpawn(player.getLocation());
							sendMessage(player, "You have set the " + ChatColor.BLUE + "Blue " + ChatColor.GRAY + "merchant's spawn");
							return true;
						}
						
					}
					
					if(args[0].equalsIgnoreCase("addgun")){
						if(args[1].equalsIgnoreCase("red")){
							getArena().addRedGunSpawn(player.getLocation());
							sendMessage(player, "You have added a gun spawn to the " + ChatColor.RED +"Red" + ChatColor.GRAY + " team");
						}else if(args[1].equalsIgnoreCase("blue")){
							getArena().addBlueGunSpawn(player.getLocation());
							sendMessage(player, "You have added a gun spawn to the " + ChatColor.BLUE +"Blue" + ChatColor.GRAY + " team");
						}else player.sendMessage(ChatColor.RED + "/cp addgun [Red/Blue]"); 
					}
					
					
					if(args[0].equalsIgnoreCase("join")){
						if(args[1].equalsIgnoreCase("red")){
							Arena.redTeam.getQueue().add(getArena().getArenaPlayer(player));
							player.sendMessage(ChatColor.GRAY + "Added to the red queue...");
						}
						
						if(args[1].equalsIgnoreCase("blue")){
							Arena.blueTeam.getQueue().add(getArena().getArenaPlayer(player));
							player.sendMessage(ChatColor.GRAY + "Added to the blue queue...");
						}
						
						
					}
					
					if(args[0].equalsIgnoreCase("getcrystal")){
						if(args[1].equalsIgnoreCase("red")){
							Location loc = Arena.redTeam.getCrystal();
							player.sendMessage("Red crystal is at X:" + loc.getBlockX() + ", Y: " + loc.getBlockY()  + ", Z:" + loc.getBlockZ()); 
						}
					}
					
					if(args[0].equalsIgnoreCase("setcrystal")){
						Location targetLoc = player.getTargetBlock(null, 20).getLocation();
						if(args[1].equalsIgnoreCase("red")){
							//getArena().setRedCrystal(targetLoc);
							Arena.redTeam.setCrystal(targetLoc);
							sender.sendMessage(ChatColor.RED + "Red " + ChatColor.GRAY + "crystal set at X: " + targetLoc.getBlockX() +
									", Y: " + targetLoc.getBlockY() + ", Z: " + targetLoc.getBlockZ());
						}else{
							if(args[1].equalsIgnoreCase("blue")){
								getArena().setBlueCrystal(targetLoc);
								sender.sendMessage(ChatColor.BLUE + "Blue " + ChatColor.GRAY + "crystal set at X: " + targetLoc.getBlockX() +
										", Y: " + targetLoc.getBlockY() + ", Z: " + targetLoc.getBlockZ());
							}else{
								player.sendMessage(ChatColor.RED + "/cp setcrystal [Blue/Red]");
							}
						}
					}
					
					if(args[0].equalsIgnoreCase("setpoint")){
						Location loc = player.getTargetBlock(null, 20).getLocation();
                        if ("1".equals(args[1])) {
                            getArena().setPoint1Spawn(loc);
                            sendMessage(player, "Point 1 location set!");
                        } else if ("2".equals(args[1])) {
                            getArena().setPoint2Spawn(loc);
                            sendMessage(player, "Point 2 location set!");
                        } else if ("3".equals(args[1])) {
                            getArena().setPoint3Spawn(loc);
                            sendMessage(player, "Point 3 location set!");
                        } else {
                            player.sendMessage(ChatColor.RED + "/cp setpoint [1/2/3]");
                        }
						
						return true;
					}
					
					if(args[0].equalsIgnoreCase("addDec")){
						Location loc = player.getTargetBlock(null, 20).getLocation();
                        if ("1".equals(args[1])) {//getArena().addPoint1Dec(loc);
                            ContestPoint.POINT1.addDecBlock(loc);
                            sendMessage(player, "Added a Decorative Changing Block for Point 1 at X: " + loc.getBlockX() + ", Y: " + loc.getBlockY() + ", Z: " + loc.getBlockZ());
                        } else if ("2".equals(args[1])) {//getArena().addPoint2Dec(loc);
                            ContestPoint.POINT2.addDecBlock(loc);
                            sendMessage(player, "Added a Decorative Changing Block for Point 2 at X: " + loc.getBlockX() + ", Y: " + loc.getBlockY() + ", Z: " + loc.getBlockZ());
                        } else if ("3".equals(args[1])) {//getArena().addPoint3Dec(loc);
                            ContestPoint.POINT3.addDecBlock(loc);
                            sendMessage(player, "Added a Decorative Changing Block for Point 3 at X: " + loc.getBlockX() + ", Y: " + loc.getBlockY() + ", Z: " + loc.getBlockZ());
                        } else {
                            player.sendMessage(ChatColor.RED + "/cp addDec [1/2/3]");
                        }
						return true;
					}
					
					if(args[0].equalsIgnoreCase("addExplosionTo")){
						if(args[1].equalsIgnoreCase(Arena.redTeam.getName())){
                            Location targetedLoc = player.getTargetBlock( null, 50).getLocation();
                            Arena.redTeam.addExplosionLocation(targetedLoc);
                            sendMessage(player, "You have added an explosion location for " + Arena.redTeam.getColoredName() + ChatColor.GRAY + " at " +Methods.getCoordinates(targetedLoc));

                        }else{
						    if(args[1].equalsIgnoreCase(Arena.blueTeam.getName())){
                                Location targetedLoc = player.getTargetBlock( null, 50).getLocation();
                                Arena.blueTeam.addExplosionLocation(targetedLoc);
                                sendMessage(player, "You have added an explosion location for " + Arena.blueTeam.getColoredName() + ChatColor.GRAY + " at " +Methods.getCoordinates(targetedLoc));

                            }
                        }


					}
					
					if(args[0].equalsIgnoreCase("addExplosionFrom")){

                        if(args[1].toLowerCase().equalsIgnoreCase(Arena.redTeam.getName())){
                            Arena.redTeam.addExplosionLocation(player.getLocation());
                            sendMessage(player, "You have added an explosion location for " + Arena.redTeam.getColoredName() + ChatColor.GRAY + " at " +Methods.getCoordinates(player.getLocation()) );
                        }

                        if(args[1].toLowerCase().equalsIgnoreCase(Arena.blueTeam.getName())){
                            Arena.blueTeam.addExplosionLocation(player.getLocation());
                            sendMessage(player, "You have added an explosion location for " + Arena.blueTeam.getColoredName() + ChatColor.GRAY + " at " +Methods.getCoordinates(player.getLocation()) );
                        }






					}
					
					
					if(args[0].equalsIgnoreCase("spawnclassmodel")){
						
						for(ClassType type : ClassType.values()){
							if(ChatColor.stripColor(type.getName()).equalsIgnoreCase(args[1])){
								Entity e;
								if(type.useSkeletonModel()){
									e = Methods.spawnModelSkeleton(player.getLocation(), type.getName(), type.getArmor(), type.getModelItem()).getBukkitEntity();
								}else{
									e = Methods.spawnModelZombie(player.getLocation(), type.getName(), type.getArmor(), type.getModelItem()).getBukkitEntity();
								}
								Methods.addModel(e, ChatColor.stripColor(type.getName())); 
							}
						}
						
					}
				}
				
				if(args.length == 3){
					if(args[0].equalsIgnoreCase("setcapture")){
						try{
							Location loc = player.getTargetBlock( null,50).getLocation();
						for(ContestPoint cp : ContestPoint.values()){
							if(args[1].equalsIgnoreCase(Integer.toString(cp.getValue()))){
								if(args[2].equalsIgnoreCase("1")){
									cp.setCapturePoint1(loc);
									sendMessage(player, "You have set the 1st Capture Point location for point: " + ChatColor.GOLD + Integer.toString(cp.getValue()) );
								}else{
									if(args[2].equalsIgnoreCase("2")){
										cp.setCapturePoint2(loc);
										sendMessage(player, "You have set the 2nd Capture Point location for point: " + ChatColor.GOLD + Integer.toString(cp.getValue()) );

									}
								}
							}
						}
						
						}catch(NumberFormatException e){
							sendMessage(player, "Error, please do this correctly. /cc setcapture <PointNumber> <LocationNumber>");
						}
						
					}
					
					if(args[0].equalsIgnoreCase("givepoints") || args[0].equalsIgnoreCase("addpoints")){
						if(Bukkit.getOfflinePlayer(args[1]) != null){
							OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(args[1]);
							if(MySQLUtil.hasRow(oPlayer.getUniqueId())){
								String amount = args[2];
								try{
									int realAmount = Integer.parseInt(amount);
									MySQLUtil.addPoints(oPlayer.getUniqueId(), realAmount);
									if(oPlayer.isOnline()){
										Methods.updateLobbyScoreboardPoints(oPlayer.getPlayer());
										oPlayer.getPlayer().sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + player.getName() + ChatColor.GRAY + " has added " + ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + realAmount + ChatColor.GRAY + " points to your"
												+ " account");
									}
									player.sendMessage(ChatColor.GRAY + "You have added "  +ChatColor.GREEN.toString() +ChatColor.BOLD.toString()+ realAmount + ChatColor.GRAY + " points to " + ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + oPlayer.getName() + "'s " + ChatColor.GRAY + "account.");
								}catch(NumberFormatException e){
									sendMessage(player, "Amount must be a number value only! /cc givepoints <player> <AMOUNT>");
								}
								
							}else{
								sendMessage(player, oPlayer.getName() + " is not in the database yet, please wait until he/she plays."); 
							}
							
							
						}else{
							sendMessage(player,"its null");
						}
					}
					
					if(args[0].equalsIgnoreCase("setpoints")){
							OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(args[1]);
							if(MySQLUtil.hasRow(oPlayer.getUniqueId())){
								String amount = args[2];
								try{
									int realAmount = Integer.parseInt(amount);
									MySQLUtil.setPoints(oPlayer.getUniqueId(), realAmount);
									if(oPlayer.isOnline()){
										Methods.updateLobbyScoreboardPoints(oPlayer.getPlayer());
										oPlayer.getPlayer().sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + player.getName() + ChatColor.GRAY + " has set your points to " + ChatColor.GREEN.toString() + ChatColor.BOLD + realAmount);
									} 
									player.sendMessage(ChatColor.GRAY + "You have set " + ChatColor.AQUA.toString() + ChatColor.BOLD + oPlayer.getName() + "'s " + ChatColor.GRAY + "points to " +ChatColor.GREEN.toString() + ChatColor.BOLD.toString() +  realAmount);
									
								}catch(NumberFormatException e){
									sendMessage(player, "Amount must be a number value only! /cc givepoints <player> <AMOUNT>");
								}
								
							}else{
								sendMessage(player, oPlayer.getName() + " is not in the database yet, please wait until he/she plays."); 
							}
							
							
						
					}
					
				}
				
				
				
				
				
			}else{

			}
		}
		
		
			return super.onCommand(sender, command, label, args);
		}
	
	
	public final String BLUE_PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "Blue" + ChatColor.DARK_GRAY + "] ";
	public final String RED_PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.RED + "Red" + ChatColor.DARK_GRAY + "] ";
	
	
	public static void sendMessage(Player player, String message) {
		player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "CrystalClash" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + message);
	}
	
	
}
