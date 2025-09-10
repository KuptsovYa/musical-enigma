/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.ImmutableMap
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.common.node.NodeAccess
 *  org.sonatype.nexus.rapture.StateContributor
 */
package org.sonatype.nexus.coreui.internal.welcome;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.common.node.NodeAccess;
import org.sonatype.nexus.rapture.StateContributor;

@Named
@Singleton
public class WelcomeStateContributor
extends ComponentSupport
implements StateContributor {
    public static final String NODE_ID = "nexus.node.id";
    private final NodeAccess nodeAccess;
    private final Boolean featureFlag;

    @Inject
    public WelcomeStateContributor(@Named(value="${nexus.react.welcome:-true}") Boolean featureFlag, NodeAccess nodeAccess) {
        this.nodeAccess = (NodeAccess)Preconditions.checkNotNull((Object)nodeAccess);
        this.featureFlag = featureFlag;
    }

    public Map<String, Object> getState() {
        return ImmutableMap.of((Object)"nexus.react.welcome", (Object)this.featureFlag, (Object)NODE_ID, (Object)this.nodeAccess.getId());
    }
}

