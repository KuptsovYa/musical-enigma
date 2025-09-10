/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.servlet.ServletRequest
 *  javax.servlet.ServletResponse
 *  org.apache.shiro.authc.AuthenticationToken
 */
package org.sonatype.nexus.security.authc;

import javax.annotation.Nullable;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.shiro.authc.AuthenticationToken;

public interface AuthenticationTokenFactory {
    @Nullable
    public AuthenticationToken createToken(ServletRequest var1, ServletResponse var2);
}

