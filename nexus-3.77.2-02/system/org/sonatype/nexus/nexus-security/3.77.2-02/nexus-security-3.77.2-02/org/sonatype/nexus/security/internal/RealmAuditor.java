/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.eventbus.AllowConcurrentEvents
 *  com.google.common.eventbus.Subscribe
 *  javax.inject.Inject
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
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.nexus.audit.AuditData;
import org.sonatype.nexus.audit.AuditorSupport;
import org.sonatype.nexus.common.event.EventAware;
import org.sonatype.nexus.security.realm.RealmConfigurationChangedEvent;
import org.sonatype.nexus.security.realm.RealmManager;

@Named
@Singleton
public class RealmAuditor
extends AuditorSupport
implements EventAware {
    public static final String DOMAIN = "security.realm";
    private final RealmManager realmManager;

    @Inject
    public RealmAuditor(RealmManager realmManager) {
        this.realmManager = realmManager;
    }

    @Subscribe
    @AllowConcurrentEvents
    public void on(RealmConfigurationChangedEvent event) {
        if (this.isRecording()) {
            AuditData data = new AuditData();
            data.setDomain(DOMAIN);
            data.setType("changed");
            data.setContext("system");
            Map attributes = data.getAttributes();
            attributes.put("realms", RealmAuditor.string(this.realmManager.getConfiguredRealmIds()));
            this.record(data);
        }
    }
}

