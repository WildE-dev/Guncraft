package com.action35.guncraft;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class Events implements Listener {
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
			if (GunManager.IsGun(p.getInventory().getItemInMainHand())) {
				e.setCancelled(true);
				GunManager.Shoot(p.getInventory().getItemInMainHand(), p);
			}
		} else if (e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR) {
			if (GunManager.IsGun(p.getInventory().getItemInMainHand())) {
				e.setCancelled(true);
				GunManager.Reload(p.getInventory().getItemInMainHand(), p);
			}
		}
	}
	
	@EventHandler
	public void onDamageEntity(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			Player damager = (Player) e.getDamager();
			
			if (GunManager.IsGun(damager.getInventory().getItemInMainHand()) && e.getEntity() instanceof Player) {
				damager.playSound(damager.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
			}
		}
	}
	
	@EventHandler
	public void onPlayerDamageItem(PlayerItemDamageEvent e) {
		if (GunManager.IsGun(e.getItem())) {
			e.setCancelled(true);
		}
		
	}
}
