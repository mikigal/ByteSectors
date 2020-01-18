package pl.mikigal.bytesectors.client;

import pl.mikigal.bytesectors.commons.configuration.ConfigEntry;

@ConfigEntry
public class Configuration {

    private static String sectorId;
    private static String redisHost;
    private static int redisPort;
    private static String redisPassword;

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
}
