package pl.mikigal.bytesectors.system.configuration;

import net.md_5.bungee.config.Configuration;
import pl.mikigal.bytesectors.commons.configuration.ConfigEntry;
import pl.mikigal.bytesectors.commons.configuration.ConfigExclude;
import pl.mikigal.bytesectors.commons.data.Sector;
import pl.mikigal.bytesectors.commons.data.SectorManager;
import pl.mikigal.bytesectors.commons.serializable.ItemBuilderSerializable;
import pl.mikigal.bytesectors.system.util.Utils;

@ConfigEntry
public class SectorsConfiguration {

    private static String redisHost;
    private static int redisPort;
    private static String redisPassword;

    @ConfigExclude
    private static String sectorsGuiName;

    @ConfigExclude
    private static ItemBuilderSerializable onlineItem;

    @ConfigExclude
    private static ItemBuilderSerializable offlineItem;

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
            SectorManager.createSector(key,
                    sectorSection.getInt("min_x"),
                    sectorSection.getInt("max_x"),
                    sectorSection.getInt("min_z"),
                    sectorSection.getInt("max_z"),
                    sectorSection.getString("world"),
                    sectorSection.getBoolean("default"));
        }

        if (SectorManager.getSectors().stream().filter(Sector::isDefaultSector).count() != 1){
            Utils.log("&4Invalid configuration, ONE sector must be default! Stopping server...");
            System.exit(0);
        }

        Configuration sectorsGui = config.getSection("sectorsGui");
        sectorsGuiName = sectorsGui.getString("guiName");
        onlineItem = parseItemStack(sectorsGui.getSection("online"));
        offlineItem = parseItemStack(sectorsGui.getSection("offline"));
    }

    private static ItemBuilderSerializable parseItemStack(Configuration section) {
        return new ItemBuilderSerializable(section.getString("material"),
                section.getInt("amount"),
                section.getShort("durability"),
                section.getString("name"),
                section.getStringList("lore").toArray(new String[0]));
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

    public static String getSectorsGuiName() {
        return sectorsGuiName;
    }

    public static ItemBuilderSerializable getOnlineItem() {
        return onlineItem;
    }

    public static ItemBuilderSerializable getOfflineItem() {
        return offlineItem;
    }
}
