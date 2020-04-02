package me.artish1.CrystalClash.other;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
 
public class Stop implements CommandExecutor{
   
   
    private static String prefix = "�7[�eCore�7] �e� "; // Can also use ChatColor.WHATEVER_COLOR (ChatColor.RED for example)
    private static String nopermission = "Sorry, you do not have the correct permissions!";
    private JavaPlugin plugin;
    public Stop(JavaPlugin plgn)
    {
      this.plugin = plgn;
    }

    public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("Stop")) {
            if(!(sender.hasPermission("core.dev"))) {
                sender.sendMessage(prefix + nopermission);
            } else {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

					@Override
					public void run() {
						sender.sendMessage(prefix + "Stopping server....");
		                Bukkit.broadcastMessage(prefix + "�l�o�nAlert");
		                Bukkit.broadcastMessage(prefix + "This server is currently stopping.. ");
		                Bukkit.broadcastMessage(prefix + "Please connect to another server.");
		                Bukkit.shutdown();
		       
					}
                	
                }, 20 * 10);
                
            
 
            }
        }
		return false;
   }
    
    
}