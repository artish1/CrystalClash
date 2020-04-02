package me.artish1.CrystalClash.crates;

public enum CrateType {
	STONE_CRATE("stone_crate"),
	IRON_CRATE("iron_crate"),
	GOLD_CRATE("gold_crate"),
	REDSTONE_CRATE("redstone_crate"),
	DIAMOND_CRATE("diamond_crate"),
	ENDER_CRATE("ender_crate");
	
	
	String mysqlColumnName;
	private CrateType(String column) {
		this.mysqlColumnName = column;
	}
	
	
	public String getMysqlColumnName() {
		return mysqlColumnName;
	}
	
}
