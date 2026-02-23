package com.example.ezjav.solution;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JNDI_Test {
    public static void main(String[] args) throws NamingException {
        InitialContext initialContext = new InitialContext();
        initialContext.lookup("ldap://127.0.0.1:58081/1548ab");

    }
}
