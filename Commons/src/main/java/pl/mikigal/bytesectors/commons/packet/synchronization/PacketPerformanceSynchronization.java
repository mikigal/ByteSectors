package pl.mikigal.bytesectors.commons.packet.synchronization;

import pl.mikigal.bytesectors.commons.packet.Packet;

public class PacketPerformanceSynchronization extends Packet {

    private String performance;
    private int online;

    public PacketPerformanceSynchronization(String performance, int online) {
        super();
        this.performance = performance;
        this.online = online;
    }

    public String getPerformance() {
        return performance;
    }

    public int getOnline() {
        return online;
    }
}
