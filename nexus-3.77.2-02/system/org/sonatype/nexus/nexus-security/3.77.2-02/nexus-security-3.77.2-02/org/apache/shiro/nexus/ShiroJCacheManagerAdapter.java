/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 *  com.google.common.base.Preconditions
 *  javax.cache.Cache
 *  javax.cache.expiry.CreatedExpiryPolicy
 *  javax.cache.expiry.Duration
 *  javax.cache.expiry.EternalExpiryPolicy
 *  javax.inject.Provider
 *  org.apache.shiro.cache.Cache
 *  org.apache.shiro.cache.CacheManager
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.goodies.common.Time
 *  org.sonatype.nexus.cache.CacheHelper
 */
package org.apache.shiro.nexus;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.util.Objects;
import java.util.Optional;
import javax.cache.Cache;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.expiry.EternalExpiryPolicy;
import javax.inject.Provider;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.nexus.ShiroJCacheAdapter;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.goodies.common.Time;
import org.sonatype.nexus.cache.CacheHelper;

public class ShiroJCacheManagerAdapter
extends ComponentSupport
implements CacheManager {
    private final Provider<CacheHelper> cacheHelperProvider;
    private final Provider<Time> defaultTimeToLive;

    public ShiroJCacheManagerAdapter(Provider<CacheHelper> cacheHelperProvider, Provider<Time> defaultTimeToLive) {
        this.cacheHelperProvider = (Provider)Preconditions.checkNotNull(cacheHelperProvider);
        this.defaultTimeToLive = (Provider)Preconditions.checkNotNull(defaultTimeToLive);
    }

    public <K, V> org.apache.shiro.cache.Cache<K, V> getCache(String name) {
        this.log.debug("Getting cache: {}", (Object)name);
        return new ShiroJCacheAdapter<K, V>(this.maybeCreateCache(name));
    }

    @VisibleForTesting
    <K, V> Cache<K, V> maybeCreateCache(String name) {
        if (Objects.equals("shiro-activeSessionCache", name)) {
            return ((CacheHelper)this.cacheHelperProvider.get()).maybeCreateCache(name, EternalExpiryPolicy.factoryOf());
        }
        Time timeToLive = Optional.ofNullable(System.getProperty(name + ".timeToLive")).map(Time::parse).orElse((Time)this.defaultTimeToLive.get());
        return ((CacheHelper)this.cacheHelperProvider.get()).maybeCreateCache(name, CreatedExpiryPolicy.factoryOf((Duration)new Duration(timeToLive.getUnit(), timeToLive.getValue())));
    }
}

