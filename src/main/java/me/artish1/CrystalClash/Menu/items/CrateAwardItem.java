package me.artish1.CrystalClash.Menu.items;

import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import me.artish1.CrystalClash.crates.items.CrateItem;

public class CrateAwardItem extends MenuItem{
	
	public CrateAwardItem(CrateItem item) {
		super(item.getName(), item.getIcon());
		lore = item.getLore();
	}
	
	
	@Override
	public void onItemClicked(ItemClickedEvent e) {
		if(e.getMenu().hasParent())
		{
			e.setGoBack(true);
		}else{
			e.setClose(true);
		}
	}
	
}
