package pl.mikigal.bytesectors.client.redis;

import pl.mikigal.bytesectors.client.Configuration;
import pl.mikigal.bytesectors.client.util.PlayerTransferUtils;
import pl.mikigal.bytesectors.commons.packet.PacketPlayerTransfer;
import pl.mikigal.bytesectors.commons.redis.RedisListener;

public class PacketPlayerTransferListener extends RedisListener<PacketPlayerTransfer> {

    public PacketPlayerTransferListener() {
        super(Configuration.getSectorId(), PacketPlayerTransfer.class);
    }

    @Override
    public void onMessage(PacketPlayerTransfer packet) {
        PlayerTransferUtils.addTransferToQueue(packet);
    }
}
