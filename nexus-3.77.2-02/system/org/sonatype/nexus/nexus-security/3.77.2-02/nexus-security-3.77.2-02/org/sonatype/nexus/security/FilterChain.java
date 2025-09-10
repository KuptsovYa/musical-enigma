/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 */
package org.sonatype.nexus.security;

import com.google.common.base.Preconditions;

public class FilterChain {
    private final String pathPattern;
    private final String filterExpression;

    public FilterChain(String pathPattern, String filterExpression) {
        this.pathPattern = (String)Preconditions.checkNotNull((Object)pathPattern);
        this.filterExpression = (String)Preconditions.checkNotNull((Object)filterExpression);
    }

    public String getPathPattern() {
        return this.pathPattern;
    }

    public String getFilterExpression() {
        return this.filterExpression;
    }

    public String toString() {
        return this.getClass().getSimpleName() + "{pathPattern='" + this.pathPattern + "', filterExpression='" + this.filterExpression + "'}";
    }
}

