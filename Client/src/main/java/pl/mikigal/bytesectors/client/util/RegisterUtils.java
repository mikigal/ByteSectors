package pl.mikigal.bytesectors.client.util;

import org.bukkit.event.Listener;
import pl.mikigal.bytesectors.client.ByteSectorsClient;

public class RegisterUtils {

    public static void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            ByteSectorsClient.getInstance().getServer().getPluginManager().registerEvents(listener, ByteSectorsClient.getInstance());
        }
    }

}
