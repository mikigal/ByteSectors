package pl.mikigal.bytesectors.client.data;

import org.bukkit.Location;

public enum Direction {
    NORTH, SOUTH, EAST, WEST, UP_DOWN;

    public Location add(Location base) {
        if (this == NORTH) {
            return base.clone().add(0, 2, -5);
        }
        if (this == SOUTH) {
            return base.clone().add(0, 2, 5);
        }
        if (this == WEST) {
            return base.clone().add(-5, 2, 0);
        }
        if (this == EAST) {
            return base.clone().add(5, 2, 0);
        }

        return base;
    }
}
