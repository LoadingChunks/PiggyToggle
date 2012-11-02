package net.loadingchunks.plugins.PiggyToggle.PiggyToggle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.Potion;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PiggyToggleEventListener implements Listener {

	private PiggyToggle plugin;

	public PiggyToggleEventListener(PiggyToggle plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event)
	{
		// Rather than checking assailant on every instance of damage, let's just set them and check later.
		Player assailant = null;
		
		// We don't manage this world.
		if(!this.plugin.worldList.contains(event.getEntity().getWorld().getName()))
			return;
		
		if(!(event.getEntity() instanceof Player))
		{
			return;
		}

		// Arrow
		if(event.getDamager() instanceof Arrow)
		{
			Arrow arrow = (Arrow) event.getDamager();
			if(arrow.getShooter() instanceof Player)
				assailant = (Player) arrow.getShooter();
			else
				return;
		}

		// Potion
		if(event.getDamager() instanceof ThrownPotion)
		{
			ThrownPotion potion = (ThrownPotion) event.getDamager();
			if(potion.getShooter() instanceof Player)
				assailant = (Player) potion.getShooter();
			else
				return;
		}
		
		// Player
		if(event.getDamager() instanceof Player)
			assailant = (Player) event.getDamager();
		
		if(assailant != null)
		{
			Boolean assailantFlagged = this.plugin.pvpList.get(assailant.getName());
			Boolean victimFlagged = this.plugin.pvpList.get(((Player)event.getEntity()).getName());

			if(assailantFlagged)
			{
				if(victimFlagged)
					return;
				else
				{
					event.setCancelled(true);
					assailant.sendMessage(ChatColor.RED + ((Player)event.getEntity()).getName() + " is not flagged for PVP!");
				}
			} else {
				event.setCancelled(true);
				assailant.sendMessage(ChatColor.RED + "You are not flagged for PVP, use /pvp to flag yourself and enable PVP!");
			}
		}
	}
}
