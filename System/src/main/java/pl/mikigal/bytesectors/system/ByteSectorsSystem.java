package pl.mikigal.bytesectors.system;

import net.md_5.bungee.api.plugin.Plugin;
import pl.mikigal.bytesectors.commons.ByteSectorsCommons;
import pl.mikigal.bytesectors.commons.configuration.ConfigAPI;
import pl.mikigal.bytesectors.commons.data.SectorManager;
import pl.mikigal.bytesectors.commons.packet.synchronization.PacketPerformanceSynchronizationRequest;
import pl.mikigal.bytesectors.commons.redis.RedisUtils;
import pl.mikigal.bytesectors.system.configuration.ConfigurationManager;
import pl.mikigal.bytesectors.system.configuration.SectorsConfiguration;
import pl.mikigal.bytesectors.system.listener.PlayerLoginListener;
import pl.mikigal.bytesectors.system.listener.ServerKickListener;
import pl.mikigal.bytesectors.system.redis.ConfigurationRequestListener;
import pl.mikigal.bytesectors.system.redis.PerformanceSyncListener;
import pl.mikigal.bytesectors.system.redis.TimeSyncRequestListener;
import pl.mikigal.bytesectors.system.redis.WeatherSyncRequestListener;
import pl.mikigal.bytesectors.system.synchronization.ClientTimeSynchronization;
import pl.mikigal.bytesectors.system.synchronization.ClientWeatherSynchronization;
import pl.mikigal.bytesectors.system.util.Utils;

import java.util.concurrent.TimeUnit;

public class ByteSectorsSystem extends Plugin {

    private static ByteSectorsSystem instance;
    private ByteSectorsCommons commons;

    private ClientTimeSynchronization timeSynchronization;
    private ClientWeatherSynchronization weatherSynchronization;

    @Override
    public void onEnable() {
        instance = this;

        Utils.log("Loading configuration...");
        ConfigAPI.load(ConfigurationManager.initConfig(), SectorsConfiguration.class);
        SectorManager.setCurrentSector(SectorManager.getSystemChannel());

        Utils.log("Connecting do Redis...");
        this.commons = new ByteSectorsCommons(SectorsConfiguration.getRedisHost(), SectorsConfiguration.getRedisPort(), SectorsConfiguration.getRedisPassword());

        Utils.log("Subscribing Redis channels...");
        RedisUtils.subscribe(SectorManager.getSystemChannel(), new ConfigurationRequestListener());
        RedisUtils.subscribe(SectorManager.getSystemChannel(), new TimeSyncRequestListener());
        RedisUtils.subscribe(SectorManager.getSystemChannel(), new WeatherSyncRequestListener());
        RedisUtils.subscribe(SectorManager.getPublicChannel(), new PerformanceSyncListener());

        Utils.log("Publishing request for sectors synchronization...");
        RedisUtils.publish(SectorManager.getClientChannel(), new PacketPerformanceSynchronizationRequest());

        Utils.log("Registering listeners...");
        this.getProxy().getPluginManager().registerListener(this, new PlayerLoginListener());
        this.getProxy().getPluginManager().registerListener(this, new ServerKickListener());

        Utils.log("Registering synchronization tasks...");
        this.timeSynchronization = new ClientTimeSynchronization();
        this.weatherSynchronization = new ClientWeatherSynchronization();
        this.getProxy().getScheduler().schedule(this, this.timeSynchronization, 1, 50, TimeUnit.SECONDS);
        this.getProxy().getScheduler().schedule(this, this.weatherSynchronization, 1, 10, TimeUnit.MINUTES);
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
