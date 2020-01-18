package pl.mikigal.bytesectors.commons.serializable;

import java.io.Serializable;

public class LocationSerializable implements Serializable {
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private String world;

    public LocationSerializable(double x, double y, double z, float yaw, float pitch, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.world = world;
    }

    public LocationSerializable(double x, double y, double z, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public LocationSerializable(double x, double z, String world) {
        this.x = x;
        this.z = z;
        this.world = world;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public String getWorld() {
        return world;
    }
}
