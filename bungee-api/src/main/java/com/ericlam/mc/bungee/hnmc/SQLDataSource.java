package com.ericlam.mc.bungee.hnmc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * MySQL 管理器
 */
public interface SQLDataSource {

    /**
     * 注意: 必須每次拿一次，不能拿一次後長期使用。
     *
     * @return SQL 連接
     * @throws SQLException SQL錯誤
     */
    Connection getConnection() throws SQLException;

    /**
     *
     * @return SQL 連接池
     */
    DataSource getDataSource();

}
