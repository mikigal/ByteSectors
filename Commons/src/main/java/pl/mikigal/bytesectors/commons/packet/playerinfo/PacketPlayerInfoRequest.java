package pl.mikigal.bytesectors.commons.packet.playerinfo;

import pl.mikigal.bytesectors.commons.packet.Packet;

public class PacketPlayerInfoRequest extends Packet {
    private String name;

    public PacketPlayerInfoRequest(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
