package me.artish1.CrystalClash.Util;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import me.artish1.CrystalClash.Arena.*;
import me.artish1.CrystalClash.Classes.ClassType;
import me.artish1.CrystalClash.CrystalClash;
import me.artish1.CrystalClash.Effect.EntityFireEffect;
import me.artish1.CrystalClash.Listeners.Classes.ArcherListener;
import me.artish1.CrystalClash.Listeners.GameListener;
import me.artish1.CrystalClash.entities.*;
import me.artish1.CrystalClash.leaderboards.Leaderboard;
import me.artish1.CrystalClash.other.ArrowStraightener;
import me.artish1.CrystalClash.other.ClassInventories;
import me.artish1.CrystalClash.other.Rocket;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.*;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.util.Vector;

import java.io.*;
import java.util.*;



public class Methods {
	private static CrystalClash plugin;
	
	private static HashSet<UUID> sheepGuns = new HashSet<UUID>();
	private static HashSet<UUID> merchants = new HashSet<UUID>();
	private static HashSet<UUID> choppergunnners = new HashSet<UUID>();
	private static Random r = new Random();
	private static HashSet<Location> importantBlocks = new HashSet<Location>();
	private static HashSet<UUID> redSummons = new HashSet<UUID>();
	private static HashSet<UUID> blueSummons = new HashSet<UUID>();
	
	public static ModelSheep bluePick;
	public static ModelSheep redPick;
	
	public static Random getRandom(){
		return r;
	}
	
	public static String getMapName()
	{
		return getPlugin().getConfig().getString("MapName");
	}
	
	public static List<Location> helix(Location loc) {
        List<Location> helixblocks = new ArrayList<Location>();
        int radius = 5;
        for(double y = 0; y <= 50; y+=0.05) {
            double x = radius * Math.cos(y);
            double z = radius * Math.sin(y);
            Location l = new Location(loc.getWorld(), (float) loc.getX() + x, (float) loc.getY() + y, (float) loc.getZ() + z);
            helixblocks.add(l);
        }
        return helixblocks;
    }
	
	public static void setLobbyInventory(Player player)
	{
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.getInventory().addItem(getClassMenuItem());
		player.getInventory().addItem(getShopMenuItem());
		player.getInventory().addItem(getTeamPickMenuItem());
		player.getInventory().addItem(getOpenCratesMenuItem());
		player.getInventory().addItem(getHelpBook());
		player.updateInventory();
	}
	
	public static ItemStack getOpenCratesMenuItem()
	{
		ItemStack item = new ItemStack(Material.CHEST);
		ClassInventories.setDisplayName(item, ChatColor.BLUE.toString() + ChatColor.BOLD + "Crates"); 
		ClassInventories.setLore(item, ClassInventories.createLore("Click to open", "the Crates Menu", "to unlock your", "crates!"));
		return item;
	}
	
	public static ItemStack getPlayerHead(Player player){
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta = (SkullMeta)skull.getItemMeta();
		meta.setOwningPlayer(player);
		skull.setItemMeta(meta);
		return skull;
	}
	
	
	public static boolean isDebugMessages()
	{
		return plugin.getConfig().getBoolean("DebugMessages");
	}
	
	private static List<Entity> models = new ArrayList<Entity>();
	
	public static String getCoordinates(Location loc){
		return "X: " + loc.getBlockX() + ", Y: " + loc.getBlockY() + ", Z: " + loc.getBlockZ();
 	}
	
	public static HashSet<UUID> getChoppergunnners() {
		return choppergunnners;
	}
	
	
	public static HashSet<UUID> getRedSummons() {
		return redSummons;
	}
	
	public static HashSet<UUID> getBlueSummons() {
		return blueSummons;
	}
	
	public static Location getRandomLocation(List<Location> list){
		int rIndex = r.nextInt(list.size());
		return list.get(rIndex);
	}
	
	
	
	
	public static void updateLobbyScoreboardPoints(Player p){
		Objective obj = p.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
		
		if(obj == null)
			return;
		
		if(!ChatColor.stripColor(obj.getName()).equalsIgnoreCase(p.getName()))
			return;
		
		for(String s : p.getScoreboard().getEntries()){
			if(ChatColor.stripColor(s).startsWith("Points:")){
				int placeholder = obj.getScore(s).getScore();
				p.getScoreboard().resetScores(s);
				obj.getScore(ChatColor.GRAY + "Points: " + ChatColor.GREEN + MySQLUtil.getPoints(p.getUniqueId())).setScore(placeholder);
				break;
			}
		}
		
	}
	
	
	public static void createLobbyScoreboard(Player player){
		//String playerCounterSB = ChatColor.GRAY + "Players: " + ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + Bukkit.getOnlinePlayers().size() 
	//			+ "/" + getArena().getAutoStartPlayers() + ChatColor.DARK_GRAY + "]";
		SimpleScoreboard board = new SimpleScoreboard(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + player.getName()); 
		for(Leaderboard b: Leaderboard.values()){
			board.add(ChatColor.GRAY + b.getShortTitle() + ": " + ChatColor.GREEN + MySQLUtil.getScore(player.getUniqueId(), b)); 
		}
		board.blankLine(); 

		board.build();
		board.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(ChatColor.GRAY + "Points: " + ChatColor.GREEN + MySQLUtil.getPoints(player.getUniqueId())).setScore(0);
		//board.add(ChatColor.GRAY + "Points: " + ChatColor.GREEN + MySQLUtil.getPoints(player.getUniqueId()));  
	//	board.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(playerCounterSB).setScore(0);
		//stuff.put(player.getUniqueId(), playerCounterSB);
 		board.send(player);
	}

	public static void sendTitle(Player player, String text, ChatColor color, PacketPlayOutTitle.EnumTitleAction action)
	{
		IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + text + "\",color:" + color.name().toLowerCase() + "}");

		PacketPlayOutTitle title = new PacketPlayOutTitle(action, chatTitle);
		PacketPlayOutTitle length = new PacketPlayOutTitle(10, 30, 8);


