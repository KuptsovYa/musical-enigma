/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 */
package org.sonatype.nexus.security.config;

import com.google.common.collect.Sets;
import java.util.Set;

public class SecuritySourceUtil {
    private static final Set<String> CASE_INSENSITIVE_SOURCES = Sets.newHashSet((Object[])new String[]{"crowd", "ldap"});

    public static boolean isCaseInsensitiveSource(String source) {
        return CASE_INSENSITIVE_SOURCES.contains(source.toLowerCase());
    }
}

