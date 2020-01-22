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

    public static UUID query(StatementSerializable statement, Consumer<? super ResultSetSerializable> future) {
        PacketDatabaseQuery packet = new PacketDatabaseQuery(statement, true);
        CompletableFuture<ResultSetSerializable> completableFuture = new CompletableFuture<>();
        completableFuture.thenAccept(future);

        waitingQueries.put(packet.getUniqueId(), completableFuture);
        packet.send(SectorManager.getSystemChannel());
        return packet.getUniqueId();
    }

    public static UUID execute(StatementSerializable statement) {
        PacketDatabaseQuery packet = new PacketDatabaseQuery(statement, false);
        packet.send(SectorManager.getSystemChannel());
        return packet.getUniqueId();
    }

    public static Map<UUID, CompletableFuture<ResultSetSerializable>> getWaitingQueries() {
        return waitingQueries;
    }
}
