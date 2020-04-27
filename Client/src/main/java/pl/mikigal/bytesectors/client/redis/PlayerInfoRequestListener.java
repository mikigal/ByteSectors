package pl.mikigal.bytesectors.client.redis;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.mikigal.bytesectors.client.util.SerializationUtils;
import pl.mikigal.bytesectors.commons.data.PlayerInfo;
import pl.mikigal.bytesectors.commons.data.SectorManager;
import pl.mikigal.bytesectors.commons.packet.playerinfo.PacketPlayerInfo;
import pl.mikigal.bytesectors.commons.packet.playerinfo.PacketPlayerInfoRequest;
import pl.mikigal.bytesectors.commons.redis.RedisListener;

public class PlayerInfoRequestListener extends RedisListener<PacketPlayerInfoRequest> {

    public PlayerInfoRequestListener() {
        super(SectorManager.getPublicChannel(), PacketPlayerInfoRequest.class);
    }

    @Override
    public void onMessage(PacketPlayerInfoRequest packet) {
        Player player = Bukkit.getPlayer(packet.getName());
        if (player == null) {
            return;
        }

        PacketPlayerInfo playerInfo = new PacketPlayerInfo(new PlayerInfo(player.getName(), player.getUniqueId(), SectorManager.getCurrentSectorId(), SerializationUtils.serializeLocation(player.getLocation())));
        playerInfo.send(packet.getSender());
    }
}
