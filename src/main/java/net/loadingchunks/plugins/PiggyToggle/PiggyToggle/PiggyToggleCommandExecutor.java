package net.loadingchunks.plugins.PiggyToggle.PiggyToggle;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PiggyToggleCommandExecutor implements CommandExecutor {

    private PiggyToggle plugin;

    public PiggyToggleCommandExecutor(PiggyToggle plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("pvp")) {
        	if(sender instanceof Player)
        	{
        		if(sender.hasPermission("pt.toggle"))
        		{
        			if(this.plugin.pvpList.containsKey(sender.getName()))
        			{
        				if(this.plugin.pvpList.get(sender.getName()))
        				{
        					this.plugin.pvpList.put(sender.getName(), false);
        					sender.sendMessage(ChatColor.RED + "You are now flagged for PVP. Type /pvp again to disable.");
        				} else
        				{
        					this.plugin.pvpList.put(sender.getName(), true);
        					sender.sendMessage(ChatColor.RED + "You are no longer flagged for PVP. Type /pvp again to enable.");
        				}
        			} else {
        				this.plugin.pvpList.put(sender.getName(), true);
    					sender.sendMessage(ChatColor.RED + "You are now flagged for PVP. Type /pvp again to disable.");
        			}
        			
        			if(!this.plugin.worldList.contains(((Player) sender).getWorld()))
        				sender.sendMessage("Please note PvP toggling has no effect in the world you are currently in (" + ((Player)sender).getWorld().getName() + ") as PvP in this world is managed elsewhere.");
        		} else {
        			sender.sendMessage("You do not have permission to toggle your PVP.");
        		}
        	} else {
        		sender.sendMessage("You must be in-game to do that.");
        	}
            return true;
        } else if(command.getName().equalsIgnoreCase("pt"))
        {
        	if(!sender.isOp())
        	{
        		sender.sendMessage("You must be an op to do that!");
        		return true;
        	}

        	if(args.length > 0)
        	{
        		if(args[0].equalsIgnoreCase("reload"))
        		{
        			this.plugin.reloadConfig();
        			sender.sendMessage("PiggyToggle Config Reloaded.");
        			return true;
        		} else if(args[0].equalsIgnoreCase("list"))
        		{
        			sender.sendMessage("Managed worlds for PVP:");
        			
        			for(String s : this.plugin.worldList)
        			{
        				sender.sendMessage("- " + ChatColor.GREEN + s);
        			}
        		} else if(args.length == 2) {
        			if(args[0].equalsIgnoreCase("add"))
        			{
        				if(this.plugin.getServer().getWorld(args[1]) != null)
        				{
        					if(this.plugin.worldList.contains(args[1]))
        						sender.sendMessage("That world is already listed!");
        					else
        					{
        						this.plugin.worldList.add(args[1]);
        						this.plugin.saveWorldConfig();
        						sender.sendMessage("World " + args[1] + " added to PVP Management List.");
        					}
        					return true;
        				} else {
        					sender.sendMessage("Please specify a valid world name.");
        					return false;
        				}
        			} else if(args[0].equalsIgnoreCase("remove")){
        				if(this.plugin.worldList.contains(args[1]))
        				{
        					this.plugin.worldList.remove(args[1]);
        					this.plugin.saveWorldConfig();
        					sender.sendMessage("World " + args[1] + " has been removed from the PVP Management List.");
        				} else {
        					sender.sendMessage("That world is not listed!");
        				}
        				return true;
        			} else {
        				sender.sendMessage("Invalid add/remove command");
        				return false;
        			}
        		} else
        			sender.sendMessage("Invalid number of arguments given.");
        	} else
        		sender.sendMessage("No arguments given.");
        }
        return false;
    }
}
