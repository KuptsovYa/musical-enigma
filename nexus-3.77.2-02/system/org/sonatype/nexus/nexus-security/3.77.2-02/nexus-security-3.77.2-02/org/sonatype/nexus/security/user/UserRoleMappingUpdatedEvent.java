/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.user;

import java.util.Set;
import org.sonatype.nexus.security.user.UserRoleMappingEvent;

public class UserRoleMappingUpdatedEvent
extends UserRoleMappingEvent {
    public UserRoleMappingUpdatedEvent(String userId, String userSource, Set<String> roles) {
        super(userId, userSource, roles);
    }
}

