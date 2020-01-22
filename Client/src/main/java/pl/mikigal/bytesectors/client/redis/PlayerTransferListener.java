package pl.mikigal.bytesectors.client.redis;

import pl.mikigal.bytesectors.client.util.PlayerTransferUtils;
import pl.mikigal.bytesectors.commons.data.SectorManager;
import pl.mikigal.bytesectors.commons.packet.PacketPlayerTransfer;
import pl.mikigal.bytesectors.commons.redis.RedisListener;

public class PlayerTransferListener extends RedisListener<PacketPlayerTransfer> {

    public PlayerTransferListener() {
        super(SectorManager.getCurrentSectorId(), PacketPlayerTransfer.class);
    }

    @Override
    public void onMessage(PacketPlayerTransfer packet) {
        PlayerTransferUtils.addTransferToQueue(packet);
    }
}
