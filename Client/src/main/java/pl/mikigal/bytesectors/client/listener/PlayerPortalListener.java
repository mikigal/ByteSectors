package pl.mikigal.bytesectors.client.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import pl.mikigal.bytesectors.client.utils.PlayerTransferUtils;

public class PlayerPortalListener implements Listener {

    @EventHandler
    public void onPortal(PlayerPortalEvent event) {
        PlayerTransferUtils.handlePlayerMove(event.getPlayer(), event.getTo(), event);
    }
}
