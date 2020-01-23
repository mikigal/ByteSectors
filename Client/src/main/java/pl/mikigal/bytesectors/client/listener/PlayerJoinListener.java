package pl.mikigal.bytesectors.client.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.mikigal.bytesectors.client.ByteSectorsClient;
import pl.mikigal.bytesectors.client.data.UserManager;
import pl.mikigal.bytesectors.client.util.PlayerTransferUtils;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        event.setJoinMessage(null);
        if (UserManager.getUser(player.getUniqueId()) == null) {
            UserManager.createUser(player.getUniqueId());
        }
        Bukkit.getScheduler().runTaskLater(ByteSectorsClient.getInstance(), () -> PlayerTransferUtils.acceptTransfer(event.getPlayer()), 5);
    }
}
