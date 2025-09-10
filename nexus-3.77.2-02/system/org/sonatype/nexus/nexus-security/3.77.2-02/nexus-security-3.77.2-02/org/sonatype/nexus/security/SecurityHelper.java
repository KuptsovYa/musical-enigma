/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.apache.shiro.SecurityUtils
 *  org.apache.shiro.authz.AuthorizationException
 *  org.apache.shiro.authz.Permission
 *  org.apache.shiro.mgt.SecurityManager
 *  org.apache.shiro.subject.Subject
 *  org.sonatype.goodies.common.ComponentSupport
 */
package org.sonatype.nexus.security;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.stream.StreamSupport;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.security.authz.WildcardPermission2;

@Named
@Singleton
public class SecurityHelper
extends ComponentSupport {
    public SecurityManager getSecurityManager() {
        return SecurityUtils.getSecurityManager();
    }

    public Subject subject() {
        return SecurityUtils.getSubject();
    }

    public void ensurePermitted(Subject subject, Permission ... permissions) {
        Preconditions.checkNotNull((Object)subject);
        Preconditions.checkNotNull((Object)permissions);
        Preconditions.checkArgument((permissions.length != 0 ? 1 : 0) != 0);
        if (this.log.isTraceEnabled()) {
            this.log.trace("Ensuring subject '{}' has permissions: {}", subject.getPrincipal(), (Object)Arrays.toString(permissions));
        }
        subject.checkPermissions(Arrays.asList(permissions));
    }

    public void ensureAnyPermitted(Subject subject, Permission ... permissions) {
        Preconditions.checkNotNull((Object)subject);
        Preconditions.checkNotNull((Object)permissions);
        Preconditions.checkArgument((permissions.length != 0 ? 1 : 0) != 0);
        if (this.log.isTraceEnabled()) {
            this.log.trace("Ensuring subject '{}' has any of the following permissions: {}", subject.getPrincipal(), (Object)Arrays.toString(permissions));
        }
        if (!this.anyPermitted(subject, permissions)) {
            throw new AuthorizationException("User is not permitted: " + (permissions.length > 1 ? "[" + permissions[0] + ", ...]" : permissions[0]));
        }
    }

    public void ensurePermitted(Permission ... permissions) {
        this.ensurePermitted(this.subject(), permissions);
    }

    public boolean anyPermitted(Subject subject, Permission ... permissions) {
        Preconditions.checkNotNull((Object)subject);
        Preconditions.checkNotNull((Object)permissions);
        Preconditions.checkArgument((permissions.length != 0 ? 1 : 0) != 0);
        boolean trace = this.log.isTraceEnabled();
        if (trace) {
            this.log.trace("Checking if subject '{}' has ANY of these permissions: {}", subject.getPrincipal(), (Object)Arrays.toString(permissions));
        }
        for (Permission permission : permissions) {
            if (!subject.isPermitted(permission)) continue;
            if (trace) {
                this.log.trace("Subject '{}' has permission: {}", subject.getPrincipal(), (Object)permission);
            }
            return true;
        }
        if (trace) {
            this.log.trace("Subject '{}' missing required permissions: {}", subject.getPrincipal(), (Object)Arrays.toString(permissions));
        }
        return false;
    }

    public boolean anyPermitted(Subject subject, Iterable<Permission> permissions) {
        return this.anyPermitted(subject, (Permission[])StreamSupport.stream(permissions.spliterator(), false).toArray(Permission[]::new));
    }

    public boolean anyPermitted(Permission ... permissions) {
        return this.anyPermitted(this.subject(), permissions);
    }

    public boolean allPermitted(Subject subject, Permission ... permissions) {
        Preconditions.checkNotNull((Object)subject);
        Preconditions.checkNotNull((Object)permissions);
        Preconditions.checkArgument((permissions.length != 0 ? 1 : 0) != 0);
        boolean trace = this.log.isTraceEnabled();
        if (trace) {
            this.log.trace("Checking if subject '{}' has ALL of these permissions: {}", subject.getPrincipal(), (Object)Arrays.toString(permissions));
        }
        for (Permission permission : permissions) {
            if (subject.isPermitted(permission)) continue;
            if (trace) {
                this.log.trace("Subject '{}' missing permission: {}", subject.getPrincipal(), (Object)permission);
            }
            return false;
        }
        if (trace) {
            this.log.trace("Subject '{}' has required permissions: {}", subject.getPrincipal(), (Object)Arrays.toString(permissions));
        }
        return true;
    }

    public boolean allPermitted(Permission ... permissions) {
        return this.allPermitted(this.subject(), permissions);
    }

    public boolean[] isPermitted(Subject subject, Permission ... permissions) {
        Preconditions.checkNotNull((Object)subject);
        Preconditions.checkNotNull((Object)permissions);
        Preconditions.checkArgument((permissions.length != 0 ? 1 : 0) != 0);
        boolean trace = this.log.isTraceEnabled();
        if (trace) {
            this.log.trace("Checking which permissions subject '{}' has in: {}", subject.getPrincipal(), (Object)Arrays.toString(permissions));
        }
        boolean[] results = subject.isPermitted(Arrays.asList(permissions));
        if (trace) {
            this.log.trace("Subject '{}' has permissions: [{}] results {}", new Object[]{subject.getPrincipal(), Arrays.toString(permissions), results});
        }
        return results;
    }

    public boolean[] isPermitted(Permission ... permissions) {
        return this.isPermitted(this.subject(), permissions);
    }

    public boolean isAllPermitted() {
        return this.isPermitted(new Permission[]{new WildcardPermission2("nexus:*")})[0];
    }
}

