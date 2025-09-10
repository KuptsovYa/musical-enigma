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
import org.sonatype.nexus.security.role.Role;
import org.sonatype.nexus.security.role.RoleCreatedEvent;
import org.sonatype.nexus.security.role.RoleDeletedEvent;
import org.sonatype.nexus.security.role.RoleEvent;
import org.sonatype.nexus.security.role.RoleUpdatedEvent;

@Named
@Singleton
public class RoleAuditor
extends AuditorSupport
implements EventAware {
    public static final String DOMAIN = "security.role";

    public RoleAuditor() {
        this.registerType(RoleCreatedEvent.class, "created");
        this.registerType(RoleDeletedEvent.class, "deleted");
        this.registerType(RoleUpdatedEvent.class, "updated");
    }

    @Subscribe
    @AllowConcurrentEvents
    public void on(RoleEvent event) {
        if (this.isRecording()) {
            Role role = event.getRole();
            AuditData data = new AuditData();
            data.setDomain(DOMAIN);
            data.setType(this.type(event.getClass()));
            data.setContext(role.getRoleId());
            Map attributes = data.getAttributes();
            attributes.put("id", role.getRoleId());
            attributes.put("name", role.getName());
            attributes.put("source", role.getSource());
            attributes.put("roles", RoleAuditor.string(role.getRoles()));
            attributes.put("privileges", RoleAuditor.string(role.getPrivileges()));
            this.record(data);
        }
    }
}