		((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
	}



	public static void sendTitle(Player player,String text,int fadeIn,int stay, int fadeOut,ChatColor color)
	{
		IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + text + "\",color:" + color.name().toLowerCase() + "}");

		PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
		PacketPlayOutTitle length = new PacketPlayOutTitle(fadeIn, stay, fadeOut);


		((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
	}

	public static void sendSubtitle(Player player,String text,int fadeIn,int stay, int fadeOut,ChatColor color)
	{
		IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + text + "\",color:" + color.name().toLowerCase() + "}");

		PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatTitle);
		PacketPlayOutTitle length = new PacketPlayOutTitle(fadeIn, stay, fadeOut);


		((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
	}


	public static void setupImportantBlocks(){
		List<Location> locs = Methods.getMultiLocation("ImportantBlocks");

		importantBlocks.addAll(locs);
		

		importantBlocks.add(Arena.redTeam.getCrystal());
		importantBlocks.add(Arena.blueTeam.getCrystal());

		for(ContestPoint point : ContestPoint.values()){
			importantBlocks.add(point.getPointBlock());
			


			importantBlocks.addAll(point.getDecBlocks());
		}
		
	}
	
	public static HashSet<Location> getImportantBlocks() {
		return importantBlocks;
	} 
	

	public static ItemStack createItem(Material mat, String displayName, List<String> lore){
		ItemStack item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack createItem(Material mat,int amount, String displayName, List<String> lore){
		ItemStack item = new ItemStack(mat,amount);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	@Deprecated
	public static void displayParticleEffect(Particle particle, boolean infRange,
                                             Location loc, float offSetX, float offSetY, float offSetZ, float speed, int amount){
	    loc.getWorld().spawnParticle(particle, loc, amount);

	}

	@Deprecated
	public static void displayParticleEffectArenaPlayer(List<ArenaPlayer> players,Particle particle, boolean infRange,
			Location loc,float offSetX,float offSetY, float offSetZ,float speed, int amount){
		

		for(ArenaPlayer p : players)
		    p.getPlayer().spawnParticle(particle, loc, amount);
		
	}

	@Deprecated
	public static void displayParticleEffectPlayers(List<Player> players,Particle particle, boolean infRange,
			Location loc,float offSetX,float offSetY, float offSetZ,float speed, int amount){
		

		for(Player p : players)
		    p.getPlayer().spawnParticle(particle, loc, amount);
		
	}
	

	@Deprecated
	public static void displayParticleEffect(Player p,Particle particle, boolean infRange,
			Location loc,float offSetX,float offSetY, float offSetZ,float speed, int amount){
		p.spawnParticle(particle, loc, amount);
	}
	
	public static void debug(Player p, String message){
		if(ArcherListener.debuggers.contains(p.getUniqueId())){
			p.sendMessage(ChatColor.GRAY + "Debugger: " + message);
		}
	}
	


    public static void sendActionBar(Player p, String msg){
            IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \""+msg+"\"}");
            
            PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, ChatMessageType.CHAT);
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppoc);
    }
   
    public static List<Location> getChopperGunnerLocs(){
    	return getMultiLocation("ChopperGunnerPath");
    }
    
    
    public static void addChopperGunnerLoc(Location loc){
    	addMultiLocation("ChopperGunnerPath", loc); 
    }
    
    public static void sendHeaderAndFooter(Player p, String head, String foot){
            CraftPlayer craftplayer = (CraftPlayer)p;
            PlayerConnection connection = craftplayer.getHandle().playerConnection;
          //  IChatBaseComponent header = ChatSerializer.a("{'color': '" + "', 'text': '" + head + "'}");
           // IChatBaseComponent footer = ChatSerializer.a("{'color': '" + "', 'text': '" + foot + "'}");
            ByteBuf byteBuffer = ByteBufAllocator.DEFAULT.buffer(head.getBytes().length+foot.getBytes().length);

            PacketDataSerializer packetDataSerializer = new PacketDataSerializer(byteBuffer);

            PacketPlayOutPlayerListHeaderFooter packetPlayOutPlayerListHeaderFooter = new PacketPlayOutPlayerListHeaderFooter();

            try {
                packetDataSerializer.a(IChatBaseComponent.ChatSerializer.a(new JsonMessageText(head).getJsonObject().toJSONString()));
                packetDataSerializer.a(IChatBaseComponent.ChatSerializer.a(new JsonMessageText(foot).getJsonObject().toJSONString()));
                packetPlayOutPlayerListHeaderFooter.a(packetDataSerializer);

                connection.sendPacket(packetPlayOutPlayerListHeaderFooter);

            } catch (IOException e) {
                e.printStackTrace();
            }
    }
	public static List<Block> loopThrough(Location loc1, Location loc2) {
		List<Block> blocks = new ArrayList<Block>();
		World w = loc1.getWorld();
	   
		 int minx = Math.min(loc1.getBlockX(), loc2.getBlockX()),
	    miny = Math.min(loc1.getBlockY(), loc2.getBlockY()),
	    minz = Math.min(loc1.getBlockZ(), loc2.getBlockZ()),
	    maxx = Math.max(loc1.getBlockX(), loc2.getBlockX()),
	    maxy = Math.max(loc1.getBlockY(), loc2.getBlockY()),
	    maxz = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
	    for(int x = minx; x<=maxx;x++){
	        for(int y = miny; y<=maxy;y++){
	            for(int z = minz; z<=maxz;z++){
	                Block b = w.getBlockAt(x, y, z);
	                blocks.add(b);
	            }
	        }
	    }
	    return blocks;
	}
	
	public static void startRemoveDelay(final List<Location> locs, int delay){
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			public void run() {
				for(Location loc : locs){
					loc.getBlock().setType(Material.AIR);
				}
			}
			
		}, delay);
	}
	
	public static void removePotionEffects(Player p){
		for(PotionEffect type : p.getActivePotionEffects()){
			p.removePotionEffect(type.getType());
		}
	}
	
	
	public static HashSet<UUID> getMerchants() {
		return merchants;
	}

	
	public static boolean isMerchant(Entity e){
		return merchants.contains(e.getUniqueId());
	}

	@Deprecated
	public static void setBlockFast(Location loc,Material material,byte data){
		//handler.setBlockFast(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), material.getId(), data);
		loc.getBlock().setType(material);
	}

	@Deprecated
	public static void setBlockFast(Location loc,Material material){
		//handler.setBlockFast(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), material.getId(), (byte) 0);
		loc.getBlock().setType(material);
		
	}
	
	public static List<Entity> getModels() {
		return models;
	}
	
	public static ModelSheep spawnModelSheep(Location loc,ArenaTeam type){
		ModelSheep sheep = (ModelSheep) CustomEntityType.MODEL_SHEEP.spawn(loc);
		if(type == Arena.blueTeam){
			sheep.setColor(EnumColor.BLUE); 
//			sheep.setCustomName(ChatColor.BLUE + "Join Blue: 0");
			sheep.setCustomNameVisible(true); 
			addModel(sheep.getBukkitEntity(), "ChooseBlueSheep");
			bluePick = sheep;
		}else{
			if(type == Arena.redTeam){
				sheep.setColor(EnumColor.RED); 
//				sheep.setCustomName(ChatColor.RED + "Join Red: 0");
				sheep.setCustomNameVisible(true); 
				redPick = sheep;
				addModel(sheep.getBukkitEntity(), "ChooseRedSheep");  
			}
		}
		
		return sheep;
	}
	
	public static Arena getArena(){
		return plugin.getArena();
	}
	
	public static void createCustomHelix(Player player, Particle particle){
		int strands = 3;
		int particles = 80;
		int radius = 2;
		float curve = 10;
		double rotation = Math.PI /4;
		Location location = player.getLocation();
        for (int i = 1; i <= strands; i++) {
            for (int j = 1; j <= particles; j++) {
                float ratio = (float) j / particles;
                double angle = curve * ratio * 2 * Math.PI / strands + (2 * Math.PI * i / strands) + rotation;
                double x = Math.cos(angle) * ratio * radius;
                double z = Math.sin(angle) * ratio * radius;
                location.add(x, 0, z);
                displayParticleEffect(particle, true, location, 0, 0, 0, 0, 1);
                location.subtract(x, 0, z);
            }
        }
	}
	
	public static void createHelix(Player player, Particle particle) {
	//	List<Location> list = new ArrayList<Location>();
	    Location loc = player.getLocation();
	    int radius = 2;
	    for(double y = 0; y <= 6; y+=0.05) {
	        double x = radius * Math.cos(y);
	        double z = radius * Math.sin(y);
	        Location newLoc = loc.clone();
	        newLoc.setX(loc.getX() + x);
	        newLoc.setY(loc.getY() + y);
	        newLoc.setZ(loc.getZ() + z);
	       // list.add(newLoc);
	       // double x1 = radius * Math.sin(y);
	       // double z1 = radius * Math.cos(y);
	        
	        Location newLoc1 = loc.clone();
	        newLoc1.setX(loc.getX() - x);
	        newLoc1.setY(loc.getY() + y);
	        newLoc1.setZ(loc.getZ() - z);
	        
		       displayParticleEffect(particle, true, newLoc1, 0, 0, 0, 0, 1);

	       displayParticleEffect(particle, true, newLoc, 0, 0, 0, 0, 1);
	    }
	    
	   // new LoopLocationsEffect(1,list,particle).start();
	    
	}
	
	private static void clearEntities(World w)
	{
		for(Entity e : w.getEntities())
		{
			e.remove();
		}
	}
	
	public static void spawnModels(){
		
		clearEntities(getArena().getLobbySpawn().getWorld());
		
		for(ClassType ct : ClassType.values()){
			Location loc = getLocation(ChatColor.stripColor(ct.getName()));
			if(loc == null)
				continue;
			 
			
			Entity e;
			if(ct.useSkeletonModel()){
				
				e = Methods.spawnModelSkeleton(loc, ct.getName(), ct.getArmor(), ct.getModelItem()).getBukkitEntity();
				if(CrystalClash.useHolograms){
					Hologram hg = HologramsAPI.createHologram(plugin, loc.clone().add(0, 2.8, 0)); 
					hg.appendTextLine(ct.getName());
				}
			}else{
				e = Methods.spawnModelZombie(loc, ct.getName(), ct.getArmor(), ct.getModelItem()).getBukkitEntity();
				if(CrystalClash.useHolograms){
					Hologram hg = HologramsAPI.createHologram(plugin, loc.clone().add(0, 2.8, 0)); 
					hg.appendTextLine(ct.getName());
				}
			}
			models.add(e);
			ct.setModelUUID(e.getUniqueId()); 
			plugin.getLogger().info("Spawned ClassModel: " + ct.getName()); 
		
		}
		
		
		
	}
	
	
	
	public static Location getLocation(String path){
		if(!plugin.arenas.contains(path + ".World")){
			
			return null;
		}
		
		Location loc = new Location(
				Bukkit.getWorld(plugin.arenas.getString(path + ".World")),
				plugin.arenas.getDouble(path + ".X"),
				plugin.arenas.getDouble(path + ".Y"),
				plugin.arenas.getDouble(path + ".Z")
				);
		
		loc.setPitch((float) plugin.arenas.getDouble(path + ".Pitch"));
		loc.setYaw((float) plugin.arenas.getDouble(path + ".Yaw"));
		
		return loc;
	}
	
	public static int getRandomRange(int range){
		int num = r.nextInt(range + range) - range;
		
		
		return num;
	}
	
	
	public static void addLocation(String path, Location loc){
		plugin.arenas.set(path + ".X", loc.getX());
		plugin.arenas.set(path + ".Y", loc.getY());
		plugin.arenas.set(path + ".Z", loc.getZ());
		plugin.arenas.set(path + ".World", loc.getWorld().getName());
		plugin.arenas.set(path + ".Pitch", loc.getPitch());
		plugin.arenas.set(path + ".Yaw", loc.getYaw());
		saveYamls();

	}
	
	public static void removeLocation(String path){
		plugin.arenas.set(path + ".X", null);
		plugin.arenas.set(path + ".Y", null);
		plugin.arenas.set(path + ".Z", null);
		plugin.arenas.set(path + ".World", null);
		plugin.arenas.set(path + ".Pitch", null);
		plugin.arenas.set(path + ".Yaw", null);
		plugin.arenas.set(path, null); 
		saveYamls();

	}
	
	
	public static void addModel(Entity e, String name){
		addLocation(name, e.getLocation());
		models.add(e);
	}
	
	public static void clearModels(){
		for(Entity e : models){
			e.remove();
		}
		
		models.clear();
	}
	
	
	
	
	
	public static ItemStack getWinnerItem(){
		ItemStack winnerItem = new ItemStack(Material.DIAMOND, 64 * 40);
		ItemMeta meta = winnerItem.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "You Win!");
		winnerItem.setItemMeta(meta);
		
		return winnerItem;
		
	}
	
	
	
	
	
	 public static Vector calculateVelocity(Vector from, Vector to, int heightGain)
	    {
	        // Gravity of a potion
	        double gravity = 0.115;
	 
	        // Block locations
	        int endGain = to.getBlockY() - from.getBlockY();
	        double horizDist = Math.sqrt(distanceSquared(from, to));
	 
	        // Height gain
	        int gain = heightGain;
	 
	        double maxGain = gain > (endGain + gain) ? gain : (endGain + gain);
	 
	        // Solve quadratic equation for velocity
	        double a = -horizDist * horizDist / (4 * maxGain);
	        double b = horizDist;
	        double c = -endGain;
	 
	        double slope = -b / (2 * a) - Math.sqrt(b * b - 4 * a * c) / (2 * a);
	 
	        // Vertical velocity
	        double vy = Math.sqrt(maxGain * gravity);
	 
	        // Horizontal velocity
	        double vh = vy / slope;
	 
	        // Calculate horizontal direction
	        int dx = to.getBlockX() - from.getBlockX();
	        int dz = to.getBlockZ() - from.getBlockZ();
	        double mag = Math.sqrt(dx * dx + dz * dz);
	        double dirx = dx / mag;
	        double dirz = dz / mag;
	 
	        // Horizontal velocity components
	        double vx = vh * dirx;
	        double vz = vh * dirz;
	 
	        return new Vector(vx, vy, vz);
	    }
	 
	    private static double distanceSquared(Vector from, Vector to)
	    {
	        double dx = to.getBlockX() - from.getBlockX();
	        double dz = to.getBlockZ() - from.getBlockZ();
	 
	        return dx * dx + dz * dz;
	    }
	public static ItemStack getLoserItem(){
		ItemStack winnerItem = new ItemStack(Material.COAL, 64 * 40);
		ItemMeta meta = winnerItem.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "You Lose!");
		winnerItem.setItemMeta(meta);
		
		return winnerItem;
		
	}
	
	
	private static String SHOP_MENU_DISPLAY_NAME = ChatColor.YELLOW + "Shop Menu";
	private static String CLASS_MENU_DISPLAY_NAME = ChatColor.GREEN + "Class Menu";
	
	public static final BlockFace[] axis = { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };
    public static final BlockFace[] radial = { BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST };
   
    
    public static ItemStack getModelRemoveItem(){
    	ItemStack item = new ItemStack(Material.BLAZE_ROD);
    	ItemMeta meta = item.getItemMeta();
    	meta.setDisplayName(ChatColor.YELLOW + "Remove Model Item");
    	meta.setLore(ClassInventories.createLore("Click on a model to remove"));
    	item.setItemMeta(meta);
    	
    	return item;
    	
    }
    
    
  
    public static void createExplosion(Location loc, int radius, double damage){
    	List<Player> effectedPlayers = getNearbyEntities(loc, radius);
    	loc.getWorld().createExplosion(loc, 0f, true);
    	displayParticleEffect(Particle.EXPLOSION_LARGE, false, loc, 0, 0, 0, 1, 1);
    	for(Player p : effectedPlayers){
    		p.damage(damage);
    	}
    	
    	
    	
    	
    }
    
   
    
    public static void createBouncyExplosion(Location loc){
    	Methods.createExplosion(loc, 5, 5.0); 
    	List<Block> blocks = getNearbyCircleBlocks(loc, 3, 1, false, true, 0);
    	 
    	for(Block b : blocks){
    		if(getImportantBlocks().contains(b.getLocation()))
    			continue;
    		
    		bounceBlock(b);
    	}
    	
    }
    
    public static HashSet<UUID> cancelFallBlocks = new HashSet<UUID>();

	public static void bounceBlock(Block b)
    {
        if(b == null) return;
       
        FallingBlock fb = b.getWorld()
                .spawnFallingBlock(b.getLocation(), b.getType(), b.getData());
       fb.setDropItem(false);
       
        b.setType(Material.AIR);
       
        float x = (float) -1 + (float) (Math.random() * ((1 - -1) + 1));
        double randomValue = 1 + (2 - 1) * r.nextDouble();

        float y = (float) randomValue;//(float) -5 + (float)(Math.random() * ((5 - -5) + 1));
        float z = (float) -0.3 + (float)(Math.random() * ((0.3 - -0.3) + 1));
       
        fb.setVelocity(new Vector(x, y, z));
        cancelFallBlocks.add(fb.getUniqueId());
    }
    
	public static void createTeamExplosion(Player player, Location loc, int radius){
    	if(!ArenaManager.isInArena(player))
    		return;
    	
    	Arena arena = ArenaManager.getArena(player);
    	
    	List<Player> effectedPlayers = getNearbyEntities(loc, radius);
    	loc.getWorld().createExplosion(loc, 0f, true);
    	displayParticleEffect(Particle.EXPLOSION_LARGE, false, loc, 0, 0, 0, 1, 1);
    	for(Player p : effectedPlayers){
    		if(arena.isOnSameTeam(player, p))
    			continue;
    		
    		p.damage(4.0, player);
    		
    	}
    	
    	
    	
    	
    }
    
	public static void createTeamFireworkExplosion(Player player, Location loc, int radius, double damage){
    	if(!ArenaManager.isInArena(player))
    		return;
    	
    	Arena arena = ArenaManager.getArena(player);
    	loc.getWorld().playSound(loc, Sound.ENTITY_BLAZE_SHOOT, 2.0f, 2.0f);
    	List<Player> effectedPlayers = getNearbyEntities(loc, radius);
    	Color color = Color.YELLOW;

    	if(getArena().getArenaPlayer(player) != null)
    	{
    		if(getArena().getArenaPlayer(player).isBlue())
    		{
    			color = Color.BLUE;
    		}else{
    			if(getArena().getArenaPlayer(player).isRed())
    			{
    				color = Color.RED;
    			}
    		}
    	}

    	playFirework(loc, Type.BALL_LARGE, color);
    	for(Player p : effectedPlayers){
    		if(arena.isOnSameTeam(player, p))
    			continue;
    		
    		
    		
    		
    		p.damage(damage, player);
    		
    	}	
    }
    
	
	public static void createTeamExplosion(Player player, Location loc, int radius, double damage){
    	if(!ArenaManager.isInArena(player))
    		return;
    	
    	Arena arena = ArenaManager.getArena(player);
    	
    	List<Player> effectedPlayers = getNearbyEntities(loc, radius);
    	loc.getWorld().createExplosion(loc, 0f, true);
    	displayParticleEffect(Particle.EXPLOSION_NORMAL, false, loc, 0, 0, 0, 1, 1);


    	for(Player p : effectedPlayers){
    		if(arena.isOnSameTeam(player, p))
    			continue;
    		
    		p.damage(damage, player);
    		
    	}	
    }




    
    public static List<Player> getNearbyEntities(Location loc, int radius){
    	List<Player> ents = new ArrayList<Player>();
    	
    	for(Entity e: loc.getWorld().getEntities()){
    		if(!(e instanceof Player))
    			continue;
    			
    		if(e.getLocation().distance(loc) <= radius){
    			ents.add((Player) e);
    		}
    		
    	}
    	
    	
    	return ents;
    }
    
    public static void addGunSheep(UUID id){
    	sheepGuns.add(id);
    }
    
    public static void removeGunSheep(UUID id){
    	sheepGuns.remove(id);
    }
    
    public static boolean isGunSheep(Sheep sheep){
        return sheepGuns.contains(sheep.getUniqueId());
    }
    
    public static String getActualLine(){
    	return  "                            ";
    }
    
    public static String newPageLine(){
    	return "\n";
    	
    }
    
    public static String newPageLine(int lines){
    	String emptyLines = "";
    	
    	for(int x = 0; x < lines; x++){
    		  emptyLines += "\n";

    	}
    	
    	return emptyLines;
    }
    
    
    
    
    public static void fireRocket(Player p){
    	Snowball arrow = p.launchProjectile(Snowball.class);
		arrow.setVelocity(p.getLocation().getDirection().multiply(1.5f));
		arrow.setShooter(p);
		ArrowStraightener straight = new ArrowStraightener(arrow, arrow.getVelocity());
		straight.start();
		removeAfterSeconds(arrow, 6);
		GameListener.rockets.put(arrow.getUniqueId(), p.getUniqueId());
		new EntityFireEffect(arrow).start();
		new Rocket(arrow,straight,p).start();
    }
    
    public static List<Location> getLocsBetween(){
    	List<Location> locs = new ArrayList<Location>();
    	
    	//TODO loc.add(x,y,z); helps!
    	return locs;
    }
    
    
    
    
    public static Entity sendEntityToLocation(Entity e, Location loc){
    	e.setVelocity(getDirection(e.getLocation(), loc));
    	return e;
    }
    
    public static Vector getDirection(Location second_location, Location first_location){
		Vector from = new Vector(first_location.getX(), first_location.getY(), first_location.getZ());
		Vector to = new Vector(second_location.getX(), second_location.getY(), second_location.getZ());
		 
		Vector vector = from.subtract(to);
		vector.normalize();
		vector.multiply(1.0);
		return vector;
	}
    
    
    public static void removeAfterSeconds(final Entity e, int seconds){
    	Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable(){

			public void run() {
				if(e.isValid())
				e.remove();
			}
    		
    	}, 20 * seconds);
    }
    
    
    public static String addSpaces(int amount){
    	String spaces = "";
    	
    	for(int x = 0; x < amount; x++){
    		spaces += " ";
    	}
    	
    	return spaces;
    }
    
    
    
    
    
    public static List<Location> getMultiLocation(String path){
		 List<Location> locs = new ArrayList<Location>();
		  for(int i = 1;i <= Methods.getPlugin().arenas.getInt( path + ".Counter"); i++ ){			  
			 Location loc = new Location(
					 Bukkit.getWorld(Methods.getPlugin().arenas.getString( path + "." + i + ".World")),
					 Methods.getPlugin().arenas.getDouble( path + "."  + i + ".X"),
					 Methods.getPlugin().arenas.getDouble( path + "."  + i + ".Y"),
					 Methods.getPlugin().arenas.getDouble( path + "." + i + ".Z"));
			 loc.setPitch((float) Methods.getPlugin().arenas.getInt( path + "."  + i + ".Pitch"));
			 loc.setYaw((float) Methods.getPlugin().arenas.getDouble( path + "." + i + ".Yaw"));
			 if(loc != null){
			 locs.add(loc);
			 }
		  }
		  
		  
		  return locs;
		
	}
	
	public static void addMultiLocation(String path, Location loc){
		if (!Methods.getPlugin().arenas.contains( path +".Counter"))
	    {
	      int counter = 1;
		  Methods.getPlugin().arenas.addDefault( path + "." + counter +  ".X", loc.getX());
	      Methods.getPlugin().arenas.addDefault( path + "." +counter + ".Y", loc.getY());
	      Methods.getPlugin().arenas.addDefault( path + "."  +counter+ ".Z", loc.getZ());
	      Methods.getPlugin().arenas.addDefault( path + "." +counter+ ".World", loc.getWorld().getName());
	      Methods.getPlugin().arenas.addDefault( path + "."  +counter+ ".Pitch", loc.getPitch());
	      Methods.getPlugin().arenas.addDefault( path + "."  +counter+ ".Yaw", loc.getYaw());
	      Methods.getPlugin().arenas.addDefault( path + ".Counter", counter);

	    }
	    else
	    {
	    	int counter = Methods.getPlugin().arenas.getInt( path + ".Counter");
	    	counter++;
	    	Methods.getPlugin().arenas.set( path + "."  +counter+ ".X", loc.getX());
	      Methods.getPlugin().arenas.set( path + "."  +counter+ ".Y", loc.getY());
	      Methods.getPlugin().arenas.set( path + "."  +counter+ ".Z", loc.getZ());
	      Methods.getPlugin().arenas.set( path + "."  +counter+ ".World", loc.getWorld().getName());
	      Methods.getPlugin().arenas.set( path + "."  +counter+ ".Pitch", loc.getPitch());
	      Methods.getPlugin().arenas.set( path + "."  +counter+ ".Yaw", loc.getYaw());
	      Methods.getPlugin().arenas.set( path + ".Counter" , counter);

	    }
	    Methods.saveYamls();
	}
    
    
    
    
    public static FireworkEffect getRandomFireworkEffect(){
    	return FireworkEffect.builder().with(Type.BALL).withColor(getRandomColor()).build();
    }
    
    public static ItemStack getHelpBook(){
    	ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
    	BookMeta meta = (BookMeta) book.getItemMeta();
    	meta.setDisplayName(ChatColor.GREEN.toString() +"How to Play");
    	meta.setTitle("CrystalClash Help.");
    	meta.setAuthor("ReefMC");
    	
    	meta.addPage(newPageLine(2) + addSpaces(12) + ChatColor.BLACK.toString() + "---" + ChatColor.GREEN +ChatColor.BOLD.toString() + "CrystalClash" + ChatColor.BLACK +"---" +
    	addSpaces(6) + addSpaces(5) + "-x+x-" + addSpaces(1)+
    	newPageLine(3) + getActualLine() + addSpaces(8) + "How to Play");//14 total
    	
    	
    	meta.addPage("There are two ways to win." + newPageLine(2) + "1: Destroy your opponent's crystal, which has 50 HP." 
    			+ newPageLine(2) + "2: Capture the wool Control Points to get 2 points every 10 seconds. " +getArena().getPointsGoal() +" points wins the game.");
    	
    	meta.addPage("There are 8 classes available to all players. Test them out and perfect your skills!");
    	
    	meta.addPage("Here are some helpful hints."
    			+ newPageLine(2) + "1. Teams need " + newPageLine() + " attackers and " + newPageLine() + "defenders. Choose a class to match your purpose."
    			+ newPageLine(2) + "2. To type in global chat, use /a before your message.");
     	
    	meta.addPage("3. Gunning Stations can be mounted by all classes."
    			 + newPageLine(2) + "4. The tunnel system is a fast and effective way to navigate the map."
    			 + newPageLine(2) + "5. Work together to plan successful attacks.");
    	
    	meta.addPage("6. Don't forget to have fun!"
    			+ newPageLine(10) + "-" + addSpaces(1) + ChatColor.ITALIC + "ArenaTeam ReefMC");
    	
    	book.setItemMeta(meta);
    	
    	return book;
    	
    }
    public static void playFireworkRandomColor(Location location) {
    	final Firework firework = location.getWorld().spawn(location, Firework.class);
    	FireworkMeta fMeta = firework.getFireworkMeta();
    	fMeta.addEffect(FireworkEffect.builder().withColor(Methods.getRandomColor()).with(Type.BALL).build());
    	firework.setFireworkMeta(fMeta);
    	Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable(){

			public void run() {
				firework.detonate();
			}
    		
    	}, 2);
    	}
    
    
    public static void playFirework(Location location, Type type, Color color)
    {
    	final Firework firework = location.getWorld().spawn(location, Firework.class);
    	FireworkMeta fMeta = firework.getFireworkMeta();
    	fMeta.addEffect(FireworkEffect.builder().withColor(color).with(type).build());
    	firework.setFireworkMeta(fMeta);
    	Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable(){

			public void run() {
				firework.detonate();
			}
    		
    	}, 2);
    }
    
    public static void playFirework(Location location, Color color) {
    	final Firework firework = location.getWorld().spawn(location, Firework.class);
    	FireworkMeta fMeta = firework.getFireworkMeta();
    	fMeta.addEffect(FireworkEffect.builder().withColor(color).with(Type.BALL).build());
    	firework.setFireworkMeta(fMeta);
    	Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable(){

			public void run() {
				firework.detonate();
			}
    		
    	}, 2);
    	}
    
    public static void playFireworkEntity(Location location, Color color) {
    	final Firework firework = location.getWorld().spawn(location, Firework.class);
    	FireworkMeta fMeta = firework.getFireworkMeta();
    	fMeta.addEffect(FireworkEffect.builder().withColor(color).with(Type.BALL).build());
    	firework.setFireworkMeta(fMeta);
    	firework.detonate();
    	}
    
    public static Color getRandomColor(){
    	Random r = new Random();
    	int rInt = r.nextInt(17) + 1;
    	
    	switch(rInt){
    	case 1:
    		return Color.AQUA;
    	case 2:
    		return Color.BLACK;
    	case 3: 
    		return Color.BLUE;
    	case 4: 
    		return Color.FUCHSIA;
    	case 5: 
    		return Color.GRAY;
    	case 6:
    		return Color.GREEN;
    	case 7: 
    		return Color.LIME;
    	case 8:
    		return Color.MAROON;
    	case 9: 
    		return Color.NAVY;
    	case 10:
    		return Color.OLIVE;
    	case 11:
    		return Color.ORANGE;
    	case 12:
    		return Color.PURPLE;
    	case 13:
    		return Color.RED;
    	case 14:
    		return Color.SILVER;
    	case 15:
    		return Color.TEAL;
    	case 16:
    		return Color.WHITE;
    	case 17: 
    		return Color.YELLOW;
    	default:
    		return Color.AQUA;
    		
    	
    	
    	}
    }
    
    public static void giveFireworkEffect(Location location, FireworkEffect effect, Color color) {
        final Firework firework = location.getWorld().spawn(location, Firework.class);
        FireworkMeta fMeta = firework.getFireworkMeta();
        fMeta.addEffect(effect);
        fMeta.addEffect(FireworkEffect.builder().withColor(color).build());
        firework.setFireworkMeta(fMeta);
      
        
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
            public void run() {
                firework.detonate();
            }
        }, 1L);
    }
    
    
    public static EntityGunSheep spawnEntityGunSheep(Location loc, DyeColor color){
		net.minecraft.server.v1_15_R1.World world = ((CraftWorld) loc.getWorld()).getHandle();
		EntityGunSheep gun = new EntityGunSheep(world);
		gun.setColor(color);
		gun.setPosition(loc.getX(), loc.getY(), loc.getZ());
		world.addEntity(gun, SpawnReason.CUSTOM);
		Sheep sheep = (Sheep) gun.getBukkitEntity();
		sheepGuns.add(sheep.getUniqueId());
		return gun;
	}
    

    public static EntityVillagerMerchant spawnVillagerMerchant(Location loc){
		net.minecraft.server.v1_15_R1.World world = ((CraftWorld) loc.getWorld()).getHandle();
		EntityVillagerMerchant gun = new EntityVillagerMerchant(world);
		gun.setPosition(loc.getX(), loc.getY(), loc.getZ());
		world.addEntity(gun, SpawnReason.CUSTOM);
		Villager villager = (Villager) gun.getBukkitEntity();
		merchants.add(villager.getUniqueId());
		return gun;
	}
    
    public static SummonedSkeleton spawnSummonedSkeleton(Location loc, Player owner){
		net.minecraft.server.v1_15_R1.World world = ((CraftWorld) loc.getWorld()).getHandle();
		SummonedSkeleton gun = new SummonedSkeleton(world);
		gun.setPosition(loc.getX(), loc.getY(), loc.getZ());
		world.addEntity(gun, SpawnReason.CUSTOM);
		gun.setOwner(owner);
		return gun;
	}
    
    public static SummonedZombie spawnSummonedZombie(Location loc, Player owner){
		net.minecraft.server.v1_15_R1.World world = ((CraftWorld) loc.getWorld()).getHandle();
		SummonedZombie gun = new SummonedZombie(world);
		gun.setPosition(loc.getX(), loc.getY(), loc.getZ());
		world.addEntity(gun, SpawnReason.CUSTOM);
		gun.setOwner(owner);
		return gun;
	}
    
  
    
    public static ModelZombie spawnModelZombie(Location loc, String name, ItemStack[] armor, ItemStack inHand){
		net.minecraft.server.v1_15_R1.World world = ((CraftWorld) loc.getWorld()).getHandle();
    	ModelZombie entity = new ModelZombie(world,name,armor,inHand);
    	entity.setPosition(loc.getX(), loc.getY(), loc.getZ());
    	world.addEntity(entity, SpawnReason.CUSTOM);
    	return entity;
    }
    
    public static ModelSkeleton spawnModelSkeleton(Location loc, String name, ItemStack[] armor, ItemStack inHand){
		net.minecraft.server.v1_15_R1.World world = ((CraftWorld) loc.getWorld()).getHandle();
    	ModelSkeleton entity = new ModelSkeleton(world,name,armor,inHand);
    	entity.setPosition(loc.getX(), loc.getY(), loc.getZ());
    	world.addEntity(entity, SpawnReason.CUSTOM);
    	return entity;
    }
    
    
    
    public static ItemStack getGunnerTrigger(){
    	ItemStack item = new ItemStack(Material.BLAZE_ROD);
    	ItemMeta meta = item.getItemMeta();
    	meta.setDisplayName(ChatColor.GOLD + "Trigger");
    	meta.setLore(ClassInventories.createLore("Left click to fire", "Right click to shoot continuously"));
    	item.setItemMeta(meta);
    	return item;
    }
    
    public static ItemStack getShopMenuItem(){
    	ItemStack item = new ItemStack(Material.DIAMOND);
    	ItemMeta iMeta = item.getItemMeta();
    	iMeta.setDisplayName(SHOP_MENU_DISPLAY_NAME);
    	iMeta.setLore(ClassInventories.createLore("Right click to", "Open the Shop!"));
    	item.setItemMeta(iMeta);
    	return item;
    }
    
    public static ItemStack getClassMenuItem(){
    	ItemStack item = new ItemStack(Material.IRON_INGOT);
    	ItemMeta iMeta = item.getItemMeta();
    	iMeta.setDisplayName(CLASS_MENU_DISPLAY_NAME);
    	iMeta.setLore(ClassInventories.createLore("Right click to open."));
    	item.setItemMeta(iMeta);
    	return item;
    }
    
    public static ItemStack getTeamPickMenuItem(){
    	ItemStack item = new ItemStack(Material.WHITE_BANNER);
    	BannerMeta iMeta = (BannerMeta)item.getItemMeta();
    	iMeta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Select a ArenaTeam!");
    	iMeta.setLore(ClassInventories.createLore("Select a team","to play with!"));
    	item.setItemMeta(iMeta);
    	return item;
    }
    
    /**
    * Gets the horizontal Block Face from a given yaw angle<br>
    * This includes the NORTH_WEST faces
    *
    * @param yaw angle
    * @return The Block Face of the angle
    */
    public static BlockFace yawToFace(float yaw) {
        return yawToFace(yaw, true);
    }
 
    /**
    * Gets the horizontal Block Face from a given yaw angle
    *
    * @param yaw angle
    * @param useSubCardinalDirections setting, True to allow NORTH_WEST to be returned
    * @return The Block Face of the angle
    */
    public static BlockFace yawToFace(float yaw, boolean useSubCardinalDirections) {
        if (useSubCardinalDirections) {
            return radial[Math.round(yaw / 45f) & 0x7];
        } else {
            return axis[Math.round(yaw / 90f) & 0x3];
        }
    }
	
    public static void setWallBlocks(Player player, Block block, int height, int width, Material mat){
		
    	if(player.getLocation().getPitch() < -30){
			int widthPerSide = width/2;
			int heightPerSide = height/2;
			
			int totalWX = block.getX() + widthPerSide;
			int totalWXM = block.getX() - widthPerSide;
			
			int totalWZ = block.getZ() + widthPerSide;
			int totalWZM = block.getZ() - widthPerSide;
			
			int totalH = block.getZ() + heightPerSide;
			int totalHM = block.getZ() - heightPerSide;
			
			int totalHX = block.getX() + heightPerSide;
			int totalHXM = block.getX() - heightPerSide;
			
			
			if(yawToFace(player.getLocation().getYaw()) == BlockFace.NORTH || yawToFace(player.getLocation().getYaw()) == BlockFace.SOUTH){
				//Facing north, south, both facing Z Coord, width is X Coord, Height is Z coord.
				
				for(int z = block.getZ(); z <= totalH; z++){
					Location loc = new Location(block.getWorld(), block.getX() , block.getY(), z);
					if(!loc.getBlock().getType().isSolid()){
						setBlockFast(loc, Material.DIRT);
					}
					
					for(int x = loc.getBlockX(); x >= totalWXM; x--){
						Location locx = new Location(loc.getWorld(), x, loc.getBlockY(), loc.getBlockZ());
						if(!locx.getBlock().getType().isSolid()){
							setBlockFast(locx, Material.DIRT);
							
							
						}
					}
					
					for(int x = loc.getBlockX(); x <= totalWX; x++){
						Location locx = new Location(loc.getWorld(), x, loc.getY(), loc.getZ());
						if(!locx.getBlock().getType().isSolid()){
							setBlockFast(locx, Material.DIRT);
						}
					}
					
					
				}
				
				for(int z = block.getZ(); z >= totalHM; z--){
					Location loc = new Location(block.getWorld(),block.getX(),block.getY(),z);
					if(!loc.getBlock().getType().isSolid()){
						setBlockFast(loc, Material.DIRT);
					}
					
					
					
					
					for(int x = loc.getBlockX(); x >= totalWXM; x--){
						Location locx = new Location(loc.getWorld(), x, loc.getY(), loc.getZ());
						if(!locx.getBlock().getType().isSolid()){
							setBlockFast(locx, Material.DIRT);
						}
					}
					
					for(int x = loc.getBlockX(); x <= totalWX; x++){
						Location locx = new Location(loc.getWorld(), x, loc.getY(), loc.getZ());
						if(!locx.getBlock().getType().isSolid()){
							setBlockFast(locx, Material.DIRT);
						}
					}
				}
			}else{
				//we are facing east/west, x is the height, z is the Width.
				for(int x = block.getX(); x <= totalHX; x++){
					Location loc = new Location(block.getWorld(), x , block.getY(), block.getZ());
					if(!loc.getBlock().getType().isSolid()){
						setBlockFast(loc, Material.DIRT);
					}
					
					
					for(int z = loc.getBlockZ(); z >= totalWZM; z--){
						Location locx = new Location(loc.getWorld(), loc.getX(), loc.getY(), z);
						if(!locx.getBlock().getType().isSolid()){
							setBlockFast(locx, Material.DIRT);
						}
					}
					for(int z = loc.getBlockZ(); z <= totalWZ; z++){
						Location locx = new Location(loc.getWorld(), loc.getX(), loc.getY(), z);
						if(!locx.getBlock().getType().isSolid()){
							setBlockFast(locx, Material.DIRT);
						}
					}
				}
				
				for(int x = block.getX(); x >= totalHXM; x--){
					Location loc = new Location(block.getWorld(),x, block.getY(),block.getZ());
					if(!loc.getBlock().getType().isSolid()){
						setBlockFast(loc, Material.DIRT);
					}
					
					for(int z = loc.getBlockZ(); z >= totalWZM; z--){
						Location locx = new Location(loc.getWorld(), loc.getX(), loc.getY(), z);
						if(!locx.getBlock().getType().isSolid()){
							setBlockFast(locx, Material.DIRT);
						}
					}
					
					for(int z = loc.getBlockZ(); z <= totalWZ; z++){
						Location locx = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), z);
						if(!locx.getBlock().getType().isSolid()){
							setBlockFast(loc, Material.DIRT);
						}
					}
				}
				
			}
			return;
		}
    	
    	int widthPerSide = width / 2;
		int heightPerSide = height/2;
		int totalWX = block.getX() + widthPerSide;
		int totalWZ = block.getZ() + widthPerSide;
		int totalWXM = block.getX() - widthPerSide;
		int totalWZM = block.getZ() - widthPerSide;
		
		int totalH = block.getY() + heightPerSide;
		int totalHM = block.getY() - heightPerSide;
		
		
		for(int i = block.getY() ; i <= totalH; i++){
			
			Location loc = new Location(block.getWorld(), block.getX(), i, block.getZ());
			if(!loc.getBlock().getType().isSolid()){
				setBlockFast(loc, mat);
			}
			
			if(yawToFace(player.getLocation().getYaw()) == BlockFace.NORTH || yawToFace(player.getLocation().getYaw()) == BlockFace.SOUTH){
				for(int x = loc.getBlockX();  x <= totalWX; x++){
					
					Location locx = new Location(loc.getWorld(), x, loc.getY(),loc.getZ());
					if(!locx.getBlock().getType().isSolid()){
						setBlockFast(locx, mat);
					}
				}
				for(int x = loc.getBlockX(); x >= totalWXM; x--){
					Location locx = new Location(loc.getWorld(), x, loc.getY(),loc.getZ());
					if(!locx.getBlock().getType().isSolid()){
						setBlockFast(locx, mat);
					}
				}
			}else{
				for(int z = loc.getBlockZ();  z <= totalWZ; z++){
					Location locz = new Location(loc.getWorld(), loc.getX(), loc.getY(),z);
					if(!locz.getBlock().getType().isSolid()){
						setBlockFast(locz, mat);
					}
				}
				for(int z = loc.getBlockZ(); z >= totalWZM; z--){
					Location locz = new Location(loc.getWorld(), loc.getX(), loc.getY(),z);
					if(!locz.getBlock().getType().isSolid()){
						setBlockFast(locz, mat);
					}
				}
			}
		}
		
		for(int i = block.getY() ; i >= totalHM; i--){
			
			Location loc = new Location(block.getWorld(), block.getX(), i, block.getZ());
			setBlockFast(loc, mat);
			if(yawToFace(player.getLocation().getYaw()) == BlockFace.NORTH || yawToFace(player.getLocation().getYaw()) == BlockFace.SOUTH){
				for(int x = loc.getBlockX();  x <= totalWX; x++){
					Location locx = new Location(loc.getWorld(), x, loc.getY(),loc.getZ());
					if(!locx.getBlock().getType().isSolid()){
						setBlockFast(locx, mat);
					}
				}
				for(int x = loc.getBlockX(); x >= totalWXM; x--){
					Location locx = new Location(loc.getWorld(), x, loc.getY(),loc.getZ());
					if(!locx.getBlock().getType().isSolid()){
						setBlockFast(locx, mat);
					} 
				}
				
			}else{
				for(int z = loc.getBlockZ();  z <= totalWZ; z++){
					Location locz = new Location(loc.getWorld(), loc.getX(), loc.getY(),z);
					if(!locz.getBlock().getType().isSolid()){
						setBlockFast(locz, mat);
					}
				}
				for(int z = loc.getBlockZ(); z >= totalWZM; z--){
					Location locz = new Location(loc.getWorld(), loc.getX(), loc.getY(),z);
					if(!locz.getBlock().getType().isSolid()){
						setBlockFast(locz, mat);
					}
				}
			}
		}
		
		
	}

    
	
    public static List<Block> getWallBlocks(Player player, Block block, int height, int width){
		List<Block> blocks = new ArrayList<Block>();
		
		
		if(player.getLocation().getPitch() < -30){
			int widthPerSide = width/2;
			int heightPerSide = height/2;
			
			int totalWX = block.getX() + widthPerSide;
			int totalWXM = block.getX() - widthPerSide;
			
			int totalWZ = block.getZ() + widthPerSide;
			int totalWZM = block.getZ() - widthPerSide;
			
			int totalH = block.getZ() + heightPerSide;
			int totalHM = block.getZ() - heightPerSide;
			
			int totalHX = block.getX() + heightPerSide;
			int totalHXM = block.getX() - heightPerSide;
			
			
			if(yawToFace(player.getLocation().getYaw()) == BlockFace.NORTH || yawToFace(player.getLocation().getYaw()) == BlockFace.SOUTH){
				//Facing north, south, both facing Z Coord, width is X Coord, Height is Z coord.
				
				for(int z = block.getZ(); z <= totalH; z++){
					Location loc = new Location(block.getWorld(), block.getX() , block.getY(), z);
					if(!loc.getBlock().getType().isSolid()){
						blocks.add(loc.getBlock());

					}
					
					for(int x = loc.getBlockX(); x >= totalWXM; x--){
						Location locx = new Location(loc.getWorld(), x, loc.getBlockY(), loc.getBlockZ());
						if(!locx.getBlock().getType().isSolid()){
							blocks.add(locx.getBlock());
							
							
						}
					}
					
					for(int x = loc.getBlockX(); x <= totalWX; x++){
						Location locx = new Location(loc.getWorld(), x, loc.getY(), loc.getZ());
						if(!locx.getBlock().getType().isSolid()){
							blocks.add(locx.getBlock());
						}
					}
					
					
				}
				
				for(int z = block.getZ(); z >= totalHM; z--){
					Location loc = new Location(block.getWorld(),block.getX(),block.getY(),z);
					if(!loc.getBlock().getType().isSolid()){
						blocks.add(loc.getBlock());
					}
					
					
					
					
					for(int x = loc.getBlockX(); x >= totalWXM; x--){
						Location locx = new Location(loc.getWorld(), x, loc.getY(), loc.getZ());
						if(!locx.getBlock().getType().isSolid()){
							blocks.add(locx.getBlock());
						}
					}
					
					for(int x = loc.getBlockX(); x <= totalWX; x++){
						Location locx = new Location(loc.getWorld(), x, loc.getY(), loc.getZ());
						if(!locx.getBlock().getType().isSolid()){
							blocks.add(locx.getBlock());
						}
					}
				}
			}else{
				//we are facing east/west, x is the height, z is the Width.
				for(int x = block.getX(); x <= totalHX; x++){
					Location loc = new Location(block.getWorld(), x , block.getY(), block.getZ());
					if(!loc.getBlock().getType().isSolid()){
						blocks.add(loc.getBlock());
					}
					
					
					for(int z = loc.getBlockZ(); z >= totalWZM; z--){
						Location locx = new Location(loc.getWorld(), loc.getX(), loc.getY(), z);
						if(!locx.getBlock().getType().isSolid()){
							blocks.add(locx.getBlock());
						}
					}
					for(int z = loc.getBlockZ(); z <= totalWZ; z++){
						Location locx = new Location(loc.getWorld(), loc.getX(), loc.getY(), z);
						if(!locx.getBlock().getType().isSolid()){
							blocks.add(locx.getBlock());
						}
					}
				}
				
				for(int x = block.getX(); x >= totalHXM; x--){
					Location loc = new Location(block.getWorld(),x, block.getY(),block.getZ());
					if(!loc.getBlock().getType().isSolid()){
						blocks.add(loc.getBlock());
					}
					
					for(int z = loc.getBlockZ(); z >= totalWZM; z--){
						Location locx = new Location(loc.getWorld(), loc.getX(), loc.getY(), z);
						if(!locx.getBlock().getType().isSolid()){
							blocks.add(locx.getBlock());
						}
					}
					
					for(int z = loc.getBlockZ(); z <= totalWZ; z++){
						Location locx = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), z);
						if(!locx.getBlock().getType().isSolid()){
							blocks.add(locx.getBlock());
						}
					}
				}
				
			}
			return blocks;
		}
		
		int widthPerSide = width / 2;
		int heightPerSide = height/2;
		int totalWX = block.getX() + widthPerSide;
		int totalWZ = block.getZ() + widthPerSide;
		int totalWXM = block.getX() - widthPerSide;
		int totalWZM = block.getZ() - widthPerSide;
		
		int totalH = block.getY() + heightPerSide;
		int totalHM = block.getY() - heightPerSide;
		for(int i = block.getY() ; i <= totalH; i++){
			
			Location loc = new Location(block.getWorld(), block.getX(), i, block.getZ());
			if(!loc.getBlock().getType().isSolid()){
				blocks.add(loc.getBlock());
			}
			
			if(yawToFace(player.getLocation().getYaw()) == BlockFace.NORTH || yawToFace(player.getLocation().getYaw()) == BlockFace.SOUTH){
				for(int x = loc.getBlockX();  x <= totalWX; x++){
					
					Location locx = new Location(loc.getWorld(), x, loc.getY(),loc.getZ());
					if(!locx.getBlock().getType().isSolid()){
						blocks.add(locx.getBlock());
					}
				}
				for(int x = loc.getBlockX(); x >= totalWXM; x--){
					Location locx = new Location(loc.getWorld(), x, loc.getY(),loc.getZ());
					if(!locx.getBlock().getType().isSolid()){
						blocks.add(locx.getBlock());
					}
				}
			}else{
				for(int z = loc.getBlockZ();  z <= totalWZ; z++){
					Location locz = new Location(loc.getWorld(), loc.getX(), loc.getY(),z);
					if(!locz.getBlock().getType().isSolid()){
						blocks.add(locz.getBlock());
					}
				}
				for(int z = loc.getBlockZ(); z >= totalWZM; z--){
					Location locz = new Location(loc.getWorld(), loc.getX(), loc.getY(),z);
					if(!locz.getBlock().getType().isSolid()){
						blocks.add(locz.getBlock());
					}
				}
			}
		}
		
		for(int i = block.getY() ; i >= totalHM; i--){
			
			Location loc = new Location(block.getWorld(), block.getX(), i, block.getZ());
			if(loc.getBlock().getType().isSolid()){
				blocks.add(loc.getBlock());
			}
			
			
			if(yawToFace(player.getLocation().getYaw()) == BlockFace.NORTH || yawToFace(player.getLocation().getYaw()) == BlockFace.SOUTH){
				for(int x = loc.getBlockX();  x <= totalWX; x++){
					Location locx = new Location(loc.getWorld(), x, loc.getY(),loc.getZ());
					if(!locx.getBlock().getType().isSolid()){
						blocks.add(locx.getBlock());
					}
				}
				for(int x = loc.getBlockX(); x >= totalWXM; x--){
					Location locx = new Location(loc.getWorld(), x, loc.getY(),loc.getZ());
					if(!locx.getBlock().getType().isSolid()){
						blocks.add(locx.getBlock());
					} 
				}
				
			}else{
				for(int z = loc.getBlockZ();  z <= totalWZ; z++){
					Location locz = new Location(loc.getWorld(), loc.getX(), loc.getY(),z);
					if(!locz.getBlock().getType().isSolid()){
						blocks.add(locz.getBlock());
					}
				}
				for(int z = loc.getBlockZ(); z >= totalWZM; z--){
					Location locz = new Location(loc.getWorld(), loc.getX(), loc.getY(),z);
					if(!locz.getBlock().getType().isSolid()){
						blocks.add(locz.getBlock());
					}
				}
			}
		}
		
		return blocks;
		
	}

	
	
	
	/**
	 * 
	 * @param mapname
	 * Map to rollback.
	 *
	 *-Snippet of code from Bukkit-
	 * Made by: skipperguy12
	 */
	//Unloading maps, to rollback maps. Will delete all player builds until last server save
    private static void unloadMap(String mapname){
    	//Bukkit.getServer().getWorld(mapname).setAutoSave(false);
        if(Bukkit.getServer().unloadWorld(Bukkit.getServer().getWorld(mapname), false)){
            plugin.getLogger().info("Successfully unloaded " + mapname);
        }else{
            plugin.getLogger().severe("COULD NOT UNLOAD " + mapname);
        }
    }
    //Loading maps (MUST BE CALLED AFTER UNLOAD MAPS TO FINISH THE ROLLBACK PROCESS)
    private static void loadMap(String mapname){
       World w = Bukkit.getServer().createWorld(new WorldCreator(mapname));
       w.setAutoSave(false);
       plugin.getLogger().info(mapname + " has loaded!");
    }
 
    //Maprollback method, because were too lazy to type 2 lines
    public static void rollback(String mapname){
        unloadMap(mapname);
        loadMap(mapname);
        plugin.getLogger().info("rollback: " + mapname + " , finished!");
    }
	
	
	
	
    public static void setNearbyCircleBlocks(Location loc, Integer r, Integer h, Boolean hollow, Boolean sphere, int plus_y, Material mat)
	  {
	    int cx = loc.getBlockX();
	    int cy = loc.getBlockY();
	    int cz = loc.getBlockZ();
	    for (int x = cx - r.intValue(); x <= cx + r.intValue(); x++) {
	      for (int z = cz - r.intValue(); z <= cz + r.intValue(); z++) {
	        for (int y = sphere.booleanValue() ? cy - r.intValue() : cy; y < (sphere.booleanValue() ? cy + r.intValue() : cy + h.intValue()); y++)
	        {
	          double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere.booleanValue() ? (cy - y) * (cy - y) : 0);
	          if ((dist < r.intValue() * r.intValue()) && ((!hollow.booleanValue()) || (dist >= (r.intValue() - 1) * (r.intValue() - 1))))
	          {
	            Location l = new Location(loc.getWorld(), x, y + plus_y, z);
	           // if(b.getType() == Material.AIR){
				setBlockFast(l, mat);
	            //}
	          }
	        }
	      }
	    }
	    
	  } 
	
	public Methods(CrystalClash plugin) {
		Methods.plugin = plugin;
	}
	
	
	public static CrystalClash getPlugin(){
		return plugin;
	}
	
	
	
	public static List<Block> getNearbyCircleBlocks(Location loc, Integer r, Integer h, Boolean hollow, Boolean sphere, int plus_y)
	  {
	    List<Block> circleblocks = new ArrayList<Block>();
	    int cx = loc.getBlockX();
	    int cy = loc.getBlockY();
	    int cz = loc.getBlockZ();
	    for (int x = cx - r.intValue(); x <= cx + r.intValue(); x++) {
	      for (int z = cz - r.intValue(); z <= cz + r.intValue(); z++) {
	        for (int y = sphere.booleanValue() ? cy - r.intValue() : cy; y < (sphere.booleanValue() ? cy + r.intValue() : cy + h.intValue()); y++)
	        {
	          double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere.booleanValue() ? (cy - y) * (cy - y) : 0);
	          if ((dist < r.intValue() * r.intValue()) && ((!hollow.booleanValue()) || (dist >= (r.intValue() - 1) * (r.intValue() - 1))))
	          {
	            Location l = new Location(loc.getWorld(), x, y + plus_y, z);
	            circleblocks.add(l.getBlock());
	          }
	        }
	      }
	    }
	    return circleblocks;
	  }
	  
	  
	  
	  
	  public static List<Location> circle(Location loc, Integer r, Integer h, Boolean hollow, Boolean sphere, int plus_y)
	  {
	    List<Location> circleblocks = new ArrayList<Location>();
	    int cx = loc.getBlockX();
	    int cy = loc.getBlockY();
	    int cz = loc.getBlockZ();
	    for (int x = cx - r.intValue(); x <= cx + r.intValue(); x++) {
	      for (int z = cz - r.intValue(); z <= cz + r.intValue(); z++) {
	        for (int y = sphere.booleanValue() ? cy - r.intValue() : cy; y < (sphere.booleanValue() ? cy + r.intValue() : cy + h.intValue()); y++)
	        {
	          double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere.booleanValue() ? (cy - y) * (cy - y) : 0);
	          if ((dist < r.intValue() * r.intValue()) && ((!hollow.booleanValue()) || (dist >= (r.intValue() - 1) * (r.intValue() - 1))))
	          {
	            Location l = new Location(loc.getWorld(), x, y + plus_y, z);
	            circleblocks.add(l);
	          }
	        }
	      }
	    }
	    return circleblocks;
	  }
	  
	  public static List<Block> getNearbyBlocks(Location location, int Radius)
	  {
	    List<Block> Blocks = new ArrayList<Block>();
	    for (int X = location.getBlockX() - Radius; X <= location.getBlockX() + Radius; X++) {
	      for (int Y = location.getBlockY() - Radius; Y <= location.getBlockY() + Radius; Y++) {
	        for (int Z = location.getBlockZ() - Radius; Z <= location.getBlockZ() + Radius; Z++)
	        {
	          Block block = location.getWorld().getBlockAt(X, Y, Z);
	          if (!block.isEmpty()) {
	            Blocks.add(block);
	          }
	        }
	      }
	    }
	    return Blocks;
	  }
	
	
	public static void setLobby(Location loc)
	  {
	    if (!plugin.arenas.contains("LobbySpawn"))
	    {
	      plugin.arenas.addDefault("LobbySpawn.X", loc.getX());
	      plugin.arenas.addDefault("LobbySpawn.Y", loc.getY());
	      plugin.arenas.addDefault("LobbySpawn.Z", loc.getZ());
	      plugin.arenas.addDefault("LobbySpawn.World", loc.getWorld().getName());
	      plugin.arenas.addDefault("LobbySpawn.Pitch", loc.getPitch());
	      plugin.arenas.addDefault("LobbySpawn.Yaw", loc.getYaw());
	    }
	    else
	    {
	      plugin.arenas.set("LobbySpawn.X", loc.getX());
	      plugin.arenas.set("LobbySpawn.Y", loc.getY());
	      plugin.arenas.set("LobbySpawn.Z", loc.getZ());
	      plugin.arenas.set("LobbySpawn.World", loc.getWorld().getName());
	      plugin.arenas.set("LobbySpawn.Pitch", loc.getPitch());
	      plugin.arenas.set("LobbySpawn.Yaw", loc.getYaw());
	    }
	    saveYamls();
	  }
	  
	  public static Location getLobby()
	  {
	    if (plugin.arenas.contains("LobbySpawn.World"))
	    {
	      Location loc = new Location(Bukkit.getWorld(plugin.arenas.getString("LobbySpawn.World")), 
	        plugin.arenas.getDouble("LobbySpawn.X"), 
	        plugin.arenas.getDouble("LobbySpawn.Y"), 
	        plugin.arenas.getDouble("LobbySpawn.Z"));
	      loc.setPitch((float)plugin.arenas.getDouble("LobbySpawn.Pitch"));
	      loc.setYaw((float)plugin.arenas.getDouble("LobbySpawn.Yaw"));
	      return loc;
	    }
	    return null;
	  }
	  
	  public static void addToList(Arena arena)
	  {
	    if (plugin.arenas.contains("Arenas.List"))
	    {
	      List<String> list = plugin.arenas.getStringList("Arenas.List");
	      list.add(arena.getName());
	      plugin.arenas.set("Arenas.List", list);
	    }
	    else
	    {
	      List<String> list = new ArrayList<String>();
	      list.add(arena.getName());
	      plugin.arenas.addDefault("Arenas.List", list);
	    }
	  }
	  
	  
	  public static void removeFromList(String name)
	  {
	    if (plugin.arenas.contains("Arenas.List"))
	    {
	      List<String> list = plugin.arenas.getStringList("Arenas.List");
	      list.remove(name);
	      plugin.arenas.set("Arenas.List", list);
	    }
	  }
	  
	  public void firstRun()
	    throws Exception
	  {
	    if (!plugin.arenasFile.exists())
	    {
	      plugin.arenasFile.getParentFile().mkdirs();
	      copy(plugin.getResource("arenasFile.yml"), plugin.arenasFile);
	    }
	    if (!plugin.playersFile.exists())
	    {
	      plugin.playersFile.getParentFile().mkdirs();
	      copy(plugin.getResource("playersFile.yml"), plugin.playersFile);
	    }
	    if (!plugin.kitsFile.exists())
	    {
	      plugin.kitsFile.getParentFile().mkdirs();
	      copy(plugin.getResource("kitsFile.yml"), plugin.kitsFile);
	    }
	  }
	  
	  private void copy(InputStream in, File file)
	  {
	    try
	    {
	      OutputStream out = new FileOutputStream(file);
	      byte[] buf = new byte[1024];
	      int len;
	      while ((len = in.read(buf)) > 0)
	      {
	        
	        out.write(buf, 0, len);
	      }
	      out.close();
	      in.close();
	    }
	    catch (Exception localException) {}
	  }
	  
	  public static void saveYamls()
	  {
	    try
	    {
	      plugin.arenas.save(plugin.arenasFile);
	      plugin.players.save(plugin.playersFile);
	      plugin.kits.save(plugin.kitsFile);
	    }
	    catch (IOException localIOException) {}
	  }
	  
	  public static void loadYamls()
	  {
	    try
	    {
	      plugin.arenas.load(plugin.arenasFile);
	      plugin.players.load(plugin.playersFile);
	      plugin.kits.load(plugin.kitsFile);
	    }
	    catch (Exception localException) {}
	  }
	
	
	
}
