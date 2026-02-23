package com.example.ezjav.utils;

import com.alibaba.druid.pool.DruidDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/* loaded from: justDeserialize-1.0-SNAPSHOT.jar:BOOT-INF/classes/com/example/ezjav/utils/PalDataSource.class */
public class PalDataSource extends DruidDataSource {
    @Override // com.alibaba.druid.pool.DruidDataSource, javax.sql.DataSource
    public Connection getConnection(String username, String password) throws SQLException {
        return super.getConnection(username, password);
    }

    @Override // com.alibaba.druid.pool.DruidAbstractDataSource, javax.sql.CommonDataSource
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
