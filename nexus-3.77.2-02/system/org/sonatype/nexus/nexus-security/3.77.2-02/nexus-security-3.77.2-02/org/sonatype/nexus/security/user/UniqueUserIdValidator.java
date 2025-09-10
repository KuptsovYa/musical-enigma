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
package org.sonatype.nexus.security.user;

import com.google.common.base.Preconditions;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintValidatorContext;
import org.sonatype.nexus.security.SecuritySystem;
import org.sonatype.nexus.security.user.NoSuchUserManagerException;
import org.sonatype.nexus.security.user.UniqueUserId;
import org.sonatype.nexus.security.user.UserManager;
import org.sonatype.nexus.security.user.UserNotFoundException;
import org.sonatype.nexus.validation.ConstraintValidatorSupport;

@Named
public class UniqueUserIdValidator
extends ConstraintValidatorSupport<UniqueUserId, String> {
    private final UserManager userManager;

    @Inject
    public UniqueUserIdValidator(SecuritySystem securitySystem) throws NoSuchUserManagerException {
        this.userManager = ((SecuritySystem)Preconditions.checkNotNull((Object)securitySystem)).getUserManager("default");
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        this.log.trace("Validating unique user-id: {}", (Object)value);
        try {
            this.userManager.getUser(value);
            return false;
        }
        catch (UserNotFoundException e) {
            return true;
        }
    }
}

