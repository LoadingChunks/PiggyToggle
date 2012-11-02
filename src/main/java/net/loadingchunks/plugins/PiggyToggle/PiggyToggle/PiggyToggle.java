package net.loadingchunks.plugins.PiggyToggle.PiggyToggle;

import java.util.HashMap;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PiggyToggle extends JavaPlugin {

	//ClassListeners
	private PiggyToggleCommandExecutor commandExecutor;
	private PiggyToggleEventListener eventListener;
	//ClassListeners
	
	public HashMap<String, Boolean> pvpList = new HashMap<String,Boolean>();

	public void onDisable() {
	}

	public void onEnable() { 

		PluginManager pm = this.getServer().getPluginManager();
		
		this.commandExecutor = new PiggyToggleCommandExecutor(this);
		this.eventListener = new PiggyToggleEventListener(this);

		getCommand("pvp").setExecutor(commandExecutor);

		// you can register multiple classes to handle events if you want
		// just call pm.registerEvents() on an instance of each class
		pm.registerEvents(eventListener, this);
		
		this.getConfig().addDefault("worlds", new String[] { "world" });
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
	}
}
