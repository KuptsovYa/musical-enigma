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
import org.sonatype.nexus.security.role.UniqueRoleIdValidator;

@Target(value={ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(value=RetentionPolicy.RUNTIME)
@Constraint(validatedBy={UniqueRoleIdValidator.class})
public @interface UniqueRoleId {
    public String message() default "Role-ID must be unique";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}

