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
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintValidatorContext;
import org.sonatype.nexus.security.SecuritySystem;
import org.sonatype.nexus.security.authz.AuthorizationManager;
import org.sonatype.nexus.security.authz.NoSuchAuthorizationManagerException;
import org.sonatype.nexus.security.role.NoSuchRoleException;
import org.sonatype.nexus.security.role.UniqueRoleId;
import org.sonatype.nexus.validation.ConstraintValidatorSupport;

@Named
public class UniqueRoleIdValidator
extends ConstraintValidatorSupport<UniqueRoleId, String> {
    private final AuthorizationManager authorizationManager;

    @Inject
    public UniqueRoleIdValidator(SecuritySystem securitySystem) throws NoSuchAuthorizationManagerException {
        this.authorizationManager = ((SecuritySystem)Preconditions.checkNotNull((Object)securitySystem)).getAuthorizationManager("default");
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        this.log.trace("Validating unique role-id: {}", (Object)value);
        try {
            this.authorizationManager.getRole(value);
            return false;
        }
        catch (NoSuchRoleException e) {
            return true;
        }
    }
}

