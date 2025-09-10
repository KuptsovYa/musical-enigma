/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.base.Splitter
 *  com.google.common.collect.Lists
 *  javax.annotation.Nullable
 *  javax.servlet.http.HttpServletRequest
 */
package org.sonatype.nexus.security.token;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import org.sonatype.nexus.security.authc.apikey.ApiKeyExtractor;

public class BearerToken
implements ApiKeyExtractor {
    private final String format;

    public BearerToken(String format) {
        this.format = (String)Preconditions.checkNotNull((Object)format);
    }

    @Override
    @Nullable
    public String extract(HttpServletRequest request) {
        ArrayList parts;
        String headerValue = request.getHeader("Authorization");
        if (headerValue != null && headerValue.startsWith("Bearer ") && (parts = Lists.newArrayList((Iterable)Splitter.on((char)' ').split((CharSequence)headerValue))).size() == 2 && "Bearer".equals(parts.get(0)) && this.matchesFormat(parts)) {
            return ((String)parts.get(1)).replaceAll(this.format + ".", "");
        }
        return null;
    }

    protected boolean matchesFormat(List<String> parts) {
        return parts.get(1).startsWith(this.format);
    }
}

