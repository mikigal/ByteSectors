package pl.mikigal.bytesectors.commons.packet.synchronization;

import pl.mikigal.bytesectors.commons.packet.Packet;

public class PacketWeatherSynchronization extends Packet {

    private boolean clear;
    private boolean thundering;

    public PacketWeatherSynchronization(String sender, boolean clear, boolean thundering) {
        super(sender);
        this.clear = clear;
        this.thundering = thundering;
    }

    public boolean isClear() {
        return clear;
    }

    public boolean isThundering() {
        return thundering;
    }
}
