package pl.mikigal.bytesectors.client.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import pl.mikigal.bytesectors.client.data.User;
import pl.mikigal.bytesectors.client.data.UserManager;
import pl.mikigal.bytesectors.client.util.PlayerTransferUtils;
import pl.mikigal.bytesectors.commons.data.SectorManager;

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

        Player player = event.getPlayer();
        if (!SectorManager.getCurrentSector().equals(SectorManager.getSector(to.getBlockX(), to.getBlockZ(), to.getWorld().getName()))) {
            User user = UserManager.getUser(player.getUniqueId());
            user.setLastLocation(from);
        }

        PlayerTransferUtils.handlePlayerMove(event.getPlayer(), to, event);
    }

    @EventHandler
    public void onVehicleMove(VehicleMoveEvent event) {
        if (event.getVehicle().getPassenger() instanceof Player) {
            PlayerTransferUtils.handlePlayerMove(((Player) event.getVehicle().getPassenger()), event.getTo(), event);
        }
    }
}
