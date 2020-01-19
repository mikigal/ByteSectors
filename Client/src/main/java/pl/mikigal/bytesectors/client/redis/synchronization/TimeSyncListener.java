package pl.mikigal.bytesectors.client.redis.synchronization;

import org.bukkit.Bukkit;
import org.bukkit.World;
import pl.mikigal.bytesectors.commons.data.SectorManager;
import pl.mikigal.bytesectors.commons.packet.synchronization.PacketTimeSynchronization;
import pl.mikigal.bytesectors.commons.redis.RedisListener;

public class TimeSyncListener extends RedisListener<PacketTimeSynchronization> {

    public TimeSyncListener() {
        super(SectorManager.getClientChannel(), PacketTimeSynchronization.class);
    }

    @Override
    public void onMessage(PacketTimeSynchronization packet) {
        for (World world : Bukkit.getWorlds()) {
            world.setFullTime(packet.getTicks());
        }
    }
}
