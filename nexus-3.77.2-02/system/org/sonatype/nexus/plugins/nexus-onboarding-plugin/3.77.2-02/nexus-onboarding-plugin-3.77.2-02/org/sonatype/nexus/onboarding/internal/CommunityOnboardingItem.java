/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  org.sonatype.nexus.common.app.ApplicationVersion
 *  org.sonatype.nexus.kv.GlobalKeyValueStore
 *  org.sonatype.nexus.kv.NexusKeyValue
 */
package org.sonatype.nexus.onboarding.internal;

import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import org.sonatype.nexus.common.app.ApplicationVersion;
import org.sonatype.nexus.kv.GlobalKeyValueStore;
import org.sonatype.nexus.kv.NexusKeyValue;
import org.sonatype.nexus.onboarding.OnboardingItem;

public abstract class CommunityOnboardingItem
implements OnboardingItem {
    private final String COMMUNITY = "COMMUNITY";
    private final String EULA_KEY = "nexus.community.eula.accepted";
    protected final ApplicationVersion applicationVersion;
    protected final GlobalKeyValueStore globalKeyValueStore;

    @Inject
    public CommunityOnboardingItem(ApplicationVersion applicationVersion, GlobalKeyValueStore globalKeyValueStore) {
        this.applicationVersion = (ApplicationVersion)Preconditions.checkNotNull((Object)applicationVersion);
        this.globalKeyValueStore = (GlobalKeyValueStore)Preconditions.checkNotNull((Object)globalKeyValueStore);
    }

    @Override
    public boolean applies() {
        boolean isCommunity = false;
        boolean accepted = false;
        Optional eulaStatusOptional = this.globalKeyValueStore.getKey("nexus.community.eula.accepted");
        if (eulaStatusOptional.isPresent()) {
            NexusKeyValue eulaStatus = (NexusKeyValue)eulaStatusOptional.get();
            Map eulaObject = eulaStatus.value();
            accepted = (Boolean)eulaObject.get("accepted");
        }
        return isCommunity && !accepted;
    }
}

