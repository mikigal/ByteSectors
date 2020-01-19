package pl.mikigal.bytesectors.client;

import pl.mikigal.bytesectors.commons.configuration.ConfigEntry;
import pl.mikigal.bytesectors.commons.configuration.ConfigExclude;

@ConfigEntry
public class Configuration {

    private static String sectorId;

    private static String redisHost;
    private static int redisPort;
    private static String redisPassword;

    @ConfigExclude
    private static int nearBorderTerrainModifyBlockDistance;

    @ConfigExclude
    private static String outOfBorderMessage;

    @ConfigExclude
    private static String sectorOfflineMessage;

    @ConfigExclude
    private static String nearBorderActionBar;

    @ConfigExclude
    private static String nearSectorActionBar;

    @ConfigExclude
    private static String nearBorderTerrainModifyMessage;

    public static String getSectorId() {
        return sectorId;
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

    public static void setOutOfBorderMessage(String outOfBorderMessage) {
        Configuration.outOfBorderMessage = outOfBorderMessage;
    }

    public static void setSectorOfflineMessage(String sectorOfflineMessage) {
        Configuration.sectorOfflineMessage = sectorOfflineMessage;
    }

    public static void setNearBorderActionBar(String nearBorderActionBar) {
        Configuration.nearBorderActionBar = nearBorderActionBar;
    }

    public static void setNearSectorActionBar(String nearSectorActionBar) {
        Configuration.nearSectorActionBar = nearSectorActionBar;
    }

    public static int getNearBorderTerrainModifyBlockDistance() {
        return nearBorderTerrainModifyBlockDistance;
    }

    public static void setNearBorderTerrainModifyBlockDistance(int nearBorderTerrainModifyBlockDistance) {
        Configuration.nearBorderTerrainModifyBlockDistance = nearBorderTerrainModifyBlockDistance;
    }

    public static String getNearBorderTerrainModifyMessage() {
        return nearBorderTerrainModifyMessage;
    }

    public static void setNearBorderTerrainModifyMessage(String nearBorderTerrainModifyMessage) {
        Configuration.nearBorderTerrainModifyMessage = nearBorderTerrainModifyMessage;
    }
}
