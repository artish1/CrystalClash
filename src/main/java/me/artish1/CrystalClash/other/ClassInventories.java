package me.artish1.CrystalClash.other;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.Util.MySQLUtil;


public class ClassInventories {
	
	
	public static HashSet<UUID> rocketLaunchers = new HashSet<UUID>();
	public static HashMap<UUID,HashMap<ClassType,ItemStack[]>> classArmor = new HashMap<UUID,HashMap<ClassType,ItemStack[]>>();
	public static HashMap<UUID,HashMap<ClassType,ItemStack>> swords = new HashMap<UUID,HashMap<ClassType,ItemStack>>();
	public static HashMap<UUID,HashMap<String,Integer>> amounts = new HashMap<UUID,HashMap<String,Integer>>();
	public static HashMap<UUID, HashMap<String, Boolean>> booleans = new HashMap<UUID, HashMap<String,Boolean>>();
	
	public static void loadClassInventories() throws SQLException
	{
			Statement statement = Methods.getPlugin().c.createStatement();
			ResultSet engineerInfo = statement.executeQuery("SELECT * FROM engineerinfo");
			while(engineerInfo.next())
			{
				UUID id = UUID.fromString(engineerInfo.getString("uuid"));
				if(engineerInfo.getBoolean("rocketlauncher"))
				{
					rocketLaunchers.add(id);
				}
			}
			
			for(ClassType type : ClassType.values())
			{
				ResultSet res  = statement.executeQuery("SELECT * FROM " + type.toString().toLowerCase() + "info");
				while(res.next())
				{
					UUID id = UUID.fromString(res.getString("uuid"));
					ItemStack helmet = new ItemStack(Material.AIR);
					ItemStack chestplate = new ItemStack(Material.AIR);
					ItemStack leggings = new ItemStack(Material.AIR);
					ItemStack boots = new ItemStack(Material.AIR); 
					ItemStack sword = new ItemStack(Material.AIR);
					if(MySQLUtil.doesColumnExist(type.toString().toLowerCase() + "info", "boots"))
						boots = new ItemStack(Material.getMaterial(res.getString("boots").toUpperCase())); 
						
					if(MySQLUtil.doesColumnExist(type.toString().toLowerCase() + "info", "chestplate"))
						chestplate = new ItemStack(Material.getMaterial(res.getString("chestplate").toUpperCase()));
						
					if(MySQLUtil.doesColumnExist(type.toString().toLowerCase() + "info", "leggings"))
						leggings = new ItemStack(Material.getMaterial(res.getString("leggings").toUpperCase()));
					
					if(MySQLUtil.doesColumnExist(type.toString().toLowerCase() + "info", "helmet"))	
						helmet = new ItemStack(Material.getMaterial(res.getString("helmet").toUpperCase()));
					
					if(MySQLUtil.doesColumnExist(type.toString().toLowerCase() + "info", "sword"))
						sword = new ItemStack(Material.getMaterial(res.getString("sword").toUpperCase()));
					
					ItemStack[] array = new ItemStack[] {boots,leggings,chestplate,helmet};
					setUnbreakable(array);
					setUnbreakable(sword);
					
					if(classArmor.containsKey(id))
					{
						classArmor.get(id).put(type, array);
					}else{
						HashMap<ClassType, ItemStack[]> map = new HashMap<ClassType,ItemStack[]>();
						map.put(type, array);
						classArmor.put(id,map);
					}
					if(swords.containsKey(id))
					{
						swords.get(id).put(type, sword);
					}else{
						HashMap<ClassType, ItemStack> map = new HashMap<ClassType,ItemStack>();
						map.put(type, sword);
						swords.put(id,map);
					}
					
					
					//Finished loading default stuff
					if(amounts.containsKey(id))
					{
						if(type == ClassType.SNIPER)
						{
							amounts.get(id).put("claymore", res.getInt("claymore"));
							amounts.get(id).put("ammo", res.getInt("ammo"));
						}
						if(type == ClassType.ARCHER)
						{
							amounts.get(id).put("archer ammo", res.getInt("ammo"));
						}
						if(type == ClassType.EXPLOSIVES)
						{
							amounts.get(id).put("explosives c4amount", res.getInt("c4amount"));
							amounts.get(id).put("explosives grenades", res.getInt("grenades"));
							amounts.get(id).put("explosives mineamount", res.getInt("mineamount"));
							amounts.get(id).put("explosives c4arrows", res.getInt("c4arrows"));
						}
						
					}else{
						if(type == ClassType.SNIPER)
						{
							HashMap<String,Integer> map = new HashMap<String,Integer>();
							map.put("claymore", res.getInt("claymore"));
							map.put("ammo", res.getInt("ammo"));
							amounts.put(id, map);
						}
						if(type == ClassType.ARCHER)
						{
							HashMap<String,Integer> map = new HashMap<String, Integer>();
							map.put("archer ammo", res.getInt("ammo"));
							amounts.put(id, map);
							
						}
						if(type == ClassType.EXPLOSIVES)
						{
							HashMap<String,Integer> map = new HashMap<String, Integer>();
							map.put("explosives c4amount", res.getInt("c4amount"));
							map.put("explosives grenades", res.getInt("grenades"));
							map.put("explosives mineamount", res.getInt("mineamount"));
							map.put("explosives c4arrows", res.getInt("c4arrows"));
							amounts.put(id, map);
							
						}
						
						
					}
					
					if(booleans.containsKey(id))
					{
						
						if(type == ClassType.ENDER)
						{
							booleans.get(id).put("ender chest ench", res.getBoolean("chestench"));
							booleans.get(id).put("ender leggings ench", res.getBoolean("leggingsench"));
							booleans.get(id).put("ender boots ench", res.getBoolean("bootsench"));
							booleans.get(id).put("ender helmet ench", res.getBoolean("helmetench"));
							booleans.get(id).put("ender sword ench", res.getBoolean("swordench"));
							
						}
						
						if(type == ClassType.TANK)
						{
							booleans.get(id).put("tank chest ench", res.getBoolean("chestench"));
							booleans.get(id).put("tank leggings ench", res.getBoolean("leggingsench"));
							booleans.get(id).put("tank boots ench", res.getBoolean("bootsench"));
							booleans.get(id).put("tank helmet ench", res.getBoolean("helmetench"));
						}
						if(type == ClassType.EARTH)
						{
							booleans.get(id).put("earth chest ench", res.getBoolean("chestench"));
							booleans.get(id).put("earth helmet ench", res.getBoolean("helmetench"));
						}
						
						
						
					}else{
						HashMap<String,Boolean> map = new HashMap<String,Boolean>();
						if(type == ClassType.ENDER)
						{
							map.put("ender chest ench", res.getBoolean("chestench"));
							map.put("ender leggings ench", res.getBoolean("leggingsench"));
							map.put("ender boots ench", res.getBoolean("bootsench"));
							map.put("ender helmet ench", res.getBoolean("helmetsench"));
						}else{
							if(type == ClassType.TANK)
							{
								map.put("tank chest ench", res.getBoolean("chestench"));
								map.put("tank leggings ench", res.getBoolean("leggingsench"));
								map.put("tank boots ench", res.getBoolean("bootsench"));
								map.put("tank helmet ench", res.getBoolean("helmetench"));
							}else{
								if(type == ClassType.EARTH)
								{
									map.put("earth chest ench", res.getBoolean("chestench"));
									map.put("earth helmet ench", res.getBoolean("helmetench"));
								}
							}
						}
						
						
						booleans.put(id, map);
					}
				}
				Methods.getPlugin().getLogger().info("Finished loading in class inventory: " + type.getName()); 
			}
		
	}
	
