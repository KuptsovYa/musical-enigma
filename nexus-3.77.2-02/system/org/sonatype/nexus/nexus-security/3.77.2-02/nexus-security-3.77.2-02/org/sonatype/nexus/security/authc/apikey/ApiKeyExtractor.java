/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.servlet.http.HttpServletRequest
 */
package org.sonatype.nexus.security.authc.apikey;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;

public interface ApiKeyExtractor {
    @Nullable
    public String extract(HttpServletRequest var1);
}

