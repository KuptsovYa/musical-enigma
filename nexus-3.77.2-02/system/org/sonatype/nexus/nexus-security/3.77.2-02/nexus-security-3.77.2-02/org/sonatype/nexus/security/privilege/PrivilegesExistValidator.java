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
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintValidatorContext;
import org.sonatype.nexus.security.SecuritySystem;
import org.sonatype.nexus.security.privilege.Privilege;
import org.sonatype.nexus.security.privilege.PrivilegesExist;
import org.sonatype.nexus.validation.ConstraintValidatorSupport;

@Named
public class PrivilegesExistValidator
extends ConstraintValidatorSupport<PrivilegesExist, Collection<?>> {
    private static final String MESSAGE = "Only letters, digits, underscores(_), hyphens(-), dots(.), and asterisks(*) are allowed and may not start with underscore or dot.";
    private final SecuritySystem securitySystem;

    @Inject
    public PrivilegesExistValidator(SecuritySystem securitySystem) {
        this.securitySystem = (SecuritySystem)Preconditions.checkNotNull((Object)securitySystem);
    }

    public boolean isValid(Collection<?> value, ConstraintValidatorContext context) {
        this.log.trace("Validating privileges exist: {}", value);
        HashSet<String> ids = new HashSet<String>();
        for (Privilege privilege : this.securitySystem.listPrivileges()) {
            ids.add(privilege.getId());
        }
        LinkedList<String> missing = new LinkedList<String>();
        for (Object item : value) {
            String privilegeId = String.valueOf(item);
            if (!privilegeId.matches("^[a-zA-Z0-9\\-]{1}[a-zA-Z0-9_\\-\\*\\.]*$")) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Invalid privilege id: " + this.getEscapeHelper().stripJavaEl(privilegeId) + ". Only letters, digits, underscores(_), hyphens(-), dots(.), and asterisks(*) are allowed and may not start with underscore or dot.").addConstraintViolation();
                return false;
            }
            if (ids.contains(item)) continue;
            missing.add(this.getEscapeHelper().stripJavaEl(item.toString()));
        }
        if (missing.isEmpty()) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("Missing privileges: " + missing).addConstraintViolation();
        return false;
    }
}

