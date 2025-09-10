/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Joiner
 *  com.google.inject.AbstractModule
 *  com.google.inject.name.Names
 */
package org.sonatype.nexus.security;

import com.google.common.base.Joiner;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import java.lang.annotation.Annotation;
import org.sonatype.nexus.security.FilterChain;

public abstract class FilterChainModule
extends AbstractModule {
    protected void addFilterChain(String pathPattern, String filterExpression) {
        this.bind(FilterChain.class).annotatedWith((Annotation)Names.named((String)pathPattern)).toInstance((Object)new FilterChain(pathPattern, filterExpression));
    }

    protected void addFilterChain(String pathPattern, String ... filterExpression) {
        this.addFilterChain(pathPattern, Joiner.on((String)",").join((Object[])filterExpression));
    }
}

