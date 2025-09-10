/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  org.apache.shiro.subject.PrincipalCollection
 *  org.sonatype.goodies.common.ComponentSupport
 */
package org.sonatype.nexus.security.token;

import com.google.common.base.Preconditions;
import javax.inject.Inject;
import org.apache.shiro.subject.PrincipalCollection;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.security.SecurityHelper;
import org.sonatype.nexus.security.authc.apikey.ApiKey;
import org.sonatype.nexus.security.authc.apikey.ApiKeyService;

public abstract class BearerTokenManager
extends ComponentSupport {
    protected final ApiKeyService apiKeyService;
    protected final SecurityHelper securityHelper;
    private final String format;

    @Inject
    public BearerTokenManager(ApiKeyService apiKeyService, SecurityHelper securityHelper, String format) {
        this.apiKeyService = (ApiKeyService)Preconditions.checkNotNull((Object)apiKeyService);
        this.securityHelper = (SecurityHelper)((Object)Preconditions.checkNotNull((Object)((Object)securityHelper)));
        this.format = (String)Preconditions.checkNotNull((Object)format);
    }

    protected String createToken(PrincipalCollection principals) {
        Preconditions.checkNotNull((Object)principals);
        char[] apiKey = this.apiKeyService.getApiKey(this.format, principals).map(ApiKey::getApiKey).orElse(null);
        if (apiKey != null) {
            return this.format + "." + new String(apiKey);
        }
        return this.format + "." + new String(this.apiKeyService.createApiKey(this.format, principals));
    }

    public boolean deleteToken() {
        PrincipalCollection principals = this.securityHelper.subject().getPrincipals();
        if (this.apiKeyService.getApiKey(this.format, principals).isPresent()) {
            this.apiKeyService.deleteApiKey(this.format, this.securityHelper.subject().getPrincipals());
            return true;
        }
        return false;
    }
}

