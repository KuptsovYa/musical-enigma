/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.Constraint
 *  javax.validation.Payload
 */
package org.sonatype.nexus.security.role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import org.sonatype.nexus.security.role.RoleNotContainSelfValidator;

@Target(value={ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(value=RetentionPolicy.RUNTIME)
@Constraint(validatedBy={RoleNotContainSelfValidator.class})
public @interface RoleNotContainSelf {
    public String message() default "A role cannot contain itself directly or indirectly through other roles.";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};

    public String id();

    public String roleIds();
}

