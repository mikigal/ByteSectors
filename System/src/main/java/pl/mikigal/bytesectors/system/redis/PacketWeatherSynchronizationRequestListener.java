package pl.mikigal.bytesectors.system.redis;

import pl.mikigal.bytesectors.commons.data.SectorManager;
import pl.mikigal.bytesectors.commons.packet.synchronization.PacketWeatherSynchronization;
import pl.mikigal.bytesectors.commons.packet.synchronization.PacketWeatherSynchronizationRequest;
import pl.mikigal.bytesectors.commons.redis.RedisListener;
import pl.mikigal.bytesectors.system.ByteSectorsSystem;

public class PacketWeatherSynchronizationRequestListener extends RedisListener<PacketWeatherSynchronizationRequest> {

    public PacketWeatherSynchronizationRequestListener() {
        super(SectorManager.getProxyChannel(), PacketWeatherSynchronizationRequest.class);
    }

    @Override
    public void onMessage(PacketWeatherSynchronizationRequest packet) {
        packet.sendResponse(new PacketWeatherSynchronization(SectorManager.getProxyChannel(),
                ByteSectorsSystem.getInstance().getWeatherSynchronization().isClear(),
                ByteSectorsSystem.getInstance().getWeatherSynchronization().isThundering()));
    }
}
