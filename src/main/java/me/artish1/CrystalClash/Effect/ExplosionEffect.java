package me.artish1.CrystalClash.Effect;

import org.bukkit.Location;

public class ExplosionEffect extends Effect{
    private Location loc;
    public ExplosionEffect(Location loc) {
        super(1, 1);
        this.loc = loc;
    }

    @Override
    public void onRun() {



    }
}
