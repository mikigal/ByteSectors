package pl.mikigal.bytesectors.commons;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import org.nustaq.serialization.FSTConfiguration;
import pl.mikigal.bytesectors.commons.packet.Packet;
import pl.mikigal.bytesectors.commons.redis.FSTCodec;
import pl.mikigal.bytesectors.commons.redis.RedisUtils;

public class ByteSectorsCommons {

    public static final FSTConfiguration FST_CONFIG = FSTConfiguration.createDefaultConfiguration();
    public static final FSTCodec FST_CODEC = new FSTCodec();

    private static ByteSectorsCommons instance;
    private RedisClient redisClient;
    private StatefulRedisPubSubConnection<String, Packet> pubSubConnection;
    private StatefulRedisConnection<String, Packet> connection;

    public ByteSectorsCommons(String hostname, int port, String password) {
        instance = this;
        this.redisClient = RedisClient.create(RedisURI.builder()
                                .withHost(hostname)
                                .withPort(port)
                                .withPassword(password)
                                .build());

        this.pubSubConnection = this.redisClient.connectPubSub(FST_CODEC);
        this.connection = this.redisClient.connect(FST_CODEC);

        System.out.println("[ByteSectorsCommons] Connected to Redis!");
    }

    public void closeConnections() {
        this.getPubSubConnection().sync().unsubscribe(RedisUtils.getSubscribedChannels().toArray(new String[0]));
        this.pubSubConnection.close();
        this.connection.close();
    }

    public RedisClient getRedisClient() {
        return redisClient;
    }

    public StatefulRedisPubSubConnection<String, Packet> getPubSubConnection() {
        return pubSubConnection;
    }

    public StatefulRedisConnection<String, Packet> getConnection() {
        return connection;
    }

    public static ByteSectorsCommons getInstance() {
        return instance;
    }
}
