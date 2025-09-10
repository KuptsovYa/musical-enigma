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
import org.sonatype.nexus.security.anonymous.AnonymousConfiguration;
import org.sonatype.nexus.security.anonymous.AnonymousConfigurationChangedEvent;

@Named
@Singleton
public class AnonymousAuditor
extends AuditorSupport
implements EventAware {
    public static final String DOMAIN = "security.anonymous";

    @Subscribe
    @AllowConcurrentEvents
    public void on(AnonymousConfigurationChangedEvent event) {
        if (this.isRecording()) {
            AnonymousConfiguration configuration = event.getConfiguration();
            AuditData data = new AuditData();
            data.setDomain(DOMAIN);
            data.setType("changed");
            data.setContext("system");
            Map attributes = data.getAttributes();
            attributes.put("enabled", AnonymousAuditor.string((Object)configuration.isEnabled()));
            attributes.put("userId", configuration.getUserId());
            attributes.put("realm", configuration.getRealmName());
            this.record(data);
        }
    }
}

