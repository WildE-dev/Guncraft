package com.action35.guncraft;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.scheduler.BukkitTask;

import com.action35.guncraft.custom_data_types.CustomDataTypes;
import com.action35.guncraft.custom_data_types.GunData;
import com.action35.guncraft.tasks.ReloadTask;

public class GunManager {

	public static Map<UUID, BukkitTask> reloadTasks = new HashMap<UUID, BukkitTask>();

	private static Main plugin;
	public static NamespacedKey idKey;
	public static NamespacedKey dataKey;

	public GunManager(Main plugin) {
		GunManager.plugin = plugin;

		idKey = new NamespacedKey(plugin, "id");
		dataKey = new NamespacedKey(plugin, "data");
	}

	public static ItemStack createGun(String[] args) {
		ItemStack item = new ItemStack(Material.getMaterial(args[0]));
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Utils.chat("&fGun"));
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.getPersistentDataContainer().set(idKey, CustomDataTypes.UUID, UUID.randomUUID());

		GunData data = new GunData();
		data.damage = Short.parseShort(args[1]);
		data.magazineSize = Short.parseShort(args[2]);
		data.ammo = Short.parseShort(args[2]);
		data.reloadTime = Byte.parseByte(args[3]);
		data.shotTime = Short.parseShort(args[4]);
		data.distance = Short.parseShort(args[5]);

		meta.getPersistentDataContainer().set(dataKey, CustomDataTypes.GUNDATA, data);
		item.setItemMeta(meta);

		return item;
	}

	public static ItemStack createGun(String type) {
		ItemStack item = null;
		ItemMeta meta = null;
		GunData data = null;
		if (type.equalsIgnoreCase("pistol")) {
			item = new ItemStack(Material.IRON_SHOVEL);
			meta = item.getItemMeta();
			meta.setDisplayName(Utils.chat("&fPistol"));
			data = new GunData().pistol();
		} else if (type.equalsIgnoreCase("rifle")) {
			item = new ItemStack(Material.IRON_AXE);
			meta = item.getItemMeta();
			meta.setDisplayName(Utils.chat("&fAssault Rifle"));
			data = new GunData().rifle();
		} else if (type.equalsIgnoreCase("sniper")) {
			item = new ItemStack(Material.IRON_HOE);
			meta = item.getItemMeta();
			meta.setDisplayName(Utils.chat("&fSniper Rifle"));
			data = new GunData().sniper();
		}

		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.getPersistentDataContainer().set(idKey, CustomDataTypes.UUID, UUID.randomUUID());
		meta.getPersistentDataContainer().set(dataKey, CustomDataTypes.GUNDATA, data);
		item.setItemMeta(meta);

		return item;
	}

	public static boolean IsGun(ItemStack gun) {
		if (!gun.hasItemMeta())
			return false;

		ItemMeta meta = gun.getItemMeta();

		PersistentDataContainer container = meta.getPersistentDataContainer();

		return container.has(idKey, CustomDataTypes.UUID);
	}

	public static void Shoot(ItemStack gun, Player shooter) {
		if (!gun.hasItemMeta())
			return;

		ItemMeta meta = gun.getItemMeta();
		PersistentDataContainer container = meta.getPersistentDataContainer();

		UUID id;
		GunData data;

		if (container.has(idKey, CustomDataTypes.UUID) && container.has(dataKey, CustomDataTypes.GUNDATA)) {
			id = container.get(idKey, CustomDataTypes.UUID);
			data = container.get(dataKey, CustomDataTypes.GUNDATA);
		} else {
			return;
		}
		
		if ((System.currentTimeMillis() - data.timestamp) < data.shotTime) {
			return;
		}

		if (reloadTasks.containsKey(id)) {
			reloadTasks.get(id).cancel();
			reloadTasks.remove(id);
			Utils.sendActionbar(shooter, "");
		}

		// If we have no ammo return
		if (data.ammo <= 0) {
			shooter.playSound(shooter.getLocation(), Sound.ITEM_SHIELD_BREAK, 1, 2);
			return;
		}

		// Shooting happens here
		data.ammo--;
		data.timestamp = System.currentTimeMillis();
		shooter.playSound(shooter.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, new Random().nextFloat() + 1);
		Predicate<Entity> filter = e -> (e != shooter);
		Location rayLoc = shooter.getEyeLocation();
		Damageable result = LagCompensation.getRaycast(shooter, rayLoc, data, filter);
		if (result != null) {
			result.damage(data.damage, shooter);
			LivingEntity entity = (LivingEntity) result;
			entity.setNoDamageTicks(0);
		}

		double maxDurability = (double) gun.getType().getMaxDurability();
		int durability = (int) (maxDurability - (((double) data.ammo / (double) data.magazineSize) * maxDurability));

		((org.bukkit.inventory.meta.Damageable) meta).setDamage(durability);

		meta.getPersistentDataContainer().set(dataKey, CustomDataTypes.GUNDATA, data);
		gun.setItemMeta(meta);
	}

	public static void Reload(ItemStack gun, Player p) {
		if (!gun.hasItemMeta())
			return;

		ItemMeta meta = gun.getItemMeta();
		PersistentDataContainer container = meta.getPersistentDataContainer();

		UUID id;
		GunData data;

		if (container.has(idKey, CustomDataTypes.UUID) && container.has(dataKey, CustomDataTypes.GUNDATA)) {
			id = container.get(idKey, CustomDataTypes.UUID);
			data = container.get(dataKey, CustomDataTypes.GUNDATA);
		} else {
			return;
		}

		if (data.ammo == data.magazineSize || reloadTasks.containsKey(id))
			return;

		((org.bukkit.inventory.meta.Damageable) meta).setDamage(0);
		data.ammo = data.magazineSize;
		meta.getPersistentDataContainer().set(GunManager.dataKey, CustomDataTypes.GUNDATA, data);

		reloadTasks.put(id, new ReloadTask(data.reloadTime, gun, p, meta).runTaskTimer(plugin, 0, 1));
	}
}