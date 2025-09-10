/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.rapture.StateContributor
 */
package org.sonatype.nexus.coreui.internal.emailsever;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.rapture.StateContributor;

@Named
@Singleton
public class EmailServerStateContributor
extends ComponentSupport
implements StateContributor {
    private final Map<String, Object> state;

    @Inject
    public EmailServerStateContributor(@Named(value="${nexus.react.emailServer:-true}") Boolean featureFlag) {
        this.state = ImmutableMap.of((Object)"nexus.react.emailServer", (Object)featureFlag);
    }

    public Map<String, Object> getState() {
        return this.state;
    }
}

