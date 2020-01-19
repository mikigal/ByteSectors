package pl.mikigal.bytesectors.system.configuration;

import net.md_5.bungee.config.Configuration;
import pl.mikigal.bytesectors.commons.configuration.ConfigEntry;
import pl.mikigal.bytesectors.commons.data.SectorManager;

@ConfigEntry
public class SectorsConfiguration {

    private static String redisHost;
    private static int redisPort;
    private static String redisPassword;

    private static int nearBorderTerrainModifyBlockDistance;

    private static String joinSectorOfflineMessage;
    private static String outOfBorderMessage;
    private static String sectorOfflineMessage;
    private static String nearBorderTerrainModifyMessage;

    private static String nearBorderActionBar;
    private static String nearSectorActionBar;

    private static void load(Configuration config) {
        for (String key : config.getSection("sectors").getKeys()) {
            Configuration sectorSection = config.getSection("sectors." + key);
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

    public static String getOutOfBorderMessage() {
        return outOfBorderMessage;
    }

    public static String getSectorOfflineMessage() {
        return sectorOfflineMessage;
    }

    public static String getNearBorderActionBar() {
        return nearBorderActionBar;
    }

    public static String getNearSectorActionBar() {
        return nearSectorActionBar;
    }

    public static int getNearBorderTerrainModifyBlockDistance() {
        return nearBorderTerrainModifyBlockDistance;
    }

    public static String getNearBorderTerrainModifyMessage() {
        return nearBorderTerrainModifyMessage;
    }

    public static String getJoinSectorOfflineMessage() {
        return joinSectorOfflineMessage;
    }
}
