package pl.mikigal.bytesectors.commons.mysql;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    public Connection getConnection() {
        try {
            return this.dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(String sql, Object... variables) {
        try {
            Connection conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            for (int i = 0; i < variables.length; i++) {
                statement.setObject(i + 1, variables[i].toString());
            }

            statement.executeUpdate();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }
}
