package pl.mikigal.bytesectors.commons.packet.configuration;

import pl.mikigal.bytesectors.commons.data.Sector;
import pl.mikigal.bytesectors.commons.packet.Packet;
import pl.mikigal.bytesectors.commons.serializable.ItemBuilderSerializable;

public class PacketConfiguration extends Packet {

    private Sector[] sectors;

    private int nearBorderTerrainModifyBlockDistance;

    private String outOfBorderMessage;
    private String sectorOfflineMessage;
    private String nearBorderTerrainModifyMessage;

    private String nearBorderActionBar;
    private String nearSectorActionBar;

    private String sectorsGuiName;
    private ItemBuilderSerializable onlineItem;
    private ItemBuilderSerializable offlineItem;

    public PacketConfiguration(String sender, Sector[] sectors, int nearBorderTerrainModifyBlockDistance, String outOfBorderMessage, String sectorOfflineMessage, String nearBorderActionBar, String nearSectorActionBar, String nearBorderTerrainModifyMessage, String sectorsGuiName, ItemBuilderSerializable onlineItem, ItemBuilderSerializable offlineItem) {
        super(sender);
        this.sectors = sectors;
        this.nearBorderTerrainModifyBlockDistance = nearBorderTerrainModifyBlockDistance;
        this.outOfBorderMessage = outOfBorderMessage;
        this.sectorOfflineMessage = sectorOfflineMessage;
        this.nearBorderActionBar = nearBorderActionBar;
        this.nearSectorActionBar = nearSectorActionBar;
        this.nearBorderTerrainModifyMessage = nearBorderTerrainModifyMessage;
        this.sectorsGuiName = sectorsGuiName;
        this.onlineItem = onlineItem;
        this.offlineItem = offlineItem;
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

    public int getNearBorderTerrainModifyBlockDistance() {
        return nearBorderTerrainModifyBlockDistance;
    }

    public String getNearBorderTerrainModifyMessage() {
        return nearBorderTerrainModifyMessage;
    }

    public String getSectorsGuiName() {
        return sectorsGuiName;
    }

    public ItemBuilderSerializable getOnlineItem() {
        return onlineItem;
    }

    public ItemBuilderSerializable getOfflineItem() {
        return offlineItem;
    }
}
