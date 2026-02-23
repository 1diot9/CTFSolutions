package com.ctf.utils;

import java.util.regex.Pattern;

/* loaded from: babyja.jar:BOOT-INF/classes/com/ctf/utils/SecurityCheck.class */
public class SecurityCheck {
    private final String input;

    public SecurityCheck(String input) {
        this.input = input;
        checkForBlockedClasses();
    }

    private void checkForBlockedClasses() {
        Pattern pattern = Pattern.compile("(?i)(TemplatesImpl|JdbcRowSetImpl|Jndi|54656D706C61746573496D706C|BadAttributeValueExpException)");
        if (pattern.matcher(this.input).find()) {
            throw new SecurityException("hacker");
        }
    }
}
