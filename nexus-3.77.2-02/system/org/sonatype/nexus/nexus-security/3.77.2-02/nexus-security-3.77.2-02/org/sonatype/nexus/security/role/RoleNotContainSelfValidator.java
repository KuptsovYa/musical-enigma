/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.validation.ConstraintValidatorContext
 *  org.sonatype.nexus.common.text.Strings2
 *  org.sonatype.nexus.validation.ConstraintValidatorSupport
 */
package org.sonatype.nexus.security.role;

import com.google.common.base.Preconditions;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintValidatorContext;
import org.sonatype.nexus.common.text.Strings2;
import org.sonatype.nexus.security.SecuritySystem;
import org.sonatype.nexus.security.authz.AuthorizationManager;
import org.sonatype.nexus.security.authz.NoSuchAuthorizationManagerException;
import org.sonatype.nexus.security.role.NoSuchRoleException;
import org.sonatype.nexus.security.role.Role;
import org.sonatype.nexus.security.role.RoleNotContainSelf;
import org.sonatype.nexus.validation.ConstraintValidatorSupport;

@Named
public class RoleNotContainSelfValidator
extends ConstraintValidatorSupport<RoleNotContainSelf, Object> {
    private final AuthorizationManager authorizationManager;
    private String idField;
    private String roleIdsField;
    private String message;

    public void initialize(RoleNotContainSelf constraintAnnotation) {
        this.idField = constraintAnnotation.id();
        this.roleIdsField = constraintAnnotation.roleIds();
        this.message = constraintAnnotation.message();
    }

    @Inject
    public RoleNotContainSelfValidator(SecuritySystem securitySystem) throws NoSuchAuthorizationManagerException {
        this.authorizationManager = ((SecuritySystem)Preconditions.checkNotNull((Object)securitySystem)).getAuthorizationManager("default");
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        this.log.trace("Validating role doesn't contain itself: {}", value);
        String id = this.getId(value);
        if (Strings2.isEmpty((String)id)) {
            return true;
        }
        HashSet<String> processedRoleIds = new HashSet<String>();
        Collection<String> roleIds = this.getRoleIds(value);
        for (String roleId : roleIds) {
            if (!this.containsRole(id, roleId, processedRoleIds)) continue;
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(this.message).addConstraintViolation();
            return false;
        }
        return true;
    }

    private String getId(Object obj) {
        try {
            Method m = obj.getClass().getMethod(this.idField, new Class[0]);
            return (String)m.invoke(obj, new Object[0]);
        }
        catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            this.log.error("Unable to find method {} in object {}", (Object)this.idField, obj);
            throw new RuntimeException(e);
        }
    }

    private Collection<String> getRoleIds(Object obj) {
        try {
            Method m = obj.getClass().getMethod(this.roleIdsField, new Class[0]);
            return (Collection)m.invoke(obj, new Object[0]);
        }
        catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            this.log.error("Unable to find method {} in object {}", (Object)this.roleIdsField, obj);
            throw new RuntimeException(e);
        }
    }

    private boolean containsRole(String roleId, String childRoleId, Set<String> processedRoleIds) {
        if (processedRoleIds.contains(childRoleId)) {
            return false;
        }
        processedRoleIds.add(childRoleId);
        if (roleId.equals(childRoleId)) {
            return true;
        }
        try {
            Role childRole = this.authorizationManager.getRole(childRoleId);
            for (String role : childRole.getRoles()) {
                if (!this.containsRole(roleId, role, processedRoleIds)) continue;
                return true;
            }
        }
        catch (NoSuchRoleException ignored) {
            this.log.trace("Missing role {}", (Object)childRoleId);
        }
        return false;
    }
}

