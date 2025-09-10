/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.inject.Key
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.servlet.Filter
 *  javax.servlet.FilterConfig
 *  javax.servlet.ServletContext
 *  org.apache.shiro.web.filter.mgt.DefaultFilterChainManager
 *  org.eclipse.sisu.BeanEntry
 *  org.eclipse.sisu.Mediator
 *  org.eclipse.sisu.inject.BeanLocator
 *  org.slf4j.Logger
 *  org.sonatype.goodies.common.Loggers
 */
package org.sonatype.nexus.security;

import com.google.common.base.Preconditions;
import com.google.inject.Key;
import java.util.Enumeration;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.eclipse.sisu.BeanEntry;
import org.eclipse.sisu.Mediator;
import org.eclipse.sisu.inject.BeanLocator;
import org.slf4j.Logger;
import org.sonatype.goodies.common.Loggers;
import org.sonatype.nexus.security.FilterChain;

@Singleton
class DynamicFilterChainManager
extends DefaultFilterChainManager {
    private static final Logger log = Loggers.getLogger(DynamicFilterChainManager.class);
    private final List<FilterChain> filterChains;
    private volatile boolean refreshChains;

    @Inject
    public DynamicFilterChainManager(@Named(value="SHIRO") ServletContext servletContext, List<FilterChain> filterChains, BeanLocator locator) {
        super((FilterConfig)new DelegatingFilterConfig("SHIRO", (ServletContext)Preconditions.checkNotNull((Object)servletContext)));
        this.filterChains = (List)Preconditions.checkNotNull(filterChains);
        locator.watch(Key.get(Filter.class, Named.class), (Mediator)new FilterInstaller(), (Object)this);
        locator.watch(Key.get(FilterChain.class), (Mediator)new FilterChainRefresher(), (Object)this);
    }

    public boolean hasChains() {
        this.refreshChains();
        return super.hasChains();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void refreshChains() {
        if (this.refreshChains) {
            DynamicFilterChainManager dynamicFilterChainManager = this;
            synchronized (dynamicFilterChainManager) {
                if (this.refreshChains) {
                    this.getChainNames().clear();
                    for (FilterChain filterChain : this.filterChains) {
                        try {
                            this.createChain(filterChain.getPathPattern(), filterChain.getFilterExpression());
                        }
                        catch (IllegalArgumentException e) {
                            log.warn("Problem registering: {}", (Object)filterChain, (Object)e);
                        }
                    }
                    this.refreshChains = false;
                }
            }
        }
    }

    private static class DelegatingFilterConfig
    implements FilterConfig {
        private final String filterName;
        private final ServletContext servletContext;

        DelegatingFilterConfig(String filterName, ServletContext servletContext) {
            this.filterName = filterName;
            this.servletContext = servletContext;
        }

        public String getFilterName() {
            return this.filterName;
        }

        public ServletContext getServletContext() {
            return this.servletContext;
        }

        public String getInitParameter(String name) {
            return this.servletContext.getInitParameter(name);
        }

        public Enumeration<String> getInitParameterNames() {
            return this.servletContext.getInitParameterNames();
        }
    }

    private static class FilterInstaller
    implements Mediator<Named, Filter, DynamicFilterChainManager> {
        private FilterInstaller() {
        }

        public void add(BeanEntry<Named, Filter> entry, DynamicFilterChainManager manager) {
            manager.addFilter(((Named)entry.getKey()).value(), (Filter)entry.getValue(), true);
        }

        public void remove(BeanEntry<Named, Filter> entry, DynamicFilterChainManager manager) {
            manager.getFilters().remove(((Named)entry.getKey()).value());
        }
    }

    private static class FilterChainRefresher
    implements Mediator<Named, FilterChain, DynamicFilterChainManager> {
        private FilterChainRefresher() {
        }

        public void add(BeanEntry<Named, FilterChain> entry, DynamicFilterChainManager manager) {
            manager.refreshChains = true;
        }

        public void remove(BeanEntry<Named, FilterChain> entry, DynamicFilterChainManager manager) {
            manager.refreshChains = true;
        }
    }
}

