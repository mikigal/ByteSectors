package pl.mikigal.bytesectors.commons.packet;

import pl.mikigal.bytesectors.commons.data.Sector;
import pl.mikigal.bytesectors.commons.data.SectorManager;
import pl.mikigal.bytesectors.commons.redis.RedisUtils;

import java.io.Serializable;

public class Packet implements Serializable {

    protected String sender;

    protected Packet() {
        this.sender = SectorManager.getCurrentSector();
    }

    public String getSender() {
        return sender;
    }

    public void send(Sector sector) {
        RedisUtils.publish(sector.getId(), this);
    }

    public void send(String channel) {
        RedisUtils.publish(channel, this);
    }

    public void sendResponse(Packet packet) {
        RedisUtils.publish(packet.getSender(), this);
    }
}
