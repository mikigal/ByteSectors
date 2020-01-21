package pl.mikigal.bytesectors.commons.redis;

import io.lettuce.core.pubsub.RedisPubSubListener;
import pl.mikigal.bytesectors.commons.packet.Packet;

public abstract class RedisListener<T extends Packet> implements RedisPubSubListener<String, Packet> {

    private Class<T> packetType;
    private String channel;

    public RedisListener(String channel, Class<T> packetType) {
        this.channel = channel;
        this.packetType = packetType;
    }

    public abstract void onMessage(T packet);

    @Override
    public void message(String channel, Packet packet) {
        if (this.channel.equals(channel) && packet.getClass().isAssignableFrom(this.packetType)) {
            this.onMessage((T) packet);
        }
    }

    @Override
    public void message(String pattern, String channel, Packet message) {

    }

    @Override
    public void subscribed(String channel, long count) {
        System.out.println("[ByteSectorsCommons] Subscribed channel " + channel + " with listener for " + packetType.getSimpleName());
    }

    @Override
    public void psubscribed(String pattern, long count) {

    }

    @Override
    public void unsubscribed(String channel, long count) {

    }

    @Override
    public void punsubscribed(String pattern, long count) {

    }

    public String getChannel() {
        return channel;
    }
}
