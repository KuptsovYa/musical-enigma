/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.Constraint
 *  javax.validation.Payload
 */
package org.sonatype.nexus.security.privilege;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import org.sonatype.nexus.security.privilege.UniquePrivilegeNameValidator;

@Target(value={ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(value=RetentionPolicy.RUNTIME)
@Constraint(validatedBy={UniquePrivilegeNameValidator.class})
public @interface UniquePrivilegeName {
    public String message() default "Privilege-name must be unique";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}

