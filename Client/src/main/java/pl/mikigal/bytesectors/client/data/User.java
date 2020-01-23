package pl.mikigal.bytesectors.client.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

public class User {

    private UUID uniqueId;
    private Location lastLocation;
    private long nextSectorChange;

    public User(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.lastLocation = Bukkit.getPlayer(uniqueId).getLocation();
        this.nextSectorChange = -1;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public long getNextSectorChange() {
        return nextSectorChange;
    }

    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    public void setNextSectorChange(long nextSectorChange) {
        this.nextSectorChange = nextSectorChange;
    }
}
