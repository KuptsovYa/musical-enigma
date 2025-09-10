/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.base.Strings
 *  org.apache.shiro.subject.Subject
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.slf4j.MDC
 */
package org.sonatype.nexus.security;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.sonatype.nexus.security.UserIdHelper;

public class UserIdMdcHelper {
    private static final Logger log = LoggerFactory.getLogger(UserIdMdcHelper.class);
    public static final String KEY = "userId";

    private UserIdMdcHelper() {
    }

    public static boolean isSet() {
        String userId = MDC.get((String)KEY);
        return !Strings.isNullOrEmpty((String)userId) && !"*UNKNOWN".equals(userId);
    }

    public static void setIfNeeded() {
        if (!UserIdMdcHelper.isSet()) {
            UserIdMdcHelper.set();
        }
    }

    public static void set(Subject subject) {
        Preconditions.checkNotNull((Object)subject);
        String userId = UserIdHelper.get(subject);
        log.trace("Set: {}", (Object)userId);
        MDC.put((String)KEY, (String)userId);
    }

    public static void set() {
        MDC.put((String)KEY, (String)UserIdHelper.get());
    }

    public static void unknown() {
        MDC.put((String)KEY, (String)"*UNKNOWN");
    }

    public static void system() {
        MDC.put((String)KEY, (String)"*SYSTEM");
    }

    public static void unset() {
        MDC.remove((String)KEY);
    }
}

