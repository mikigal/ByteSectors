package pl.mikigal.bytesectors.client.synchronization;

import pl.mikigal.bytesectors.client.Configuration;
import pl.mikigal.bytesectors.client.utils.PerformanceUtils;
import pl.mikigal.bytesectors.commons.data.SectorManager;
import pl.mikigal.bytesectors.commons.packet.synchronization.PacketPerformanceSynchronization;

public class ClientPerformanceSynchronization implements Runnable {

    @Override
    public void run() {
        String performance = PerformanceUtils.getTps();
        SectorManager.getSector(Configuration.getSectorId()).setPerformance(performance);
        new PacketPerformanceSynchronization(Configuration.getSectorId(), performance).send(SectorManager.getClientChannel());
    }
}
