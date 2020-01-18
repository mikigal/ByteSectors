package pl.mikigal.bytesectors.client.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import pl.mikigal.bytesectors.client.utils.ActionBarAPI;
import pl.mikigal.bytesectors.client.utils.PlayerTransferUtils;
import pl.mikigal.bytesectors.client.utils.Utils;
import pl.mikigal.bytesectors.commons.data.Sector;
import pl.mikigal.bytesectors.commons.data.SectorManager;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();

        if (from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to.getBlockZ()) {
            return;
        }

        Player player = event.getPlayer();
        Sector currentSector = SectorManager.getSector(from.getBlockX(), from.getBlockZ(), from.getWorld().getName());
        Sector newSector = SectorManager.getSector(to.getBlockX(), to.getBlockZ(), to.getWorld().getName());

        int distance = currentSector.getDistanceToBorder(from.getBlockX(), from.getBlockZ());
        Sector nearestSector = currentSector.getNearestSector(distance, from.getBlockX(), from.getBlockZ(), from.getWorld().getName());
        if (distance <= 150) {
            ActionBarAPI.sendActionBar(event.getPlayer(), "&c&lSektor " + nearestSector.getId() + " (" + distance + "m) [" + nearestSector.getPerformance() + " TPS]");
        }

        if (newSector == null) {
            player.teleport(from);
            Utils.sendMessage(player, "&cWyszedles poza border mapy!");
            return;
        }

        if (!currentSector.equals(newSector)) {
            PlayerTransferUtils.transfer(player, to, newSector);
        }
    }
}
