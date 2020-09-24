package com.ericlam.mc.bungee.hnmc.managers;

import com.ericlam.mc.bungee.hnmc.config.DatabaseConfig;
import com.ericlam.mc.bungee.hnmc.config.MainConfig;
import com.ericlam.mc.bungee.hnmc.SQLDataSource;
import com.ericlam.mc.bungee.hnmc.main.HNBungeeConfig;
import com.google.inject.Inject;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.md_5.bungee.config.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceManager implements SQLDataSource {
    private static DataSource dataSource;

    @Inject
    private DataSourceManager(MainConfig mainConfig) {
        DatabaseConfig db = ((HNBungeeConfig)mainConfig).getDatabaseConfig();
        HikariConfig config = new HikariConfig();
        String host = db.host;
        String port = db.port + "";
        String database = db.database;
        String username = db.username;
        String password = db.password;
        String poolname = db.Pool.name;
        int minsize = db.Pool.minSize;
        int maxsize = db.Pool.maxSize;
        boolean SSL = db.useSSL;
        long connectionTimeout = db.Pool.connectionTimeout;
        long idleTimeout = db.Pool.idleTimeout;
        long maxLifeTime = db.Pool.maxLifeTime;
        String jdbc = "jdbc:mysql://" + host + ":" + port + "/" + database + "?" + "useSSL=" + SSL;
        config.setJdbcUrl(jdbc);
        config.setPoolName(poolname);
        config.setMaximumPoolSize(maxsize);
        config.setMinimumIdle(minsize);
        config.setUsername(username);
        config.setPassword(password);
        config.setConnectionTimeout(connectionTimeout);
        config.setIdleTimeout(idleTimeout);
        config.setMaxLifetime(maxLifeTime);
        config.addDataSourceProperty("cachePrepStmts", true);
        config.addDataSourceProperty("useServerPrepStmts", true);
        config.addDataSourceProperty("prepStmtCacheSize", 250);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        //config.addDataSourceProperty("useUnicode",true);
        config.addDataSourceProperty("characterEncoding", "utf8");

        dataSource = new HikariDataSource(config);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }
}
