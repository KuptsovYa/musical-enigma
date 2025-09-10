/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Provider
 *  javax.inject.Singleton
 */
package org.sonatype.nexus.security.internal;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import org.sonatype.nexus.security.anonymous.AnonymousConfiguration;

@Named(value="initial")
@Singleton
public class InitialAnonymousConfigurationProvider
implements Provider<AnonymousConfiguration> {
    private final boolean enabled;

    @Inject
    public InitialAnonymousConfigurationProvider(@Named(value="${nexus.security.default.anonymous:-true}") boolean enabled) {
        this.enabled = enabled;
    }

    public AnonymousConfiguration get() {
        return new InitialAnonymousConfiguration();
    }

    private class InitialAnonymousConfiguration
    implements AnonymousConfiguration {
        private InitialAnonymousConfiguration() {
        }

        @Override
        public AnonymousConfiguration copy() {
            return this;
        }

        @Override
        public String getRealmName() {
            return "NexusAuthorizingRealm";
        }

        @Override
        public String getUserId() {
            return "anonymous";
        }

        @Override
        public boolean isEnabled() {
            return InitialAnonymousConfigurationProvider.this.enabled;
        }

        @Override
        public void setEnabled(boolean enabled) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setRealmName(String realmName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setUserId(String userId) {
            throw new UnsupportedOperationException();
        }
    }
}

