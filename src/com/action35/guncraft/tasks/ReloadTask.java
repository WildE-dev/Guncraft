package com.action35.guncraft.tasks;

import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.scheduler.BukkitRunnable;

import com.action35.guncraft.GunManager;
import com.action35.guncraft.Utils;
import com.action35.guncraft.custom_data_types.CustomDataTypes;

public class ReloadTask extends BukkitRunnable {

	private int timer;
	private ItemStack gun;
	private Player player;
	private ItemMeta meta;
	private UUID id;

	public ReloadTask(int time, ItemStack gun, Player player, ItemMeta meta) {
		if (time <= 0) {
			throw new IllegalArgumentException("Time must be greater than 0");
		} else {
			this.timer = time;
			this.gun = gun;
			this.player = player;
			this.meta = meta;
			
			PersistentDataContainer container = meta.getPersistentDataContainer();
			if (container.has(GunManager.idKey, CustomDataTypes.UUID)) {
				id = container.get(GunManager.idKey, CustomDataTypes.UUID);
			} else {
				return;
			}
		}
	}

	@Override
	public void run() {
		// What you want to schedule goes here
		if (timer > 0) {
			if (!player.getInventory().getItemInMainHand().equals(gun)) {
				player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BREAK, 1, 1);
				GunManager.reloadTasks.remove(id);
				Utils.sendActionbar(player, "");
				this.cancel();
				return;
			}
			Utils.sendActionbar(player, String.format("Reloading... %1.1f", (double) timer / 20));
			timer--;
		} else {
			gun.setItemMeta(meta);
			player.playSound(player.getLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, 1, 1.5f);
			GunManager.reloadTasks.remove(id);
			Utils.sendActionbar(player, "");
			this.cancel();
		}
	}
}
