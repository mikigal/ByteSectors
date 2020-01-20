package pl.mikigal.bytesectors.client.listener;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import pl.mikigal.bytesectors.client.util.PlayerTransferUtils;

public class SectorChangeListener implements Listener {

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        PlayerTransferUtils.handlePlayerMove(event.getPlayer(), event.getTo(), event);
    }

    @EventHandler
    public void onPortal(PlayerPortalEvent event) {
        PlayerTransferUtils.handlePlayerMove(event.getPlayer(), event.getTo(), event);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();

        if (from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to.getBlockZ()) {
            return;
        }

        PlayerTransferUtils.handlePlayerMove(event.getPlayer(), to, event);
    }
}
