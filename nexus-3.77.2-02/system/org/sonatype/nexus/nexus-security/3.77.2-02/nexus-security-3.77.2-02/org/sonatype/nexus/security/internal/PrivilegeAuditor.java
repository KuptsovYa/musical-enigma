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
import org.sonatype.nexus.security.privilege.Privilege;
import org.sonatype.nexus.security.privilege.PrivilegeCreatedEvent;
import org.sonatype.nexus.security.privilege.PrivilegeDeletedEvent;
import org.sonatype.nexus.security.privilege.PrivilegeEvent;
import org.sonatype.nexus.security.privilege.PrivilegeUpdatedEvent;

@Named
@Singleton
public class PrivilegeAuditor
extends AuditorSupport
implements EventAware {
    public static final String DOMAIN = "security.privilege";

    public PrivilegeAuditor() {
        this.registerType(PrivilegeCreatedEvent.class, "created");
        this.registerType(PrivilegeDeletedEvent.class, "deleted");
        this.registerType(PrivilegeUpdatedEvent.class, "updated");
    }

    @Subscribe
    @AllowConcurrentEvents
    public void on(PrivilegeEvent event) {
        if (this.isRecording()) {
            Privilege privilege = event.getPrivilege();
            AuditData data = new AuditData();
            data.setDomain(DOMAIN);
            data.setType(this.type(event.getClass()));
            data.setContext(privilege.getId());
            Map attributes = data.getAttributes();
            attributes.put("id", privilege.getId());
            attributes.put("name", privilege.getName());
            attributes.put("type", privilege.getType());
            for (Map.Entry<String, String> entry : privilege.getProperties().entrySet()) {
                attributes.put("property." + entry.getKey(), entry.getValue());
            }
            this.record(data);
        }
    }
}

