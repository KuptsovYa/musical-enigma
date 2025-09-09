/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.goodies.lifecycle.LifecycleSupport
 *  org.sonatype.licensing.LicensingException
 *  org.sonatype.licensing.product.ProductLicenseKey
 *  org.sonatype.nexus.common.app.ApplicationLicense
 *  org.sonatype.nexus.common.app.ManagedLifecycle
 *  org.sonatype.nexus.common.app.ManagedLifecycle$Phase
 *  org.sonatype.nexus.common.event.EventManager
 */
package com.sonatype.nexus.licensing.ext;

import com.google.common.base.Preconditions;
import com.sonatype.nexus.licensing.ext.LicenseChangedEvent;
import com.sonatype.nexus.licensing.ext.LicenseChangedWrapperEvent;
import com.sonatype.nexus.licensing.ext.LicenseEnvironment;
import com.sonatype.nexus.licensing.ext.LicenseManager;
import com.sonatype.nexus.licensing.ext.LicenseSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.goodies.lifecycle.LifecycleSupport;
import org.sonatype.licensing.LicensingException;
import org.sonatype.licensing.product.ProductLicenseKey;
import org.sonatype.nexus.common.app.ApplicationLicense;
import org.sonatype.nexus.common.app.ManagedLifecycle;
import org.sonatype.nexus.common.event.EventManager;

@Named
@ManagedLifecycle(phase=ManagedLifecycle.Phase.KERNEL)
@Singleton
public class NexusLicenseInstaller
extends LifecycleSupport {
    private static final String ENV_LICENSE = "NEXUS_LICENSE_FILE";
    private final LicenseManager licenseManager;
    private final ApplicationLicense applicationLicense;
    private final LicenseEnvironment licenseEnvironment;
    @Nullable
    private final File licenseFile;
    private final EventManager eventManager;

    @Inject
    public NexusLicenseInstaller(LicenseManager licenseManager, ApplicationLicense applicationLicense, @Nullable @Named(value="${nexus.licenseFile}") @Nullable @Named(value="${nexus.licenseFile}") File licenseFile, EventManager eventManager, LicenseEnvironment licenseEnvironment) {
        this.licenseManager = (LicenseManager)Preconditions.checkNotNull((Object)licenseManager);
        this.applicationLicense = (ApplicationLicense)Preconditions.checkNotNull((Object)applicationLicense);
        this.licenseFile = licenseFile;
        this.eventManager = (EventManager)Preconditions.checkNotNull((Object)eventManager);
        this.licenseEnvironment = (LicenseEnvironment)Preconditions.checkNotNull((Object)licenseEnvironment);
    }

    protected void doStart() throws Exception {
        if (this.licenseFile != null) {
            this.log.info("Using license file from property nexus.licenseFile");
            this.validateLicense(this.licenseFile, LicenseSource.SYSTEM_PROPERTY);
        } else {
            File envLicenseFile = this.licenseEnvironment.getLicenseFile();
            if (envLicenseFile != null && envLicenseFile.canRead()) {
                this.log.info("Using license file from environment variable {} located at {}", (Object)ENV_LICENSE, (Object)envLicenseFile.getPath());
                this.validateLicense(envLicenseFile, LicenseSource.ENVIRONMENT_VARIABLE);
            } else {
                this.log.debug("No file or environmental variable for the license has been detected.");
            }
        }
    }

    private void validateLicense(File licenseFile, LicenseSource source) throws IOException {
        try {
            ProductLicenseKey existing = this.licenseManager.getLicenseDetails();
            if (existing != null) {
                this.log.info("Ignoring License File={}. A license is already installed.", (Object)licenseFile);
            } else {
                this.installLicense(licenseFile, source);
            }
        }
        catch (LicensingException e) {
            this.log.debug("caught LicensingException, indicates no license present", (Throwable)e);
            this.installLicense(licenseFile, source);
        }
    }

    private void installLicense(File licenseFile, LicenseSource source) throws IOException {
        this.log.info("Installing license {}", (Object)licenseFile);
        this.licenseManager.installLicense(Files.readAllBytes(licenseFile.toPath()), source);
        this.applicationLicense.refresh();
        LicenseChangedEvent licenseChangedEvent = new LicenseChangedEvent(null, true);
        this.eventManager.post((Object)new LicenseChangedWrapperEvent(licenseChangedEvent, LicenseSource.UI));
    }
}

