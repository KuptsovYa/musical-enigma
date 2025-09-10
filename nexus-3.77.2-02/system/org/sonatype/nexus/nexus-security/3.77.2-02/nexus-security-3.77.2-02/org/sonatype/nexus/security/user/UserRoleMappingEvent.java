/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 */
package org.sonatype.nexus.security.user;

import com.google.common.base.Preconditions;
import java.util.Set;

public abstract class UserRoleMappingEvent {
    private final String userId;
    private final String userSource;
    private final Set<String> roles;

    public UserRoleMappingEvent(String userId, String userSource, Set<String> roles) {
        this.userId = (String)Preconditions.checkNotNull((Object)userId);
        this.userSource = (String)Preconditions.checkNotNull((Object)userSource);
        this.roles = (Set)Preconditions.checkNotNull(roles);
    }

    public String getUserId() {
        return this.userId;
    }

    public String getUserSource() {
        return this.userSource;
    }

    public Set<String> getRoles() {
        return this.roles;
    }

    public String toString() {
        return this.getClass().getSimpleName() + "{userId='" + this.userId + "', userSource='" + this.userSource + "', roles=" + this.roles + "}";
    }
}

