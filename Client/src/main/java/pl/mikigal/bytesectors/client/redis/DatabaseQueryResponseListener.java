package pl.mikigal.bytesectors.client.redis;

import pl.mikigal.bytesectors.commons.data.SectorManager;
import pl.mikigal.bytesectors.commons.mysql.DatabaseAPI;
import pl.mikigal.bytesectors.commons.mysql.ResultSetSerializable;
import pl.mikigal.bytesectors.commons.packet.database.PacketDatabaseQueryResponse;
import pl.mikigal.bytesectors.commons.redis.RedisListener;

import java.util.concurrent.CompletableFuture;

public class DatabaseQueryResponseListener extends RedisListener<PacketDatabaseQueryResponse> {

    public DatabaseQueryResponseListener() {
        super(SectorManager.getCurrentSectorId(), PacketDatabaseQueryResponse.class);
    }

    @Override
    public void onMessage(PacketDatabaseQueryResponse packet) {
        CompletableFuture<ResultSetSerializable> future = DatabaseAPI.getWaitingQueries().get(packet.getUniqueId());
        if (future == null) {
            throw new IllegalStateException("Received query with UUID " + packet.getUniqueId() + " that wasn't sent from this client!");
        }

        DatabaseAPI.getWaitingQueries().remove(packet.getUniqueId());
        if (packet.getResultSet().getException() != null) {
            future.completeExceptionally(packet.getResultSet().getException());
            return;
        }

        future.complete(packet.getResultSet());
    }
}
