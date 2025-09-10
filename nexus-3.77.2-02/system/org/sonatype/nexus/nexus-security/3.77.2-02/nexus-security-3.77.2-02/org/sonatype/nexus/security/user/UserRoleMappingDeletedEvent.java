/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.user;

import java.util.Collections;
import org.sonatype.nexus.security.user.UserRoleMappingEvent;

public class UserRoleMappingDeletedEvent
extends UserRoleMappingEvent {
    public UserRoleMappingDeletedEvent(String userId, String userSource) {
        super(userId, userSource, Collections.emptySet());
    }
}

