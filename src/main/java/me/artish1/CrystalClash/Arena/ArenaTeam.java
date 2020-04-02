package me.artish1.CrystalClash.Arena;

import me.artish1.CrystalClash.Util.Methods;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ArenaTeam {

    private String name;
    private int crystalHealth = 50;
    private Point point;
    private int teamPoints;
    private ChatColor chatColor;
    private DyeColor dye;
    private HashSet<ArenaPlayer> playerList = new HashSet<ArenaPlayer>();
    private List<ArenaPlayer> queue = new ArrayList<ArenaPlayer>();
    private Location spawn;
    private Material materialType;
    public ArenaTeam(String name, ChatColor color, DyeColor dye, Point point1, Material material) {
        this.name = name;
        this.materialType = material;
        this.point = point1;
        this.dye = dye;
        this.chatColor = color;
    }


    public Material getMaterialType() {
        return materialType;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public DyeColor getDyeColor()
    {
        return dye;
    }

    public Location getSpawn()
    {
        return spawn;
    }


    public Point getPoint(){
        return point;
    }

    public void init()
    {
        spawn = Methods.getLocation(getName() + ".Spawn");

    }

    public void setSpawn(Location loc)
    {
        spawn = loc;
        Methods.addLocation(getName() + ".Spawn", loc);
    }


    public String getName() {
        return name;
    }

    public HashSet<ArenaPlayer> getPlayerList() {
        return playerList;
    }

    public int getTeamPoints()
    {
        return teamPoints;
    }

    public void addTeamPoints(int amount)
    {
        Arena arena = Methods.getArena();
        int placeholderScore = arena.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(getChatColor().toString() + "Points: " + teamPoints).getScore();
        arena.getScoreboard().resetScores(getChatColor().toString() + "Points: " + getTeamPoints());
        teamPoints += amount;

        arena.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(getChatColor().toString() + "Points: " + teamPoints).setScore(placeholderScore);

        if(teamPoints >= arena.getPointsGoal()){
            Bukkit.broadcastMessage(getChatColor().toString() + ChatColor.BOLD.toString() + getName() +" ArenaTeam"
                    + ChatColor.GREEN + " reached the point goal and won!");
          //  arena.setWinner(this);
            arena.stop(StopReason.GAME_END);
        }

    }

    public void subtractTeamPoints(int amount)
    {
        teamPoints-= amount;
    }

    public void setTeamPoints(int amount)
    {
        teamPoints = amount;
    }

    public String getColoredName(){
        return chatColor + getName();
    }
    public List<ArenaPlayer> getQueue() {
        return queue;
    }



    public int getCrystalHealth() {
        return crystalHealth;
    }

    public boolean isOnTeam(Player player){
        return (getPlayerList().contains(Methods.getArena().getArenaPlayer(player)));
    }

    private void explodeBouncies(){
        for(Location loc : getBouncyExplosions()){
            Methods.createBouncyExplosion(loc);
        }
    }

    public void subtractCrystalHealth(int amount){
        crystalHealth -= amount;

        if(crystalHealth == 25){
            explodeBouncies();
        }

    }

    public void awardAllPoints(int amount){
        if(getName().equalsIgnoreCase("Red")){
            for(ArenaPlayer ap : Methods.getPlugin().getArena().getRedPlayers()){
                ap.addMoney(amount);
            }
        }

        if(getName().equalsIgnoreCase("Blue")){
            for(ArenaPlayer ap : Methods.getPlugin().getArena().getBluePlayers()){
                ap.addMoney(amount);
            }
        }
    }

    public void addBouncyExplosionLocation(Location loc){
        addMultiLocation("BouncyExplosions", loc);
    }

    public List<Location> getBouncyExplosions(){
        return getMultiLocation("BouncyExplosions");
    }

    public void addExplosionLocation(Location loc){
        addMultiLocation("Explosions", loc);
    }

    public List<Location> getExplosionLocations(){
        return getMultiLocation("Explosions");
    }

    public void setCrystal(Location loc){
        addSingleLocation("Crystal", loc);
    }

    public void setMerchantSpawn(Location loc){
        Methods.addLocation("Merchants." + getName(), loc);
    }

    private int id;
    public void startExplosions(){
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Methods.getPlugin(), new Runnable(){

            public void run() {
                Methods.createExplosion(Methods.getRandomLocation(getExplosionLocations()), 5, 5);
            }

        }, 0, 20 * 5);
    }

    public void stopExplosions(){
        Bukkit.getScheduler().cancelTask(id);
    }
    public Location getMerchantSpawn(){
        return Methods.getLocation("Merchants." + getName());
    }

    public Location getCrystal(){
        return getSingleLocation("Crystal");
    }


    private Location getSingleLocation(String path){
        if (Methods.getPlugin().arenas.contains(getName() + "." + path + ".World"))
        {
            Location loc = new Location(Bukkit.getWorld(Methods.getPlugin().arenas.getString(getName() + "." + path + ".World")),
                    Methods.getPlugin().arenas.getDouble(getName() + "." + path  + ".X"),
                    Methods.getPlugin().arenas.getDouble(getName() + "." + path + ".Y"),
                    Methods.getPlugin().arenas.getDouble(getName() + "." + path  + ".Z"));
            loc.setPitch((float)Methods.getPlugin().arenas.getDouble(getName() + "." + path  + ".Pitch"));
            loc.setYaw((float)Methods.getPlugin().arenas.getDouble(getName() + "." + path  + ".Yaw"));
            return loc;
        }
        return null;
    }


    private void addSingleLocation(String path, Location loc){
        if (!Methods.getPlugin().arenas.contains(getName() + "." + path))
        {
            Methods.getPlugin().arenas.addDefault(getName() + "." + path + ".X", loc.getX());
            Methods.getPlugin().arenas.addDefault(getName() + "." + path + ".Y", loc.getY());
            Methods.getPlugin().arenas.addDefault(getName() + "." + path + ".Z", loc.getZ());
            Methods.getPlugin().arenas.addDefault(getName() + "." + path + ".World", loc.getWorld().getName());
            Methods.getPlugin().arenas.addDefault(getName() + "." + path+ ".Pitch", loc.getPitch());
            Methods.getPlugin().arenas.addDefault(getName() + "." + path + ".Yaw", loc.getYaw());
        }
        else
        {
            Methods.getPlugin().arenas.set(getName() + "." + path + ".X", loc.getX());
            Methods.getPlugin().arenas.set(getName() + "." + path + ".Y", loc.getY());
            Methods.getPlugin().arenas.set(getName() + "." + path+ ".Z", loc.getZ());
            Methods.getPlugin().arenas.set(getName() + "." + path + ".World", loc.getWorld().getName());
            Methods.getPlugin().arenas.set(getName() + "." + path + ".Pitch", loc.getPitch());
            Methods.getPlugin().arenas.set(getName() + "." + path + ".Yaw", loc.getYaw());
        }
        Methods.saveYamls();
    }


    private List<Location> getMultiLocation(String path){
        List<Location> locs = new ArrayList<Location>();
        for(int i = 1;i <= Methods.getPlugin().arenas.getInt(getName() + "." + path + ".Counter"); i++ ){
            Location loc = new Location(
                    Bukkit.getWorld(Methods.getPlugin().arenas.getString(getName() + "." + path + "." + i + ".World")),
                    Methods.getPlugin().arenas.getDouble(getName() + "." + path + "."  + i + ".X"),
                    Methods.getPlugin().arenas.getDouble(getName() + "." + path + "."  + i + ".Y"),
                    Methods.getPlugin().arenas.getDouble(getName() + "." + path + "." + i + ".Z"));
            loc.setPitch((float) Methods.getPlugin().arenas.getInt(getName() + "." + path + "."  + i + ".Pitch"));
            loc.setYaw((float) Methods.getPlugin().arenas.getDouble(getName() + "." + path + "." + i + ".Yaw"));
            if(loc != null){
                locs.add(loc);
            }
        }


        return locs;

    }

    private void addMultiLocation(String path, Location loc){
        if (!Methods.getPlugin().arenas.contains(getName() + "." + path +".Counter"))
        {
            int counter = 1;
            Methods.getPlugin().arenas.addDefault(getName() + "." + path + "." + counter +  ".X", loc.getX());
            Methods.getPlugin().arenas.addDefault(getName() + "." + path + "." +counter + ".Y", loc.getY());
            Methods.getPlugin().arenas.addDefault(getName() + "." + path + "."  +counter+ ".Z", loc.getZ());
            Methods.getPlugin().arenas.addDefault(getName() + "." + path + "." +counter+ ".World", loc.getWorld().getName());
            Methods.getPlugin().arenas.addDefault(getName() + "." + path + "."  +counter+ ".Pitch", loc.getPitch());
            Methods.getPlugin().arenas.addDefault(getName() + "." + path + "."  +counter+ ".Yaw", loc.getYaw());
            Methods.getPlugin().arenas.addDefault(getName() + "." + path + ".Counter", counter);

        }
        else
        {
            int counter = Methods.getPlugin().arenas.getInt(getName() + "." + path + ".Counter");
            counter++;
            Methods.getPlugin().arenas.set(getName() + "." + path + "."  +counter+ ".X", loc.getX());
            Methods.getPlugin().arenas.set(getName() + "." + path + "."  +counter+ ".Y", loc.getY());
            Methods.getPlugin().arenas.set(getName() + "." + path + "."  +counter+ ".Z", loc.getZ());
            Methods.getPlugin().arenas.set(getName() + "." + path + "."  +counter+ ".World", loc.getWorld().getName());
            Methods.getPlugin().arenas.set(getName() + "." + path + "."  +counter+ ".Pitch", loc.getPitch());
            Methods.getPlugin().arenas.set(getName() + "." + path + "."  +counter+ ".Yaw", loc.getYaw());
            Methods.getPlugin().arenas.set(getName() + "." + path + ".Counter" , counter);

        }
        Methods.saveYamls();
    }



}
