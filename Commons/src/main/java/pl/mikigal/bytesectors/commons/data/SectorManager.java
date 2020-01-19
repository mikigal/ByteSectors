package pl.mikigal.bytesectors.commons.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SectorManager {

    private static Map<String, Sector> sectors = new HashMap<>();

    public static void createSector(String id, int minX, int maxX, int minZ, int maxZ, String world) {
        Sector sector = new Sector(id, minX, maxX, minZ, maxZ, world);
        sectors.put(id, sector);
    }

    public static void loadSectors(Sector[] sectorsArray) {
        for (Sector sector : sectorsArray) {
            sectors.put(sector.getId(), sector);
        }
    }

    public static Sector getSector(int x, int z, String world) {
        for (Sector sector : sectors.values()) {
            if (sector.isInSector(x, z, world)) {
                return sector;
            }
        }

        return null;
    }

    public static Sector getSector(String id) {
        return sectors.get(id);
    }

    public static Collection<Sector> getSectors() {
        return sectors.values();
    }


    public static String getPublicChannel() {
        return "public";
    }

    public static String getProxyChannel() {
        return "proxy";
    }

    public static String getClientChannel() {
        return "bukkit";
    }
}
