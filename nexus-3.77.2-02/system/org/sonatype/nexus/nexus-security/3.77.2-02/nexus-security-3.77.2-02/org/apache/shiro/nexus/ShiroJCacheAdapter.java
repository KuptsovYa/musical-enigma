/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.Iterables
 *  javax.cache.Cache
 *  javax.cache.Cache$Entry
 *  org.apache.shiro.cache.Cache
 *  org.sonatype.goodies.common.ComponentSupport
 */
package org.apache.shiro.nexus;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.cache.Cache;
import org.sonatype.goodies.common.ComponentSupport;

public class ShiroJCacheAdapter<K, V>
extends ComponentSupport
implements org.apache.shiro.cache.Cache<K, V> {
    private final Cache<K, V> cache;
    private final String name;

    public ShiroJCacheAdapter(Cache<K, V> cache) {
        this.cache = (Cache)Preconditions.checkNotNull(cache);
        this.name = cache.getName();
    }

    public V get(K key) {
        return (V)this.cache.get(key);
    }

    public V put(K key, V value) {
        return (V)this.cache.getAndPut(key, value);
    }

    public V remove(K key) {
        return (V)this.cache.getAndRemove(key);
    }

    public void clear() {
        this.cache.clear();
    }

    public int size() {
        return Iterables.size(this.cache);
    }

    public Set<K> keys() {
        HashSet<Object> keys = new HashSet<Object>();
        for (Cache.Entry entry : this.cache) {
            keys.add(entry.getKey());
        }
        return Collections.unmodifiableSet(keys);
    }

    public Collection<V> values() {
        ArrayList<Object> values = new ArrayList<Object>();
        for (Cache.Entry entry : this.cache) {
            values.add(entry.getValue());
        }
        return Collections.unmodifiableCollection(values);
    }

    public String toString() {
        return ((Object)((Object)this)).getClass().getSimpleName() + "{cache=" + this.cache + ", name='" + this.name + "'}";
    }
}

