package pl.mikigal.bytesectors.commons.packet.synchronization;

import pl.mikigal.bytesectors.commons.packet.Packet;

public class PacketTimeSynchronizationRequest extends Packet {

    public PacketTimeSynchronizationRequest(String sender) {
        super(sender);
    }
}
