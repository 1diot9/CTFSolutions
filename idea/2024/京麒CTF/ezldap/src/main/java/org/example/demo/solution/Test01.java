package org.example.demo.solution;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Test01 {
    public static void main(String[] args) throws NamingException {
//        System.setProperty("com.sun.jndi.ldap.object.trustSerialData", "false");
        InitialContext initialContext = new InitialContext();
        initialContext.lookup("ldap://127.0.0.1:50389/923536");
    }
}
