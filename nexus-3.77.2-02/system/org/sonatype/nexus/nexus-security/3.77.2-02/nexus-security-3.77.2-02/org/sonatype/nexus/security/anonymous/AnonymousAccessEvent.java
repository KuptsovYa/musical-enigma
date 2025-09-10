/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 */
package org.sonatype.nexus.security.anonymous;

import com.google.common.base.Preconditions;
import java.util.Date;
import org.sonatype.nexus.security.ClientInfo;

public class AnonymousAccessEvent {
    private final ClientInfo clientInfo;
    private final Date eventDate;

    public AnonymousAccessEvent(ClientInfo clientInfo, Date eventDate) {
        this.clientInfo = (ClientInfo)Preconditions.checkNotNull((Object)clientInfo);
        this.eventDate = (Date)Preconditions.checkNotNull((Object)eventDate);
    }

    public ClientInfo getClientInfo() {
        return this.clientInfo;
    }

    public Date getEventDate() {
        return this.eventDate;
    }
}

