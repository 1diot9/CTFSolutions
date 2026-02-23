package com.example.demo.security;

import java.io.FilePermission;
import java.security.Permission;

/* loaded from: ez-solon.jar:BOOT-INF/classes/com/example/demo/security/RestrictiveSecurityManager.class */
public class RestrictiveSecurityManager extends SecurityManager {
    @Override // java.lang.SecurityManager
    public void checkExec(String cmd) {
        throw new SecurityException("命令执行已被禁用");
    }

    @Override // java.lang.SecurityManager
    public void checkPermission(Permission perm) {
        if (perm instanceof FilePermission) {

        }
    }

    @Override // java.lang.SecurityManager
    public void checkPermission(Permission perm, Object context) {
        checkPermission(perm);
    }
}
