package com.action35.guncraft;

import org.bukkit.plugin.java.JavaPlugin;

import com.action35.guncraft.commands.TestCommands;

public class Main extends JavaPlugin {

	@Override
	public void onEnable() {		
		new GunManager(this);
		new LagCompensation(this);
		TestCommands commands = new TestCommands();
		
		getCommand("gun").setExecutor(commands);
		getCommand("gun").setTabCompleter(commands);
		getCommand("creategun").setExecutor(commands);
		getCommand("creategun").setTabCompleter(commands);
		getServer().getPluginManager().registerEvents(new Events(), this);
		getServer().getConsoleSender().sendMessage("[Guncraft] Plugin is enabled!");
	}
	
	@Override
	public void onDisable() {
		getServer().getConsoleSender().sendMessage("[Guncraft] Plugin is disabled!");
	}
}
