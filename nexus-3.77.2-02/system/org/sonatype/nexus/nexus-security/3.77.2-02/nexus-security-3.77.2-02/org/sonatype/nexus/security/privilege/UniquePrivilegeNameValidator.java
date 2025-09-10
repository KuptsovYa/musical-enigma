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
package org.sonatype.nexus.security.privilege;

import com.google.common.base.Preconditions;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintValidatorContext;
import org.sonatype.nexus.security.SecuritySystem;
import org.sonatype.nexus.security.privilege.Privilege;
import org.sonatype.nexus.security.privilege.UniquePrivilegeName;
import org.sonatype.nexus.validation.ConstraintValidatorSupport;

@Named
public class UniquePrivilegeNameValidator
extends ConstraintValidatorSupport<UniquePrivilegeName, String> {
    private final SecuritySystem securitySystem;

    @Inject
    public UniquePrivilegeNameValidator(SecuritySystem securitySystem) {
        this.securitySystem = (SecuritySystem)Preconditions.checkNotNull((Object)securitySystem);
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        this.log.trace("Validating unique privilege name: {}", (Object)value);
        for (Privilege privilege : this.securitySystem.listPrivileges()) {
            if (!value.equals(privilege.getName())) continue;
            return false;
        }
        return true;
    }
}

