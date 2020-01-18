package pl.mikigal.bytesectors.system.synchronization;

import pl.mikigal.bytesectors.commons.data.SectorManager;
import pl.mikigal.bytesectors.commons.packet.synchronization.PacketWeatherSynchronization;

import java.util.concurrent.ThreadLocalRandom;

public class ClientWeatherSynchronization implements Runnable {

    private boolean clear;
    private boolean thundering;

    @Override
    public void run() {
        this.clear = ThreadLocalRandom.current().nextBoolean();
        this.thundering = ThreadLocalRandom.current().nextBoolean();

        new PacketWeatherSynchronization(SectorManager.getClientChannel(), this.clear, this.thundering).send(SectorManager.getClientChannel());
    }

    public boolean isClear() {
        return clear;
    }

    public boolean isThundering() {
        return thundering;
    }
}
