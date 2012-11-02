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
        			}
        			
        			if(!this.plugin.getConfig().getStringList("worlds").contains(((Player) sender).getWorld()))
        				sender.sendMessage("Please note PvP toggling has no effect in the world you are currently in (" + ((Player)sender).getWorld().getName() + ") as PvP in this world is managed elsewhere.");
        		} else {
        			sender.sendMessage("You do not have permission to toggle your PVP.");
        		}
        	} else {
        		sender.sendMessage("You must be in-game to do that.");
        	}
            return true;
        }
        return false;
    }
}
