package pl.mikigal.bytesectors.commons.packet.playerinfo;

import pl.mikigal.bytesectors.commons.data.PlayerInfo;
import pl.mikigal.bytesectors.commons.packet.Packet;

public class PacketPlayerInfo extends Packet {
    private PlayerInfo playerInfo;

    public PacketPlayerInfo(PlayerInfo playerInfo) {
        super();
        this.playerInfo = playerInfo;
    }

    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }
}
