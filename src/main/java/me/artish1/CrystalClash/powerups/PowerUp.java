package me.artish1.CrystalClash.powerups;

import java.util.HashSet;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class PowerUp {
	
	public static HashSet<PowerUp> powerups = new HashSet<PowerUp>();
	
	private String name;
	private ItemStack droppedItem;
	private Item itemEntity;
	
	public PowerUp(String name) {
		this.name = name;
	}
	
	public static HashSet<PowerUp> getPowerups() {
		return powerups;
	}
	
	public String getName() {
		return name;
	}
	
	public ItemStack getItemStack() {
		return droppedItem;
	}
	
	public Item getItemEntity() {
		return itemEntity;
	}
	
	
	public void setItemEntity(Item itemEntity) {
		this.itemEntity = itemEntity;
	}
	
	public void dropItem(Location loc){
		final Location middleLoc = loc.clone();
		middleLoc.add(0.5, 1, 0.5);
		itemEntity = middleLoc.getWorld().dropItem(middleLoc, getItemStack());
		itemEntity.setVelocity(new Vector(0,0,0));
		
		itemEntity.teleport(middleLoc);
				
		
		
	}
	
	public void remove(){
		if(getItemEntity().isValid()){
			getItemEntity().remove();
		}
		PowerUp.getPowerups().remove(this);
		
	}
	
	public void setItemStack(ItemStack droppedItem) {
		this.droppedItem = droppedItem;
		
		
	}
	
	public void activatePowerUp(Player p){ 
		p.sendMessage(ChatColor.GRAY + "You have picked up a Power-Up: " + ChatColor.GREEN + getName());
	}
	
}
