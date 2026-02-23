package challenge;

import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;

/* loaded from: H2Revenge.jar:BOOT-INF/classes/challenge/MyDataSource.class */
public class MyDataSource implements DataSource, Serializable {
    private String url;
    private String username;
    private String password;

    public MyDataSource(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override // javax.sql.DataSource
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.url, this.username, this.password);
    }

    @Override // javax.sql.DataSource
    public Connection getConnection(String username, String password) throws SQLException {
        return DriverManager.getConnection(this.url, username, password);
    }

    @Override // javax.sql.CommonDataSource
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override // javax.sql.CommonDataSource
    public void setLogWriter(PrintWriter out) throws SQLException {
    }

    @Override // javax.sql.CommonDataSource
    public void setLoginTimeout(int seconds) throws SQLException {
    }

    @Override // javax.sql.CommonDataSource
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override // java.sql.Wrapper
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override // java.sql.Wrapper
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override // javax.sql.CommonDataSource
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
