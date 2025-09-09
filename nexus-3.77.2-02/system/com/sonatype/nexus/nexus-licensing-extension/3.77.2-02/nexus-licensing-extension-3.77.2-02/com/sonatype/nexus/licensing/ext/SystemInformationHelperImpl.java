/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.eventbus.AllowConcurrentEvents
 *  com.google.common.eventbus.Subscribe
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.licensing.LicensingException
 *  org.sonatype.licensing.feature.Feature
 *  org.sonatype.licensing.product.ProductLicenseKey
 *  org.sonatype.licensing.product.util.LicenseFingerprinter
 *  org.sonatype.nexus.common.app.ManagedLifecycle
 *  org.sonatype.nexus.common.app.ManagedLifecycle$Phase
 *  org.sonatype.nexus.common.app.SystemInformationHelper
 *  org.sonatype.nexus.common.event.EventAware
 *  org.sonatype.nexus.common.stateguard.StateGuardLifecycleSupport
 */
package com.sonatype.nexus.licensing.ext;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.sonatype.nexus.licensing.ext.AbstractApplicationLicense;
import com.sonatype.nexus.licensing.ext.LicenseChangedEvent;
import com.sonatype.nexus.licensing.ext.LicenseManager;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.licensing.LicensingException;
import org.sonatype.licensing.feature.Feature;
import org.sonatype.licensing.product.ProductLicenseKey;
import org.sonatype.licensing.product.util.LicenseFingerprinter;
import org.sonatype.nexus.common.app.ManagedLifecycle;
import org.sonatype.nexus.common.app.SystemInformationHelper;
import org.sonatype.nexus.common.event.EventAware;
import org.sonatype.nexus.common.stateguard.StateGuardLifecycleSupport;

@Named(value="nexus-license")
@Singleton
@ManagedLifecycle(phase=ManagedLifecycle.Phase.SERVICES)
public class SystemInformationHelperImpl
extends StateGuardLifecycleSupport
implements SystemInformationHelper,
EventAware {
    private final LicenseFingerprinter fingerprinter;
    private final List<Feature> availableFeatures;
    private final LicenseManager licenseManager;
    private ApplicationLicenseImpl applicationLicense;

    @Inject
    public SystemInformationHelperImpl(LicenseFingerprinter fingerprinter, List<Feature> availableFeatures, LicenseManager licenseManager) {
        this.fingerprinter = (LicenseFingerprinter)Preconditions.checkNotNull((Object)fingerprinter);
        this.availableFeatures = (List)Preconditions.checkNotNull(availableFeatures);
        this.licenseManager = (LicenseManager)Preconditions.checkNotNull((Object)licenseManager);
    }

    protected void doStart() {
        ProductLicenseKey licenseKey = null;
        try {
            licenseKey = this.licenseManager.getLicenseDetails();
        }
        catch (LicensingException licensingException) {
            this.log.debug(licensingException.getMessage());
        }
        this.applicationLicense = new ApplicationLicenseImpl(this.fingerprinter, this.availableFeatures, licenseKey);
    }

    public HashMap<String, Object> getValue() {
        HashMap<String, Object> licenseInfoMap = new HashMap<String, Object>();
        licenseInfoMap.put("licenseRequired", this.applicationLicense.isRequired());
        licenseInfoMap.put("licenseValid", this.applicationLicense.isValid());
        licenseInfoMap.put("licenseInstalled", this.applicationLicense.isInstalled());
        if (this.applicationLicense.isInstalled()) {
            licenseInfoMap.put("licenseExpired", this.applicationLicense.isExpired());
            licenseInfoMap.put("licenseFingerprint", this.applicationLicense.getFingerprint());
            this.applicationLicense.getAttributes().forEach(licenseInfoMap::put);
        }
        return licenseInfoMap;
    }

    @Subscribe
    @AllowConcurrentEvents
    void on(LicenseChangedEvent event) {
        if (this.applicationLicense != null) {
            this.applicationLicense.setLicenseKey(event.getLicenseKey());
        }
    }

    private static class ApplicationLicenseImpl
    extends AbstractApplicationLicense {
        ApplicationLicenseImpl(LicenseFingerprinter fingerPrinter, List<Feature> availableFeatures, ProductLicenseKey licenseKey) {
            super(fingerPrinter, availableFeatures, licenseKey);
        }

        @Override
        public void on(LicenseChangedEvent event) {
        }

        public void refresh() {
        }

        @Override
        public boolean isRequired() {
            return super.getLicenseKey() != null;
        }
    }
}

