package pl.mikigal.bytesectors.system;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import net.md_5.bungee.api.plugin.Plugin;
import pl.mikigal.bytesectors.commons.ByteSectorsCommons;
import pl.mikigal.bytesectors.commons.data.SectorManager;
import pl.mikigal.bytesectors.commons.packet.Packet;
import pl.mikigal.bytesectors.commons.redis.RedisUtils;
import pl.mikigal.bytesectors.system.redis.PacketConfigurationRequestListener;
import pl.mikigal.bytesectors.system.redis.PacketTimeSynchronizationRequestListener;
import pl.mikigal.bytesectors.system.redis.PacketWeatherSynchronizationRequestListener;
import pl.mikigal.bytesectors.system.synchronization.ClientTimeSynchronization;
import pl.mikigal.bytesectors.system.synchronization.ClientWeatherSynchronization;

import java.util.concurrent.TimeUnit;

public class ByteSectorsSystem extends Plugin {

    private static ByteSectorsSystem instance;
    private ByteSectorsCommons commons;

    private ClientTimeSynchronization timeSynchronization;
    private ClientWeatherSynchronization weatherSynchronization;

    @Override
    public void onEnable() {
        instance = this;
        this.commons = new ByteSectorsCommons("localhost", 6379, "zaq1@WSX");

        ConfigurationManager.loadConfig();

        RedisUtils.subscribe(SectorManager.getProxyChannel(), new PacketConfigurationRequestListener());
        RedisUtils.subscribe(SectorManager.getProxyChannel(), new PacketTimeSynchronizationRequestListener());
        RedisUtils.subscribe(SectorManager.getProxyChannel(), new PacketWeatherSynchronizationRequestListener());

        this.timeSynchronization = new ClientTimeSynchronization();
        this.weatherSynchronization = new ClientWeatherSynchronization();
        this.getProxy().getScheduler().schedule(this, this.timeSynchronization, 50, TimeUnit.SECONDS);
        this.getProxy().getScheduler().schedule(this, this.weatherSynchronization, 10, TimeUnit.MINUTES);
    }

    @Override
    public void onDisable() {
        this.commons.closeConnections();
    }

    public static ByteSectorsSystem getInstance() {
        return instance;
    }

    public ClientTimeSynchronization getTimeSynchronization() {
        return timeSynchronization;
    }

    public ClientWeatherSynchronization getWeatherSynchronization() {
        return weatherSynchronization;
    }
}
