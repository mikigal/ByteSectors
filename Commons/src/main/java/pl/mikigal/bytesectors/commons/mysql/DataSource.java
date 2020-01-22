package pl.mikigal.bytesectors.commons.mysql;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSource {

    private HikariDataSource dataSource;

    public DataSource(String host, int port, String user, String password, String database) {
        this.dataSource = new HikariDataSource();

        this.dataSource.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        this.dataSource.setUsername(user);
        this.dataSource.setPassword(password);
        this.dataSource.setAutoCommit(true);
        this.dataSource.setMaxLifetime(480000);
        this.dataSource.setMaximumPoolSize(10);
        this.dataSource.setPoolName("HikariMySQL");

        this.dataSource.addDataSourceProperty("cachePrepStmts", true);
        this.dataSource.addDataSourceProperty("prepStmtCacheSize", 250);
        this.dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        this.dataSource.addDataSourceProperty("useServerPrepStmts", true);
        this.dataSource.addDataSourceProperty("useLocalSessionState", true);
        this.dataSource.addDataSourceProperty("rewriteBatchedStatements", true);
        this.dataSource.addDataSourceProperty("cacheResultSetMetadata", true);
        this.dataSource.addDataSourceProperty("cacheServerConfiguration", true);
        this.dataSource.addDataSourceProperty("elideSetAutoCommits", true);
        this.dataSource.addDataSourceProperty("maintainTimeStats", false);
        this.dataSource.addDataSourceProperty("autoClosePStmtStreams", true);
        this.dataSource.addDataSourceProperty("useSSL", false);

        try {
            this.getConnection().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void execute(StatementSerializable serializable) {
        try {
            Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(serializable.getSql());
            for (Map.Entry<Integer, Object> replacement : serializable.getReplacements().entrySet()) {
                statement.setObject(replacement.getKey(), replacement.getValue());
            }

            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSetSerializable query(StatementSerializable serializable) {
        try {
            Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(serializable.getSql());
            for (Map.Entry<Integer, Object> replacement : serializable.getReplacements().entrySet()) {
                statement.setObject(replacement.getKey(), replacement.getValue());
            }
            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();

            Column[] columns = new Column[metaData.getColumnCount()];
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                int sqlIndex = i + 1;
                Column column = new Column(
                        sqlIndex,
                        metaData.getColumnName(sqlIndex),
                        metaData.getColumnTypeName(sqlIndex),
                        metaData.isAutoIncrement(sqlIndex),
                        metaData.isNullable(sqlIndex) != ResultSetMetaData.columnNoNulls);

                columns[i] = column;
            }

            List<Row> rows = new ArrayList<>();
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (Column column : columns) {
                    row.put(column.getName(), resultSet.getObject(column.getName()));
                }

                rows.add(new Row(row));
            }

            resultSet.close();
            statement.close();
            connection.close();
            return new ResultSetSerializable(columns, rows.toArray(new Row[0]));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        try {
            return this.dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }
}
