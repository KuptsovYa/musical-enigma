/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.httpclient.HttpDefaultsCustomizer
 *  org.sonatype.nexus.rapture.StateContributor
 */
package org.sonatype.nexus.coreui.internal.http;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.httpclient.HttpDefaultsCustomizer;
import org.sonatype.nexus.rapture.StateContributor;

@Named
@Singleton
public class HttpStateContributor
extends ComponentSupport
implements StateContributor {
    private final HttpDefaultsCustomizer customizer;
    private boolean featureFlag;

    @Inject
    public HttpStateContributor(@Named(value="${nexus.react.httpSettings:-true}") Boolean featureFlag, HttpDefaultsCustomizer customizer) {
        this.customizer = customizer;
        this.featureFlag = featureFlag;
    }

    public Map<String, Object> getState() {
        return ImmutableMap.of((Object)"nexus.react.httpSettings", (Object)this.featureFlag, (Object)"requestTimeout", (Object)this.customizer.getRequestTimeout(), (Object)"retryCount", (Object)this.customizer.getRetryCount());
    }
}

