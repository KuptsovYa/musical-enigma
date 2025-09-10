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
import org.sonatype.nexus.security.user.User;
import org.sonatype.nexus.security.user.UserExists;
import org.sonatype.nexus.security.user.UserNotFoundException;
import org.sonatype.nexus.validation.ConstraintValidatorSupport;

@Named
public class UserExistsValidator
extends ConstraintValidatorSupport<UserExists, String> {
    private final SecuritySystem securitySystem;

    @Inject
    public UserExistsValidator(SecuritySystem securitySystem) {
        this.securitySystem = (SecuritySystem)Preconditions.checkNotNull((Object)securitySystem);
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        this.log.trace("Validating user exists: {}", (Object)value);
        try {
            User user = this.securitySystem.getUser(value);
            return true;
        }
        catch (UserNotFoundException e) {
            return false;
        }
    }
}

