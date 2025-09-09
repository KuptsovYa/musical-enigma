/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.licensing.LicensingException
 *  org.sonatype.licensing.feature.Feature
 *  org.sonatype.licensing.product.ProductLicenseKey
 *  org.sonatype.licensing.product.ProductLicenseManager
 *  org.sonatype.licensing.product.util.LicenseContent
 *  org.sonatype.licensing.product.util.LicenseFingerprinter
 *  org.sonatype.nexus.common.app.ApplicationDirectories
 *  org.sonatype.nexus.common.event.EventManager
 */
package com.sonatype.nexus.licensing.ext.internal;

import com.google.common.base.Preconditions;
import com.sonatype.nexus.licensing.ext.LicenseChangedEvent;
import com.sonatype.nexus.licensing.ext.LicenseChangedWrapperEvent;
import com.sonatype.nexus.licensing.ext.LicenseManager;
import com.sonatype.nexus.licensing.ext.LicenseSource;
import com.sonatype.nexus.licensing.ext.LicenseUninstallEvent;
import com.sonatype.nexus.licensing.ext.LicenseUpdateFailedEvent;
import com.sonatype.nexus.licensing.ext.MultiProductPreferenceFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.licensing.LicensingException;
import org.sonatype.licensing.feature.Feature;
import org.sonatype.licensing.product.ProductLicenseKey;
import org.sonatype.licensing.product.ProductLicenseManager;
import org.sonatype.licensing.product.util.LicenseContent;
import org.sonatype.licensing.product.util.LicenseFingerprinter;
import org.sonatype.nexus.common.app.ApplicationDirectories;
import org.sonatype.nexus.common.event.EventManager;

@Named
@Singleton
public class NexusLicenseManager
extends ComponentSupport
implements LicenseManager {
    private final ProductLicenseManager licenseManager;
    private final Feature proFeature;
    private final MultiProductPreferenceFactory productPreferenceFactory;
    private final LicenseFingerprinter licenseFingerprinter;
    private final LicenseContent licenseContent;
    private final ApplicationDirectories applicationDirectories;
    private final EventManager eventManager;

    @Inject
    public NexusLicenseManager(ProductLicenseManager licenseManager, LicenseFingerprinter licenseFingerprinter, LicenseContent licenseContent, @Named(value="NexusProfessional") @Named(value="NexusProfessional") Feature proFeature, MultiProductPreferenceFactory productPreferenceFactory, ApplicationDirectories applicationDirectories, EventManager eventManager) {
        this.licenseManager = (ProductLicenseManager)Preconditions.checkNotNull((Object)licenseManager);
        this.licenseFingerprinter = (LicenseFingerprinter)Preconditions.checkNotNull((Object)licenseFingerprinter);
        this.licenseContent = (LicenseContent)Preconditions.checkNotNull((Object)licenseContent);
        this.proFeature = (Feature)Preconditions.checkNotNull((Object)proFeature);
        this.productPreferenceFactory = (MultiProductPreferenceFactory)Preconditions.checkNotNull((Object)productPreferenceFactory);
        this.applicationDirectories = (ApplicationDirectories)Preconditions.checkNotNull((Object)applicationDirectories);
        this.eventManager = eventManager;
    }

    @Override
    public void installLicense(byte[] licenseFile, LicenseSource source) throws IOException {
        try {
            ProductLicenseKey license = this.licenseManager.getLicenseDetails((InputStream)new ByteArrayInputStream(licenseFile));
            if (!this.isLicenseApplicable(license)) {
                throw new LicensingException("Invalid license");
            }
            String licenseProduct = this.getLicenseProduct(license);
            this.productPreferenceFactory.setProduct(licenseProduct);
            this.licenseManager.installLicense((InputStream)new ByteArrayInputStream(licenseFile));
            this.markEdition(licenseProduct);
            LicenseChangedEvent licenseChangedEvent = new LicenseChangedEvent(license, true);
            this.eventManager.post((Object)new LicenseChangedWrapperEvent(licenseChangedEvent, source));
        }
        catch (LicensingException e) {
            this.eventManager.post((Object)new LicenseUpdateFailedEvent(licenseFile, e.getMessage()));
            throw e;
        }
    }

    private boolean isLicenseApplicable(ProductLicenseKey licenseKey) {
        return licenseKey.getFeatureSet().hasFeature(this.proFeature) || this.isCommunityLicense(licenseKey);
    }

    private boolean isCommunityLicense(ProductLicenseKey licenseKey) {
        return licenseKey.getRawFeatures().contains("NexusCore");
    }

    private String getLicenseProduct(ProductLicenseKey productLicenseKey) {
        if (productLicenseKey.getFeatureSet().hasFeature(this.proFeature)) {
            return "PRO";
        }
        if (this.isCommunityLicense(productLicenseKey)) {
            return "CORE";
        }
        return null;
    }

    @Override
    public void uninstallLicense(LicenseSource source) {
        this.licenseManager.uninstallLicense();
        this.removeProMarker();
        this.eventManager.post((Object)new LicenseUninstallEvent(null, false, source));
    }

    @Override
    public ProductLicenseKey getLicenseDetails() {
        return this.licenseManager.getLicenseDetails();
    }

    @Override
    public ProductLicenseKey getLicenseDetails(InputStream licenseFile) throws IOException {
        return this.licenseManager.getLicenseDetails(licenseFile);
    }

    @Override
    public String getLicenseFingerprint() {
        return this.licenseFingerprinter.calculate();
    }

    @Override
    public byte[] getLicenseFile() {
        return this.licenseContent.raw();
    }

    private File getEditionMarkerFile(String licenseProduct) {
        String filename = "edition_" + licenseProduct.toLowerCase().replace('-', '_');
        return this.applicationDirectories.getWorkDirectory().toPath().resolve(filename).toFile();
    }

    private void markEdition(String licenseProduct) {
        File marker = this.getEditionMarkerFile(licenseProduct);
        try {
            if (marker.createNewFile()) {
                this.log.debug("Created pro edition marker file: {}", (Object)marker);
            }
        }
        catch (IOException e) {
            this.log.error("Failed to create pro edition marker file: {}", (Object)marker, (Object)e);
        }
    }

    private void removeProMarker() {
        File marker = this.getEditionMarkerFile("PRO");
        if (marker.delete()) {
            this.log.debug("Deleted pro edition marker file: {}", (Object)marker);
        }
    }
}

