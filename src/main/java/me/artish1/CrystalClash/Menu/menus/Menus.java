package me.artish1.CrystalClash.Menu.menus;

import me.artish1.CrystalClash.Util.Methods;

import org.bukkit.ChatColor;

public class Menus {
	private static ShopMenu shopMenu; 
	private static IngameMenu ingameMenu;
	private static ShopClassMenu shopClassMenu;
	private static ShopKeysMenu shopKeysMenu;
	private static UpgradeMenu upgradeMenu;
	private static TeamPickMenu teamPickMenu;
	
	
	public static ScoutUpgradeMenu getScoutUpgradeMenu() {
		return new ScoutUpgradeMenu(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "Scout Upgrades", 9*6, Methods.getPlugin(),upgradeMenu);
	}
	public static ArcherUpgradeMenu getArcherUpgradeMenu()
	{
		return new ArcherUpgradeMenu(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Archer upgrades", 9 * 6, Methods.getPlugin(), upgradeMenu);
	}
	
	public static SniperUpgradeMenu getSniperUpgradeMenu()
	{
		return new SniperUpgradeMenu(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "Sniper upgrades", 9*6, Methods.getPlugin(), upgradeMenu);
	}
	public static AssassinUpgradeMenu getAssassinUpgradeMenu()
	{
		return new AssassinUpgradeMenu(ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString() + "Assassin upgrades.", 9 * 6,Methods.getPlugin(),upgradeMenu);
	}
	
	public static UpgradeMenu getUpgradeMenu() {
		return upgradeMenu;
	}
	public static ShopClassMenu getClassShopMenu(){
		return shopClassMenu;
	}
	
	public static IngameMenu getIngameMenu() {
		return ingameMenu;
	}
	
	public static ClassMenu getClassMenu(){
		return new ClassMenu(ChatColor.DARK_PURPLE.toString() +ChatColor.BOLD + "Class Menu", 9 * 5, Methods.getPlugin(), null);

	}
	
	public static ShopMenu getShopMenu(){
		return shopMenu;
	}
 	
	public static ShopKeysMenu getShopKeysMenu() {
		return shopKeysMenu;
	}
	public static TeamPickMenu getTeamPickMenu()
	{
		return teamPickMenu;
	}
	
	public static void initMenus()
	{
		//classMenu = new ClassMenu(ChatColor.DARK_PURPLE.toString() +ChatColor.BOLD + "Class Menu", 9 * 5, Methods.getPlugin(), null);
		shopMenu = new ShopMenu(ChatColor.YELLOW +ChatColor.BOLD.toString() + "Shop Menu",9 * 4,Methods.getPlugin());
		ingameMenu = new IngameMenu(ChatColor.GREEN.toString()+ChatColor.BOLD + "Ingame Shop",9 * 4, Methods.getPlugin(), null);
		shopClassMenu = new ShopClassMenu(ChatColor.GOLD.toString() +net.md_5.bungee.api.ChatColor.BOLD+ "Class Shop", 9*4, Methods.getPlugin(), shopMenu);
		shopKeysMenu = new ShopKeysMenu(ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + "Key Shop", 9*4, Methods.getPlugin(), shopMenu);
	
		upgradeMenu = new UpgradeMenu(ChatColor.DARK_RED.toString() +ChatColor.BOLD.toString() + "Upgrade Menu", 9*6, Methods.getPlugin(), shopMenu);
		teamPickMenu = new TeamPickMenu(ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + "ArenaTeam Pick", 9 * 5,Methods.getPlugin());
	}
	
	
}
