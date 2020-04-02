package me.artish1.CrystalClash.Classes;

import me.artish1.CrystalClash.Menu.Menu;
import me.artish1.CrystalClash.Menu.items.*;
import me.artish1.CrystalClash.Menu.menus.*;
import me.artish1.CrystalClash.Util.Methods;
import me.artish1.CrystalClash.other.ClassInventories;
import me.artish1.CrystalClash.other.TipManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public enum ClassType {
	
	//Elemental Classes
	EARTH(ChatColor.YELLOW + "Earth",false,new EarthItem(),new ItemStack(Material.DIRT) ,TipManager.getEarthTips(),false), 
	//FIRE,//TODO+++++++++++++++++++++++++++++++
	
	
	//Normal Classes
	SCOUT(ChatColor.AQUA + "Scout",true,new ScoutItem(),ClassInventories.getEnchantedSword(Material.IRON_SWORD) ,TipManager.getScoutTips(),false),
	ARCHER(ChatColor.GREEN + "Archer",true,new ArcherItem(),ClassInventories.getArcherBow() ,TipManager.getArcherTips(),false),
	SNIPER(ChatColor.RED + "Sniper",true,new SniperItem(), ClassInventories.getSniperItem() , TipManager.getSniperTips(),false),
	ASSASSIN(ChatColor.RED  + "Assassin",true,new AssassinItem(),ClassInventories.getEnchantedSword(Material.DIAMOND_SWORD) ,TipManager.getAssassinTips(),false),
	ENGINEER(ChatColor.DARK_GRAY + "Engineer",false,new EngineerItem(),new ItemStack(Material.IRON_PICKAXE) ,TipManager.getEngineerTips(),false),
	EXPLOSIVES(ChatColor.LIGHT_PURPLE + "Bomber",false,new ExplosivesItem(), new ItemStack(Material.TNT),TipManager.getExplosiveTips(),false),
	TANK(ChatColor.BLUE  + "Tank",false,new TankItem(),new ItemStack(Material.STONE_SWORD),TipManager.getTankTips(),false),
	GUARDIAN(ChatColor.YELLOW + "Guardian", true,new GuardianItem(null), new ItemStack(Material.GOLDEN_SWORD),
            TipManager.getGuardianTips(),false),
	//Abnormal Classes
	MAGE(ChatColor.AQUA + "Mage",true,new MageItem(),ClassInventories.getFireballItem(),TipManager.getMageTips(),false),
	NECROMANCER(ChatColor.DARK_PURPLE + "Necromancer", true,new NecromancerItem(null),ClassInventories.getNecromancerWand(), TipManager.getNecromancerTips(),false),
	//GHOST, 
	
	//Mob-Type classes 
	ENDER(ChatColor.DARK_PURPLE + "Ender",false,new EnderItem(), ClassInventories.getEnchantedSword(Material.IRON_SWORD),TipManager.getEnderTips(),false),
	SPIDER(ChatColor.DARK_PURPLE + "Spider",false,new SpiderItem(), ClassInventories.getSpiderCobwebItem(),TipManager.getSpiderTips(),false);
	
	private List<String> tipsList;
	//private ItemStack[] armor;
	//private ItemStack[] contents;
	private boolean useSkeleton;
	private UUID modelUUID;
	private String name;
	private ItemStack modelItem;
	private MenuItem classItem;
	private int price = 0;
	private boolean free = true;
	private boolean donator;
	
	private ClassType(String name ,boolean useSkeleton,MenuItem classPickItem,ItemStack modelHandItem,List<String> tips,boolean donator) {
		this.tipsList = tips;
		this.donator = donator;
		this.modelItem = modelHandItem;
		this.name = name;
		this.classItem = classPickItem;
		this.useSkeleton = useSkeleton;
	}
	
	
	public boolean isDonator() {
		return donator;
	}
	
	
	
	public boolean useSkeletonModel(){
		return useSkeleton;
	}
	
	public ItemStack getModelItem() {
		return modelItem;
	}
	
	
	
	public Menu getUpgradeMenu() {
		switch (this) {
		case ARCHER:
			return new ArcherUpgradeMenu(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Archer upgrades", 9 * 6, Methods.getPlugin(), Menus.getUpgradeMenu());
		case ASSASSIN:
			return new AssassinUpgradeMenu(ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString() + "Assassin upgrades.", 9 * 6,Methods.getPlugin(),Menus.getUpgradeMenu());
		case EARTH:
			return new EarthUpgradeMenu();
		case ENDER:
			return new EnderUpgradeMenu();
		case ENGINEER:
			return new EngineerUpgradeMenu();
		case EXPLOSIVES:
			return new ExplosivesUpgradeMenu();
		case GUARDIAN:
			return new GuardianUpgradeMenu();
		case MAGE:
			return new MageUpgradeMenu();
		case NECROMANCER:
			return new NecromancerUpgradeMenu();
		case SCOUT:
			return new ScoutUpgradeMenu(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "Scout Upgrades", 9*6, Methods.getPlugin(),Menus.getUpgradeMenu());
		case SNIPER:
			return new SniperUpgradeMenu(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "Sniper upgrades", 9*6, Methods.getPlugin(), Menus.getUpgradeMenu());
		case SPIDER:
			return new SpiderUpgradeMenu();
		case TANK:
			return new TankUpgradeMenu();
		default:
			return null;
		}
	}
	
	public ItemStack[] getContents()
	{
		switch(this)
		{
		case ARCHER:
			return ClassInventories.getArcherContents();
			
		case ASSASSIN:
			return ClassInventories.getAssassinContents();
			
		case EARTH:
			return ClassInventories.getEarthContents();
			
		case ENDER:
			return ClassInventories.getEnderContents();
			
		case ENGINEER:
			return ClassInventories.getEngineerContents();
			
		case EXPLOSIVES:
			return ClassInventories.getExplosivesContents();
			
		case GUARDIAN:
			return ClassInventories.getGuardianContents();
			
		case MAGE:
			return ClassInventories.getMageContents();
			
		case NECROMANCER:
			return ClassInventories.getNecromancerContents();
			
		case SCOUT:
			return ClassInventories.getScoutContents();
			
		case SNIPER:
			return ClassInventories.getSniperContents();
			
		case SPIDER:
			return ClassInventories.getSpiderContents();
		case TANK:
			return ClassInventories.getTankContents();
			
		default:
			return new ItemStack[]{};
			
		}
		
	}
	
	public ItemStack[] getArmor()
	{
		switch(this)
		{
		case ARCHER:
			return ClassInventories.getArcherArmor();
			
		case ASSASSIN:
			return ClassInventories.getAssassinArmor();
			
		case EARTH:
			return ClassInventories.getEarthArmor();
			
		case ENDER:
			return ClassInventories.getEnderArmor();
			
		case ENGINEER:
			return ClassInventories.getEngineerArmor();
			
		case EXPLOSIVES:
			return ClassInventories.getExplosivesArmor();
			
		case GUARDIAN:
			return ClassInventories.getGuardianArmor();
			
		case MAGE:
			return ClassInventories.getMageArmor();
			
		case NECROMANCER:
			return ClassInventories.getNecromancerArmor();
			
		case SCOUT:
			return ClassInventories.getScoutArmor();
			
		case SNIPER:
			return ClassInventories.getSniperArmor();
			
		case SPIDER:
			return ClassInventories.getSpiderArmor();
		case TANK:
			return ClassInventories.getTankArmor();
			
		default:
			return new ItemStack[]{};
			
		}	
	}
	
	public void setInventory(Player player){
		//.getInventory().setArmorContents(armor);
		//player.getInventory().setContents(contents);
		ItemStack[] contents;
		ItemStack[] armor;
		switch(this)
		{
		case ARCHER:
			armor = ClassInventories.getArcherArmor(player);
			contents = ClassInventories.getArcherContents(player);
			break;
		case ASSASSIN:
			armor = ClassInventories.getAssassinArmor();
			contents = ClassInventories.getAssassinContents();
			break;
		case EARTH:
			armor = ClassInventories.getEarthArmor(player);
			contents = ClassInventories.getEarthContents(player);
			break;
		case ENDER:
			armor = ClassInventories.getEnderArmor(player);
			contents = ClassInventories.getEnderContents(player);
			break;
		case ENGINEER:
			armor = ClassInventories.getEngineerArmor(player);
			contents = ClassInventories.getEngineerContents(player);
			break;
		case EXPLOSIVES:
			armor = ClassInventories.getExplosivesArmor();
			contents = ClassInventories.getExplosivesContents(player);
			break;
		case GUARDIAN:
			armor = ClassInventories.getGuardianArmor();
			contents = ClassInventories.getGuardianContents();
			break;
		case MAGE:
			armor = ClassInventories.getMageArmor();
			contents = ClassInventories.getMageContents();
			break;
		case NECROMANCER:
			armor = ClassInventories.getNecromancerArmor();
			contents = ClassInventories.getNecromancerContents();
			break;
		case SCOUT:
			armor = ClassInventories.getScoutArmor(player);
			contents = ClassInventories.getScoutContents();
			break;
		case SNIPER:
			armor = ClassInventories.getSniperArmor();
			contents = ClassInventories.getSniperContents(player);
			break;
		case SPIDER:
			armor = ClassInventories.getSpiderArmor();
			contents = ClassInventories.getSpiderContents();
			break;
		case TANK:
			armor = ClassInventories.getTankArmor(player);
			contents = ClassInventories.getTankContents();
			break;
		default:
			armor = new ItemStack[]{};
			contents = new ItemStack[]{};
			break;
		}

		player.getInventory().setContents(contents);
		player.getInventory().setArmorContents(armor);
		
	}
	
	

	public static void init(){
		//Prices-Shop
		GUARDIAN.setFree(false);
		GUARDIAN.setPrice(110); 
		
		NECROMANCER.setFree(false);
		NECROMANCER.setPrice(150); 
		
		//Unbreakable setting
		
		
	}
	
	public boolean isFree() {
		return free;
	}
	
	public void setFree(boolean free) {
		this.free = free;
		if(free)
		this.price = 0;
	}
	
	public int getPrice() {
		return price;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
	
	
	public MenuItem getMenuItem() {
		return classItem;
	}
	
	
	public void setModelUUID(UUID id){
		this.modelUUID = id;
	}
	
	public UUID getModelUUID(){
		return this.modelUUID;
	}
	 
	public String getName() {
		return name;
	}
	
	
	
	
	
	
	public List<String> getTipsList() {
		return tipsList;
	}
	
	
	
}
