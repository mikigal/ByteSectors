package pl.mikigal.bytesectors.system.redis;

import pl.mikigal.bytesectors.commons.data.Sector;
import pl.mikigal.bytesectors.commons.data.SectorManager;
import pl.mikigal.bytesectors.commons.packet.configuration.PacketConfiguration;
import pl.mikigal.bytesectors.commons.packet.configuration.PacketConfigurationRequest;
import pl.mikigal.bytesectors.commons.redis.RedisListener;
import pl.mikigal.bytesectors.system.utils.Utils;

public class PacketConfigurationRequestListener extends RedisListener<PacketConfigurationRequest> {

    public PacketConfigurationRequestListener() {
        super("proxy", PacketConfigurationRequest.class);
    }

    @Override
    public void onMessage(PacketConfigurationRequest packet) {
        PacketConfiguration response = new PacketConfiguration(SectorManager.getProxyChannel(), SectorManager.getSectors().toArray(new Sector[0]));
        response.sendResponse(packet);
        Utils.log("Received configuration request from sector &4" + packet.getSender() + "&c!");
    }
}
