package challenge;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
/* loaded from: EasyDB.jar:BOOT-INF/classes/challenge/JdbcConfig.class */
public class JdbcConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(this.url);
        dataSource.setUser(this.username);
        dataSource.setPassword(this.password);
        return dataSource;
    }

    @Bean
    public Connection jdbcConnection(DataSource dataSource) throws SQLException {
        return dataSource.getConnection();
    }
}
