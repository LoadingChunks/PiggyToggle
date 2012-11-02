package net.loadingchunks.plugins.PiggyToggle.PiggyToggle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;

public class PiggyToggleEventListener implements Listener {

	private PiggyToggle plugin;

	public PiggyToggleEventListener(PiggyToggle plugin) {
		this.plugin = plugin;
	}
	
	
	
	@EventHandler
	public void onPotionSplash(PotionSplashEvent event)
	{
		Boolean isbad = false;
		if(!(event.getPotion().getShooter() instanceof Player))
			return;
		
		for(PotionEffect f : event.getPotion().getEffects())
		{
			if(f.getType().equals(PotionType.INSTANT_DAMAGE) || f.getType().equals(PotionType.POISON) || f.getType().equals(PotionType.SLOWNESS) || f.getType().equals(PotionType.WEAKNESS))
			{
				isbad = true;
				break;
			}
		}
		
		if(!isbad)
			return;
		
		for(LivingEntity l : event.getAffectedEntities())
		{
			if(!(l instanceof Player))
				continue;
			
			Player victim = (Player)l;
			
			if(this.checkPVP((Entity)l, (Player)event.getPotion().getShooter()))
				event.getAffectedEntities().remove(l);
		}
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
		
		if(this.checkPVP(event.getEntity(), assailant))
			event.setCancelled(true);
	}
	
	public Boolean checkPVP(Entity e, Player assailant)
	{
		if(assailant != null)
		{
			Boolean assailantFlagged = false;
			assailantFlagged = this.plugin.pvpList.get(assailant.getName());
			Boolean victimFlagged = false;
			victimFlagged = this.plugin.pvpList.get(((Player)e).getName());

			if(assailantFlagged != null && assailantFlagged)
			{
				if(victimFlagged != null && victimFlagged)
					return false;
				else
				{
					assailant.sendMessage(ChatColor.RED + ((Player)e).getName() + " is not flagged for PVP!");
					return true;
				}
			} else {
				assailant.sendMessage(ChatColor.RED + "You are not flagged for PVP, use /pvp to flag yourself and enable PVP!");
				return true;
			}
		}
		return false;
	}
}
