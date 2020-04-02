package me.artish1.CrystalClash.crates;

import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.crates.items.ArmorUpgradeCrateItem;
import me.artish1.CrystalClash.crates.items.ArmorUpgradeCrateItem.ArmorType;
import me.artish1.CrystalClash.crates.items.AwardActivationCrateItem;
import me.artish1.CrystalClash.crates.items.AwardClassTypeCrateItem;
import me.artish1.CrystalClash.crates.items.C4TippedArrAmountItem;
import me.artish1.CrystalClash.crates.items.UpgradeAmountCrateItem;
import me.artish1.CrystalClash.crates.items.UpgradeArrowAmountCrateItem;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GoldCrate extends Crate{

	public GoldCrate() {
		super(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Gold Crate", 20,
				Material.GOLD_BLOCK, CrateType.GOLD_CRATE);
		
		setKeyMaterial(Material.GOLDEN_HOE);
		setKeyPrice(20); 
		
		addItem(new UpgradeArrowAmountCrateItem(20));
		addItem(new UpgradeArrowAmountCrateItem(30));
		
		addItem(new AwardClassTypeCrateItem(ClassType.GUARDIAN));
		
		addItem(new ArmorUpgradeCrateItem(ClassType.ARCHER,Material.CHAINMAIL_BOOTS,ArmorType.BOOTS));
		addItem(new ArmorUpgradeCrateItem(ClassType.ARCHER,Material.CHAINMAIL_LEGGINGS,ArmorType.LEGGINGS));
		addItem(new ArmorUpgradeCrateItem(ClassType.ARCHER,Material.CHAINMAIL_CHESTPLATE,ArmorType.CHESTPLATE));
		addItem(new ArmorUpgradeCrateItem(ClassType.ARCHER,Material.CHAINMAIL_HELMET,ArmorType.HELMET));
		
		addItem(new ArmorUpgradeCrateItem(ClassType.SCOUT,Material.IRON_BOOTS,ArmorType.BOOTS));
		addItem(new ArmorUpgradeCrateItem(ClassType.SCOUT,Material.IRON_HELMET,ArmorType.HELMET));
		
		
		AwardActivationCrateItem explC4bow = new AwardActivationCrateItem(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() +
				"C4-Tipped Arrows & Bow!",
				new ItemStack(Material.BOW),
				ClassType.EXPLOSIVES,
				"c4bow",
				"C4-Tipped Arrows & Detonator Bow");
		explC4bow.addLore("Shoot C4-Tipped arrows"); 
		explC4bow.addLore("with your high frequency");
		explC4bow.addLore("bow, and left click your");
		explC4bow.addLore("bow to detonate them!");
		explC4bow.addLore("This is for the Bomber Class!");
		addItem(explC4bow);
		
		UpgradeAmountCrateItem grenades2 = new UpgradeAmountCrateItem(ChatColor.RED.toString() + ChatColor.BOLD +
				"Grenade Amount Upgrade!", new ItemStack(Material.EGG,2),
				ClassType.EXPLOSIVES, "grenades", 2 ,"Grenade");
		
		UpgradeAmountCrateItem grenades5 = new UpgradeAmountCrateItem(ChatColor.RED.toString() + ChatColor.BOLD +
				"Grenade Amount Upgrade!", new ItemStack(Material.EGG,5),
				ClassType.EXPLOSIVES, "grenades", 5 ,"Grenade");
		UpgradeAmountCrateItem grenades7 = new UpgradeAmountCrateItem(ChatColor.RED.toString() + ChatColor.BOLD +
				"Grenade Amount Upgrade!", new ItemStack(Material.EGG,7),
				ClassType.EXPLOSIVES, "grenades", 7 ,"Grenade");
		
		UpgradeAmountCrateItem c4amount5 = new UpgradeAmountCrateItem(ChatColor.RED.toString() + ChatColor.BOLD +
				"C4 Amount Upgrade!", new ItemStack(Material.SPONGE,5),
				ClassType.EXPLOSIVES, "c4amount", 5 ,"C4");
		
		UpgradeAmountCrateItem c4amount7 = new UpgradeAmountCrateItem(ChatColor.RED.toString() + ChatColor.BOLD +
				"C4 Amount Upgrade!", new ItemStack(Material.SPONGE,7),
				ClassType.EXPLOSIVES, "c4amount", 7 ,"C4");
		
		UpgradeAmountCrateItem c4amount10 = new UpgradeAmountCrateItem(ChatColor.RED.toString() + ChatColor.BOLD +
				"C4 Amount Upgrade!", new ItemStack(Material.SPONGE,10),
				ClassType.EXPLOSIVES, "c4amount", 10 ,"C4");
		
		UpgradeAmountCrateItem mineAmount3 = new UpgradeAmountCrateItem(ChatColor.RED.toString() + ChatColor.BOLD +
				"Landmine Amount Upgrade!", new ItemStack(Material.STONE_PRESSURE_PLATE,3),
				ClassType.EXPLOSIVES, "mineamount", 3 ,"Landmine");
		
		UpgradeAmountCrateItem mineAmount7 = new UpgradeAmountCrateItem(ChatColor.RED.toString() + ChatColor.BOLD +
				"Landmine Amount Upgrade!", new ItemStack(Material.STONE_PRESSURE_PLATE,7),
				ClassType.EXPLOSIVES, "mineamount", 2 ,"Grenade");
		
		UpgradeAmountCrateItem mineAmount10 = new UpgradeAmountCrateItem(ChatColor.RED.toString() + ChatColor.BOLD +
				"Landmine Amount Upgrade!", new ItemStack(Material.STONE_PRESSURE_PLATE,10),
				ClassType.EXPLOSIVES, "mineamount", 10 ,"Landmine");
		
		
		addItem(grenades7);
		addItem(grenades5);
		addItem(grenades2);
		addItem(c4amount10);
		addItem(c4amount7);
		addItem(c4amount5);
		addItem(mineAmount10);
		addItem(mineAmount7);
		addItem(mineAmount3);
		 
		
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
		
		addItem(new C4TippedArrAmountItem(8));
		addItem(new C4TippedArrAmountItem(12));
		addItem(new C4TippedArrAmountItem(15));
		
		
		
		
	}

}
