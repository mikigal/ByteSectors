package pl.mikigal.bytesectors.commons.packet.database;

import pl.mikigal.bytesectors.commons.mysql.DatabaseStatement;
import pl.mikigal.bytesectors.commons.packet.Packet;

import java.util.UUID;

public class PacketDatabaseQuery extends Packet {

    private UUID uniqueId;
    private boolean query;
    private DatabaseStatement statement;

    public PacketDatabaseQuery(DatabaseStatement statement, boolean query) {
        super();
        this.uniqueId = UUID.randomUUID();
        this.query = query;
        this.statement = statement;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public boolean isQuery() {
        return query;
    }

    public DatabaseStatement getStatement() {
        return statement;
    }
}
