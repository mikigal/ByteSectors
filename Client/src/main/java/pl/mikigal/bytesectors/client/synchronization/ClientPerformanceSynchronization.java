package pl.mikigal.bytesectors.client.synchronization;

import org.bukkit.Bukkit;
import pl.mikigal.bytesectors.client.Configuration;
import pl.mikigal.bytesectors.client.util.PerformanceUtils;
import pl.mikigal.bytesectors.commons.data.SectorManager;
import pl.mikigal.bytesectors.commons.packet.synchronization.PacketPerformanceSynchronization;

public class ClientPerformanceSynchronization implements Runnable {

    @Override
    public void run() {
        String performance = PerformanceUtils.getTps();
        SectorManager.getSector(Configuration.getSectorId()).setPerformance(performance);
        new PacketPerformanceSynchronization(Configuration.getSectorId(), performance, Bukkit.getOnlinePlayers().size()).send(SectorManager.getPublicChannel());
    }
}
