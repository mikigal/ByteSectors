package pl.mikigal.bytesectors.commons.packet.database;

import pl.mikigal.bytesectors.commons.mysql.ResultSetSerializable;
import pl.mikigal.bytesectors.commons.packet.Packet;

import java.util.UUID;

public class PacketDatabaseQueryResponse extends Packet {

    private UUID uniqueId;
    private ResultSetSerializable resultSet;

    public PacketDatabaseQueryResponse(UUID uniqueId, ResultSetSerializable resultSet) {
        this.uniqueId = uniqueId;
        this.resultSet = resultSet;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public ResultSetSerializable getResultSet() {
        return resultSet;
    }
}
