/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.shiro.subject.PrincipalCollection
 */
package org.sonatype.nexus.security.authc.apikey;

import java.time.OffsetDateTime;
import org.apache.shiro.subject.PrincipalCollection;

public interface ApiKey {
    public char[] getApiKey();

    public PrincipalCollection getPrincipals();

    public OffsetDateTime getCreated();

    default public String getPrimaryPrincipal() {
        if (this.getPrincipals() == null) {
            return null;
        }
        return this.getPrincipals().getPrimaryPrincipal().toString();
    }
}

