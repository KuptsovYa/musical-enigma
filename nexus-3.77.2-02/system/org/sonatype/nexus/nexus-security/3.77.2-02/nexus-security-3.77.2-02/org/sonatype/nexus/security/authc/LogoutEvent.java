/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.authc;

import org.sonatype.nexus.security.authc.SecurityEvent;

public class LogoutEvent
extends SecurityEvent {
    public LogoutEvent(String principal, String realm) {
        super(principal, realm);
    }
}

