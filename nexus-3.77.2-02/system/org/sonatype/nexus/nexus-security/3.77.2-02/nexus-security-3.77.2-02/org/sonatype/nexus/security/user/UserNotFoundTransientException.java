/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.user;

import org.sonatype.nexus.security.user.UserNotFoundException;

public class UserNotFoundTransientException
extends UserNotFoundException {
    private static final long serialVersionUID = 7565547428483146620L;

    public UserNotFoundTransientException(String userId, String message, Throwable cause) {
        super(userId, message, cause);
    }
}

