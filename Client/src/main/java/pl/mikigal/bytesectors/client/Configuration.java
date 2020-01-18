package pl.mikigal.bytesectors.client;

import org.bukkit.configuration.file.FileConfiguration;

public class Configuration {

    private static String sectorId;

    public static void load() {
        FileConfiguration configuration = ByteSectorsClient.getInstance().getConfig();
        sectorId = configuration.getString("sector_id");
    }

    public static String getSectorId() {
        return sectorId;
    }
}
