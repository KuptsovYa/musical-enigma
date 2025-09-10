/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.apache.commons.lang.StringUtils
 *  org.sonatype.nexus.rest.ValidationErrorsException
 */
package org.sonatype.nexus.security.internal;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.commons.lang.StringUtils;
import org.sonatype.nexus.rest.ValidationErrorsException;

@Named
@Singleton
public class PasswordValidator {
    private final Predicate<String> passwordValidator;
    private final String errorMessage;

    @Inject
    public PasswordValidator(@Nullable @Named(value="nexus.password.validator") String passwordValidator, @Nullable @Named(value="nexus.password.validator.message") String errorMessage) {
        this.passwordValidator = Optional.ofNullable(passwordValidator).map(Pattern::compile).map(Pattern::asPredicate).orElse(pw -> true);
        this.errorMessage = StringUtils.defaultIfBlank((String)errorMessage, (String)"Password does not match corporate policy");
    }

    public void validate(String password) {
        if (!this.passwordValidator.test(password)) {
            throw new ValidationErrorsException(this.errorMessage);
        }
    }
}

