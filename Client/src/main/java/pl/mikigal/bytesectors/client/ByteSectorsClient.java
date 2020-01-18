package pl.mikigal.bytesectors.client;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import org.bukkit.plugin.java.JavaPlugin;
import pl.mikigal.bytesectors.client.listener.PlayerJoinListener;
import pl.mikigal.bytesectors.client.listener.PlayerMoveListener;
import pl.mikigal.bytesectors.client.redis.PacketConfigurationListener;
import pl.mikigal.bytesectors.client.redis.PacketPlayerTransferListener;
import pl.mikigal.bytesectors.client.redis.synchronization.PacketPerformanceSynchronizationListener;
import pl.mikigal.bytesectors.client.redis.synchronization.PacketPerformanceSynchronizationRequestListener;
import pl.mikigal.bytesectors.client.redis.synchronization.PacketTimeSynchronizationListener;
import pl.mikigal.bytesectors.client.redis.synchronization.PacketWeatherSynchronizationListener;
import pl.mikigal.bytesectors.client.synchronization.SystemConfigurationSynchronization;
import pl.mikigal.bytesectors.client.synchronization.ClientPerformanceOfflineSynchronization;
import pl.mikigal.bytesectors.client.synchronization.ClientPerformanceSynchronization;
import pl.mikigal.bytesectors.client.task.SectorBorderParticleTask;
import pl.mikigal.bytesectors.client.utils.RegisterUtils;
import pl.mikigal.bytesectors.client.utils.Utils;
import pl.mikigal.bytesectors.commons.ByteSectorsCommons;
import pl.mikigal.bytesectors.commons.data.SectorManager;
import pl.mikigal.bytesectors.commons.packet.Packet;
import pl.mikigal.bytesectors.commons.packet.configuration.PacketConfigurationRequest;
import pl.mikigal.bytesectors.commons.packet.synchronization.PacketPerformanceSynchronizationRequest;
import pl.mikigal.bytesectors.commons.packet.synchronization.PacketTimeSynchronizationRequest;
import pl.mikigal.bytesectors.commons.packet.synchronization.PacketWeatherSynchronizationRequest;
import pl.mikigal.bytesectors.commons.redis.RedisUtils;

public class ByteSectorsClient extends JavaPlugin {

    private static ByteSectorsClient instance;
    private ByteSectorsCommons commons;

    @Override
    public void onEnable() {
        instance = this;
        commons = new ByteSectorsCommons("127.0.0.1", 6379, "zaq1@WSX");

        this.saveDefaultConfig();
        Configuration.load();


        RedisUtils.subscribe(Configuration.getSectorId(), new PacketConfigurationListener());
        RedisUtils.subscribe(SectorManager.getClientChannel(), new PacketTimeSynchronizationListener());
        RedisUtils.subscribe(SectorManager.getClientChannel(), new PacketWeatherSynchronizationListener());
        RedisUtils.subscribe(SectorManager.getClientChannel(), new PacketPerformanceSynchronizationListener());
        RedisUtils.subscribe(SectorManager.getClientChannel(), new PacketPerformanceSynchronizationRequestListener());
        RedisUtils.subscribe(SectorManager.getClientChannel(), new PacketPlayerTransferListener());

        RedisUtils.publish(SectorManager.getProxyChannel(), new PacketConfigurationRequest(Configuration.getSectorId()));
        RedisUtils.publish(SectorManager.getProxyChannel(), new PacketTimeSynchronizationRequest(Configuration.getSectorId()));
        RedisUtils.publish(SectorManager.getProxyChannel(), new PacketWeatherSynchronizationRequest(Configuration.getSectorId()));
        RedisUtils.publish(SectorManager.getClientChannel(), new PacketPerformanceSynchronizationRequest(Configuration.getSectorId()));

        if (!SystemConfigurationSynchronization.waitForConfiguration()) {
            return;
        }

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        RegisterUtils.registerListeners(new PlayerMoveListener(), new PlayerJoinListener());


        this.getServer().getScheduler().runTaskTimerAsynchronously(this, new ClientPerformanceSynchronization(), 60, 1200);
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, new ClientPerformanceOfflineSynchronization(), 60, 60);
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, new SectorBorderParticleTask(), 3, 3);

        Utils.log("Done!");
    }

    @Override
    public void onDisable() {
        this.commons.closeConnections();
    }

    public static ByteSectorsClient getInstance() {
        return instance;
    }
}
