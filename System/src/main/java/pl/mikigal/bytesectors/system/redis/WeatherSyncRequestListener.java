package pl.mikigal.bytesectors.system.redis;

import pl.mikigal.bytesectors.commons.data.SectorManager;
import pl.mikigal.bytesectors.commons.packet.synchronization.PacketWeatherSynchronization;
import pl.mikigal.bytesectors.commons.packet.synchronization.PacketWeatherSynchronizationRequest;
import pl.mikigal.bytesectors.commons.redis.RedisListener;
import pl.mikigal.bytesectors.system.ByteSectorsSystem;

public class WeatherSyncRequestListener extends RedisListener<PacketWeatherSynchronizationRequest> {

    public WeatherSyncRequestListener() {
        super(SectorManager.getSystemChannel(), PacketWeatherSynchronizationRequest.class);
    }

    @Override
    public void onMessage(PacketWeatherSynchronizationRequest packet) {
        packet.sendResponse(new PacketWeatherSynchronization(
                ByteSectorsSystem.getInstance().getWeatherSynchronization().isClear(),
                ByteSectorsSystem.getInstance().getWeatherSynchronization().isThundering()));
    }
}
