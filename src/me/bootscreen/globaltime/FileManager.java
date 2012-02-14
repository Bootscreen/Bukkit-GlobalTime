package me.bootscreen.globaltime;

import java.io.File;

import org.bukkit.util.config.*;


/**
* GlobalTime for CraftBukkit
*
* handels all functions about the config
* 
* @author Bootscreen
*
*/


public class FileManager {
	
	private static String ordner = "plugins/GlobalTime";
	private static File configFile = new File(ordner + File.separator + "config.yml");
	@SuppressWarnings("deprecation")
	private static Configuration config;

	@SuppressWarnings("deprecation")
	private Configuration loadConfig()
	{
		try{
			Configuration config = new Configuration(configFile);
			config.load();
			return config;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("deprecation")
	public void createConfig()
	{
		new File(ordner).mkdir();
		
		if(!configFile.exists())
		{
			try 
			{
				configFile.createNewFile();
				
				config = loadConfig();
				//config.setHeader(header)
				config.setProperty("on_start", false);
				config.setProperty("continuous", false);
				config.setProperty("continuous_time", 10);

				config.save();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		config = loadConfig();
	}

	public boolean readBoolean(String key)
	{
		boolean value = config.getBoolean(key, false);
		return value;
	}
	
	public int readInt(String key)
	{
		int value = config.getInt(key, 10);
		return value;
	}
}
