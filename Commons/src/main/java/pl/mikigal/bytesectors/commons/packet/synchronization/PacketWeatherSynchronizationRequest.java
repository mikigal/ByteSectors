package pl.mikigal.bytesectors.commons.packet.synchronization;

import pl.mikigal.bytesectors.commons.packet.Packet;

public class PacketWeatherSynchronizationRequest extends Packet {

    public PacketWeatherSynchronizationRequest(String sender) {
        super(sender);
    }
}
