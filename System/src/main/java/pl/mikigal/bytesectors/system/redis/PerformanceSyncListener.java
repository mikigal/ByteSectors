package pl.mikigal.bytesectors.system.redis;

import pl.mikigal.bytesectors.commons.data.SectorManager;
import pl.mikigal.bytesectors.commons.packet.synchronization.PacketPerformanceSynchronization;
import pl.mikigal.bytesectors.commons.redis.RedisListener;

public class PerformanceSyncListener extends RedisListener<PacketPerformanceSynchronization> {

    public PerformanceSyncListener() {
        super(SectorManager.getPublicChannel(), PacketPerformanceSynchronization.class);
    }

    @Override
    public void onMessage(PacketPerformanceSynchronization packet) {
        SectorManager.getSector(packet.getSender()).setPerformance(packet.getPerformance());
        SectorManager.getSector(packet.getSender()).setOnline(packet.getOnline());
    }
}
