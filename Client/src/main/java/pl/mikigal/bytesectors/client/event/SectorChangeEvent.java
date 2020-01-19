package pl.mikigal.bytesectors.client.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import pl.mikigal.bytesectors.commons.data.Sector;

public class SectorChangeEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private Sector currentSector;
    private Sector newSector;
    private boolean cancelled;

    public SectorChangeEvent(Player player, Sector currentSector, Sector newSector) {
        this.player = player;
        this.currentSector = currentSector;
        this.newSector = newSector;
        this.cancelled = false;
    }

    public Player getPlayer() {
        return player;
    }

    public Sector getCurrentSector() {
        return currentSector;
    }

    public Sector getNewSector() {
        return newSector;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
