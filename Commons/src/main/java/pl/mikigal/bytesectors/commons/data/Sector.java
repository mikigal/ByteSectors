package pl.mikigal.bytesectors.commons.data;

import pl.mikigal.bytesectors.commons.serializable.LocationSerializable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sector implements Serializable {

    private String id;
    private LocationSerializable minimum;
    private LocationSerializable maximum;
    private boolean defaultSector;

    private String performance;
    private int online;
    private long lastPerformancePacket;

    public Sector(String id, int minX, int maxX, int minZ, int maxZ, String world, boolean defaultSector) {
        this.id = id;
        this.minimum = new LocationSerializable(minX, minZ, world);
        this.maximum = new LocationSerializable(maxX, maxZ, world);
        this.defaultSector = defaultSector;

        this.performance = "OFFLINE";
        this.online = 0;
        this.lastPerformancePacket = -1;
    }

    public String getId() {
        return id;
    }

    public LocationSerializable getMinimum() {
        return minimum;
    }

    public LocationSerializable getMaximum() {
        return maximum;
    }

    public boolean isInSector(int x, int z, String world) {
        if (!world.equals(minimum.getWorld())) {
            return false;
        }

        return x > minimum.getX() &&
                z > minimum.getZ() &&
                x <= maximum.getX() &&
                z <= maximum.getZ();
    }

    public int getDistanceToBorder(int x, int z) { // TODO: 18/01/2020 World 
        double x1 = Math.abs(x - this.minimum.getX());
        double x2 = Math.abs(x - this.maximum.getX());
        double z1 = Math.abs(z - this.minimum.getZ());
        double z2 = Math.abs(z - this.maximum.getZ());

        return (int) Math.min(Math.min(x1, x2), Math.min(z1, z2));
    }

    public Sector getNearestSector(int x, int z, String world) {
        return getNearestSector(this.getDistanceToBorder(x, z), x, z, world);
    }

    public Sector getNearestSector(int distance, int x, int z, String world) {
        int border = distance + 5;

        List<Sector> sectors = new ArrayList<>(Arrays.asList(
                SectorManager.getSector(x + border, z, world),
                SectorManager.getSector(x - border, z, world),
                SectorManager.getSector(x, z + border, world),
                SectorManager.getSector(x, z - border, world)));

        for (Sector sector : sectors) {
            if (!this.equals(sector)) {
                return sector;
            }
        }

        return null;
    }

    public String getPerformance() {
        return performance;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
        if (!performance.equals("OFFLINE")) {
            this.lastPerformancePacket = System.currentTimeMillis();
        }
    }

    public long getLastPerformancePacket() {
        return lastPerformancePacket;
    }

    public void setLastPerformancePacket(long lastPerformancePacket) {
        this.lastPerformancePacket = lastPerformancePacket;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public boolean isOffline() {
        return this.performance.equals("OFFLINE");
    }

    public boolean isDefaultSector() {
        return defaultSector;
    }
}
