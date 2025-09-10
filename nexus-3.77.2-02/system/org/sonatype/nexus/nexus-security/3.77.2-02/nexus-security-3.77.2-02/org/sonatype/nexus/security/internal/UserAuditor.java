/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.eventbus.AllowConcurrentEvents
 *  com.google.common.eventbus.Subscribe
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.nexus.audit.AuditData
 *  org.sonatype.nexus.audit.AuditorSupport
 *  org.sonatype.nexus.common.event.EventAware
 */
package org.sonatype.nexus.security.internal;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import java.util.ArrayList;
import java.util.Map;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.nexus.audit.AuditData;
import org.sonatype.nexus.audit.AuditorSupport;
import org.sonatype.nexus.common.event.EventAware;
import org.sonatype.nexus.security.role.RoleIdentifier;
import org.sonatype.nexus.security.user.User;
import org.sonatype.nexus.security.user.UserCreatedEvent;
import org.sonatype.nexus.security.user.UserDeletedEvent;
import org.sonatype.nexus.security.user.UserEvent;
import org.sonatype.nexus.security.user.UserUpdatedEvent;

@Named
@Singleton
public class UserAuditor
extends AuditorSupport
implements EventAware {
    public static final String DOMAIN = "security.user";

    public UserAuditor() {
        this.registerType(UserCreatedEvent.class, "created");
        this.registerType(UserDeletedEvent.class, "deleted");
        this.registerType(UserUpdatedEvent.class, "updated");
    }

    @Subscribe
    @AllowConcurrentEvents
    public void on(UserEvent event) {
        if (this.isRecording()) {
            User user = event.getUser();
            AuditData data = new AuditData();
            data.setDomain(DOMAIN);
            data.setType(this.type(event.getClass()));
            data.setContext(user.getUserId());
            Map attributes = data.getAttributes();
            attributes.put("id", user.getUserId());
            attributes.put("name", user.getName());
            attributes.put("email", user.getEmailAddress());
            attributes.put("source", user.getSource());
            attributes.put("status", user.getStatus().name());
            attributes.put("roles", UserAuditor.roles(user));
            this.record(data);
        }
    }

    private static String roles(User user) {
        ArrayList<String> result = new ArrayList<String>();
        for (RoleIdentifier role : user.getRoles()) {
            result.add(role.getRoleId());
        }
        return UserAuditor.string(result);
    }
}

