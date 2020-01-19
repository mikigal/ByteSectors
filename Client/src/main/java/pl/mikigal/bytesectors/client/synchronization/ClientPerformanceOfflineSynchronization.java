package pl.mikigal.bytesectors.client.synchronization;

import pl.mikigal.bytesectors.commons.data.Sector;
import pl.mikigal.bytesectors.commons.data.SectorManager;

public class ClientPerformanceOfflineSynchronization implements Runnable {

    @Override
    public void run() {
        for (Sector sector : SectorManager.getSectors()) {
            if (sector.getLastPerformancePacket() < System.currentTimeMillis() - 4000) {
                sector.setPerformance("OFFLINE");
            }
        }
    }
}
