/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.eclipse.sisu.Priority
 *  org.sonatype.nexus.rapture.UiPluginDescriptorSupport
 */
package org.sonatype.nexus.onboarding.internal;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.eclipse.sisu.Priority;
import org.sonatype.nexus.rapture.UiPluginDescriptorSupport;

@Named
@Singleton
@Priority(value=2147483347)
public class UiPluginDescriptorImpl
extends UiPluginDescriptorSupport {
    @Inject
    public UiPluginDescriptorImpl() {
        super("nexus-onboarding-plugin");
        this.setNamespace("NX.onboarding");
        this.setConfigClassName("NX.onboarding.app.PluginConfig");
    }
}

