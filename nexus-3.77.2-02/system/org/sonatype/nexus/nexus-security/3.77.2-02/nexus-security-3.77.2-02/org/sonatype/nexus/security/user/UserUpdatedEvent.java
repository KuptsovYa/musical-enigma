/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.user;

import org.sonatype.nexus.security.user.User;
import org.sonatype.nexus.security.user.UserEvent;

public class UserUpdatedEvent
extends UserEvent {
    public UserUpdatedEvent(User user) {
        super(user);
    }
}