	public static ItemStack setUnbreakable(ItemStack item){
		if(item == null || item.getType() == Material.AIR)
			return item;
		
		ItemMeta meta = item.getItemMeta();
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack[] setUnbreakable(ItemStack[] items)
	{
		for(ItemStack item : items)
		{
			setUnbreakable(item);
		}
		return items;
	}
	
	public static ItemStack getEngineerRocketLauncher()
	{
		ItemStack item = new ItemStack(Material.IRON_HOE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Rocket Launcher");
		meta.setLore(createLore("Right click to", "shoot a rocket!"));
		item.setItemMeta(meta);
		return item;
	}
	
	
	public static ItemStack[] getSpiderArmor(){
		ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
		ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
		setLeatherColor(boots, Color.PURPLE);
		setLeatherColor(chestplate, Color.PURPLE);
		setLeatherColor(leggings, Color.PURPLE);
		setLeatherColor(helmet, Color.PURPLE);
		return setUnbreakable(new ItemStack[] {boots,leggings,chestplate,helmet});	
	}
	
	public static ItemStack[] getGuardianArmor(){
		ItemStack chestplate = new ItemStack(Material.GOLDEN_CHESTPLATE);
		chestplate.addEnchantment(Enchantment.DURABILITY, 3); 
		ItemStack boots = new ItemStack(Material.GOLDEN_BOOTS);
		boots.addEnchantment(Enchantment.DURABILITY, 3); 

		ItemStack leggings = new ItemStack(Material.GOLDEN_LEGGINGS);
		leggings.addEnchantment(Enchantment.DURABILITY, 3); 

		ItemStack helmet = new ItemStack(Material.GOLDEN_HELMET);
		helmet.addEnchantment(Enchantment.DURABILITY, 3); 
		
		return setUnbreakable(new ItemStack[] {boots,leggings,chestplate,helmet});	
	}
	
	public static ItemStack[] getNecromancerArmor(){
		ItemStack head = new ItemStack(Material.WITHER_SKELETON_SKULL);
		
		ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
		chestplate.addEnchantment(Enchantment.DURABILITY, 3); 
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		boots.addEnchantment(Enchantment.DURABILITY, 3); 
		
		ItemStack leggings = new ItemStack(Material.AIR);

		
		setLeatherColor(boots, Color.BLACK);
		setLeatherColor(chestplate,Color.BLACK);
		
		
		return setUnbreakable(new ItemStack[] {boots,leggings,chestplate,head});	
	}
	
	public static ItemStack[] getGuardianContents(){
		ItemStack sword = new ItemStack(Material.GOLDEN_SWORD);
		sword.addEnchantment(Enchantment.DURABILITY, 3); 
		ItemMeta meta = sword.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "Zeus's Empowered Sword"); 
		sword.setItemMeta(meta);
		
		return setUnbreakable(new ItemStack[] {sword, getGuardianHealItem()});
	}
	
	public static ItemStack[] getNecromancerContents(){
		return setUnbreakable(new ItemStack[] {getNecromancerWand(),getNecromancerSkeletonItem(),getNecromancerZombieItem()});
	}
	
	public static ItemStack getNecromancerWand(){
		ItemStack item = new ItemStack(Material.STICK); 
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_PURPLE + "Wither Wand");
		meta.setLore(createLore("Shoots wither skulls."));
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, 2);  
		return item;
	}
	
	public static ItemStack getNecromancerSkeletonItem(){
		ItemStack item = new ItemStack(Material.GUNPOWDER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_PURPLE + "Summon Skeleton");
		meta.setLore(createLore("Spawns a Skeleton to ", "fight for you"));
		item.setItemMeta(meta);
		return item;
	}
	public static ItemStack getNecromancerZombieItem(){
		ItemStack item = new ItemStack(Material.ROTTEN_FLESH); 
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_AQUA + "Summon Zombie");
		meta.setLore(createLore("Spawns a Zombie to ", "fight for you"));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getGuardianHealItem(){
		ItemStack item = new ItemStack(Material.EMERALD); 
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Heavenly Heal");
		meta.setLore(createLore("Heal yourself and all teammates around you"));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack[] getSpiderContents(){
		ItemStack sword = new ItemStack(Material.IRON_SWORD);
		
		return setUnbreakable(new ItemStack[] {sword, getSpiderCobwebItem()});
	}
	
	public static ItemStack getSpiderCobwebItem(){
		ItemStack item = new ItemStack(Material.MAGMA_CREAM);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Web Throw");
		meta.setLore(createLore("Throws webs"));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack[] getMageArmor(){
		ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		ItemStack leggings = new ItemStack(Material.AIR);
		ItemStack helmet = new ItemStack(Material.AIR);
		setLeatherColor(boots, Color.AQUA);
		setLeatherColor(chestplate, Color.AQUA);

		return setUnbreakable(new ItemStack[] {boots,leggings,chestplate,helmet});	
	}
	
	public static ItemStack getMeteorShowerItem(){
		ItemStack item = new ItemStack(Material.BLAZE_ROD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_RED + "Meteor Shower");
		meta.setLore(createLore("Calls in a Meteor Shower."));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getTurretItem(){
		ItemStack item = new ItemStack(Material.DISPENSER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GRAY + "Turret");
		meta.setLore(createLore("Place it down somewhere!"));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack[] getEngineerContents(){
		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
		return setUnbreakable(new ItemStack[] {sword,getTurretItem()});	
	}
	
	
	
	public static ItemStack[] getEngineerContents(Player p){
		List<ItemStack> itemList = new ArrayList<ItemStack>();
		
		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
		if(swords.containsKey(p.getUniqueId()))
		{
			sword = swords.get(p.getUniqueId()).get(ClassType.ENGINEER);
		}
		itemList.add(sword);
		/*if(MySQLUtil.getBoolean(p.getUniqueId(), "engineerinfo", "rocketlauncher"))
		{
			itemList.add(getEngineerRocketLauncher());
		}*/
		if(rocketLaunchers.contains(p.getUniqueId()))
		{
			itemList.add(getEngineerRocketLauncher()); 
		}
		itemList.add(getTurretItem());
		ItemStack[] array = itemList.toArray(new ItemStack[itemList.size()]);
		return setUnbreakable(array);	
	}
	
	
	public static ItemStack[] getEngineerArmor(Player p){
		if(classArmor.containsKey(p.getUniqueId()))
		{	
			return classArmor.get(p.getUniqueId()).get(ClassType.ENGINEER);
		}
		return getEngineerArmor();
	}
	
	public static ItemStack[] getEngineerArmor(){
		ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
		ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
		ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		return setUnbreakable(new ItemStack[] {boots,leggings,chestplate,helmet});	
	}
	
	public static ItemStack[] getMageContents(){
		ItemStack sword = new ItemStack(Material.WOODEN_SWORD);
		return setUnbreakable(new ItemStack[] {sword,getFireballItem(), getLightningMageItem(), getMeteorShowerItem()});	
	}
	
	
	public static ItemStack getLightningMageItem(){
		ItemStack item = new ItemStack(Material.NETHER_STAR);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "Lightning Storm");
		meta.setLore(createLore("Sets off a lightning storm."));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getFireballItem(){
		ItemStack item = new ItemStack(Material.STICK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Fireball");
		meta.setLore(createLore("Spawns a Fireball"));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getClaymoreItem(){
		ItemStack item = new ItemStack(Material.REDSTONE_LAMP, 3);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Claymore");
		meta.setLore(createLore("A motion sensor explosive."));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getClaymoreItem(Player p){
		
		ItemStack item = new ItemStack(Material.REDSTONE_LAMP, amounts.get(p.getUniqueId()).get("claymore"));
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Claymore");
		meta.setLore(createLore("A motion sensor explosive."));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getSniperAmmo(int amount){
		ItemStack item = new ItemStack(Material.SNOWBALL, amount);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Ammo");
		meta.setLore(createLore("Necessary ammo."));
		item.setItemMeta(meta);
		return item;
	}
	
	public static void setLeatherColor(ItemStack item,Color color){
		LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(color);
		item.setItemMeta(meta);
	}
	
	public static ItemStack[] getEnderArmor(){
		ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
		ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
		ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		
		setLeatherColor(boots, Color.PURPLE);
		setLeatherColor(chestplate, Color.PURPLE);
		setLeatherColor(leggings, Color.PURPLE);
		setLeatherColor(helmet, Color.PURPLE);

		return setUnbreakable(new ItemStack[] {boots,leggings,chestplate,helmet});	
	}
	
	public static ItemStack[] getEnderArmor(Player p){
		ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
		ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
		ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		
		if(booleans.containsKey(p.getUniqueId()))
		{
			if(booleans.get(p.getUniqueId()).get("ender helmet ench"))
			helmet.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		}
		if(booleans.containsKey(p.getUniqueId()))
		{
			if(booleans.get(p.getUniqueId()).get("ender chest ench"))
			chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		}
		if(booleans.containsKey(p.getUniqueId()))
		{
			if(booleans.get(p.getUniqueId()).get("ender leggings ench"))
			leggings.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		}
		if(booleans.containsKey(p.getUniqueId()))
		{
			if(booleans.get(p.getUniqueId()).get("ender boots ench"))
			boots.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		}
		
		setLeatherColor(boots, Color.PURPLE);
		setLeatherColor(chestplate, Color.PURPLE);
		setLeatherColor(leggings, Color.PURPLE);
		setLeatherColor(helmet, Color.PURPLE);

		return setUnbreakable(new ItemStack[] {boots,leggings,chestplate,helmet});	
	}

	public static ItemStack getEndermanTeleportItem(){
		ItemStack item = new ItemStack(Material.ENDER_EYE);
		ItemMeta meta = item.getItemMeta();
//		meta.spigot().setUnbreakable(true);
		meta.setDisplayName(ChatColor.DARK_PURPLE + "Teleport");
		meta.setLore(createLore("Teleports you to", "a target location", "of up to 10 blocks"));
		item.setItemMeta(meta);
		
		return item;
	}
	
	public static ItemStack[] getSniperArmor(){
		ItemStack helmet = new ItemStack(Material.AIR);
		ItemStack chestplate = new ItemStack(Material.AIR);
		ItemStack leggings = new ItemStack(Material.AIR);
		ItemStack boots = new ItemStack(Material.AIR);
		
		ItemStack[] items = new ItemStack[]{boots,leggings,chestplate,helmet};
		setUnbreakable(items);
		return items;
	}
	
	public static ItemStack[] getSniperContents(){
		ItemStack ammo = new ItemStack(Material.SNOWBALL, 64);
		ItemStack[] items = new ItemStack[]{getSniperItem(), getClaymoreItem() ,ammo};
		setUnbreakable(items);
		return items;
	}
	
	public static ItemStack[] getSniperContents(Player p){
		ItemStack ammo = new ItemStack(Material.SNOWBALL, 64);
		if(amounts.containsKey(p.getUniqueId())){
			ammo = new ItemStack(Material.SNOWBALL, amounts.get(p.getUniqueId()).get("ammo"));
		}
		ItemStack[] items = new ItemStack[]{getSniperItem(), getClaymoreItem(p) ,ammo};
		setUnbreakable(items);
		return items;
	} 
	
	public static ItemStack getSniperItem(){
		ItemStack item = new ItemStack(Material.BLAZE_ROD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Sniper Rifle");
		meta.setLore(createLore("A powerful rifle."));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack[] getEnderContents(){
		ItemStack sword = new ItemStack(Material.IRON_SWORD);
		
		
		return setUnbreakable(new ItemStack[] {sword,getEndermanTeleportItem()});
		
	}
	
	public static ItemStack[] getEnderContents(Player player){
		ItemStack sword = new ItemStack(Material.IRON_SWORD);
		if(booleans.get(player.getUniqueId()).get("ender sword ench"))
			sword.addEnchantment(Enchantment.DAMAGE_ALL, 1); 
		
		
		return setUnbreakable(new ItemStack[] {sword,getEndermanTeleportItem()});
	}
	
	public static void addArrow(Player player){
		ItemStack arrow = new ItemStack(Material.ARROW,1);
		player.getInventory().addItem(arrow);
		player.updateInventory();
	}
	
	public static ItemStack getTankAbilityItem(){
		ItemStack abilityItem = new ItemStack(Material.FIRE_CHARGE);
		ItemMeta meta = abilityItem.getItemMeta();
		meta.setDisplayName(ChatColor.BLUE.toString() +  "Ground Smash");
		meta.setLore(createLore("Click the ground to", "throw nearby players", "in the sky"));
		abilityItem.setItemMeta(meta);
		return abilityItem;
	}
	
	public static ItemStack[] getTankContents(){
		ItemStack sword = new ItemStack(Material.WOODEN_SWORD);
		setDisplayName(sword, "Simple Sword");
		setLore(sword, createLore("Take them down."));
		ItemStack[] items = new ItemStack[] {sword, getTankAbilityItem()};
		setUnbreakable(items);
		return items;
	}
	
	public static ItemStack[] getTankArmor(){
		ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
		ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
		ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
		ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
		
		return setUnbreakable(new ItemStack[] {boots,leggings,chestplate,helmet});
		
	}
	
	public static ItemStack[] getTankArmor(Player p){
		ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
		ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
		ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
		ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
		
		if(booleans.get(p.getUniqueId()).get("tank helmet ench"))
			helmet.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		
		if(booleans.get(p.getUniqueId()).get("tank chest ench"))
			chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		
		if(booleans.get(p.getUniqueId()).get("tank leggings ench"))
			leggings.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		
		if(booleans.get(p.getUniqueId()).get("tank boots ench"))
			boots.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		
		return setUnbreakable(new ItemStack[] {boots,leggings,chestplate,helmet});
		
	}

	public static ItemStack getEarthWallItem(){
		ItemStack wall = new ItemStack(Material.FIRE_CHARGE);
		ItemMeta meta = wall.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Earth Wall");

		wall.setItemMeta(meta);
		setLore(wall, createLore("Right click to make a wall."));
		return wall;
	}
	
	public static ItemStack getEarthDomeItem(){
		ItemStack dome = new ItemStack(Material.CLAY_BALL);
		ItemMeta domeMeta = dome.getItemMeta();
		domeMeta.setDisplayName(ChatColor.RED + "Earth Dome");
		dome.setItemMeta(domeMeta);
		
		setLore(dome, createLore("Right click to make a dome."));
		return dome;
	}
	
	public static ItemStack[]  getEarthContents(){
		ItemStack sword = new ItemStack(Material.STONE_SWORD);
		setDisplayName(sword, ChatColor.RED + "Earthly Sword");
		setLore(sword, createLore("A run-of-the-mill weapon."));
		
		
		ItemStack blockThrow = new ItemStack(Material.STICK);
		ItemMeta itemMeta = blockThrow.getItemMeta();
		itemMeta.setDisplayName(ChatColor.RED + "Block Throw");
		blockThrow.setItemMeta(itemMeta);
		setLore(blockThrow, createLore("Right click a block and left click its target."));
		
		ItemStack[] items = new ItemStack[] {sword,blockThrow,getEarthWallItem(),getEarthDomeItem()};
		setUnbreakable(items);
		return items;
	}
	
	public static ItemStack[]  getEarthContents(Player p){
		ItemStack sword = swords.get(p.getUniqueId()).get(ClassType.EARTH); 
		setDisplayName(sword, ChatColor.RED + "Earthly Sword");
		setLore(sword, createLore("A run-of-the-mill weapon."));
		
		
		ItemStack blockThrow = new ItemStack(Material.STICK);
		ItemMeta itemMeta = blockThrow.getItemMeta();
		itemMeta.setDisplayName(ChatColor.RED + "Block Throw");
		blockThrow.setItemMeta(itemMeta);
		setLore(blockThrow, createLore("Right click a block and left click its target."));
		
		ItemStack[] items = new ItemStack[] {sword,blockThrow,getEarthWallItem(),getEarthDomeItem()};
		setUnbreakable(items);
		return items;
	}
	
	public static ItemStack[] getEarthArmor(){
		ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
		ItemStack chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
		ItemStack leggings = new ItemStack(Material.AIR);
		ItemStack boots = new ItemStack(Material.AIR);


        return new ItemStack[] { boots, leggings, chestplate, helmet};
	}
	
	public static ItemStack[] getEarthArmor(Player player){
		ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
		ItemStack chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
		ItemStack leggings = new ItemStack(Material.AIR);
		ItemStack boots = new ItemStack(Material.AIR);
		
		if(booleans.get(player.getUniqueId()).get("earth helmet ench"))
			helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		
		 
		if(booleans.get(player.getUniqueId()).get("earth chest ench"))
			chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		
		
		ItemStack[] items = new ItemStack[] { boots, leggings, chestplate, helmet};
		return items;
	}
	
	public static void setDisplayName(ItemStack item, String message){
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(message);
		item.setItemMeta(meta);
	}
	
	public static ItemStack[] getScoutContents(){
		ItemStack sword = new ItemStack(Material.IRON_SWORD);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
		setDisplayName(sword, ChatColor.AQUA + "Scout Sword");
		
		ItemStack[] items = new ItemStack[] {sword};
		setUnbreakable(items);
		return items;
	}
	
	public static ItemStack[] getExplosivesArmor(){
		ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
		ItemStack chestplate = new ItemStack(Material.AIR);
		ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		ItemStack[] items = new ItemStack[]{boots,leggings,chestplate,helmet};
		setUnbreakable(items);
		return items;
	}
	
	public static ItemStack getExplosivesBow()
	{
		ItemStack item = new ItemStack(Material.BOW);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "High Frequency Bow");
		meta.setLore(createLore("Shoot C4 tipped", "arrows from this", "bow, then left", "click the bow to", "explode all the arrows"));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack[] getExplosivesContents(){
		ItemStack sword = new ItemStack(Material.STONE_SWORD);
		setDisplayName(sword, "Basic Sword");
		setLore(sword, createLore("When you're up, close, and personal."));
		ItemStack c4 = new ItemStack(Material.SPONGE,32);
		ItemMeta c4M = c4.getItemMeta();
		c4M.setDisplayName(ChatColor.GREEN + "C4");
		c4.setItemMeta(c4M);
		setLore(c4, createLore("Place them down and detonate!"));
		
		
		ItemStack grenades = new ItemStack(Material.EGG,32);
		ItemMeta grenadeM = grenades.getItemMeta();
		grenadeM.setDisplayName(ChatColor.GREEN + "Grenade");
		grenades.setItemMeta(grenadeM);
		setLore(grenades, createLore("Throw to explode."));
		
		ItemStack mines = new ItemStack(Material.STONE_PRESSURE_PLATE,5);
		ItemMeta mineMeta = mines.getItemMeta();
		mineMeta.setDisplayName(ChatColor.DARK_AQUA + "Land Mine");
		mineMeta.setLore(createLore("Give a nasty surprise", "to your enemies."));
		mines.setItemMeta(mineMeta);
		
		List<ItemStack> itemss = new ArrayList<ItemStack>();
		itemss.add(sword);
		itemss.add(getExplosiveDetonator());
		itemss.add(mines);
		itemss.add(grenades);
		itemss.add(c4);
		ItemStack[] iArray = itemss.toArray(new ItemStack[itemss.size()]);
		setUnbreakable(iArray);
		return iArray;
	}
	
	public static ItemStack getExplosiveGrenades(int amount)
	{
		ItemStack grenades = new ItemStack(Material.EGG,amount);
		ItemMeta grenadeM = grenades.getItemMeta();
		grenadeM.setDisplayName(ChatColor.GREEN + "Grenade");
		grenades.setItemMeta(grenadeM);
		setLore(grenades, createLore("Throw to explode."));
		return grenades;
	}
	
	public static ItemStack[] getExplosivesContents(Player p){
		ItemStack sword = swords.get(p.getUniqueId()).get(ClassType.EXPLOSIVES); 
		setDisplayName(sword, "Basic Sword");
		setLore(sword, createLore("When you're up, close, and personal."));
		ItemStack c4 = new ItemStack(Material.SPONGE,amounts.get(p.getUniqueId()).get("explosives c4amount"));
		ItemMeta c4M = c4.getItemMeta();
		c4M.setDisplayName(ChatColor.GREEN + "C4");
		c4.setItemMeta(c4M);
		setLore(c4, createLore("Place them down and detonate!"));
		
		
		ItemStack grenades = new ItemStack(Material.EGG,amounts.get(p.getUniqueId()).get("explosives grenades"));
		ItemMeta grenadeM = grenades.getItemMeta();
		grenadeM.setDisplayName(ChatColor.GREEN + "Grenade");
		grenades.setItemMeta(grenadeM);
		setLore(grenades, createLore("Throw to explode."));
		
		ItemStack mines = new ItemStack(Material.STONE_PRESSURE_PLATE,amounts.get(p.getUniqueId()).get("explosives mineamount"));
		ItemMeta mineMeta = mines.getItemMeta();
		mineMeta.setDisplayName(ChatColor.DARK_AQUA + "Land Mine");
		mineMeta.setLore(createLore("Give a nasty surprise", "to your enemies."));
		mines.setItemMeta(mineMeta);
		
		List<ItemStack> itemss = new ArrayList<ItemStack>();
		itemss.add(sword);
		if(MySQLUtil.getBoolean(p.getUniqueId(), "explosivesinfo", "c4bow"))
		{
			itemss.add(getExplosivesBow());
			ItemStack arrows = new ItemStack(Material.ARROW,amounts.get(p.getUniqueId()).get("explosives c4arrows"));
			ItemMeta meta = arrows.getItemMeta();
			meta.setDisplayName(ChatColor.GREEN + "C4-Tipped Arrows");
			arrows.setItemMeta(meta);
			itemss.add(arrows);
		}
		itemss.add(getExplosiveDetonator());
		itemss.add(mines);
		itemss.add(grenades);
		itemss.add(c4);
		p.getInventory().setItemInHand(grenades);
		p.updateInventory();
		ItemStack[] iArray = itemss.toArray(new ItemStack[itemss.size()]);
		setUnbreakable(iArray);
		return iArray;
	}
	
	
	public static ItemStack[] getScoutArmor(){
		ItemStack helmet = new ItemStack(Material.IRON_HELMET);
		ItemStack chestplate = new ItemStack(Material.AIR);
		
		ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
		ItemStack boots = new ItemStack(Material.IRON_BOOTS);
		
		ItemStack[] items = new ItemStack[]{boots,leggings,chestplate,helmet};
		setUnbreakable(items);
		return items;
	}
	public static ItemStack[] getScoutArmor(Player p){
		return classArmor.get(p.getUniqueId()).get(ClassType.SCOUT);
	}
	
	public static ItemStack getAssassinInvis(){
		ItemStack invis = new ItemStack(Material.getMaterial(Methods.getPlugin().kits.getString("Assassin.Invisible.Material")));
		ItemMeta invism = invis.getItemMeta();
		invism.setDisplayName(ChatColor.RED + "Invisibility Trigger");
		invism.setLore(createLore("Click for invisibility"));
		invis.setItemMeta(invism);
		return invis;
	}
	
	public static void setLore(ItemStack item, List<String> lore){
		ItemMeta meta = item.getItemMeta();
		meta.setLore(lore);
		item.setItemMeta(meta);
	}
	
	public static ItemStack[] getAssassinContents(){
		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
		setDisplayName(sword, ChatColor.RED + "Diamond Dagger");
		setLore(sword, createLore("Quick and easy."));  
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
		
		ItemStack[] items = new ItemStack[] {sword, getAssassinInvis()};
		setUnbreakable(items);
		return items; 
	}

	public static ItemStack[] getAssassinArmor(){
		ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
		ItemStack chestplate = new ItemStack(Material.AIR);
		ItemStack leggings = new ItemStack(Material.AIR);
		ItemStack boots = new ItemStack(Material.AIR);
		
		ItemStack[] items = new ItemStack[] {boots,leggings,chestplate,helmet};
		setUnbreakable(items);
		return items;
	}
	
	public static ItemStack getArcherArrowBarrage(){
		ItemStack caller = new ItemStack(Material.STICK);
		ItemMeta callerMeta = caller.getItemMeta();
		callerMeta.setDisplayName(ChatColor.DARK_RED + "Arrow Barrage");
		callerMeta.setLore(createLore("Left click a location to make it rain."));
		
		caller.setItemMeta(callerMeta);
		return caller;
	}
	
	public static ItemStack getArcherBow(){
		ItemStack bow = new ItemStack(Material.BOW);
		bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
		ItemMeta bowMeta = bow.getItemMeta();
		bowMeta.setDisplayName(ChatColor.GREEN + "Multi-Mode Bow");
		bowMeta.setLore(createLore("Left click To change Modes."));
		bow.setItemMeta(bowMeta);
		return bow;
	}
	
	public static ItemStack[] getArcherContents(){
		ItemStack sword = new ItemStack(Material.STONE_SWORD);
		setDisplayName(sword, ChatColor.GREEN + "Dull Knife");
		setLore(sword, createLore("Only for extreme cases."));
		ItemStack arrows = new ItemStack(Material.ARROW,64);
		setDisplayName(arrows, ChatColor.GREEN +"Arrow");
		setLore(arrows, createLore("Necessary ammo."));
		
		
		ItemStack[] items = new ItemStack[] {getArcherBow(),getArcherArrowBarrage(),sword,arrows};
		setUnbreakable(items);
		return items;
	}
	
	public static ItemStack[] getArcherContents(Player p){
		ItemStack sword = swords.get(p.getUniqueId()).get(ClassType.ARCHER); 
		setDisplayName(sword, ChatColor.GREEN + "Dull Knife"); 
		setLore(sword, createLore("Only for extreme cases."));
		ItemStack arrows = new ItemStack(Material.ARROW,amounts.get(p.getUniqueId()).get("archer ammo"));
		setDisplayName(arrows, ChatColor.GREEN +"Arrow");
		setLore(arrows, createLore("Necessary ammo."));
		
		
		ItemStack[] items = new ItemStack[] {getArcherBow(),getArcherArrowBarrage(),sword,arrows};
		setUnbreakable(items);
		return items;
	}
	
	public static ItemStack[] getArcherArmor(){
		ItemStack helmet = new ItemStack(Material.CHAINMAIL_HELMET);
		ItemStack chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
		ItemStack leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
		ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS);
		
		ItemStack[] items = new ItemStack[] {boots,leggings,chestplate,helmet};
		setUnbreakable(items);
		return items;
	}
	
	public static ItemStack[] getArcherArmor(Player p){
		
		return classArmor.get(p.getUniqueId()).get(ClassType.ARCHER);
	}
	
	public static ItemStack getEnchantedSword(Material mat){
		ItemStack item = new ItemStack(mat);
		item.addEnchantment(Enchantment.DAMAGE_ALL, 5);
		
		return item;
	}
	
	public static List<String> createLore(String... strings){
		List<String> oList = new ArrayList<String>();
		for(String s : strings){
			s = ChatColor.GRAY + s;
			oList.add(s);
		}
		List<String> lore = oList;
		return lore;
	}
	
	public static ItemStack getExplosiveDetonator(){
		ItemStack detonator = new ItemStack(Material.EMERALD);
		ItemMeta meta = detonator.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "C4 Detonator");
		meta.setLore(createLore("Right click to detonate", "all your C4"));
		detonator.setItemMeta(meta);
		return detonator;
	}
	
}
