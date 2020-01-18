package pl.mikigal.bytesectors.client.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.mikigal.bytesectors.client.ByteSectorsClient;
import pl.mikigal.bytesectors.client.utils.PlayerTransferUtils;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(ByteSectorsClient.getInstance(), () -> PlayerTransferUtils.acceptTransfer(event.getPlayer()), 5);
    }
}
