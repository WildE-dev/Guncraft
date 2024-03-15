package com.action35.guncraft;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.action35.guncraft.custom_data_types.GunData;

public class LagCompensation {

	private static Main plugin;

	public static HashMap<UUID, TreeMap<Long, BoundingBox>> storedPositions = new HashMap<UUID, TreeMap<Long, BoundingBox>>();

	public LagCompensation(Main plugin) {
		LagCompensation.plugin = plugin;
		startLagComp();
	}

	private void startLagComp() {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					TreeMap<Long, BoundingBox> map = storedPositions.get(player.getUniqueId());
					if (map == null)
						map = new TreeMap<Long, BoundingBox>();
					map.put(System.currentTimeMillis(), player.getBoundingBox());
					if (map.size() > 100) {
						map.pollFirstEntry();
					}

					storedPositions.put(player.getUniqueId(), map);
				}
			}

		}, 0L, 1L);
	}

	public static Damageable getRaycast(Player shooter, Location rayLoc, GunData data, Predicate<Entity> filter) {

		Long latency = shooter.getPing() / 2L;
		Long time = System.currentTimeMillis() - latency;

		HashMap<Player, BoundingBox> playerBoxes = new HashMap<Player, BoundingBox>();
		
		for (Entry<UUID, TreeMap<Long, BoundingBox>> entry : storedPositions.entrySet()) {

			Player p = Bukkit.getPlayer(entry.getKey());

			TreeMap<Long, BoundingBox> map = entry.getValue();
			Map.Entry<Long, BoundingBox> low = map.floorEntry(time);
			Map.Entry<Long, BoundingBox> high = map.ceilingEntry(time);
			BoundingBox box = null;
			if (low != null && high != null) {
				box = Math.abs(time - low.getKey()) < Math.abs(time - high.getKey()) ? low.getValue()
						: high.getValue();
			} else if (low != null || high != null) {
				box = low != null ? low.getValue() : high.getValue();
			}

			if (box != null && p != shooter) {
				playerBoxes.put(p, box);
			}
		}

		Damageable entity = null;
		
		RayTraceResult result = shooter.getWorld().rayTrace(rayLoc, rayLoc.getDirection(), data.distance, FluidCollisionMode.NEVER, true,
				0, filter);

		if (result != null && !(result instanceof Player))
			entity = (Damageable) result.getHitEntity();
		
		double dist = data.distance;
		
		for (Entry<Player, BoundingBox> entry : playerBoxes.entrySet()) {
			Player player = entry.getKey();
			BoundingBox box = entry.getValue();
			
			if (result != null) 
				dist = result.getHitPosition().distance(rayLoc.toVector());
			RayTraceResult r = box.rayTrace(rayLoc.toVector(), rayLoc.getDirection(), dist);
			
			if (r != null)
				entity = player;
		}

		Vector dir = shooter.getEyeLocation().getDirection();
		
		int distance = (int) dist;
		
		for (int i = 1; i < distance; i++) {
			Location particleLoc = shooter.getEyeLocation().add(dir.normalize().multiply(i));
			shooter.getWorld().spawnParticle(Particle.CRIT, particleLoc, 1, 0, 0, 0, 0);
		}
		
		return entity;
	}
}
