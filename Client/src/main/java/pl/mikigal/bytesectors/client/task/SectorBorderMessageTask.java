package pl.mikigal.bytesectors.client.task;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import pl.mikigal.bytesectors.client.Configuration;
import pl.mikigal.bytesectors.client.utils.ActionBarAPI;
import pl.mikigal.bytesectors.commons.data.Sector;
import pl.mikigal.bytesectors.commons.data.SectorManager;

public class SectorBorderMessageTask implements Runnable {

    @Override
    public void run() {
        Sector sector = SectorManager.getSector(Configuration.getSectorId());
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location location = player.getLocation();
            int distance = sector.getDistanceToBorder(location.getBlockX(), location.getBlockZ());
            if (distance > 150) {
                continue;
            }

            Sector nearestSector = sector.getNearestSector(distance, location.getBlockX(), location.getBlockZ(), location.getWorld().getName());
            if (nearestSector == null) {
                ActionBarAPI.sendActionBar(player, "&c&lZblizasz sie do granicy mapy (" + distance + "m)");
                continue;
            }

            ActionBarAPI.sendActionBar(player, "&c&lSektor " + nearestSector.getId() + " (" + distance + "m) [" + nearestSector.getPerformance() + " TPS]");
        }
    }
}
