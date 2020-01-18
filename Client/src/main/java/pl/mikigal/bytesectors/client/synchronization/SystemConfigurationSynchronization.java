package pl.mikigal.bytesectors.client.synchronization;

import org.bukkit.Bukkit;
import pl.mikigal.bytesectors.client.ByteSectorsClient;
import pl.mikigal.bytesectors.client.utils.Utils;
import pl.mikigal.bytesectors.commons.data.SectorManager;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SystemConfigurationSynchronization {

    private static CountDownLatch configurationWaitLatch = new CountDownLatch(1);

    public static boolean waitForConfiguration() {
        try {
            Utils.log("Waiting for sectors configuration from System");
           configurationWaitLatch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Utils.log(e);
        }

        if (SectorManager.getSectors().size() == 0) {
            Utils.log("&4Client didn't received configuration from System! Stopping server...");
            Bukkit.getServer().shutdown();
            return false;
        }

        return true;
    }

    public static CountDownLatch getConfigurationWaitLatch() {
        return configurationWaitLatch;
    }
}
