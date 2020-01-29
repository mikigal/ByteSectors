package pl.mikigal.bytesectors.client.redis.synchronization;

import org.bukkit.Bukkit;
import pl.mikigal.bytesectors.client.util.NMSUtils;
import pl.mikigal.bytesectors.commons.data.SectorManager;
import pl.mikigal.bytesectors.commons.packet.synchronization.PacketPerformanceSynchronization;
import pl.mikigal.bytesectors.commons.packet.synchronization.PacketPerformanceSynchronizationRequest;
import pl.mikigal.bytesectors.commons.redis.RedisListener;

public class PerformanceSyncRequestListener extends RedisListener<PacketPerformanceSynchronizationRequest> {

    public PerformanceSyncRequestListener() {
        super(SectorManager.getClientChannel(), PacketPerformanceSynchronizationRequest.class);
    }

    @Override
    public void onMessage(PacketPerformanceSynchronizationRequest packet) {
        if (packet.getSender().equals(SectorManager.getCurrentSectorId())) {
            return;
        }

        String performance = NMSUtils.getTps();
        SectorManager.getCurrentSector().setPerformance(performance);
        new PacketPerformanceSynchronization(performance, Bukkit.getOnlinePlayers().size()).send(SectorManager.getPublicChannel());
    }
}
