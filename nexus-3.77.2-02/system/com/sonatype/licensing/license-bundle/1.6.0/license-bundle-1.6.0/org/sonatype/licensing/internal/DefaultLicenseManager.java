/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Provider
 */
package org.sonatype.licensing.internal;

import codeguard.licensing.aec;
import codeguard.licensing.zsv;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseContentException;
import java.io.File;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import org.sonatype.licensing.CustomLicenseContent;
import org.sonatype.licensing.LicenseKey;
import org.sonatype.licensing.LicenseKeyRequest;
import org.sonatype.licensing.LicenseManager;
import org.sonatype.licensing.LicenseParam;
import org.sonatype.licensing.LicenseValidator;
import org.sonatype.licensing.LicensingException;
import org.sonatype.licensing.feature.Feature;
import org.sonatype.licensing.feature.FeatureValidator;

@Named(value="licensing.default")
@Deprecated
public class DefaultLicenseManager
implements LicenseManager {
    private final Provider<LicenseKey> cgs;
    private final FeatureValidator fmh;
    private final LicenseValidator wst;

    public DefaultLicenseManager(Provider<LicenseKey> provider, FeatureValidator featureValidator) {
        this(provider, featureValidator, new zsv());
    }

    @Inject
    public DefaultLicenseManager(Provider<LicenseKey> provider, FeatureValidator featureValidator, @Nullable LicenseValidator licenseValidator) {
        this.cgs = provider;
        this.fmh = featureValidator;
        this.wst = licenseValidator;
    }

    @Override
    public LicenseKey createLicense(LicenseParam licenseParam, LicenseKeyRequest licenseKeyRequest) throws LicensingException {
        Object object;
        try {
            object = licenseKeyRequest.getLicenseKeyFile();
            File file = ((File)object).getParentFile();
            if (!file.exists()) {
                file.mkdirs();
            }
            new aec(licenseParam, this.wst).itm((LicenseContent)licenseKeyRequest.getLicenseContent(), (File)object);
        }
        catch (LicenseContentException licenseContentException) {
            throw new LicensingException(this.itm(licenseContentException), licenseContentException);
        }
        catch (Exception exception) {
            throw new LicensingException("Unable to create license", exception);
        }
        object = (LicenseKey)this.cgs.get();
        object.populateFromLicenseContent(licenseKeyRequest.getLicenseContent());
        return object;
    }

    @Override
    public LicenseKey installLicense(LicenseParam licenseParam, File file) throws LicensingException {
        LicenseKey licenseKey;
        try {
            licenseKey = (LicenseKey)this.cgs.get();
            CustomLicenseContent customLicenseContent = (CustomLicenseContent)new aec(licenseParam, this.wst).itm(file);
            licenseKey.populateFromLicenseContent(customLicenseContent);
        }
        catch (LicenseContentException licenseContentException) {
            throw new LicensingException(this.itm(licenseContentException), licenseContentException);
        }
        catch (Exception exception) {
            throw new LicensingException("Unable to install license: " + this.itm(exception), exception);
        }
        return licenseKey;
    }

    @Override
    public void uninstallLicense(LicenseParam licenseParam) throws LicensingException {
        try {
            new aec(licenseParam, this.wst).bao();
        }
        catch (LicenseContentException licenseContentException) {
            throw new LicensingException(this.itm(licenseContentException), licenseContentException);
        }
        catch (Exception exception) {
            throw new LicensingException("Unable to uninstall license: " + this.itm(exception), exception);
        }
    }

    @Override
    public LicenseKey verifyLicense(LicenseParam licenseParam) throws LicensingException {
        LicenseKey licenseKey = (LicenseKey)this.cgs.get();
        try {
            CustomLicenseContent customLicenseContent = (CustomLicenseContent)new aec(licenseParam, this.wst).mif();
            licenseKey.populateFromLicenseContent(customLicenseContent);
        }
        catch (LicenseContentException licenseContentException) {
            throw new LicensingException(this.itm(licenseContentException), licenseContentException);
        }
        catch (Exception exception) {
            throw new LicensingException("Unable to verify license", exception);
        }
        return licenseKey;
    }

    @Override
    public LicenseKey verifyLicense(LicenseParam licenseParam, File file) throws LicensingException {
        LicenseKey licenseKey = (LicenseKey)this.cgs.get();
        try {
            CustomLicenseContent customLicenseContent = new aec(licenseParam, this.wst).rkn(file);
            licenseKey.populateFromLicenseContent(customLicenseContent);
        }
        catch (LicenseContentException licenseContentException) {
            throw new LicensingException(this.itm(licenseContentException), licenseContentException);
        }
        catch (Exception exception) {
            throw new LicensingException("Unable to verify license", exception);
        }
        return licenseKey;
    }

    @Override
    public void validateFeature(LicenseKey licenseKey, Feature feature) throws LicensingException {
        this.fmh.validate(feature, licenseKey);
    }

    private String itm(Exception exception) {
        return exception.getLocalizedMessage() != null ? exception.getLocalizedMessage() : exception.getMessage();
    }
}

