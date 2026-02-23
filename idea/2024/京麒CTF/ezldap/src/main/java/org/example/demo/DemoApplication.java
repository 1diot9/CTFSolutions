package org.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/* loaded from: demo-0.0.1-SNAPSHOT.jar:BOOT-INF/classes/com/example/demo/DemoApplication.class */
public class DemoApplication {
    public static void main(String[] args) {
        System.setProperty("com.sun.jndi.ldap.object.trustSerialData", "false");
        SpringApplication.run((Class<?>) DemoApplication.class, args);
    }
}
