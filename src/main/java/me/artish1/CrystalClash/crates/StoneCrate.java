package me.artish1.CrystalClash.crates;

import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.crates.items.ArmorUpgradeCrateItem;
import me.artish1.CrystalClash.crates.items.ArmorUpgradeCrateItem.ArmorType;
import me.artish1.CrystalClash.crates.items.AwardActivationCrateItem;
import me.artish1.CrystalClash.crates.items.UpgradeAmountCrateItem;
import me.artish1.CrystalClash.crates.items.UpgradeArrowAmountCrateItem;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class StoneCrate extends Crate{

	public StoneCrate() {
		super(ChatColor.GRAY +ChatColor.BOLD.toString() + "Stone Crate", 30,Material.STONE,CrateType.STONE_CRATE);
		
		addItem(new UpgradeArrowAmountCrateItem(10));
		addItem(new UpgradeArrowAmountCrateItem(15));
		addItem(new UpgradeArrowAmountCrateItem(20));
		addItem(new UpgradeArrowAmountCrateItem(25));
		addItem(new UpgradeArrowAmountCrateItem(30));
		
		addItem(new ArmorUpgradeCrateItem(ClassType.SCOUT,Material.IRON_BOOTS,ArmorType.BOOTS));
		addItem(new ArmorUpgradeCrateItem(ClassType.SCOUT,Material.IRON_HELMET,ArmorType.HELMET));
		
		addItem(new ArmorUpgradeCrateItem(ClassType.ARCHER,Material.GOLDEN_BOOTS,ArmorType.BOOTS));
		addItem(new ArmorUpgradeCrateItem(ClassType.ARCHER,Material.GOLDEN_LEGGINGS,ArmorType.LEGGINGS));
		addItem(new ArmorUpgradeCrateItem(ClassType.ARCHER,Material.GOLDEN_CHESTPLATE,ArmorType.CHESTPLATE));
		addItem(new ArmorUpgradeCrateItem(ClassType.ARCHER,Material.GOLDEN_HELMET,ArmorType.HELMET));

		addItem(new ArmorUpgradeCrateItem(ClassType.ARCHER,Material.CHAINMAIL_BOOTS,ArmorType.BOOTS));
		addItem(new ArmorUpgradeCrateItem(ClassType.ARCHER,Material.CHAINMAIL_LEGGINGS,ArmorType.LEGGINGS));
		addItem(new ArmorUpgradeCrateItem(ClassType.ARCHER,Material.CHAINMAIL_CHESTPLATE,ArmorType.CHESTPLATE));
		addItem(new ArmorUpgradeCrateItem(ClassType.ARCHER,Material.CHAINMAIL_HELMET,ArmorType.HELMET));

		
		//addItem(new AwardClassTypeCrateItem(ClassType.GUARDIAN));
		
		UpgradeAmountCrateItem sniperAmmo10 = new UpgradeAmountCrateItem(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + 
				"Sniper ammo upgrade!",
				new ItemStack(Material.SNOWBALL,10),
				ClassType.SNIPER,
				"ammo",
				10,
				"Bullet");
		addItem(sniperAmmo10);
		UpgradeAmountCrateItem sniperAmmo15 = new UpgradeAmountCrateItem(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + 
				"Sniper ammo upgrade!",
				new ItemStack(Material.SNOWBALL,15),
				ClassType.SNIPER,
				"ammo",
				15,
				"Bullet");
		addItem(sniperAmmo15);
		UpgradeAmountCrateItem sniperAmmo20 = new UpgradeAmountCrateItem(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + 
				"Sniper ammo upgrade!",
				new ItemStack(Material.SNOWBALL,20),
				ClassType.SNIPER,
				"ammo",
				20,
				"Bullet");
		addItem(sniperAmmo20);
		
		AwardActivationCrateItem enchEarthHelm = new AwardActivationCrateItem(ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD.toString() +
				"Protection 1 Enchantment",
				new ItemStack(Material.ENCHANTED_BOOK),
				ClassType.EARTH,
				"helmetench",
				"Helmet Enchantment");
		enchEarthHelm.addLore("You have been awarded");
		enchEarthHelm.addLore("the helmet enchantment");
		enchEarthHelm.addLore("of Protection 1 for ");
		enchEarthHelm.addLore("the Earth Class.");
		
		AwardActivationCrateItem enchEarthChest = new AwardActivationCrateItem(ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD.toString() +
				"Protection 1 Enchantment",
				new ItemStack(Material.ENCHANTED_BOOK),
				ClassType.EARTH,
				"chestench",
				"Chestplate Enchantment");
		enchEarthChest.addLore("You have been awarded");
		enchEarthChest.addLore("the chestplate enchantment");
		enchEarthChest.addLore("of Protection 1 for ");
		enchEarthChest.addLore("the Earth Class.");
		
		addItem(enchEarthChest);
		addItem(enchEarthHelm);
		
		AwardActivationCrateItem archerAB = new AwardActivationCrateItem(ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString() + 
				"Set fire to the rain!",
				new ItemStack(Material.FIRE),
				ClassType.ARCHER,
				"firebarrage",
				"Fire Barrage Upgrade");
		archerAB.addLore("Sets the Arrow Barrage's");
		archerAB.addLore("arrows on " + ChatColor.DARK_RED + ChatColor.BOLD.toString() + "Fire");
		archerAB.addLore("for the Archer Class!");
		
		addItem(archerAB);
		
		
		AwardActivationCrateItem enchEnderHelm = new AwardActivationCrateItem(ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD.toString() +
				"Protection 1 Enchantment",
				new ItemStack(Material.ENCHANTED_BOOK),
				ClassType.ENDER,
				"helmetench",
				"Helmet Enchantment");
		enchEnderHelm.addLore("You have been awarded");
		enchEnderHelm.addLore("the helmet enchantment");
		enchEnderHelm.addLore("of Protection 1 for ");
		enchEnderHelm.addLore("the Ender Class.");
		
		AwardActivationCrateItem enchEnderChest = new AwardActivationCrateItem(ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD.toString() +
				"Protection 1 Enchantment",
				new ItemStack(Material.ENCHANTED_BOOK),
				ClassType.ENDER,
				"chestench",
				"Chestplate Enchantment");
		enchEnderChest.addLore("You have been awarded");
		enchEnderChest.addLore("the chestplate enchantment");
		enchEnderChest.addLore("of Protection 1 for ");
		enchEnderChest.addLore("the Ender Class.");
		
		AwardActivationCrateItem enchEnderlegs = new AwardActivationCrateItem(ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD.toString() +
				"Protection 1 Enchantment",
				new ItemStack(Material.ENCHANTED_BOOK),
				ClassType.ENDER,
				"leggingsench",
				"Leggings Enchantment");
		enchEnderlegs.addLore("You have been awarded");
		enchEnderlegs.addLore("the leggings enchantment");
		enchEnderlegs.addLore("of Protection 1 for ");
		enchEnderlegs.addLore("the Ender Class.");
		
		AwardActivationCrateItem encnhEnderboots = new AwardActivationCrateItem(ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD.toString() +
				"Protection 1 Enchantment",
				new ItemStack(Material.ENCHANTED_BOOK),
				ClassType.ENDER,
				"bootsench",
				"Boots Enchantment");
		encnhEnderboots.addLore("You have been awarded");
		encnhEnderboots.addLore("the boots enchantment");
		encnhEnderboots.addLore("of Protection 1 for ");
		encnhEnderboots.addLore("the Ender Class.");
		
		addItem(enchEnderChest);
		addItem(enchEnderHelm);
		addItem(enchEnderlegs);
		addItem(encnhEnderboots);
		
	//	AwardActivationCrateItem 
		
		setKeyPrice(7);
		setKeyMaterial(Material.STONE_HOE);
	}
	

	
}
