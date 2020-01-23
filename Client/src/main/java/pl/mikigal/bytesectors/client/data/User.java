package pl.mikigal.bytesectors.client.data;

import java.util.UUID;

public class User {

    private UUID uniqueId;
    private long nextSectorChange;

    public User(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.nextSectorChange = -1;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public long getNextSectorChange() {
        return nextSectorChange;
    }

    public void setNextSectorChange(long nextSectorChange) {
        this.nextSectorChange = nextSectorChange;
    }
}
