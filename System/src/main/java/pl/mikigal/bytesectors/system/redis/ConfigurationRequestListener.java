package pl.mikigal.bytesectors.system.redis;

import pl.mikigal.bytesectors.commons.data.Sector;
import pl.mikigal.bytesectors.commons.data.SectorManager;
import pl.mikigal.bytesectors.commons.packet.configuration.PacketConfiguration;
import pl.mikigal.bytesectors.commons.packet.configuration.PacketConfigurationRequest;
import pl.mikigal.bytesectors.commons.redis.RedisListener;
import pl.mikigal.bytesectors.system.configuration.SectorsConfiguration;
import pl.mikigal.bytesectors.system.util.Utils;

public class ConfigurationRequestListener extends RedisListener<PacketConfigurationRequest> {

    public ConfigurationRequestListener() {
        super("proxy", PacketConfigurationRequest.class);
    }

    @Override
    public void onMessage(PacketConfigurationRequest packet) {
        PacketConfiguration response = new PacketConfiguration(
                SectorManager.getSectors().toArray(new Sector[0]),
                SectorsConfiguration.getNearBorderTerrainModifyBlockDistance(),
                SectorsConfiguration.getOutOfBorderMessage(),
                SectorsConfiguration.getSectorOfflineMessage(),
                SectorsConfiguration.getNearBorderActionBar(),
                SectorsConfiguration.getNearSectorActionBar(),
                SectorsConfiguration.getNearBorderTerrainModifyMessage(),
                SectorsConfiguration.getSectorsGuiName(),
                SectorsConfiguration.getOnlineItem(),
                SectorsConfiguration.getOfflineItem());

        response.sendResponse(packet);
        Utils.log("Received configuration request from sector &4" + packet.getSender() + "&c!");
    }
}
