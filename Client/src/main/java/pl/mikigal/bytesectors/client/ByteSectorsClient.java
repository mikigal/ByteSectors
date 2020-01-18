package pl.mikigal.bytesectors.client;

import org.bukkit.plugin.java.JavaPlugin;
import pl.mikigal.bytesectors.commons.configuration.ConfigAPI;
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
import pl.mikigal.bytesectors.client.task.SectorBorderMessageTask;
import pl.mikigal.bytesectors.client.task.SectorBorderParticleTask;
import pl.mikigal.bytesectors.client.utils.RegisterUtils;
import pl.mikigal.bytesectors.client.utils.Utils;
import pl.mikigal.bytesectors.commons.ByteSectorsCommons;
import pl.mikigal.bytesectors.commons.data.SectorManager;
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

        Utils.log("Loading configuration...");
        this.saveDefaultConfig();
        ConfigAPI.load(this.getConfig(), Configuration.class);

        Utils.log("Connecting to Redis...");
        commons = new ByteSectorsCommons(Configuration.getRedisHost(), Configuration.getRedisPort(), Configuration.getRedisPassword());

        Utils.log("Subscribing Redis channels...");
        RedisUtils.subscribe(Configuration.getSectorId(), new PacketConfigurationListener());
        RedisUtils.subscribe(SectorManager.getClientChannel(), new PacketTimeSynchronizationListener());
        RedisUtils.subscribe(SectorManager.getClientChannel(), new PacketWeatherSynchronizationListener());
        RedisUtils.subscribe(SectorManager.getClientChannel(), new PacketPerformanceSynchronizationListener());
        RedisUtils.subscribe(SectorManager.getClientChannel(), new PacketPerformanceSynchronizationRequestListener());
        RedisUtils.subscribe(SectorManager.getClientChannel(), new PacketPlayerTransferListener());

        Utils.log("Publishing request for sectors configuration and synchronization...");
        RedisUtils.publish(SectorManager.getProxyChannel(), new PacketConfigurationRequest(Configuration.getSectorId()));
        RedisUtils.publish(SectorManager.getProxyChannel(), new PacketTimeSynchronizationRequest(Configuration.getSectorId()));
        RedisUtils.publish(SectorManager.getProxyChannel(), new PacketWeatherSynchronizationRequest(Configuration.getSectorId()));
        RedisUtils.publish(SectorManager.getClientChannel(), new PacketPerformanceSynchronizationRequest(Configuration.getSectorId()));

        if (!SystemConfigurationSynchronization.waitForConfiguration()) {
            return;
        }

        Utils.log("Registering outgoing BungeeCord channel...");
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        Utils.log("Registering listeners...");
        RegisterUtils.registerListeners(new PlayerMoveListener(), new PlayerJoinListener());

        Utils.log("Registering synchronization tasks...");
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, new ClientPerformanceSynchronization(), 60, 60);
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, new ClientPerformanceOfflineSynchronization(), 60, 60);

        Utils.log("Registering other tasks...");
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, new SectorBorderParticleTask(), 3, 3);
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, new SectorBorderMessageTask(), 10, 10);

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
