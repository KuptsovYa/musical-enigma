/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.Lists
 *  javax.inject.Inject
 *  javax.servlet.ServletRequest
 *  javax.servlet.ServletResponse
 *  org.apache.shiro.authc.AuthenticationToken
 */
package org.sonatype.nexus.security.authc;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.shiro.authc.AuthenticationToken;
import org.sonatype.nexus.security.authc.AuthenticationTokenFactory;
import org.sonatype.nexus.security.authc.NexusBasicHttpAuthenticationFilter;

public class NexusAuthenticationFilter
extends NexusBasicHttpAuthenticationFilter {
    public static final String NAME = "nx-authc";
    private List<AuthenticationTokenFactory> factories = Lists.newArrayList();

    @Inject
    public void install(List<AuthenticationTokenFactory> factories) {
        this.factories = (List)Preconditions.checkNotNull(factories);
    }

    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        AuthenticationToken token = this.createAuthenticationToken(request, response);
        return token != null || super.isLoginAttempt(request, response);
    }

    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        AuthenticationToken token = this.createAuthenticationToken(request, response);
        if (token != null) {
            return token;
        }
        return super.createToken(request, response);
    }

    private AuthenticationToken createAuthenticationToken(ServletRequest request, ServletResponse response) {
        for (AuthenticationTokenFactory factory : this.factories) {
            try {
                AuthenticationToken token = factory.createToken(request, response);
                if (token == null) continue;
                this.log.debug("Token '{}' created by {}", (Object)token, (Object)factory);
                return token;
            }
            catch (Exception e) {
                this.log.warn("Factory {} failed to create an authentication token {}/{}", new Object[]{factory, e.getClass().getName(), e.getMessage(), this.log.isDebugEnabled() ? e : null});
            }
        }
        return null;
    }
}

