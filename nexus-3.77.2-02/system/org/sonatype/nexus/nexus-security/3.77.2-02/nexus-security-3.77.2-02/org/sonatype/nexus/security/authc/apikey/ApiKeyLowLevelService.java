/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  org.apache.shiro.subject.PrincipalCollection
 */
package org.sonatype.nexus.security.authc.apikey;

import java.time.OffsetDateTime;
import java.util.Collection;
import javax.annotation.Nullable;
import org.apache.shiro.subject.PrincipalCollection;
import org.sonatype.nexus.security.authc.apikey.ApiKey;
import org.sonatype.nexus.security.authc.apikey.ApiKeyService;

public interface ApiKeyLowLevelService
extends ApiKeyService {
    public Collection<ApiKey> browse(String var1);

    public Collection<ApiKey> browseByCreatedDate(String var1, OffsetDateTime var2);

    public Collection<ApiKey> browsePaginated(String var1, int var2, int var3);

    default public void persistApiKey(String domain, PrincipalCollection principals, char[] apiKey) {
        this.persistApiKey(domain, principals, apiKey, null);
    }

    public void persistApiKey(String var1, PrincipalCollection var2, char[] var3, @Nullable OffsetDateTime var4);

    public void updateApiKeyRealm(ApiKey var1, PrincipalCollection var2);
}

