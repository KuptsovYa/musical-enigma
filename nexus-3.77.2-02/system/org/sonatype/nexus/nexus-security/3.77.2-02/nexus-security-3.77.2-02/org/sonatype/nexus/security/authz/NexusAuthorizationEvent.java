/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.authz;

import java.util.Date;
import org.sonatype.nexus.security.ClientInfo;
import org.sonatype.nexus.security.authz.ResourceInfo;

public class NexusAuthorizationEvent {
    private final ClientInfo clientInfo;
    private final ResourceInfo resourceInfo;
    private final boolean successful;
    private final Date date;

    public NexusAuthorizationEvent(ClientInfo info, ResourceInfo resInfo, boolean successful) {
        this.clientInfo = info;
        this.resourceInfo = resInfo;
        this.successful = successful;
        this.date = new Date();
    }

    public ClientInfo getClientInfo() {
        return this.clientInfo;
    }

    public ResourceInfo getResourceInfo() {
        return this.resourceInfo;
    }

    public boolean isSuccessful() {
        return this.successful;
    }

    public Date getEventDate() {
        return this.date;
    }
}

