package pl.mikigal.bytesectors.system;

import net.md_5.bungee.api.plugin.Plugin;
import pl.mikigal.bytesectors.commons.ByteSectorsCommons;
import pl.mikigal.bytesectors.commons.configuration.ConfigAPI;
import pl.mikigal.bytesectors.commons.data.SectorManager;
import pl.mikigal.bytesectors.commons.redis.RedisUtils;
import pl.mikigal.bytesectors.system.configuration.ConfigurationManager;
import pl.mikigal.bytesectors.system.configuration.SectorsConfiguration;
import pl.mikigal.bytesectors.system.redis.PacketConfigurationRequestListener;
import pl.mikigal.bytesectors.system.redis.PacketTimeSynchronizationRequestListener;
import pl.mikigal.bytesectors.system.redis.PacketWeatherSynchronizationRequestListener;
import pl.mikigal.bytesectors.system.synchronization.ClientTimeSynchronization;
import pl.mikigal.bytesectors.system.synchronization.ClientWeatherSynchronization;
import pl.mikigal.bytesectors.system.utils.Utils;

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

        Utils.log("Connecting do Redis...");
        this.commons = new ByteSectorsCommons("localhost", 6379, "zaq1@WSX");

        Utils.log("Subscribing Redis channels...");
        RedisUtils.subscribe(SectorManager.getProxyChannel(), new PacketConfigurationRequestListener());
        RedisUtils.subscribe(SectorManager.getProxyChannel(), new PacketTimeSynchronizationRequestListener());
        RedisUtils.subscribe(SectorManager.getProxyChannel(), new PacketWeatherSynchronizationRequestListener());

        Utils.log("Registering synchronization tasks...");
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
