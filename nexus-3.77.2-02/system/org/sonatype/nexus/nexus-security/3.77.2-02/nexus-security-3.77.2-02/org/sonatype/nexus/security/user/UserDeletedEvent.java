/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.user;

import org.sonatype.nexus.security.user.User;
import org.sonatype.nexus.security.user.UserEvent;

public class UserDeletedEvent
extends UserEvent {
    public UserDeletedEvent(User user) {
        super(user);
    }
}

