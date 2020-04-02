package me.artish1.CrystalClash.Menu.items;

import me.artish1.CrystalClash.Menu.events.ItemClickedEvent;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ColoredPaneItem extends MenuItem{

	public ColoredPaneItem(DyeColor color) {
		super(ChatColor.RED +"CrystalClash", new ItemStack(Material.GLASS_PANE));

		Material mat = Material.GLASS_PANE;
		switch(color) {
            case RED:
                mat = Material.RED_STAINED_GLASS;
                break;
            case BLUE:
                mat = Material.BLUE_STAINED_GLASS_PANE;
                break;
        }
        setIcon(new ItemStack(mat));
	}
	
	@Override
	public void onItemClicked(ItemClickedEvent e) {
		
	}
	

}
