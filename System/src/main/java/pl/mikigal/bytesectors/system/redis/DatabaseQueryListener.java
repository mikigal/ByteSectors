package pl.mikigal.bytesectors.system.redis;

import pl.mikigal.bytesectors.commons.data.SectorManager;
import pl.mikigal.bytesectors.commons.mysql.ResultSetSerializable;
import pl.mikigal.bytesectors.commons.packet.database.PacketDatabaseQuery;
import pl.mikigal.bytesectors.commons.packet.database.PacketDatabaseQueryResponse;
import pl.mikigal.bytesectors.commons.redis.RedisListener;
import pl.mikigal.bytesectors.system.ByteSectorsSystem;

public class DatabaseQueryListener extends RedisListener<PacketDatabaseQuery> {

    public DatabaseQueryListener() {
        super(SectorManager.getSystemChannel(), PacketDatabaseQuery.class);
    }

    @Override
    public void onMessage(PacketDatabaseQuery packet) {
        ByteSectorsSystem.getInstance().getProxy().getScheduler().runAsync(ByteSectorsSystem.getInstance(), () -> {
            if (!packet.isQuery()) {
                ByteSectorsSystem.getInstance().getDataSource().execute(packet.getStatement());
                return;
            }

            ResultSetSerializable resultSet = ByteSectorsSystem.getInstance().getDataSource().query(packet.getStatement());
            new PacketDatabaseQueryResponse(packet.getUniqueId(), resultSet).sendResponse(packet);
        });
    }
}
