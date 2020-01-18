package pl.mikigal.bytesectors.commons.packet.synchronization;

import pl.mikigal.bytesectors.commons.packet.Packet;

public class PacketTimeSynchronization extends Packet {

    private long ticks;

    public PacketTimeSynchronization(String sender, long ticks) {
        super(sender);
        this.ticks = ticks;
    }

    public long getTicks() {
        return ticks;
    }
}
