/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  com.google.common.eventbus.AllowConcurrentEvents
 *  com.google.common.eventbus.Subscribe
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.nexus.audit.AuditData
 *  org.sonatype.nexus.audit.AuditorSupport
 *  org.sonatype.nexus.common.event.EventAware
 */
package org.sonatype.nexus.security.internal;

import com.google.common.collect.Sets;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.nexus.audit.AuditData;
import org.sonatype.nexus.audit.AuditorSupport;
import org.sonatype.nexus.common.event.EventAware;
import org.sonatype.nexus.security.ClientInfo;
import org.sonatype.nexus.security.authc.AuthenticationFailureReason;
import org.sonatype.nexus.security.authc.NexusAuthenticationEvent;

@Named
@Singleton
public class NexusAuthenticationEventAuditor
extends AuditorSupport
implements EventAware {
    private static final String DOMAIN = "security.user";
    private static Set<AuthenticationFailureReason> AUDITABLE_FAILURE_REASONS = new HashSet<AuthenticationFailureReason>();

    @Subscribe
    @AllowConcurrentEvents
    public void on(NexusAuthenticationEvent event) {
        Set<AuthenticationFailureReason> failureReasonsToLog = this.getFailureReasonsToLog(event);
        if (this.isRecording() && !failureReasonsToLog.isEmpty()) {
            AuditData auditData = new AuditData();
            auditData.setType("authentication");
            auditData.setDomain(DOMAIN);
            auditData.setTimestamp(event.getEventDate());
            Map attributes = auditData.getAttributes();
            attributes.put("failureReasons", failureReasonsToLog);
            attributes.put("wasSuccessful", event.isSuccessful());
            if (event.getClientInfo() != null) {
                ClientInfo clientInfo = event.getClientInfo();
                attributes.put("userId", clientInfo.getUserid());
                attributes.put("remoteIp", clientInfo.getRemoteIP());
                attributes.put("userAgent", clientInfo.getUserAgent());
                attributes.put("path", clientInfo.getPath());
            }
            this.record(auditData);
        }
    }

    private Set<AuthenticationFailureReason> getFailureReasonsToLog(NexusAuthenticationEvent event) {
        return Sets.intersection(event.getAuthenticationFailureReasons(), AUDITABLE_FAILURE_REASONS);
    }

    static {
        AUDITABLE_FAILURE_REASONS.add(AuthenticationFailureReason.INCORRECT_CREDENTIALS);
    }
}

