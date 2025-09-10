/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.inject.Key
 *  com.google.inject.name.Names
 *  javax.inject.Provider
 *  javax.servlet.Filter
 */
package org.sonatype.nexus.security;

import com.google.common.base.Preconditions;
import com.google.inject.Key;
import com.google.inject.name.Names;
import java.lang.annotation.Annotation;
import javax.inject.Provider;
import javax.servlet.Filter;

public class FilterProviderSupport
implements Provider<Filter> {
    private final Filter filter;

    public FilterProviderSupport(Filter filter) {
        this.filter = (Filter)Preconditions.checkNotNull((Object)filter);
    }

    public Filter get() {
        return this.filter;
    }

    public static Key<Filter> filterKey(String name) {
        return Key.get(Filter.class, (Annotation)Names.named((String)name));
    }
}

