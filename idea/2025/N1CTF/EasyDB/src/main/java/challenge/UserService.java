package challenge;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.stereotype.Service;

@Service
/* loaded from: EasyDB.jar:BOOT-INF/classes/challenge/UserService.class */
public class UserService {
    private final Connection connection;

    public UserService(Connection connection) {
        this.connection = connection;
        initializeDatabase();
    }

    private void initializeDatabase() {
        try {
            Statement stmt = this.connection.createStatement();
            Throwable th = null;
            try {
                stmt.execute("CREATE TABLE IF NOT EXISTS users (id INT PRIMARY KEY, username VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL);");
                stmt.executeUpdate("INSERT INTO users (id, username, password) VALUES (1, 'admin', 'admin');");
                if (stmt != null) {
                    if (0 != 0) {
                        try {
                            stmt.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        stmt.close();
                    }
                }
            } finally {
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean validateUser(String username, String password) throws SQLException {
        String query = String.format("SELECT * FROM users WHERE username = '%s' AND password = '%s'", username, password);
        if (!SecurityUtils.check(query)) {
            return false;
        }
        Statement stmt = this.connection.createStatement();
        Throwable th = null;
        try {
            stmt.executeQuery(query);
            ResultSet resultSet = stmt.getResultSet();
            Throwable th2 = null;
            try {
                boolean next = resultSet.next();
                if (resultSet != null) {
                    if (0 != 0) {
                        try {
                            resultSet.close();
                        } catch (Throwable th3) {
                            th2.addSuppressed(th3);
                        }
                    } else {
                        resultSet.close();
                    }
                }
                return next;
            } catch (Throwable th4) {
                if (resultSet != null) {
                    if (0 != 0) {
                        try {
                            resultSet.close();
                        } catch (Throwable th5) {
                            th2.addSuppressed(th5);
                        }
                    } else {
                        resultSet.close();
                    }
                }
                throw th4;
            }
        } finally {
            if (stmt != null) {
                if (0 != 0) {
                    try {
                        stmt.close();
                    } catch (Throwable th6) {
                        th.addSuppressed(th6);
                    }
                } else {
                    stmt.close();
                }
            }
        }
    }
}
