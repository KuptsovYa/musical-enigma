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
package org.sonatype.nexus.coreui.internal.upload;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.rapture.StateContributor;

@Named
@Singleton
public class UploadStateContributor
extends ComponentSupport
implements StateContributor {
    private final Map<String, Object> state;

    @Inject
    public UploadStateContributor(@Named(value="${nexus.react.upload:-true}") boolean featureFlag) {
        this.state = ImmutableMap.of((Object)"nexus.react.upload", (Object)featureFlag);
    }

    public Map<String, Object> getState() {
        return this.state;
    }
}

