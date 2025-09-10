/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.eclipse.sisu.Priority
 *  org.sonatype.nexus.rapture.UiPluginDescriptorSupport
 */
package org.sonatype.nexus.coreui.internal;

import javax.inject.Named;
import javax.inject.Singleton;
import org.eclipse.sisu.Priority;
import org.sonatype.nexus.rapture.UiPluginDescriptorSupport;

@Named
@Singleton
@Priority(value=2147483547)
public class UiPluginDescriptorImpl
extends UiPluginDescriptorSupport {
    public UiPluginDescriptorImpl() {
        super("nexus-coreui-plugin");
        this.setNamespace("NX.coreui");
        this.setConfigClassName("NX.coreui.app.PluginConfig");
    }
}

