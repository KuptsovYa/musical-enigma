/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.Lists
 *  com.google.common.eventbus.Subscribe
 *  com.google.inject.Key
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Provider
 *  javax.inject.Singleton
 *  org.apache.shiro.cache.Cache
 *  org.apache.shiro.mgt.RealmSecurityManager
 *  org.apache.shiro.realm.AuthenticatingRealm
 *  org.apache.shiro.realm.AuthorizingRealm
 *  org.apache.shiro.realm.Realm
 *  org.eclipse.sisu.inject.BeanLocator
 *  org.sonatype.goodies.common.Mutex
 *  org.sonatype.nexus.common.event.EventManager
 *  org.sonatype.nexus.common.stateguard.Guarded
 *  org.sonatype.nexus.common.stateguard.StateGuardLifecycleSupport
 */
package org.sonatype.nexus.security.internal;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.eclipse.sisu.inject.BeanLocator;
import org.sonatype.goodies.common.Mutex;
import org.sonatype.nexus.common.event.EventManager;
import org.sonatype.nexus.common.stateguard.Guarded;
import org.sonatype.nexus.common.stateguard.StateGuardLifecycleSupport;
import org.sonatype.nexus.security.UserPrincipalsExpired;
import org.sonatype.nexus.security.authc.UserPasswordChanged;
import org.sonatype.nexus.security.authz.AuthorizationConfigurationChanged;
import org.sonatype.nexus.security.internal.AuthenticatingRealmImpl;
import org.sonatype.nexus.security.realm.RealmConfiguration;
import org.sonatype.nexus.security.realm.RealmConfigurationChangedEvent;
import org.sonatype.nexus.security.realm.RealmConfigurationEvent;
import org.sonatype.nexus.security.realm.RealmConfigurationStore;
import org.sonatype.nexus.security.realm.RealmManager;
import org.sonatype.nexus.security.realm.SecurityRealm;

