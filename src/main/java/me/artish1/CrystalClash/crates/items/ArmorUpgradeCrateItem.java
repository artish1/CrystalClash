package me.artish1.CrystalClash.crates.items;

import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.Util.MySQLUtil;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ArmorUpgradeCrateItem extends CrateItem{
	private ClassType type;
	private Material mat;
	private ArmorType armType;
	public ArmorUpgradeCrateItem(ClassType type,Material mat,ArmorType armType) {
		super(ChatColor.GRAY +ChatColor.BOLD.toString()+ "Armor Upgrade!", new ItemStack(mat));
		addLore("Armor upgrade for ");
		addLore("the " + type.getName() + " class!");
		this.type = type;
		this.armType = armType;
		this.mat = mat;
	}
	
	
	@Override
	public boolean onAward(Player player) {
		Material currentMat = null;
		if(armType == ArmorType.BOOTS)
			currentMat = MySQLUtil.getBoots(player.getUniqueId(), type);
				
		if(armType == ArmorType.CHESTPLATE)
			currentMat = MySQLUtil.getChestplate(player.getUniqueId(), type);
		
		
		if(armType == ArmorType.HELMET)
			currentMat = MySQLUtil.getHelmet(player.getUniqueId(), type);
		
		if(armType == ArmorType.LEGGINGS)
			currentMat = MySQLUtil.getLeggings(player.getUniqueId(), type);
		if(currentMat == null)
			return false;
		
		
		if(!isBetterArmor(currentMat, mat)){
			return false;
		}else{
			if(armType == ArmorType.BOOTS)
			{
				MySQLUtil.setBoots(player.getUniqueId(), type, mat);
			}
			if(armType == ArmorType.CHESTPLATE)
			{
				MySQLUtil.setChestplate(player.getUniqueId(), type, mat);

			}
			if(armType == ArmorType.LEGGINGS)
			{
				MySQLUtil.setLeggings(player.getUniqueId(), type, mat);

			}if(armType == ArmorType.HELMET)
			{
				MySQLUtil.setHelmet(player.getUniqueId(), type, mat);

			}
			return true;
		}
		
		
		
	}
	
	
	private boolean isBetterArmor(Material mat,Material award)
	{
		ArmorTier tier = null;
		for(ArmorTier aTier : ArmorTier.values())
		{
			if(aTier.getBoots() == mat)
			{
				tier = aTier;
				break;
			}
			if(aTier.getLeggings() == mat)
			{
				tier = aTier;
				break;
			}
			if(aTier.getChestplate() == mat)
			{
				tier = aTier;
				break;
			}
			if(aTier.getHelmet() == mat)
			{
				tier = aTier;
				break;
			}
		}
		if(tier == null)
			return false;
		ArmorTier awardTier = null;
		for(ArmorTier aTier : ArmorTier.values())
		{
			if(aTier.getBoots() == award)
			{
				awardTier = aTier;
				break;
			}
			if(aTier.getLeggings() == award)
			{
				awardTier = aTier;
				break;
			}
			if(aTier.getChestplate() == award)
			{
				awardTier = aTier;
				break;
			}
			if(aTier.getHelmet() == award)
			{
				awardTier = aTier;
				break;
			}
		}
		if(awardTier == null)
			return false;
		
		if(awardTier.getPriorityValue() > tier.getPriorityValue())
			return true;
		
		
		
		
		return false;
	}
	
	
	
	public ClassType getType() {
		return type;
	}
	public Material getMat() {
		return mat;
	}
	
	
	public enum ArmorType{
		
		BOOTS,LEGGINGS,CHESTPLATE,HELMET;
		
	}
	
	public enum ArmorTier
	{
		LEATHER(0,Material.LEATHER_HELMET,Material.LEATHER_CHESTPLATE,Material.LEATHER_LEGGINGS,Material.LEATHER_BOOTS),
		GOLD(1,Material.GOLDEN_HELMET,Material.GOLDEN_CHESTPLATE,Material.GOLDEN_LEGGINGS,Material.GOLDEN_BOOTS),
		CHAINMAIL(2,Material.CHAINMAIL_HELMET,Material.CHAINMAIL_CHESTPLATE,Material.CHAINMAIL_LEGGINGS,Material.CHAINMAIL_BOOTS),
		IRON(3,Material.IRON_HELMET,Material.IRON_CHESTPLATE,Material.IRON_LEGGINGS,Material.IRON_BOOTS),
		DIAMOND(4,Material.DIAMOND_HELMET,Material.DIAMOND_CHESTPLATE,Material.DIAMOND_LEGGINGS,Material.DIAMOND_BOOTS);
		
		private Material helmet;
		private Material chestplate;
		private Material leggings;
		private Material boots;
		private int priorityValue;
		
		private ArmorTier(int value,Material helmet, Material chestplate, Material leggings, Material boots) {
			this.helmet = helmet;
			this.priorityValue = value;
			this.chestplate = chestplate;
			this.leggings= leggings;
			this.boots = boots;
		}
		public int getPriorityValue() {
			return priorityValue;
		}
		
		public Material getBoots() {
			return boots;
		}
		public Material getChestplate() {
			return chestplate;
		}
		public Material getHelmet() {
			return helmet;
		}
		public Material getLeggings() {
			return leggings;
		}
		
	}
	
}
