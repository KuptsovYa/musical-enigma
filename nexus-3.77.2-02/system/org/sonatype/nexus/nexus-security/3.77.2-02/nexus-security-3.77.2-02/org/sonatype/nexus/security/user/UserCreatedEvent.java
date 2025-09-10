/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.user;

import org.sonatype.nexus.security.user.User;
import org.sonatype.nexus.security.user.UserEvent;

public class UserCreatedEvent
extends UserEvent {
    public UserCreatedEvent(User user) {
        super(user);
    }
}

