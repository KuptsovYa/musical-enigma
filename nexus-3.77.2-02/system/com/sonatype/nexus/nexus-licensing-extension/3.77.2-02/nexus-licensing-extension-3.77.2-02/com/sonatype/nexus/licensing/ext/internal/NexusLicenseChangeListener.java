/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.licensing.product.LicenseChangeListener
 *  org.sonatype.licensing.product.ProductLicenseKey
 *  org.sonatype.nexus.common.event.EventManager
 */
package com.sonatype.nexus.licensing.ext.internal;

import com.google.common.base.Preconditions;
import com.sonatype.nexus.licensing.ext.LicenseChangedEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.licensing.product.LicenseChangeListener;
import org.sonatype.licensing.product.ProductLicenseKey;
import org.sonatype.nexus.common.event.EventManager;

@Named
@Singleton
public class NexusLicenseChangeListener
extends ComponentSupport
implements LicenseChangeListener {
    private final EventManager eventManager;

    @Inject
    public NexusLicenseChangeListener(EventManager eventManager) {
        this.eventManager = (EventManager)Preconditions.checkNotNull((Object)eventManager);
    }

    public void licenseChanged(ProductLicenseKey licenseKey, boolean valid) {
        if (valid) {
            this.log.info("License changed; valid: {}", (Object)valid);
        }
        this.eventManager.post((Object)new LicenseChangedEvent(licenseKey, valid));
    }
}

