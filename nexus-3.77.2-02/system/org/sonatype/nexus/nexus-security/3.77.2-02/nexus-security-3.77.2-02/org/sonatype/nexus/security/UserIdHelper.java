/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  org.apache.shiro.SecurityUtils
 *  org.apache.shiro.subject.Subject
 */
package org.sonatype.nexus.security;

import javax.annotation.Nullable;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class UserIdHelper {
    public static final String UNKNOWN = "*UNKNOWN";
    public static final String SYSTEM = "*SYSTEM";

    private UserIdHelper() {
    }

    public static String get() {
        return UserIdHelper.get(SecurityUtils.getSubject());
    }

    public static String get(@Nullable Subject subject) {
        Object principal;
        if (subject != null && (principal = subject.getPrincipal()) != null) {
            return principal.toString();
        }
        return UNKNOWN;
    }

    public static boolean isSystem() {
        return UserIdHelper.get().equals(SYSTEM);
    }

    public static boolean isUnknown() {
        return UserIdHelper.get().equals(UNKNOWN);
    }
}

