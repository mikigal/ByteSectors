package pl.mikigal.bytesectors.client.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import pl.mikigal.bytesectors.client.util.PlayerTransferUtils;

public class PlayerTeleportListener implements Listener {

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        PlayerTransferUtils.handlePlayerMove(event.getPlayer(), event.getTo(), event);
    }
}
