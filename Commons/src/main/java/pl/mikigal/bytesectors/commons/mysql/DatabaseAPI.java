package pl.mikigal.bytesectors.commons.mysql;

import pl.mikigal.bytesectors.commons.data.SectorManager;
import pl.mikigal.bytesectors.commons.packet.database.PacketDatabaseQuery;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class DatabaseAPI {

    private static Map<UUID, CompletableFuture<ResultSetSerializable>> waitingQueries = new HashMap<>();

    public static UUID query(DatabaseStatement statement, Consumer<ResultSetSerializable> complete, Consumer<QueryTimedOutException> timedOut, long time) {
        PacketDatabaseQuery packet = new PacketDatabaseQuery(statement, true);
        CompletableFuture<ResultSetSerializable> completableFuture = new CompletableFuture<>();
        completableFuture.thenAccept(complete);
        completableFuture.exceptionally(exception -> {
            timedOut.accept((QueryTimedOutException) exception);
            return null;
        });

        waitingQueries.put(packet.getUniqueId(), completableFuture);
        packet.send(SectorManager.getSystemChannel());
        new Thread(new QueryTimedOutWatcher(time, packet.getUniqueId())).start();
        return packet.getUniqueId();
    }

    public static UUID query(DatabaseStatement statement, Consumer<ResultSetSerializable> complete, Consumer<QueryTimedOutException> timedOut) {
        return query(statement, complete, timedOut, 5000);
    }

    public static void execute(DatabaseStatement statement) {
        PacketDatabaseQuery packet = new PacketDatabaseQuery(statement, false);
        packet.send(SectorManager.getSystemChannel());
    }

    public static Map<UUID, CompletableFuture<ResultSetSerializable>> getWaitingQueries() {
        return waitingQueries;
    }
}
