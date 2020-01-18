package pl.mikigal.bytesectors.commons.packet.synchronization;

import pl.mikigal.bytesectors.commons.packet.Packet;

public class PacketPerformanceSynchronization extends Packet {

    private String performance;

    public PacketPerformanceSynchronization(String sender, String performance) {
        super(sender);
        this.performance = performance;
    }

    public String getPerformance() {
        return performance;
    }
}
