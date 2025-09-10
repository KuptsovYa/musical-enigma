/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Joiner
 *  com.google.common.base.Preconditions
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter
 */
package org.sonatype.nexus.security.authz;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

@Named
@Singleton
public class PermissionsFilter
extends PermissionsAuthorizationFilter {
    public static final String NAME = "nx-perms";

    public static String config(String ... permissions) {
        Preconditions.checkNotNull((Object)permissions);
        Preconditions.checkArgument((permissions.length != 0 ? 1 : 0) != 0);
        return String.format("%s[%s]", NAME, Joiner.on((String)",").join((Object[])permissions));
    }
}

