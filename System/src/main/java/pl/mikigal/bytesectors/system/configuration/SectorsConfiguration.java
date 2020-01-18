package pl.mikigal.bytesectors.system.configuration;

import net.md_5.bungee.config.Configuration;
import pl.mikigal.bytesectors.commons.configuration.ConfigEntry;
import pl.mikigal.bytesectors.commons.data.SectorManager;

@ConfigEntry
public class SectorsConfiguration {

    private static String redisHost;
    private static int redisPort;
    private static String redisPassword;

    private static void load(Configuration config) {
        for (String key : config.getSection("sectors").getKeys()) {
            net.md_5.bungee.config.Configuration sectorSection = config.getSection("sectors." + key);
            SectorManager.createSector(key, sectorSection.getInt("min_x"), sectorSection.getInt("max_x"), sectorSection.getInt("min_z"), sectorSection.getInt("max_z"), sectorSection.getString("world"));
        }
    }

    public static String getRedisHost() {
        return redisHost;
    }

    public static int getRedisPort() {
        return redisPort;
    }

    public static String getRedisPassword() {
        return redisPassword;
    }
}
