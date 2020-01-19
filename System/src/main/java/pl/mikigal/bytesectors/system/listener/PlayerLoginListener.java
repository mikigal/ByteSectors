package pl.mikigal.bytesectors.system.listener;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pl.mikigal.bytesectors.commons.data.Sector;
import pl.mikigal.bytesectors.commons.data.SectorManager;
import pl.mikigal.bytesectors.commons.redis.RedisUtils;
import pl.mikigal.bytesectors.system.ByteSectorsSystem;
import pl.mikigal.bytesectors.system.configuration.SectorsConfiguration;
import pl.mikigal.bytesectors.system.utils.Utils;

import java.util.concurrent.TimeUnit;

public class PlayerLoginListener implements Listener {

    @EventHandler
    public void onLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        ByteSectorsSystem.getInstance().getProxy().getScheduler().runAsync(ByteSectorsSystem.getInstance(), () -> {
           String lastSector = RedisUtils.get(player.getUniqueId().toString());
           if (lastSector == null) {
               // Connect to default server from Bungee's config
               return;
           }

           Sector sector = SectorManager.getSector(lastSector);

           if (sector.isOffline()) {
               ByteSectorsSystem.getInstance().getProxy().getScheduler().schedule(ByteSectorsSystem.getInstance(),
                       () -> player.disconnect(new TextComponent(Utils.fixColors(SectorsConfiguration.getJoinSectorOfflineMessage()))),
                       300, TimeUnit.MILLISECONDS);
           }

           ByteSectorsSystem.getInstance().getProxy().getScheduler().schedule(ByteSectorsSystem.getInstance(),
                   () -> player.connect(ByteSectorsSystem.getInstance().getProxy().getServerInfo(sector.getId())),
                   300, TimeUnit.MILLISECONDS);

            // TODO: 19/01/2020 Fix already connecting, better way to switch to new server
        });
    }
}
