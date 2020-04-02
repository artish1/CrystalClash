package me.artish1.CrystalClash.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.artish1.CrystalClash.Arena.GameState;
import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.crates.CrateType;
import me.artish1.CrystalClash.leaderboards.Leaderboard;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

public class MySQLUtil {
	
	public static boolean isClassColumnAdded(ClassType type){
		
		try {
			Statement statement = Methods.getPlugin().c.createStatement();
			ResultSet res = statement.executeQuery("SELECT * FROM information_schema.COLUMNS WHERE "+
    "TABLE_SCHEMA = "+"'" + Methods.getPlugin().mysql.getString("Database") +"' " +
"AND TABLE_NAME = 'crazytest1' "+
"AND COLUMN_NAME = '" +type.toString().toLowerCase() +"'");
			int check = 0;
			while(res.next()){
				check++;
			}
			return check > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean doesColumnExist(String tableName,String columnName)
	{
		try {
			Statement statement = Methods.getPlugin().c.createStatement();
			ResultSet res = statement.executeQuery("SELECT * FROM information_schema.COLUMNS WHERE "+
    "TABLE_SCHEMA = "+"'" + Methods.getPlugin().mysql.getString("Database") +"' " +
    "AND TABLE_NAME = '" + tableName + "' "+
	"AND COLUMN_NAME = '" +columnName +"'");
			int check = 0;
			while(res.next()){
				check++;
			}
			return check > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void addClassColumn(ClassType type){
		String name = type.toString().toLowerCase();
		try {
			Statement statement = Methods.getPlugin().c.createStatement();
			statement.executeUpdate("ALTER TABLE crazytest1 ADD " + name);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void setBoolean(UUID id, String table, String column, boolean bool)
	{
		int boolInt = bool ? 1 : 0;
		try {
			Statement statement = Methods.getPlugin().c.createStatement();
			statement.executeUpdate("UPDATE " + table + " SET " + column + "='" + boolInt + "' WHERE uuid='" + id + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static int getAmountOf(UUID id,String table,String column)
	{
		try {
			Statement statement = Methods.getPlugin().c.createStatement();
			ResultSet res = statement.executeQuery("SELECT " + column  + " FROM " + table  + " WHERE uuid='" + id + "'");
			res.next();
			return res.getInt(column);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return 0;
	}
	
	
	
	public static void setAmountOf(UUID id, String table, String column, int amount)
	{
		
		try {
			Statement statement = Methods.getPlugin().c.createStatement();
			statement.executeUpdate("UPDATE " + table + " SET " + column + "='" + amount + "' WHERE uuid='" + id + "'"); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static boolean hasClass(UUID id, ClassType type){
		String name = type.toString().toLowerCase();
		
		try {
			Statement statement = Methods.getPlugin().c.createStatement();
			ResultSet res = statement.executeQuery("SELECT " + name + " FROM crazytest1 WHERE uuid='" + id + "'");
			res.next();
			
			return res.getBoolean(name); 
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static int getClassLevel(UUID id, ClassType type)
	{
		String name = type.toString().toLowerCase();
		
		try {
			Statement statement = Methods.getPlugin().c.createStatement();
			ResultSet res = statement.executeQuery("SELECT level FROM " + name + "info WHERE uuid='" + id + "'");
			res.next();
			
			return res.getInt("level");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return 1;
	}
	
	public static int getCrateAmount(UUID id,CrateType type)
	{
		
		try{
			Statement statement = Methods.getPlugin().c.createStatement();
			ResultSet res = statement.executeQuery("SELECT " + type.getMysqlColumnName() + " FROM crates WHERE uuid='" + id + "'");
			res.next();
			
			return res.getInt(type.getMysqlColumnName());
		}catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public static int getCrateKeyAmount(UUID id, CrateType type)
	{
		
		try {
			Statement statement = Methods.getPlugin().c.createStatement();
			ResultSet res = statement.executeQuery("SELECT " + type.getMysqlColumnName() + "_keys FROM crates WHERE uuid='" + id + "'"); 
			res.next();
			return res.getInt(type.getMysqlColumnName() + "_keys"); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static void addCrateKeyAmount(UUID id, CrateType type, int amount)
	{
		int keys = getCrateKeyAmount(id,type);
		keys+= amount;
		try {
			Statement statement = Methods.getPlugin().c.createStatement();
			statement.executeUpdate("UPDATE crates SET " + type.getMysqlColumnName() + "_keys='" + keys + "' WHERE uuid='" + id + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public static void subtractCrateKeyAmount(UUID id, CrateType type, int amount)
	{
		int keys = getCrateKeyAmount(id,type);
		keys-= amount;
		try {
			Statement statement = Methods.getPlugin().c.createStatement();
			statement.executeUpdate("UPDATE crates SET " + type.getMysqlColumnName() + "_keys='" + keys + "' WHERE uuid='" + id + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addCrateAmount(UUID id, CrateType type, int amount)
	{
		try{
			int currentAmount = getCrateAmount(id, type);
			currentAmount += amount; 
			Statement statement = Methods.getPlugin().c.createStatement();
			statement.executeUpdate("UPDATE crates SET " + type.getMysqlColumnName() + "='" + currentAmount+ "' WHERE uuid='" + id + "'");
		}catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	
	public static void subtractCrateAmount(UUID id, CrateType type, int amount)
	{
		try{
			int currentAmount = getCrateAmount(id, type);
			currentAmount -= amount; 
			Statement statement = Methods.getPlugin().c.createStatement();
			statement.executeUpdate("UPDATE crates SET " + type.getMysqlColumnName() + "='" + currentAmount+ "' WHERE uuid='" + id + "'");
		}catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	
	public static boolean getBoolean(UUID id, String table, String column)
	{
		try {
			Statement statement = Methods.getPlugin().c.createStatement();
			ResultSet res = statement.executeQuery("SELECT " + column + " FROM " + table + " WHERE uuid='" + id + "'");
			res.next();
			
			
			return res.getBoolean(column);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return false;
	}
	
	
	public static int getClassExperience(UUID id, ClassType type)
	{
		String name = type.toString().toLowerCase();
		
		try {
			Statement statement = Methods.getPlugin().c.createStatement();
			ResultSet res = statement.executeQuery("SELECT experience FROM " + name + "info WHERE uuid='" + id + "'");
			res.next();
			
			return res.getInt("experience"); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	
	public static Material getBoots(UUID id, ClassType type)
	{
		return getItem(id, type,"boots");
	}
	
	public static void setBoots(UUID id, ClassType type, Material mat)
	{
		setItemStack(id, type, mat, "boots"); 
	}
	
	public static Material getLeggings(UUID id, ClassType type)
	{
		return getItem(id, type,"leggings");
	}
	
	public static void setLeggings(UUID id, ClassType type, Material mat)
	{
		setItemStack(id, type, mat, "leggings"); 
	}
	
	public static Material getHelmet(UUID id, ClassType type)
	{
		return getItem(id, type,"helmet");
	}
	
	public static void setHelmet(UUID id, ClassType type, Material mat)
	{
		setItemStack(id, type, mat, "helmet"); 
	}
	
	public static Material getChestplate(UUID id, ClassType type)
	{
		return getItem(id, type,"chestplate");
	}
	
	public static void setChestplate(UUID id, ClassType type, Material mat)
	{
		setItemStack(id, type, mat, "chestplate"); 
	}
	
	public static void setAmmo(UUID id, ClassType type, int amount)
	{
		String name = type.toString().toLowerCase();
		
		try {
			Statement statement = Methods.getPlugin().c.createStatement();
			statement.executeUpdate("UPDATE " + name + "info SET ammo='" + amount + "' WHERE uuid='" + id + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static int getAmmo(UUID id, ClassType type)
	{
		String name = type.toString().toLowerCase();
		
		try {
			Statement statement = Methods.getPlugin().c.createStatement();
			ResultSet res = statement.executeQuery("SELECT ammo FROM " + name+ "info WHERE uuid='" + id + "'");
			res.next();
			
			return res.getInt("ammo");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public static void addAmmo(UUID id, ClassType type, int amount)
	{
		
		int current = getAmmo(id, type);
		current += amount;
		setAmmo(id, type, current); 
		
	}
	
	public static void setItemStack(UUID id, ClassType type, Material mat, String itemType)
	{
		String name = type.toString().toLowerCase();
		String materialName = mat.toString();
		try {
			Statement statement = Methods.getPlugin().c.createStatement();
			statement.executeUpdate("UPDATE " + name + "info SET " + itemType + "='" + materialName+ "' WHERE uuid='" + id + "'");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void checkNames(UUID id)
	{
		
		OfflinePlayer op = Bukkit.getOfflinePlayer(id);
		
		if(op.getName().equals(getNameCheck(id))){
			return;
		}
		
		try {
			Statement statement = Methods.getPlugin().c.createStatement();
			statement.executeUpdate("UPDATE crazytest1 SET name='" + op.getName() + "' WHERE uuid='" + id + "'");
			
			
		} catch (SQLException e) {
		}
	}
	
	private static String getNameCheck(UUID id)
	{
		
		try{
			if(Methods.isDebugMessages())
			{
				Methods.getPlugin().getLogger().info("Starting try/catch");
			}
			Statement statement = Methods.getPlugin().c.createStatement();
			ResultSet res = statement.executeQuery("SELECT name FROM crazytest1 WHERE uuid='" +id + "'" );
			res.next();
			String name = res.getString("name");
			if(Methods.isDebugMessages())
			{
				Methods.getPlugin().getLogger().info("Returning: " + name);
			}
			return name;
		}catch(SQLException e){
			
		}
		if(Methods.isDebugMessages())
		{
			Methods.getPlugin().getLogger().info("RETURNING NULL????");
		}
		return null;
	}
	
	public static Material getItem(UUID id, ClassType type, String itemType)
	{
		
		String name = type.toString().toLowerCase();
		try {
			Statement statement = Methods.getPlugin().c.createStatement();
			ResultSet res = statement.executeQuery("SELECT " + itemType + " FROM " + name + "info WHERE uuid='" + id +"'" );
			res.next();
			
			return Material.getMaterial(res.getString(itemType).toUpperCase());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	public static void addClass(UUID id, ClassType type){
		String name = type.toString().toLowerCase();
		try {
			Statement statement = Methods.getPlugin().c.createStatement();
			statement.executeUpdate("UPDATE crazytest1 SET " + name + "=1 WHERE uuid='" + id.toString() +"'");
			 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static boolean hasRow(UUID id){
		boolean flag = false;
		
		try {
			Statement statement = Methods.getPlugin().c.createStatement();
			ResultSet res = statement.executeQuery("SELECT EXISTS(SELECT uuid FROM crazytest1 WHERE uuid='" + id.toString()  + "')");
			res.next();
			flag = res.getBoolean(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	
	public static boolean hasClassTypeRow(UUID id,ClassType type)
	{
		String name = type.toString().toLowerCase();
		boolean flag = false;
		
		try {
			Statement statement = Methods.getPlugin().c.createStatement();
			ResultSet res = statement.executeQuery("SELECT EXISTS(SELECT uuid FROM " + name +  "info WHERE uuid='" + id.toString()  + "')");
			res.next();
			flag = res.getBoolean(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return flag;
	}
	
	public static void setPoints(UUID id, int amount){
		try {
			Statement statement = Methods.getPlugin().c.createStatement();
			
			statement.executeUpdate("UPDATE crazytest1 SET points=" + amount + " WHERE uuid='" + id + "'");
			OfflinePlayer player = Bukkit.getOfflinePlayer(id);
			if(player.isOnline()){
				if(Methods.getArena().getState() != GameState.INGAME && Methods.getArena().getState() != GameState.STOPPING){
					Methods.updateLobbyScoreboardPoints(player.getPlayer()); 
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addPoints(UUID id, int toAdd){
		setPoints(id, getPoints(id) + toAdd); 
		
		
		
	}
	
	
	public static void subtractPoints(UUID id, int toSubtract){
		int total = getPoints(id);
		if(total-toSubtract < 0){ 
			setPoints(id, 0);
		}else{
			setPoints(id, total - toSubtract);
		}
		
	}
	
	public static int getPoints(UUID id){
		try {
			Statement statement = Methods.getPlugin().c.createStatement();
			ResultSet res = statement.executeQuery("SELECT points FROM crazytest1 WHERE uuid='" + id + "'");
			res.next();
			return res.getInt("points");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static int getScore(UUID id,Leaderboard lb){
		
		try {
			Statement statement = Methods.getPlugin().c.createStatement();
			ResultSet res = statement.executeQuery("SELECT "+ lb.getMySQLColumn() +" FROM crazytest1 WHERE uuid='" + id.toString() + "'");
			res.next();
			
			return res.getInt(1); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public static List<String> getUUIDList(){
		List<String> uuids = new ArrayList<String>();
		
		try {
			Statement statement = Methods.getPlugin().c.createStatement();
			ResultSet res = statement.executeQuery("SELECT uuid FROM crazytest1");
			
			while(res.next()){
				
				uuids.add(res.getString("uuid"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		return uuids;
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
	
	
	public static void insertNew(UUID id){
		OfflinePlayer player = Bukkit.getOfflinePlayer(id);
		
		try {
			Statement statement = Methods.getPlugin().c.createStatement();
			//statement.executeUpdate("INSERT INTO crazytest1 VALUES('"+player.getName()+"', '"+id+"',0 ,0,0,0,0,0,0,0,0) ");
			statement.executeUpdate("INSERT INTO crazytest1 (name,uuid) VALUES('"+player.getName()+"', '"+id+"')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void insertNewCrates(UUID id)
	{
		try{
			Statement statement = Methods.getPlugin().c.createStatement();
			statement.executeUpdate("INSERT INTO crates (uuid) VALUES ('" + id + "')");
		}catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public static boolean hasNewCrates(UUID id)
	{
		boolean flag = false;
		
		try {
			Statement statement = Methods.getPlugin().c.createStatement();
			ResultSet res = statement.executeQuery("SELECT EXISTS(SELECT uuid FROM crates WHERE uuid='" + id.toString()  + "')");
			res.next();
			flag = res.getBoolean(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	public static void insertNewClassTypeInfo(UUID id,ClassType type)
	{
		String name = type.toString().toLowerCase();
		try {
			Statement statement = Methods.getPlugin().c.createStatement();
			statement.executeUpdate("INSERT INTO " + name + "info (uuid) VALUES('"+id+"')");
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	public static void incrementScore(UUID id, Leaderboard lb){
		updateScore(id, lb, getScore(id,lb) + 1);
	}
	
	
	
	public static void updateScore(UUID id, Leaderboard lb, int score){
		
		try {
			Statement statement = Methods.getPlugin().c.createStatement();
			statement.executeUpdate("UPDATE crazytest1 SET " + lb.getMySQLColumn() + "=" + score + " WHERE uuid='" + id + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void removeEntry(){
		
	}
	
}
