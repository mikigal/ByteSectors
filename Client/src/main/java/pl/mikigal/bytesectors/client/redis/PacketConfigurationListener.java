package pl.mikigal.bytesectors.client.redis;

import pl.mikigal.bytesectors.client.Configuration;
import pl.mikigal.bytesectors.client.synchronization.SystemConfigurationSynchronization;
import pl.mikigal.bytesectors.client.utils.Utils;
import pl.mikigal.bytesectors.commons.data.SectorManager;
import pl.mikigal.bytesectors.commons.packet.configuration.PacketConfiguration;
import pl.mikigal.bytesectors.commons.redis.RedisListener;

public class PacketConfigurationListener extends RedisListener<PacketConfiguration> {

    public PacketConfigurationListener() {
        super(Configuration.getSectorId(), PacketConfiguration.class);
    }

    @Override
    public void onMessage(PacketConfiguration packet) {
        SectorManager.loadSectors(packet.getSectors());
        SystemConfigurationSynchronization.getConfigurationWaitLatch().countDown();
        Utils.log("Received configuration from System! Loaded &4" + packet.getSectors().length + "&c sectors!");
    }
}
