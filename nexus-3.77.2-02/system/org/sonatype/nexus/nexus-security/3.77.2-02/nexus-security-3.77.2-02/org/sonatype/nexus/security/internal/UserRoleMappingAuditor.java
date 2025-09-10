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
import java.util.Map;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.nexus.audit.AuditData;
import org.sonatype.nexus.audit.AuditorSupport;
import org.sonatype.nexus.common.event.EventAware;
import org.sonatype.nexus.security.user.UserRoleMappingCreatedEvent;
import org.sonatype.nexus.security.user.UserRoleMappingDeletedEvent;
import org.sonatype.nexus.security.user.UserRoleMappingEvent;
import org.sonatype.nexus.security.user.UserRoleMappingUpdatedEvent;

@Named
@Singleton
public class UserRoleMappingAuditor
extends AuditorSupport
implements EventAware {
    public static final String DOMAIN = "security.user-role-mapping";

    public UserRoleMappingAuditor() {
        this.registerType(UserRoleMappingCreatedEvent.class, "created");
        this.registerType(UserRoleMappingDeletedEvent.class, "deleted");
        this.registerType(UserRoleMappingUpdatedEvent.class, "updated");
    }

    @Subscribe
    @AllowConcurrentEvents
    public void on(UserRoleMappingEvent event) {
        if (this.isRecording()) {
            AuditData data = new AuditData();
            data.setDomain(DOMAIN);
            data.setType(this.type(event.getClass()));
            data.setContext(event.getUserId());
            Map attributes = data.getAttributes();
            attributes.put("id", event.getUserId());
            attributes.put("source", event.getUserSource());
            attributes.put("roles", UserRoleMappingAuditor.string(event.getRoles()));
            this.record(data);
        }
    }
}

