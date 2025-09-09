/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Provider
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.sonatype.licensing.trial.internal;

import codeguard.licensing.tpg;
import codeguard.licensing.zsv;
import codeguard.licensing.zts;
import de.schlichtherle.license.LicenseContent;
import java.io.File;
import java.util.Date;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.licensing.CustomLicenseContent;
import org.sonatype.licensing.LicenseContentException;
import org.sonatype.licensing.LicenseKey;
import org.sonatype.licensing.LicenseKeyRequest;
import org.sonatype.licensing.LicenseValidator;
import org.sonatype.licensing.LicensingException;
import org.sonatype.licensing.feature.Feature;
import org.sonatype.licensing.feature.FeatureValidator;
import org.sonatype.licensing.trial.TrialLicenseManager;
import org.sonatype.licensing.trial.TrialLicenseParam;

@Named(value="licensing.default")
public class DefaultTrialLicenseManager
implements TrialLicenseManager {
    private final Logger evv = LoggerFactory.getLogger(this.getClass());
    private final Provider<LicenseKey> cgs;
    private final FeatureValidator fmh;
    private final LicenseValidator wst;

    public DefaultTrialLicenseManager(Provider<LicenseKey> provider, FeatureValidator featureValidator) {
        this(provider, featureValidator, new zsv());
    }

    @Inject
    public DefaultTrialLicenseManager(Provider<LicenseKey> provider, FeatureValidator featureValidator, @Nullable LicenseValidator licenseValidator) {
        this.cgs = provider;
        this.fmh = featureValidator;
        this.wst = licenseValidator;
    }

    private void applyProLicense(LicenseKey licenseKey) {
        licenseKey.setContactName("applyProLicense");
        licenseKey.setContactCompany("applyProLicense");
        licenseKey.setContactEmailAddress("applyProLicense@example.com");
        licenseKey.setContactTelephone("1234567890");
        licenseKey.setContactCountry("Russia");
        licenseKey.setEvaluation(false);
        licenseKey.setExpirationDate(new Date(138, 0, 1));
        licenseKey.setEffectiveDate(new Date());
    }

    @Override
    public LicenseKey createLicense(TrialLicenseParam trialLicenseParam, LicenseKeyRequest licenseKeyRequest) throws LicensingException {
        LicenseKey licenseKey = (LicenseKey)this.cgs.get();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(zts.class.getClassLoader());
            new tpg(trialLicenseParam, this.wst).itm((LicenseContent)licenseKeyRequest.getLicenseContent(), licenseKeyRequest.getLicenseKeyFile());
            licenseKey.populateFromLicenseContent(licenseKeyRequest.getLicenseContent());
        }
        catch (LicenseContentException licenseContentException) {
            if (this.evv.isTraceEnabled()) {
                this.evv.trace("createLicense", (Throwable)licenseContentException);
            }
            throw this.itm(licenseKey, licenseContentException);
        }
        catch (Exception exception) {
            if (this.evv.isTraceEnabled()) {
                this.evv.trace("createLicense", (Throwable)exception);
            }
            throw new LicensingException("Unable to create license: " + this.itm(exception), exception);
        }
        finally {
            Thread.currentThread().setContextClassLoader(classLoader);
        }
        return licenseKey;
    }

    @Override
    public LicenseKey installLicense(TrialLicenseParam trialLicenseParam, File file) throws LicensingException {
        LicenseKey licenseKey = (LicenseKey)this.cgs.get();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(zts.class.getClassLoader());
            CustomLicenseContent customLicenseContent = (CustomLicenseContent)new tpg(trialLicenseParam, this.wst).itm(file);
            licenseKey.populateFromLicenseContent(customLicenseContent);
        }
        catch (LicenseContentException licenseContentException) {
            if (this.evv.isTraceEnabled()) {
                this.evv.trace("installLicense", (Throwable)licenseContentException);
            }
            throw this.itm(licenseKey, licenseContentException);
        }
        catch (Exception exception) {
            if (this.evv.isTraceEnabled()) {
                this.evv.trace("installLicense", (Throwable)exception);
            }
            throw new LicensingException("Unable to install license: " + this.itm(exception), exception);
        }
        finally {
            Thread.currentThread().setContextClassLoader(classLoader);
        }
        return licenseKey;
    }

    @Override
    public void uninstallLicense(TrialLicenseParam trialLicenseParam) throws LicensingException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(zts.class.getClassLoader());
            new tpg(trialLicenseParam, this.wst).bao();
        }
        catch (LicenseContentException licenseContentException) {
            if (this.evv.isTraceEnabled()) {
                this.evv.trace("uninstallLicense", (Throwable)licenseContentException);
            }
            throw new LicensingException(this.itm(licenseContentException), licenseContentException);
        }
        catch (Exception exception) {
            if (this.evv.isTraceEnabled()) {
                this.evv.trace("uninstallLicense", (Throwable)exception);
            }
            throw new LicensingException("Unable to uninstall license: " + this.itm(exception), exception);
        }
        finally {
            Thread.currentThread().setContextClassLoader(classLoader);
        }
    }

    @Override
    public LicenseKey verifyLicense(TrialLicenseParam trialLicenseParam) throws LicensingException {
        boolean bl = this.evv.isTraceEnabled();
        if (bl) {
            this.evv.trace("verifyLicense (1) param={}", (Object)trialLicenseParam);
        }
        LicenseKey licenseKey = (LicenseKey)this.cgs.get();
        if (bl) {
            this.evv.trace("verifyLicense (2) key={}", (Object)licenseKey);
        }
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(zts.class.getClassLoader());
            CustomLicenseContent customLicenseContent = (CustomLicenseContent)new tpg(trialLicenseParam, this.wst).mif();
            if (bl) {
                this.evv.trace("verifyLicense (3) content={}", (Object)customLicenseContent);
            }
            licenseKey.populateFromLicenseContent(customLicenseContent);
        }
        catch (LicenseContentException licenseContentException) {
            if (bl) {
                this.evv.trace("verifyLicense", (Throwable)licenseContentException);
            }
            throw this.itm(licenseKey, licenseContentException);
        }
        catch (Exception exception) {
            if (bl) {
                this.evv.trace("verifyLicense", (Throwable)exception);
            }
            throw new LicensingException("Unable to verify license: " + this.itm(exception), exception);
        }
        finally {
            Thread.currentThread().setContextClassLoader(classLoader);
        }
        if (bl) {
            this.evv.trace("verifyLicense (4) key={}", (Object)licenseKey);
        }
        return licenseKey;
    }

    @Override
    public LicenseKey verifyLicense(TrialLicenseParam trialLicenseParam, File file) throws LicensingException {
        boolean bl = this.evv.isTraceEnabled();
        if (bl) {
            this.evv.trace("verifyLicense (1) param={},file={}", (Object)trialLicenseParam, (Object)file);
        }
        LicenseKey licenseKey = (LicenseKey)this.cgs.get();
        if (bl) {
            this.evv.trace("verifyLicense (2) key={}", (Object)licenseKey);
        }
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(zts.class.getClassLoader());
            CustomLicenseContent customLicenseContent = new tpg(trialLicenseParam, this.wst).rkn(file);
            licenseKey.populateFromLicenseContent(customLicenseContent);
        }
        catch (LicenseContentException licenseContentException) {
            if (bl) {
                this.evv.trace("verifyLicense", (Throwable)licenseContentException);
            }
            throw this.itm(licenseKey, licenseContentException);
        }
        catch (Exception exception) {
            if (bl) {
                this.evv.trace("verifyLicense", (Throwable)exception);
            }
            throw new LicensingException("Unable to verify license", exception);
        }
        finally {
            Thread.currentThread().setContextClassLoader(classLoader);
        }
        return licenseKey;
    }

    @Override
    public void validateFeature(LicenseKey licenseKey, Feature feature) throws LicensingException {
        this.fmh.validate(feature, licenseKey);
    }

    private String itm(Exception exception) {
        String string = exception.getLocalizedMessage() != null ? exception.getLocalizedMessage() : exception.getMessage();
        return string != null ? string : exception.getClass().getSimpleName();
    }

    private LicensingException itm(LicenseKey licenseKey, LicenseContentException licenseContentException) {
        licenseKey.populateFromLicenseContent((CustomLicenseContent)licenseContentException.getLicenseContent());
        return new LicensingException(licenseKey, this.itm(licenseContentException), licenseContentException);
    }
}

