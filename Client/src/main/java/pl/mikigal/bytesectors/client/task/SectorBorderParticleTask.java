package pl.mikigal.bytesectors.client.task;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import pl.mikigal.bytesectors.client.ByteSectorsClient;
import pl.mikigal.bytesectors.client.util.BlockUtils;
import pl.mikigal.bytesectors.client.util.VersionUtils;
import pl.mikigal.bytesectors.commons.data.Sector;
import pl.mikigal.bytesectors.commons.data.SectorManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SectorBorderParticleTask implements Runnable {

    @Override
    public void run() {
        Map<Player, List<Location>> particles = new HashMap<>();
        Sector sector = SectorManager.getCurrentSector();
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location location = player.getLocation();
            if (sector.getDistanceToBorder(location.getBlockX(), location.getBlockZ()) > 150) {
                continue;
            }

            particles.put(player, new ArrayList<>());
            for (Location loc : BlockUtils.sphere(location, 8, 0, false, true, 1)) {
                if ((loc.getBlockX() == sector.getMinimum().getX() ||
                        loc.getBlockX() == sector.getMaximum().getX() ||
                        loc.getBlockZ() == sector.getMinimum().getZ() ||
                        loc.getBlockZ() == sector.getMaximum().getZ()) &&
                        loc.getBlock().getType() == Material.AIR &&
                        player.isOnline()) {

                    particles.get(player).add(loc);
                }
            }
        }

        Bukkit.getScheduler().runTaskAsynchronously(ByteSectorsClient.getInstance(), () -> {
            for (Map.Entry<Player, List<Location>> entry : particles.entrySet()) {
                for (Location loc : entry.getValue()) {
                    final Player player = entry.getKey();

                    if (VersionUtils.getVersion().startsWith("v1_13_")
                            || VersionUtils.getVersion().startsWith("v1_14_")
                            || VersionUtils.getVersion().startsWith("v1_15_")) {
                        try {
                            Object particleType = null;
                            final Class<?> effectClass = Class.forName("org.bukkit.Particle");
                            final Object[] effectTypes = effectClass.getEnumConstants();

                            for (Object object : effectTypes) {
                                if (object.toString().equals("PORTAL")) {
                                    particleType = object;
                                }
                            }

                            final Class<?> playerClass = Class.forName("org.bukkit.entity.Player");
                            final Method playerSpawnParticleMethod = playerClass.getDeclaredMethod("spawnParticle", effectClass, Location.class, int.class);

                            playerSpawnParticleMethod.invoke(player, particleType, loc, 1);
                        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    } else {
                        player.playEffect(loc, Effect.PORTAL, 0);
                    }
                }
            }
        });
    }
}
