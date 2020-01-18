package pl.mikigal.bytesectors.system.redis;

import pl.mikigal.bytesectors.commons.data.SectorManager;
import pl.mikigal.bytesectors.commons.packet.synchronization.PacketTimeSynchronization;
import pl.mikigal.bytesectors.commons.packet.synchronization.PacketTimeSynchronizationRequest;
import pl.mikigal.bytesectors.commons.redis.RedisListener;
import pl.mikigal.bytesectors.system.ByteSectorsSystem;

public class PacketTimeSynchronizationRequestListener extends RedisListener<PacketTimeSynchronizationRequest> {

    public PacketTimeSynchronizationRequestListener() {
        super(SectorManager.getProxyChannel(), PacketTimeSynchronizationRequest.class);
    }

    @Override
    public void onMessage(PacketTimeSynchronizationRequest packet) {
        packet.sendResponse(new PacketTimeSynchronization(SectorManager.getProxyChannel(), ByteSectorsSystem.getInstance().getTimeSynchronization().getTicks()));
    }
}
