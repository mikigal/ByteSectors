package pl.mikigal.bytesectors.commons.packet.database;

import pl.mikigal.bytesectors.commons.mysql.StatementSerializable;
import pl.mikigal.bytesectors.commons.packet.Packet;

import java.util.UUID;

public class PacketDatabaseQuery extends Packet {

    private UUID uniqueId;
    private boolean query;
    private StatementSerializable statement;

    public PacketDatabaseQuery(StatementSerializable statement, boolean query) {
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

    public StatementSerializable getStatement() {
        return statement;
    }
}
