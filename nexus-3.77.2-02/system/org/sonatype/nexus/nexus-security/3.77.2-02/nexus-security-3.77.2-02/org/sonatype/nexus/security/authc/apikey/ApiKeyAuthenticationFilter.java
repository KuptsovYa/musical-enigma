/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.servlet.ServletRequest
 *  javax.servlet.ServletResponse
 *  javax.servlet.http.HttpServletRequest
 *  org.apache.shiro.authc.AuthenticationToken
 *  org.apache.shiro.web.util.WebUtils
 *  org.sonatype.nexus.common.text.Strings2
 */
package org.sonatype.nexus.security.authc.apikey;

import com.google.common.base.Preconditions;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.util.WebUtils;
import org.sonatype.nexus.common.text.Strings2;
import org.sonatype.nexus.security.authc.NexusApiKeyAuthenticationToken;
import org.sonatype.nexus.security.authc.NexusBasicHttpAuthenticationFilter;
import org.sonatype.nexus.security.authc.apikey.ApiKeyExtractor;

public class ApiKeyAuthenticationFilter
extends NexusBasicHttpAuthenticationFilter {
    private static final String NX_APIKEY_PRINCIPAL = ApiKeyAuthenticationFilter.class.getName() + ".principal";
    private static final String NX_APIKEY_TOKEN = ApiKeyAuthenticationFilter.class.getName() + ".apiKey";
    public static final String NAME = "nx-apikey-authc";
    private final Map<String, ApiKeyExtractor> apiKeys;

    @Inject
    public ApiKeyAuthenticationFilter(Map<String, ApiKeyExtractor> apiKeys) {
        this.apiKeys = (Map)Preconditions.checkNotNull(apiKeys);
    }

    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest http = WebUtils.toHttp((ServletRequest)request);
        for (Map.Entry<String, ApiKeyExtractor> apiKeyEntry : this.apiKeys.entrySet()) {
            String apiKey = apiKeyEntry.getValue().extract(http);
            if (null == apiKey) continue;
            this.log.trace("ApiKeyExtractor {} detected presence of API Key", (Object)apiKeyEntry.getKey());
            request.setAttribute(NX_APIKEY_PRINCIPAL, (Object)apiKeyEntry.getKey());
            request.setAttribute(NX_APIKEY_TOKEN, (Object)apiKey);
            return true;
        }
        return super.isLoginAttempt(request, response);
    }

    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String principal = (String)request.getAttribute(NX_APIKEY_PRINCIPAL);
        String token = (String)request.getAttribute(NX_APIKEY_TOKEN);
        if (!Strings2.isBlank((String)principal) && !Strings2.isBlank((String)token)) {
            return new NexusApiKeyAuthenticationToken(principal, token.toCharArray(), request.getRemoteHost());
        }
        return super.createToken(request, response);
    }
}