@Named
@Singleton
public class RealmManagerImpl
extends StateGuardLifecycleSupport
implements RealmManager {
    private final BeanLocator beanLocator;
    private final EventManager eventManager;
    private final RealmConfigurationStore store;
    private final Provider<RealmConfiguration> defaults;
    private final RealmSecurityManager realmSecurityManager;
    private final Map<String, Realm> availableRealms;
    private final Mutex lock = new Mutex();
    private RealmConfiguration configuration;
    private final boolean enableAuthorizationRealmManagement;

    @Inject
    public RealmManagerImpl(BeanLocator beanLocator, EventManager eventManager, RealmConfigurationStore store, @Named(value="initial") Provider<RealmConfiguration> defaults, RealmSecurityManager realmSecurityManager, Map<String, Realm> availableRealms, @Named(value="${nexus.security.enableAuthorizationRealmManagement:-false}") boolean enableAuthorizationRealmManagement) {
        this.beanLocator = (BeanLocator)Preconditions.checkNotNull((Object)beanLocator);
        this.eventManager = (EventManager)Preconditions.checkNotNull((Object)eventManager);
        this.store = (RealmConfigurationStore)Preconditions.checkNotNull((Object)store);
        this.log.debug("Store: {}", (Object)store);
        this.defaults = (Provider)Preconditions.checkNotNull(defaults);
        this.log.debug("Defaults: {}", defaults);
        this.realmSecurityManager = (RealmSecurityManager)Preconditions.checkNotNull((Object)realmSecurityManager);
        this.availableRealms = (Map)Preconditions.checkNotNull(availableRealms);
        this.enableAuthorizationRealmManagement = enableAuthorizationRealmManagement;
    }

    protected void doStart() throws Exception {
        this.installRealms();
        this.eventManager.register((Object)this);
    }

    protected void doStop() throws Exception {
        this.eventManager.unregister((Object)this);
        this.configuration = null;
        Collection realms = this.realmSecurityManager.getRealms();
        if (realms != null) {
            for (Realm realm : realms) {
                if (realm instanceof AuthenticatingRealm) {
                    ((AuthenticatingRealm)realm).setAuthenticationCache(null);
                }
                if (!(realm instanceof AuthorizingRealm)) continue;
                ((AuthorizingRealm)realm).setAuthorizationCache(null);
            }
        }
    }

    private RealmConfiguration newEntity() {
        return this.store.newEntity();
    }

    private RealmConfiguration loadConfiguration() {
        RealmConfiguration model = this.store.load();
        if (model == null) {
            model = (RealmConfiguration)this.defaults.get();
            Preconditions.checkNotNull((Object)model);
            this.log.info("Using default configuration: {}", (Object)model);
        } else {
            this.log.info("Loaded configuration: {}", (Object)model);
        }
        return model;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private RealmConfiguration getConfigurationInternal() {
        Mutex mutex = this.lock;
        synchronized (mutex) {
            if (this.configuration == null) {
                this.configuration = this.loadConfiguration();
            }
            return this.configuration;
        }
    }

    private RealmConfiguration getConfiguration() {
        return this.getConfigurationInternal().copy();
    }

    private void setConfiguration(RealmConfiguration configuration) {
        Preconditions.checkNotNull((Object)configuration);
        this.maybeAddAuthorizingRealm(configuration.getRealmNames());
        this.changeConfiguration(configuration, true);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void changeConfiguration(RealmConfiguration configuration, boolean save) {
        RealmConfiguration model = configuration.copy();
        this.log.info("Changing configuration: {}", (Object)model);
        Mutex mutex = this.lock;
        synchronized (mutex) {
            if (save) {
                this.store.save(model);
            }
            this.configuration = model;
        }
        this.installRealms();
        this.eventManager.post((Object)new RealmConfigurationChangedEvent(model));
    }

    private void installRealms() {
        List<Realm> realms = this.resolveRealms();
        this.log.debug("Installing realms: {}", realms);
        this.realmSecurityManager.setRealms(realms);
    }

    private List<Realm> resolveRealms() {
        ArrayList result = Lists.newArrayList();
        RealmConfiguration model = this.getConfigurationInternal();
        ArrayList<String> configuredRealmIds = new ArrayList<String>(model.getRealmNames());
        this.maybeAddAuthorizingRealm(configuredRealmIds);
        this.log.debug("Available realms: {}", this.availableRealms);
        for (String configuredRealmId : configuredRealmIds) {
            Realm realm = this.availableRealms.get(configuredRealmId);
            if (realm == null) {
                this.log.debug("Failed to look up realm '{}' as a component, trying reflection", (Object)configuredRealmId);
                try {
                    realm = (Realm)this.getClass().getClassLoader().loadClass(configuredRealmId).newInstance();
                }
                catch (Exception e) {
                    this.log.error("Unable to lookup security realms", (Throwable)e);
                }
            }
            if (realm == null) continue;
            result.add(realm);
        }
        return result;
    }

    @Override
    public boolean isRealmEnabled(String realmName) {
        Preconditions.checkNotNull((Object)realmName);
        return this.getConfigurationInternal().getRealmNames().contains(realmName);
    }

    @Override
    public void enableRealm(String realmName, boolean enable) {
        if (enable) {
            this.enableRealm(realmName);
        } else {
            this.disableRealm(realmName);
        }
    }

    @Override
    public void enableRealm(String realmName) {
        Preconditions.checkNotNull((Object)realmName);
        this.log.debug("Enabling realm: {}", (Object)realmName);
        RealmConfiguration model = this.getConfiguration();
        if (!model.getRealmNames().contains(realmName)) {
            model.getRealmNames().add(realmName);
            this.setConfiguration(model);
        } else {
            this.log.debug("Realm already enabled: {}", (Object)realmName);
        }
    }

    @Override
    public void enableRealm(String realmName, int index) {
        ArrayList<String> configuredRealms = new ArrayList<String>(this.getConfiguration().getRealmNames());
        configuredRealms.remove(realmName);
        if (index > configuredRealms.size()) {
            this.log.debug("Enabling realm: {} as last member", (Object)realmName);
            configuredRealms.add(realmName);
        } else {
            this.log.debug("Enabling realm: {} at position: {}", (Object)realmName, (Object)index);
            configuredRealms.add(index, realmName);
        }
        this.setConfiguredRealmIds(configuredRealms);
    }

    @Override
    public void disableRealm(String realmName) {
        Preconditions.checkNotNull((Object)realmName);
        if (!this.enableAuthorizationRealmManagement && "NexusAuthorizingRealm".equals(realmName)) {
            this.log.error("Cannot disable the {} realm", (Object)"NexusAuthorizingRealm");
        } else {
            this.log.debug("Disabling realm: {}", (Object)realmName);
            RealmConfiguration model = this.getConfiguration();
            model.getRealmNames().remove(realmName);
            this.setConfiguration(model);
        }
    }

    @Subscribe
    public void on(RealmConfigurationEvent event) {
        if (!event.isLocal()) {
            this.changeConfiguration(event.getConfiguration(), false);
        }
    }

    @Subscribe
    public void onEvent(UserPrincipalsExpired event) {
        this.clearAuthcRealmCaches();
    }

    @Subscribe
    public void onEvent(AuthorizationConfigurationChanged event) {
        this.clearAuthzRealmCaches();
    }

    @Subscribe
    public void onEvent(UserPasswordChanged event) {
        if (event.isClearCache()) {
            this.clearAuthcRealmCacheForUserId(event.getUserId());
        }
    }

    private void clearAuthcRealmCacheForUserId(String userId) {
        ((Collection)Optional.of(this.realmSecurityManager).map(RealmSecurityManager::getRealms).orElse(Collections.emptyList())).stream().filter(realm -> realm instanceof AuthenticatingRealmImpl).map(realm -> (AuthenticatingRealmImpl)((Object)realm)).findFirst().ifPresent(realm -> realm.clearCache(userId));
    }

    private void clearAuthcRealmCaches() {
        Collection realms = this.realmSecurityManager.getRealms();
        if (realms != null) {
            for (Realm realm : realms) {
                Cache cache;
                if (!(realm instanceof AuthenticatingRealm) || (cache = ((AuthenticatingRealm)realm).getAuthenticationCache()) == null) continue;
                this.log.debug("Clearing cache: {}", (Object)cache);
                cache.clear();
            }
        }
    }

    private void clearAuthzRealmCaches() {
        Collection realms = this.realmSecurityManager.getRealms();
        if (realms != null) {
            for (Realm realm : realms) {
                Cache cache;
                if (!(realm instanceof AuthorizingRealm) || (cache = ((AuthorizingRealm)realm).getAuthorizationCache()) == null) continue;
                this.log.debug("Clearing cache: {}", (Object)cache);
                cache.clear();
            }
        }
    }

    @Override
    public List<SecurityRealm> getAvailableRealms() {
        return this.getAvailableRealms(false);
    }

    @Override
    public List<SecurityRealm> getAvailableRealms(boolean includeHidden) {
        return StreamSupport.stream(this.beanLocator.locate(Key.get(Realm.class, Named.class)).spliterator(), false).filter(entry -> {
            if (includeHidden || this.enableAuthorizationRealmManagement) {
                return true;
            }
            return !"NexusAuthorizingRealm".equals(((Named)entry.getKey()).value());
        }).map(entry -> new SecurityRealm(((Named)entry.getKey()).value(), entry.getDescription())).sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName())).collect(Collectors.toList());
    }

    @Override
    @Guarded(by={"STARTED"})
    public List<String> getConfiguredRealmIds() {
        List availableRealmIds = this.getAvailableRealms().stream().map(SecurityRealm::getId).collect(Collectors.toList());
        return this.getConfiguredRealmIds(false).stream().filter(availableRealmIds::contains).collect(Collectors.toList());
    }

    @Override
    @Guarded(by={"STARTED"})
    public List<String> getConfiguredRealmIds(boolean includeHidden) {
        return this.getConfiguration().getRealmNames().stream().filter(realmName -> {
            if (includeHidden || this.enableAuthorizationRealmManagement) {
                return true;
            }
            return !"NexusAuthorizingRealm".equals(realmName);
        }).collect(Collectors.toList());
    }

    @Override
    @Guarded(by={"STARTED"})
    public void setConfiguredRealmIds(List<String> realmIds) {
        ArrayList<String> realmIdsToSave = new ArrayList<String>(realmIds);
        RealmConfiguration realmConfiguration = this.getConfiguration();
        realmConfiguration.setRealmNames(realmIdsToSave);
        this.setConfiguration(realmConfiguration);
    }

    private void maybeAddAuthorizingRealm(List<String> realmIds) {
        if (!this.enableAuthorizationRealmManagement) {
            realmIds.remove("NexusAuthorizingRealm");
            realmIds.add("NexusAuthorizingRealm");
        }
    }
}

