package pl.mikigal.bytesectors.commons.redis;

import io.lettuce.core.RedisFuture;
import pl.mikigal.bytesectors.commons.ByteSectorsCommons;
import pl.mikigal.bytesectors.commons.packet.Packet;

import java.util.ArrayList;
import java.util.List;

public class RedisUtils {

    private static List<String> subscribedChannels = new ArrayList<>();

    public static void set(String key, String value) {
        ByteSectorsCommons.getInstance().getDatabaseConnect().sync().set(key, value);
    }

    public static RedisFuture<String> setAsync(String key, String value) {
        return ByteSectorsCommons.getInstance().getDatabaseConnect().async().set(key, value);
    }

    public static String get(String key) {
        return ByteSectorsCommons.getInstance().getDatabaseConnect().sync().get(key);
    }

    public static RedisFuture<String> getAsync(String key, String value) {
        return ByteSectorsCommons.getInstance().getDatabaseConnect().async().get(key);
    }

    public static void publish(String channel, Packet packet) {
        ByteSectorsCommons.getInstance().getConnection().sync().publish(channel, packet);
    }

    public static void subscribe(String channel, RedisListener<? extends Packet> listener) {
        if (!channel.equals(listener.getChannel())) {
            throw new IllegalStateException("Channel from subscribe method must be the same as Listener channel");
        }

        ByteSectorsCommons.getInstance().getPubSubConnection().addListener(listener);

        if (!subscribedChannels.contains(channel)) {
            ByteSectorsCommons.getInstance().getPubSubConnection().sync().subscribe(channel);
            subscribedChannels.add(channel);
        }
    }

    public static List<String> getSubscribedChannels() {
        return subscribedChannels;
    }
}
