package pl.mikigal.bytesectors.commons.packet.configuration;

import pl.mikigal.bytesectors.commons.data.Sector;
import pl.mikigal.bytesectors.commons.packet.Packet;

public class PacketConfiguration extends Packet {

    private Sector[] sectors;

    private String outOfBorderMessage;
    private String sectorOfflineMessage;

    private String nearBorderActionBar;
    private String nearSectorActionBar;

    public PacketConfiguration(String sender, Sector[] sectors, String outOfBorderMessage, String sectorOfflineMessage, String nearBorderActionBar, String nearSectorActionBar) {
        super(sender);
        this.sectors = sectors;
        this.outOfBorderMessage = outOfBorderMessage;
        this.sectorOfflineMessage = sectorOfflineMessage;
        this.nearBorderActionBar = nearBorderActionBar;
        this.nearSectorActionBar = nearSectorActionBar;
    }

    public Sector[] getSectors() {
        return sectors;
    }

    public String getOutOfBorderMessage() {
        return outOfBorderMessage;
    }

    public String getSectorOfflineMessage() {
        return sectorOfflineMessage;
    }

    public String getNearBorderActionBar() {
        return nearBorderActionBar;
    }

    public String getNearSectorActionBar() {
        return nearSectorActionBar;
    }
}
