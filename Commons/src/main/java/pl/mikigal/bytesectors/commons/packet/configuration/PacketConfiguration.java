package pl.mikigal.bytesectors.commons.packet.configuration;

import pl.mikigal.bytesectors.commons.data.Sector;
import pl.mikigal.bytesectors.commons.packet.Packet;

public class PacketConfiguration extends Packet {

    private Sector[] sectors;

    public PacketConfiguration(String sender, Sector[] sectors) {
        super(sender);
        this.sectors = sectors;
    }

    public Sector[] getSectors() {
        return sectors;
    }
}
