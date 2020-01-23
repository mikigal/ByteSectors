package pl.mikigal.bytesectors.commons.mysql;

import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class QueryTimedOutWatcher implements Runnable {

    private long time;
    private UUID queryId;

    public QueryTimedOutWatcher(long time, UUID queryId) {
        this.time = time;
        this.queryId = queryId;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(this.time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        CompletableFuture<ResultSetSerializable> future = DatabaseAPI.getWaitingQueries().get(this.queryId);
        if (future != null) {
            DatabaseAPI.getWaitingQueries().remove(this.queryId);
            future.completeExceptionally(new SQLException("Query with UUID " + this.queryId + " didn't get response after 5000ms!"));
        }
    }
}
