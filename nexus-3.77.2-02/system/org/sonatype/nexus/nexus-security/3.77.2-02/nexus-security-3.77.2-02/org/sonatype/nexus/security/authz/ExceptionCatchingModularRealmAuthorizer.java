/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Provider
 *  org.apache.shiro.authz.AuthorizationException
 *  org.apache.shiro.authz.Authorizer
 *  org.apache.shiro.authz.ModularRealmAuthorizer
 *  org.apache.shiro.authz.Permission
 *  org.apache.shiro.authz.permission.RolePermissionResolver
 *  org.apache.shiro.realm.Realm
 *  org.apache.shiro.subject.PrincipalCollection
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.sonatype.nexus.security.authz;

import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Provider;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionCatchingModularRealmAuthorizer
extends ModularRealmAuthorizer {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionCatchingModularRealmAuthorizer.class);
    private Provider<RolePermissionResolver> rolePermissionResolverProvider;

    public ExceptionCatchingModularRealmAuthorizer(Collection<Realm> realms) {
        super(realms);
    }

    @Inject
    public ExceptionCatchingModularRealmAuthorizer(Collection<Realm> realms, Provider<RolePermissionResolver> rolePermissionResolverProvider) {
        this.rolePermissionResolverProvider = rolePermissionResolverProvider;
        this.setRealms(realms);
    }

    public RolePermissionResolver getRolePermissionResolver() {
        return this.rolePermissionResolverProvider != null ? (RolePermissionResolver)this.rolePermissionResolverProvider.get() : null;
    }

    public void checkPermission(PrincipalCollection subjectPrincipal, String permission) {
        if (!this.isPermitted(subjectPrincipal, permission)) {
            throw new AuthorizationException("User is not permitted: " + permission);
        }
    }

    public void checkPermission(PrincipalCollection subjectPrincipal, Permission permission) {
        if (!this.isPermitted(subjectPrincipal, permission)) {
            throw new AuthorizationException("User is not permitted: " + permission);
        }
    }

    public void checkPermissions(PrincipalCollection subjectPrincipal, String ... permissions) {
        for (String permission : permissions) {
            this.checkPermission(subjectPrincipal, permission);
        }
    }

    public void checkPermissions(PrincipalCollection subjectPrincipal, Collection<Permission> permissions) {
        for (Permission permission : permissions) {
            this.checkPermission(subjectPrincipal, permission);
        }
    }

    public void checkRole(PrincipalCollection subjectPrincipal, String roleIdentifier) {
        if (!this.hasRole(subjectPrincipal, roleIdentifier)) {
            throw new AuthorizationException("User is not permitted role: " + roleIdentifier);
        }
    }

    public void checkRoles(PrincipalCollection subjectPrincipal, Collection<String> roleIdentifiers) {
        if (!this.hasAllRoles(subjectPrincipal, roleIdentifiers)) {
            throw new AuthorizationException("User is not permitted role: " + roleIdentifiers);
        }
    }

    public boolean hasAllRoles(PrincipalCollection subjectPrincipal, Collection<String> roleIdentifiers) {
        for (String roleIdentifier : roleIdentifiers) {
            if (this.hasRole(subjectPrincipal, roleIdentifier)) continue;
            return false;
        }
        return true;
    }

    public boolean hasRole(PrincipalCollection subjectPrincipal, String roleIdentifier) {
        for (Realm realm : this.getRealms()) {
            if (!(realm instanceof Authorizer)) continue;
            try {
                if (!((Authorizer)realm).hasRole(subjectPrincipal, roleIdentifier)) continue;
                return true;
            }
            catch (AuthorizationException e) {
                this.logAndIgnore(realm, (Exception)((Object)e));
            }
            catch (RuntimeException e) {
                this.logAndIgnore(realm, e);
            }
        }
        return false;
    }

    public boolean[] hasRoles(PrincipalCollection subjectPrincipal, List<String> roleIdentifiers) {
        boolean[] combinedResult = new boolean[roleIdentifiers.size()];
        for (Realm realm : this.getRealms()) {
            if (!(realm instanceof Authorizer)) continue;
            try {
                boolean[] result = ((Authorizer)realm).hasRoles(subjectPrincipal, roleIdentifiers);
                for (int i = 0; i < combinedResult.length; ++i) {
                    combinedResult[i] = combinedResult[i] | result[i];
                }
            }
            catch (AuthorizationException e) {
                this.logAndIgnore(realm, (Exception)((Object)e));
            }
            catch (RuntimeException e) {
                this.logAndIgnore(realm, e);
            }
        }
        return combinedResult;
    }

    public boolean isPermitted(PrincipalCollection subjectPrincipal, String permission) {
        for (Realm realm : this.getRealms()) {
            if (!(realm instanceof Authorizer)) continue;
            try {
                if (((Authorizer)realm).isPermitted(subjectPrincipal, permission)) {
                    if (logger.isTraceEnabled()) {
                        logger.trace("Realm: " + realm.getName() + " user: " + subjectPrincipal.iterator().next() + " has permission: " + permission);
                    }
                    return true;
                }
                if (!logger.isTraceEnabled()) continue;
                logger.trace("Realm: " + realm.getName() + " user: " + subjectPrincipal.iterator().next() + " does NOT have permission: " + permission);
            }
            catch (AuthorizationException e) {
                this.logAndIgnore(realm, (Exception)((Object)e));
            }
            catch (RuntimeException e) {
                this.logAndIgnore(realm, e);
            }
        }
        return false;
    }

    public boolean isPermitted(PrincipalCollection subjectPrincipal, Permission permission) {
        for (Realm realm : this.getRealms()) {
            if (!(realm instanceof Authorizer)) continue;
            try {
                if (!((Authorizer)realm).isPermitted(subjectPrincipal, permission)) continue;
                return true;
            }
            catch (AuthorizationException e) {
                this.logAndIgnore(realm, (Exception)((Object)e));
            }
            catch (RuntimeException e) {
                this.logAndIgnore(realm, e);
            }
        }
        return false;
    }

    public boolean[] isPermitted(PrincipalCollection subjectPrincipal, String ... permissions) {
        boolean[] combinedResult = new boolean[permissions.length];
        for (Realm realm : this.getRealms()) {
            if (!(realm instanceof Authorizer)) continue;
            try {
                boolean[] result = ((Authorizer)realm).isPermitted(subjectPrincipal, permissions);
                for (int i = 0; i < combinedResult.length; ++i) {
                    combinedResult[i] = combinedResult[i] | result[i];
                }
            }
            catch (AuthorizationException e) {
                this.logAndIgnore(realm, (Exception)((Object)e));
            }
            catch (RuntimeException e) {
                this.logAndIgnore(realm, e);
            }
        }
        return combinedResult;
    }

    public boolean[] isPermitted(PrincipalCollection subjectPrincipal, List<Permission> permissions) {
        boolean[] combinedResult = new boolean[permissions.size()];
        for (Realm realm : this.getRealms()) {
            if (!(realm instanceof Authorizer)) continue;
            try {
                boolean[] result = ((Authorizer)realm).isPermitted(subjectPrincipal, permissions);
                for (int i = 0; i < combinedResult.length; ++i) {
                    combinedResult[i] = combinedResult[i] | result[i];
                }
            }
            catch (AuthorizationException e) {
                this.logAndIgnore(realm, (Exception)((Object)e));
            }
            catch (RuntimeException e) {
                this.logAndIgnore(realm, e);
            }
        }
        return combinedResult;
    }

    public boolean isPermittedAll(PrincipalCollection subjectPrincipal, String ... permissions) {
        for (String permission : permissions) {
            if (this.isPermitted(subjectPrincipal, permission)) continue;
            return false;
        }
        return true;
    }

    public boolean isPermittedAll(PrincipalCollection subjectPrincipal, Collection<Permission> permissions) {
        for (Permission permission : permissions) {
            if (this.isPermitted(subjectPrincipal, permission)) continue;
            return false;
        }
        return true;
    }

    private void logAndIgnore(Realm realm, Exception e) {
        logger.trace("Realm '{}' failure", (Object)realm.getName(), (Object)e);
    }
}

