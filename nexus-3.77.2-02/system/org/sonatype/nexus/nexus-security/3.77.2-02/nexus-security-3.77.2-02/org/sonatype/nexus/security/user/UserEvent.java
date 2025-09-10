/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 */
package org.sonatype.nexus.security.user;

import com.google.common.base.Preconditions;
import org.sonatype.nexus.security.user.User;

public abstract class UserEvent {
    private final User user;

    public UserEvent(User user) {
        this.user = (User)Preconditions.checkNotNull((Object)user);
    }

    public User getUser() {
        return this.user;
    }

    public String toString() {
        return this.getClass().getSimpleName() + "{user=" + this.user + "}";
    }
}

