/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  org.apache.shiro.authc.HostAuthenticationToken
 */
package org.sonatype.nexus.security.authc;

import com.google.common.base.Preconditions;
import org.apache.shiro.authc.HostAuthenticationToken;

public class HttpHeaderAuthenticationToken
implements HostAuthenticationToken {
    private final String headerName;
    private final String headerValue;
    private final String host;

    public HttpHeaderAuthenticationToken(String headerName, String headerValue, String host) {
        this.headerName = (String)Preconditions.checkNotNull((Object)headerName);
        this.headerValue = (String)Preconditions.checkNotNull((Object)headerValue);
        this.host = host;
    }

    public String getPrincipal() {
        return this.headerValue;
    }

    public Object getCredentials() {
        return null;
    }

    public String getHost() {
        return this.host;
    }

    public String getHeaderName() {
        return this.headerName;
    }

    public String getHeaderValue() {
        return this.headerValue;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder(this.getClass().getName());
        buf.append(" - ").append(this.getPrincipal());
        if (this.host != null) {
            buf.append(" (").append(this.host).append(")");
        }
        return buf.toString();
    }
}

