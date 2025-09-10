/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package org.sonatype.nexus.security;

import javax.annotation.Nullable;
import org.sonatype.nexus.security.ClientInfo;

public interface ClientInfoProvider {
    @Nullable
    public ClientInfo getCurrentThreadClientInfo();

    public void setClientInfo(String var1, String var2);

    public void unsetClientInfo();
}

