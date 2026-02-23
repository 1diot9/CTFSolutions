package challenge;

import java.util.HashSet;
import java.util.Iterator;

/* loaded from: EasyDB.jar:BOOT-INF/classes/challenge/SecurityUtils.class */
public class SecurityUtils {
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
