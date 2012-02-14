package me.bootscreen.globaltime;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
* GlobalTime for CraftBukkit
*
* @author Bootscreen
* 
*/

public class GlobalTime extends JavaPlugin{
	public final Logger log = Logger.getLogger("Minecraft");
	
	private FileManager fileManager = new FileManager();
	
	PluginDescriptionFile plugdisc;
		
	@Override
	public void onDisable() {
		log.info("[" + plugdisc.getName() + "] Version " + plugdisc.getVersion() + " disabled.");		
	}

	@Override
	public void onEnable() {

		fileManager.createConfig();
		
		plugdisc = this.getDescription();
		
		log.info("[" + plugdisc.getName() + "] Version " + plugdisc.getVersion() + " enabled.");
		
		
		if(fileManager.readBoolean("on_start"))
		{
			synctime();
		}				
		
		if(fileManager.readBoolean("continuous"))
		{
			long time_s = fileManager.readInt("continuous_time")*20;
			
			if(time_s < 20)
			{
				time_s = 20;
			}
			
			this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
			    public void run() {
			    	synctime();
			    }
			}, 60L, time_s);
		}
	}


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		boolean succeed = false;
		Player player = null;
		if (sender instanceof Player)
		{
			player = (Player) sender;
			if(!player.hasPermission("GlobalTime")) 
			{
				player.sendMessage(ChatColor.RED + "[" + plugdisc.getName() + "] you don't have permission to do this.");
				return true;
			}
		}
					
		
		if(cmd.getName().equalsIgnoreCase("globaltime") || cmd.getName().equalsIgnoreCase("gt"))
		{
			if(args.length == 0)
			{
				synctime();
				return true;
			}
			else if(args.length == 1)
			{
				try
				{
					long time = Long.parseLong(args[0]);
					synctime(time);
					return true;
				}
				catch(NumberFormatException nfe)
				{
					try
					{
						if(synctime(args[0]))
						{
							return true;
						}
						else
						{
							if(player != null)
							{
								player.sendMessage(ChatColor.RED + "[" + plugdisc.getName() + "] the world "+args[0]+" doesn't exists.");
							}
							else
							{
								log.info("[" + plugdisc.getName() + "] the world "+args[0]+" doesn't exists.");
							}
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
						return false;
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
					return false;
				}
			}
		}
		
		return succeed;
	}
	
	public void synctime()
	{
		World defaultw = getServer().getWorlds().get(0);
		long time = defaultw.getTime();
		
		List<World> worlds = getServer().getWorlds();
		
		for (int i=0;i<worlds.size();i++) 
		{
			worlds.get(i).setTime(time);
		}
	}
	
	public boolean synctime(String world_s)
	{
		World defaultw = getServer().getWorld(world_s);
		if(defaultw != null)
		{
			long time = defaultw.getTime();
			
			List<World> worlds = getServer().getWorlds();
	
			for (World world: worlds) 
			{
				   world.setTime(time);
			}
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void synctime(long time)
	{			
		List<World> worlds = getServer().getWorlds();

		for (World world: worlds) 
		{
			   world.setTime(time);
		}
	}
}
