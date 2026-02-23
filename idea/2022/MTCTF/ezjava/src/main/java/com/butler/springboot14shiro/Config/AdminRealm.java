package com.butler.springboot14shiro.Config;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/* loaded from: easyjava.jar:BOOT-INF/classes/com/butler/springboot14shiro/Config/AdminRealm.class */
public class AdminRealm extends AuthorizingRealm {
    @Override // org.apache.shiro.realm.AuthorizingRealm
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override // org.apache.shiro.realm.AuthenticatingRealm
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        return null;
    }
}
