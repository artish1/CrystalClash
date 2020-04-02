package me.artish1.CrystalClash.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class PluginListener implements PluginMessageListener{

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if(!channel.equalsIgnoreCase("BungeeCord"))
			return;
		
	    ByteArrayDataInput in = ByteStreams.newDataInput(message);
	    String subchannel = in.readUTF();
	    if(subchannel.equalsIgnoreCase("maxplayersfrom")){
	    	short len = in.readShort();
	    	byte[] msgbytes = new byte[len];
	    	in.readFully(msgbytes);
	    	
	    	 ByteArrayDataOutput out = ByteStreams.newDataOutput();
	    	  out.writeUTF("Forward");
	    	  out.writeUTF("");

	    	
	    	
	    }
		
	}

}
