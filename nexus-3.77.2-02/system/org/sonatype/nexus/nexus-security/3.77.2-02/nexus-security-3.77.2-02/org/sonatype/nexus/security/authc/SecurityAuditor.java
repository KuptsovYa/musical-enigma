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
package org.sonatype.nexus.security.authc;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.nexus.audit.AuditData;
import org.sonatype.nexus.audit.AuditorSupport;
import org.sonatype.nexus.common.event.EventAware;
import org.sonatype.nexus.security.authc.LoginEvent;
import org.sonatype.nexus.security.authc.LogoutEvent;
import org.sonatype.nexus.security.authc.SecurityEvent;

@Named
@Singleton
public class SecurityAuditor
extends AuditorSupport
implements EventAware {
    public SecurityAuditor() {
        this.registerType(LoginEvent.class, "login");
        this.registerType(LogoutEvent.class, "logout");
    }

    @Subscribe
    @AllowConcurrentEvents
    public void on(SecurityEvent event) {
        AuditData data = new AuditData();
        data.setDomain(event.getRealm());
        data.setType(this.type(event.getClass()));
        data.getAttributes().put("principal", event.getPrincipal());
        this.record(data);
    }
}

