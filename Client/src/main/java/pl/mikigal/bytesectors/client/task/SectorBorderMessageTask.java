package pl.mikigal.bytesectors.client.task;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import pl.mikigal.bytesectors.client.Configuration;
import pl.mikigal.bytesectors.client.util.ActionBarAPI;
import pl.mikigal.bytesectors.commons.data.Sector;
import pl.mikigal.bytesectors.commons.data.SectorManager;

public class SectorBorderMessageTask implements Runnable {

    @Override
    public void run() {
        Sector sector = SectorManager.getCurrentSector();
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location location = player.getLocation();
            int distance = sector.getDistanceToBorder(location.getBlockX(), location.getBlockZ());
            if (distance > 150) {
                continue;
            }

            Sector nearestSector = sector.getNearestSector(distance, location.getBlockX(), location.getBlockZ(), location.getWorld().getName());
            if (nearestSector == null) {
                ActionBarAPI.sendActionBar(player, Configuration.getNearBorderActionBar().replace("{DISTANCE}", String.valueOf(distance)));
                continue;
            }

            ActionBarAPI.sendActionBar(player, Configuration.getNearSectorActionBar()
                    .replace("{ID}", nearestSector.getId())
                    .replace("{DISTANCE}", String.valueOf(distance))
                    .replace("{PERFORMANCE}", nearestSector.isOffline() ? "OFFLINE" : nearestSector.getPerformance() + " TPS")
                    .replace("{ONLINE}", String.valueOf(nearestSector.getOnline())));
        }
    }
}
