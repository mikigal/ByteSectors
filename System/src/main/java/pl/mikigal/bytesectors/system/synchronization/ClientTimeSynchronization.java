package pl.mikigal.bytesectors.system.synchronization;

import pl.mikigal.bytesectors.commons.data.SectorManager;
import pl.mikigal.bytesectors.commons.packet.synchronization.PacketTimeSynchronization;

public class ClientTimeSynchronization implements Runnable {

    private long ticks;

    public ClientTimeSynchronization() {
        this.ticks = 0;
    }

    @Override
    public void run() {
        this.ticks += 1000;

        if (this.ticks > 24000) {
            this.ticks = 0;
        }

        new PacketTimeSynchronization(SectorManager.getProxyChannel(), this.ticks).send(SectorManager.getClientChannel());
    }

    public long getTicks() {
        return ticks;
    }
}
