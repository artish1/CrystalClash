package me.artish1.CrystalClash.crates;

import me.artish1.CrystalClash.Menu.Menu;
import me.artish1.CrystalClash.Menu.menus.CrateAwardMenu;
import me.artish1.CrystalClash.crates.items.CrateItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Crate {
	protected String name;
	protected Random r = new Random();
	protected int rarity;
	protected List<CrateItem> items = new ArrayList<CrateItem>();
	protected Material borderMaterial;
	private CrateType type;
	private Material keyMaterial;
	private int keyPrice;
	
	public Crate(String name,int rarity,Material borderMaterial,CrateType type) {
		this.name = name;
		this.rarity = rarity;
		this.type = type;
		this.borderMaterial = borderMaterial;
	}
	
	public void setKeyMaterial(Material keyMaterial) {
		this.keyMaterial = keyMaterial;
	}
	public void setKeyPrice(int keyPrice) {
		this.keyPrice = keyPrice;
	}
	
	public Material getKeyMaterial() {
		return keyMaterial;
	}
	public int getKeyPrice() {
		return keyPrice;
	}
	
	public CrateItem getRandomItem()
	{
		int rNum = r.nextInt(items.size());
		return items.get(rNum);
	}
	
	public CrateType getType() {
		return type;
	}
	
	public Material getBorderMaterial() {
		return borderMaterial;
	}
	public void addItem(CrateItem item)
	{
		items.add(item);
	}
	
	public List<CrateItem> getItems() {
		return items;
	}
	
	public void open(Player player,Menu parent)
	{
		boolean flag = true;
		while(flag)
		{
			CrateItem item = getRandomItem();
			if(item.onAward(player))
			{
				CrateAwardMenu menu = new CrateAwardMenu(this,item);
				menu.setParent(parent);
				menu.open(player);
				break;
			}
		}
	}
	
	public int getRarity() {
		return rarity;
	}
	
	public String getName() {
		return name;
	}
	
	
}
