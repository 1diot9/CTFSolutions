package com.example.ycbjava.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

/* loaded from: ycbjava-0.0.1-SNAPSHOT.jar:BOOT-INF/classes/com/example/ycbjava/config/MainRealm.class */
public class MainRealm extends AuthorizingRealm {
    @Override // org.apache.shiro.realm.AuthorizingRealm
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override // org.apache.shiro.realm.AuthenticatingRealm
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        String password = new String((char[]) authenticationToken.getCredentials());
        String realusername = "";
        String realpassword = "";
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/ez_java?serverTimezone=GMT%2B8", "javauser", "password");
            Statement stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("select * from users");
            while (res.next()) {
                realusername = res.getString(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
                realpassword = res.getString("password");
            }
            res.close();
            stmt.close();
            con.close();
            if (username.equals(realusername) && password.equals(realpassword)) {
                return new SimpleAuthenticationInfo(username, password, getName());
            }
            throw new IncorrectCredentialsException("Username or password is incorrect.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
