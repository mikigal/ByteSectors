package pl.mikigal.bytesectors.client.redis.synchronization;

import org.bukkit.Bukkit;
import pl.mikigal.bytesectors.client.Configuration;
import pl.mikigal.bytesectors.client.utils.PerformanceUtils;
import pl.mikigal.bytesectors.commons.data.SectorManager;
import pl.mikigal.bytesectors.commons.packet.synchronization.PacketPerformanceSynchronization;
import pl.mikigal.bytesectors.commons.packet.synchronization.PacketPerformanceSynchronizationRequest;
import pl.mikigal.bytesectors.commons.redis.RedisListener;

public class PacketPerformanceSynchronizationRequestListener extends RedisListener<PacketPerformanceSynchronizationRequest> {

    public PacketPerformanceSynchronizationRequestListener() {
        super(SectorManager.getClientChannel(), PacketPerformanceSynchronizationRequest.class);
    }

    @Override
    public void onMessage(PacketPerformanceSynchronizationRequest packet) {
        if (packet.getSender().equals(Configuration.getSectorId())) {
            return;
        }

        String performance = PerformanceUtils.getTps();
        SectorManager.getSector(Configuration.getSectorId()).setPerformance(performance);
        new PacketPerformanceSynchronization(Configuration.getSectorId(), performance, Bukkit.getOnlinePlayers().size()).send(SectorManager.getClientChannel());
    }
}
