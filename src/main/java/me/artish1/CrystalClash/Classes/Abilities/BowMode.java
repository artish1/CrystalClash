package me.artish1.CrystalClash.Classes.Abilities;

public enum BowMode {
	NORMAL("Normal",false),POISON("Poison",false),EXPLOSIVE("Explosive",false),LIGHTNING("Lightning",false),VOLLEY("Volley",true);
	
	
	private String name;
	private boolean isDonator;
	
	private BowMode(String name, boolean isDonator) {
		this.name = name;
		this.isDonator = isDonator;
	}
	
	
	public String getName() {
		return name;
	}
	
	public boolean isDonator() {
		return isDonator;
	}
	
	
}
