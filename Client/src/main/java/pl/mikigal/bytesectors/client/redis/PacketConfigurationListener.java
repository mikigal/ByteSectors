package pl.mikigal.bytesectors.client.redis;

import pl.mikigal.bytesectors.client.Configuration;
import pl.mikigal.bytesectors.client.synchronization.SystemConfigurationSynchronization;
import pl.mikigal.bytesectors.client.util.SerializationUtils;
import pl.mikigal.bytesectors.client.util.Utils;
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

        Configuration.setOutOfBorderMessage(Utils.fixColors(packet.getOutOfBorderMessage()));
        Configuration.setSectorOfflineMessage(Utils.fixColors(packet.getSectorOfflineMessage()));
        Configuration.setNearBorderActionBar(Utils.fixColors(packet.getNearBorderActionBar()));
        Configuration.setNearSectorActionBar(Utils.fixColors(packet.getNearSectorActionBar()));
        Configuration.setNearBorderTerrainModifyBlockDistance(packet.getNearBorderTerrainModifyBlockDistance());
        Configuration.setNearBorderTerrainModifyMessage(packet.getNearBorderTerrainModifyMessage());
        Configuration.setSectorsGuiName(packet.getSectorsGuiName());
        Configuration.setOnlineItem(SerializationUtils.deserializeIemBuilder(packet.getOnlineItem()));
        Configuration.setOfflineItem(SerializationUtils.deserializeIemBuilder(packet.getOfflineItem()));

        Utils.log("Received configuration from System! Loaded &4" + packet.getSectors().length + "&c sectors!");
    }
}
