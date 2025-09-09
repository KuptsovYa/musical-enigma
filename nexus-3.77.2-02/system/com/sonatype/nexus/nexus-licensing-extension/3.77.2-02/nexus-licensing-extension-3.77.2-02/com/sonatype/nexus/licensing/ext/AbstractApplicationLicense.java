/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.eventbus.Subscribe
 *  javax.annotation.Nullable
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.licensing.feature.Feature
 *  org.sonatype.licensing.feature.FeatureSet
 *  org.sonatype.licensing.product.ProductLicenseKey
 *  org.sonatype.licensing.product.util.LicenseFingerprinter
 *  org.sonatype.nexus.common.app.ApplicationLicense
 *  org.sonatype.nexus.common.app.ApplicationLicense$Attributes
 *  org.sonatype.nexus.common.event.EventAware
 *  org.sonatype.nexus.common.time.DateHelper
 */
package com.sonatype.nexus.licensing.ext;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import com.sonatype.nexus.licensing.ext.LicenseChangedEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Nullable;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.licensing.feature.Feature;
import org.sonatype.licensing.feature.FeatureSet;
import org.sonatype.licensing.product.ProductLicenseKey;
import org.sonatype.licensing.product.util.LicenseFingerprinter;
import org.sonatype.nexus.common.app.ApplicationLicense;
import org.sonatype.nexus.common.event.EventAware;
import org.sonatype.nexus.common.time.DateHelper;

public abstract class AbstractApplicationLicense
extends ComponentSupport
implements ApplicationLicense,
EventAware {
    private final LicenseFingerprinter fingerprinter;
    private final List<Feature> availableFeatures;
    private final AtomicReference<ProductLicenseKey> licenseKey = new AtomicReference();
    private final AtomicReference<Map<String, Object>> attributes = new AtomicReference();
    private volatile String fingerprint;

    protected AbstractApplicationLicense(LicenseFingerprinter fingerprinter, List<Feature> availableFeatures) {
        this.fingerprinter = (LicenseFingerprinter)Preconditions.checkNotNull((Object)fingerprinter);
        this.availableFeatures = (List)Preconditions.checkNotNull(availableFeatures);
    }

    protected AbstractApplicationLicense(LicenseFingerprinter fingerprinter, List<Feature> availableFeatures, ProductLicenseKey licenseKey) {
        this.fingerprinter = (LicenseFingerprinter)Preconditions.checkNotNull((Object)fingerprinter);
        this.availableFeatures = (List)Preconditions.checkNotNull(availableFeatures);
        this.licenseKey.set(licenseKey);
    }

    protected void setLicenseKey(ProductLicenseKey licenseKey) {
        this.licenseKey.set(licenseKey);
        this.fingerprint = null;
        this.attributes.set(null);
        this.log.debug("License key: {}", (Object)licenseKey);
    }

    protected ProductLicenseKey getLicenseKey() {
        return this.licenseKey.get();
    }

    public boolean isRequired() {
        return true;
    }

    public boolean isValid() {
        return this.isInstalled() && !this.isExpired();
    }

    public boolean isEvaluation() {
        return this.isValid() && this.licenseKey.get().isEvaluation();
    }

    public boolean isInstalled() {
        return this.licenseKey.get() != null;
    }

    public boolean isExpired() {
        return !this.isInstalled() || new Date().after(this.licenseKey.get().getExpirationDate());
    }

    public Map<String, Object> getAttributes() {
        if (this.attributes.get() == null && this.licenseKey.get() != null) {
            LinkedHashMap<String, Object> attrs = new LinkedHashMap<String, Object>();
            attrs.put(ApplicationLicense.Attributes.EVAL.getKey(), this.licenseKey.get().isEvaluation());
            attrs.put(ApplicationLicense.Attributes.USERS.getKey(), this.licenseKey.get().getLicensedUsers());
            attrs.put(ApplicationLicense.Attributes.FEATURES.getKey(), Collections.unmodifiableList(this.licenseKey.get().getRawFeatures()));
            attrs.put(ApplicationLicense.Attributes.EFFECTIVE_FEATURES.getKey(), this.effectiveFeatures(this.licenseKey.get()));
            attrs.put(ApplicationLicense.Attributes.EFFECTIVE_DATE.getKey(), DateHelper.copy((Date)this.licenseKey.get().getEffectiveDate()));
            attrs.put(ApplicationLicense.Attributes.EXPIRATION_DATE.getKey(), DateHelper.copy((Date)this.licenseKey.get().getExpirationDate()));
            attrs.put(ApplicationLicense.Attributes.CONTACT_NAME.getKey(), this.licenseKey.get().getContactName());
            attrs.put(ApplicationLicense.Attributes.CONTACT_EMAIL.getKey(), this.licenseKey.get().getContactEmailAddress());
            attrs.put(ApplicationLicense.Attributes.CONTACT_COMPANY.getKey(), this.licenseKey.get().getContactCompany());
            attrs.put(ApplicationLicense.Attributes.CONTACT_COUNTRY.getKey(), this.licenseKey.get().getContactCountry());
            this.attributes.set(Collections.unmodifiableMap(attrs));
        }
        return this.attributes.get();
    }

    private List<String> effectiveFeatures(ProductLicenseKey key) {
        FeatureSet featureSet = key.getFeatureSet();
        ArrayList<String> result = new ArrayList<String>();
        for (Feature feature : this.availableFeatures) {
            if (!featureSet.hasFeature(feature)) continue;
            result.add(feature.getId());
        }
        return Collections.unmodifiableList(result);
    }

    @Nullable
    public String getFingerprint() {
        if (this.fingerprint == null && this.licenseKey.get() != null) {
            this.fingerprint = this.fingerprinter.calculate(this.licenseKey.get());
        }
        return this.fingerprint;
    }

    @Subscribe
    public void on(LicenseChangedEvent event) {
        this.setLicenseKey(event.getLicenseKey());
    }
}

