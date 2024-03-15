package com.action35.guncraft.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.action35.guncraft.GunManager;
import com.action35.guncraft.Utils;

public class TestCommands implements CommandExecutor, TabExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Only players can use this command!");
			return true;
		}

		Player p = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("gun")) {
			if (p.isOp()) {
				p.getInventory().addItem(GunManager.createGun(args[0]));				
			}
			else {
				p.sendMessage(Utils.chat("&cMust be opped to use this command!"));
			}
		} 
		
		if (cmd.getName().equalsIgnoreCase("creategun")) {
			if (p.isOp()) {
				p.getInventory().addItem(GunManager.createGun(args));				
			}
			else {
				p.sendMessage(Utils.chat("&cMust be opped to use this command!"));
			}
		}

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {		
		if (cmd.getName().equalsIgnoreCase("gun") && sender.isOp()) {
			if (args.length == 1) {
				List<String> gunTypes = new ArrayList<String>();
				gunTypes.add("pistol");
				gunTypes.add("rifle");
				gunTypes.add("sniper");
				return gunTypes;
			}
		}
		
		if (cmd.getName().equalsIgnoreCase("creategun") && sender.isOp()) {
			if (args.length == 1) {
				List<String> materials = new ArrayList<String>();
				for (Material mat : Material.values()) {
					if (mat.getMaxDurability() > 1 && mat.name().toLowerCase().startsWith(args[0].toLowerCase())) {
						materials.add(mat.name());
					}
				}
				return materials;
			} else if (args.length == 2) {
				List<String> hint = new ArrayList<String>();
				hint.add("<damage>");
				return hint;
			} else if (args.length == 3) {
				List<String> hint = new ArrayList<String>();
				hint.add("<magazine size>");
				return hint;
			} else if (args.length == 4) {
				List<String> hint = new ArrayList<String>();
				hint.add("<reload time>");
				return hint;
			} else if (args.length == 5) {
				List<String> hint = new ArrayList<String>();
				hint.add("<shot time>");
				return hint;
			} else if (args.length == 6) {
				List<String> hint = new ArrayList<String>();
				hint.add("<distance>");
				return hint;
			}
		}
		
		return null;
	}
}
