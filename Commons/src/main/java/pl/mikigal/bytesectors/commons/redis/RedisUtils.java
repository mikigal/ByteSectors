package pl.mikigal.bytesectors.commons.redis;

import pl.mikigal.bytesectors.commons.ByteSectorsCommons;
import pl.mikigal.bytesectors.commons.packet.Packet;

import java.util.ArrayList;
import java.util.List;

public class RedisUtils {

    private static List<String> subscribedChannels = new ArrayList<>();

    public static void publish(String channel, Packet packet) {
        ByteSectorsCommons.getInstance().getConnection().sync().publish(channel, packet);
    }

    public static void set(String key, String value) {
        ByteSectorsCommons.getInstance().getDatabaseConnect().sync().set(key, value);
    }

    public static String get(String key) {
        return ByteSectorsCommons.getInstance().getDatabaseConnect().sync().get(key);
    }

    public static void subscribe(String channel, RedisListener<? extends Packet> listener) {
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
