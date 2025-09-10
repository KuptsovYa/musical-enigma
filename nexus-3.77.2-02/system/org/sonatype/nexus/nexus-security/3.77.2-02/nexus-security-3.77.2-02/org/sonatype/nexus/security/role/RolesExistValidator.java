/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.validation.ConstraintValidatorContext
 *  org.sonatype.nexus.validation.ConstraintValidatorSupport
 */
package org.sonatype.nexus.security.role;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.LinkedList;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintValidatorContext;
import org.sonatype.nexus.security.SecuritySystem;
import org.sonatype.nexus.security.authz.AuthorizationManager;
import org.sonatype.nexus.security.authz.NoSuchAuthorizationManagerException;
import org.sonatype.nexus.security.role.NoSuchRoleException;
import org.sonatype.nexus.security.role.RolesExist;
import org.sonatype.nexus.validation.ConstraintValidatorSupport;

@Named
public class RolesExistValidator
extends ConstraintValidatorSupport<RolesExist, Collection<?>> {
    private final AuthorizationManager authorizationManager;

    @Inject
    public RolesExistValidator(SecuritySystem securitySystem) throws NoSuchAuthorizationManagerException {
        this.authorizationManager = ((SecuritySystem)Preconditions.checkNotNull((Object)securitySystem)).getAuthorizationManager("default");
    }

    public boolean isValid(Collection<?> value, ConstraintValidatorContext context) {
        this.log.trace("Validating roles exist: {}", value);
        LinkedList<String> missing = new LinkedList<String>();
        for (Object item : value) {
            try {
                this.authorizationManager.getRole(String.valueOf(item));
            }
            catch (NoSuchRoleException e) {
                missing.add(this.getEscapeHelper().stripJavaEl(item.toString()));
            }
        }
        if (missing.isEmpty()) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("Missing roles: " + missing).addConstraintViolation();
        return false;
    }
}

