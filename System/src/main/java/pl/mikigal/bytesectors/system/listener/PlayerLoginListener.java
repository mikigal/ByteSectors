package pl.mikigal.bytesectors.system.listener;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pl.mikigal.bytesectors.commons.data.Sector;
import pl.mikigal.bytesectors.commons.data.SectorManager;
import pl.mikigal.bytesectors.commons.redis.RedisUtils;
import pl.mikigal.bytesectors.system.ByteSectorsSystem;
import pl.mikigal.bytesectors.system.configuration.SectorsConfiguration;
import pl.mikigal.bytesectors.system.util.Utils;

public class PlayerLoginListener implements Listener {

    @EventHandler
    public void onConnect(ServerConnectEvent event) {
        // TODO: 19/01/2020 Async?
        ServerConnectEvent.Reason reason = event.getReason();
        ProxiedPlayer player = event.getPlayer();

        if (reason == ServerConnectEvent.Reason.LOBBY_FALLBACK ||
                reason == ServerConnectEvent.Reason.SERVER_DOWN_REDIRECT ||
                reason == ServerConnectEvent.Reason.KICK_REDIRECT ||
                reason == ServerConnectEvent.Reason.COMMAND) {

            event.setCancelled(true);
            player.disconnect(new TextComponent(Utils.fixColors(SectorsConfiguration.getJoinSectorOfflineMessage())));
            return;
        }

        String lastSector = RedisUtils.get(player.getUniqueId().toString());
        if (lastSector == null) {
            lastSector = SectorManager.getDefaultSector().getId();
            RedisUtils.set(player.getUniqueId().toString(), lastSector);
        }

        Sector sector = SectorManager.getSector(lastSector);
        if (sector.isOffline()) {
            event.setCancelled(true);
            player.disconnect(new TextComponent(Utils.fixColors(SectorsConfiguration.getJoinSectorOfflineMessage())));
            return;
        }

        event.setTarget(ByteSectorsSystem.getInstance().getProxy().getServerInfo(sector.getId()));
    }
}
