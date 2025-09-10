/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.goodies.common.Locks
 *  org.sonatype.nexus.common.event.EventManager
 */
package org.sonatype.nexus.security.config;

import com.google.common.base.Preconditions;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Nullable;
import javax.inject.Inject;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.goodies.common.Locks;
import org.sonatype.nexus.common.event.EventManager;
import org.sonatype.nexus.security.config.CPrivilege;
import org.sonatype.nexus.security.config.MemorySecurityConfiguration;
import org.sonatype.nexus.security.config.SecurityConfiguration;
import org.sonatype.nexus.security.config.SecurityConfigurationManager;
import org.sonatype.nexus.security.config.SecurityContributor;
import org.sonatype.nexus.security.internal.SecurityContributionChangedEvent;

public class MutableSecurityContributor
extends ComponentSupport
implements SecurityContributor {
    private final SecurityConfiguration model = new MemorySecurityConfiguration();
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private boolean initialized;
    @Nullable
    private EventManager eventManager;
    @Nullable
    private SecurityConfigurationManager configurationManager;

    @Inject
    protected void initialize(EventManager eventManager, SecurityConfigurationManager configurationManager) {
        Preconditions.checkState((!this.initialized ? 1 : 0) != 0, (Object)"already initialized");
        this.eventManager = (EventManager)Preconditions.checkNotNull((Object)eventManager);
        this.configurationManager = (SecurityConfigurationManager)Preconditions.checkNotNull((Object)configurationManager);
        this.initial(this.model);
        this.initialized = true;
    }

    protected void initial(SecurityConfiguration model) {
    }

    @Override
    public SecurityConfiguration getContribution() {
        Preconditions.checkState((boolean)this.initialized, (Object)"not initialized");
        Lock lock = Locks.read((ReadWriteLock)this.readWriteLock);
        try {
            SecurityConfiguration securityConfiguration = this.model;
            return securityConfiguration;
        }
        finally {
            lock.unlock();
        }
    }

    public void apply(Mutator mutator) {
        Preconditions.checkState((boolean)this.initialized, (Object)"not initialized");
        Preconditions.checkNotNull((Object)mutator);
        Lock lock = Locks.write((ReadWriteLock)this.readWriteLock);
        try {
            mutator.apply(this.model, this.configurationManager);
        }
        finally {
            lock.unlock();
        }
        this.eventManager.post((Object)new SecurityContributionChangedEvent());
    }

    protected void maybeAddPrivilege(SecurityConfiguration model, CPrivilege privilege) {
        if (model.getPrivilege(privilege.getId()) == null) {
            model.addPrivilege(privilege);
        }
    }

    public static interface Mutator {
        public void apply(SecurityConfiguration var1, SecurityConfigurationManager var2);
    }
}

