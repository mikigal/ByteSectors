package pl.mikigal.bytesectors.client.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
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

        if (newSector == null) {
            player.teleport(from);
            Utils.sendMessage(player, "&cWyszedles poza border mapy!");
            return;
        }

        if (!currentSector.equals(newSector)) {
            player.teleport(from);
            PlayerTransferUtils.transfer(player, to, newSector);
        }
    }
}
