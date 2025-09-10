/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.shiro.subject.PrincipalCollection
 */
package org.sonatype.nexus.security.authc.apikey;

import java.time.OffsetDateTime;
import java.util.Optional;
import org.apache.shiro.subject.PrincipalCollection;
import org.sonatype.nexus.security.authc.apikey.ApiKey;

public interface ApiKeyService {
    public char[] createApiKey(String var1, PrincipalCollection var2);

    public Optional<ApiKey> getApiKey(String var1, PrincipalCollection var2);

    public Optional<ApiKey> getApiKeyByToken(String var1, char[] var2);

    public int count(String var1);

    public int deleteApiKey(String var1, PrincipalCollection var2);

    public int deleteApiKeys(PrincipalCollection var1);

    public int deleteApiKeys(String var1);

    public int deleteApiKeys(OffsetDateTime var1);
}

