package pl.mikigal.bytesectors.client.redis.synchronization;

import org.bukkit.Bukkit;
import org.bukkit.World;
import pl.mikigal.bytesectors.commons.data.SectorManager;
import pl.mikigal.bytesectors.commons.packet.synchronization.PacketWeatherSynchronization;
import pl.mikigal.bytesectors.commons.redis.RedisListener;

public class WeatherSyncListener extends RedisListener<PacketWeatherSynchronization> {

    public WeatherSyncListener() {
        super(SectorManager.getClientChannel(), PacketWeatherSynchronization.class);
    }

    @Override
    public void onMessage(PacketWeatherSynchronization packet) {
        for (World world : Bukkit.getWorlds()) {
            world.setWeatherDuration(12500);
            world.setStorm(!packet.isClear());
            world.setThundering(packet.isThundering());
        }
    }
}
