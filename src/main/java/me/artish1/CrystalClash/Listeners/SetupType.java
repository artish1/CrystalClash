package me.artish1.CrystalClash.Listeners;



public enum SetupType {
	ADD_IMPORTANT_BLOCKS("Adding Important Blocks"), ADD_BOUNCY_EXPLOSIONS_RED("Adding Bouncy Explosion locations for Red team."),
	ADD_BOUNCY_EXPLOSIONS_BLUE("Adding Bouncy Explosions locations for Blue team."), ADD_DEC_BLOCK_POINT1("Adding decorative blocks for Point 1"),
	ADD_DEC_BLOCK_POINT2("Adding decorative blocks for Point 2"),ADD_DEC_BLOCK_POINT3("Adding decorative blocks for Point 3");
	
	private String name;

	private SetupType(String name) {
		this.name = name;
	}
	
	
	public String getName() {
		return name;
	}
	
}
