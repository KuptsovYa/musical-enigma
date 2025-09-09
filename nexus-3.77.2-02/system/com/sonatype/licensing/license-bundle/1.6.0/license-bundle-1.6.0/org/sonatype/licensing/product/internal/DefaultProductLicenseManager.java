/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.codehaus.plexus.util.FileUtils
 *  org.codehaus.plexus.util.io.InputStreamFacade
 *  org.codehaus.plexus.util.io.RawInputStreamFacade
 */
package org.sonatype.licensing.product.internal;

import com.google.common.base.Preconditions;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.io.InputStreamFacade;
import org.codehaus.plexus.util.io.RawInputStreamFacade;
import org.sonatype.licensing.LicenseKey;
import org.sonatype.licensing.LicensingException;
import org.sonatype.licensing.feature.Feature;
import org.sonatype.licensing.product.LicenseBuilder;
import org.sonatype.licensing.product.LicenseChangeNotifier;
import org.sonatype.licensing.product.ProductLicenseKey;
import org.sonatype.licensing.product.ProductLicenseManager;
import org.sonatype.licensing.trial.TrialLicenseManager;
import org.sonatype.licensing.trial.TrialLicenseParam;

@Named(value="licensing.default")
@Singleton
public class DefaultProductLicenseManager
implements ProductLicenseManager {
    private final TrialLicenseManager aff;
    private final LicenseBuilder zuc;
    private final LicenseChangeNotifier wnl;

    @Inject
    public DefaultProductLicenseManager(TrialLicenseManager trialLicenseManager, LicenseBuilder licenseBuilder, LicenseChangeNotifier licenseChangeNotifier) {
        this.aff = trialLicenseManager;
        this.zuc = licenseBuilder;
        this.wnl = (LicenseChangeNotifier)Preconditions.checkNotNull((Object)licenseChangeNotifier);
    }

    @Override
    public ProductLicenseKey getLicenseDetails() throws LicensingException {
        LicenseKey licenseKey = this.aff.verifyLicense(this.zuc.buildPublicParam());
        return this.itm(licenseKey);
    }

    private ProductLicenseKey itm(LicenseKey licenseKey) throws LicensingException {
        if (licenseKey instanceof ProductLicenseKey) {
            return (ProductLicenseKey)licenseKey;
        }
        if (licenseKey == null) {
            return null;
        }
        throw new LicensingException("Key implementation is not a ProductLicenseKey: " + licenseKey.getClass());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public ProductLicenseKey getLicenseDetails(InputStream inputStream) throws LicensingException, IOException {
        File file = null;
        try {
            file = File.createTempFile("license-", ".lic");
            FileUtils.copyStreamToFile((InputStreamFacade)new RawInputStreamFacade(inputStream), (File)file);
            LicenseKey licenseKey = this.aff.verifyLicense(this.zuc.buildPublicParam(), file);
            ProductLicenseKey productLicenseKey = this.itm(licenseKey);
            return productLicenseKey;
        }
        finally {
            if (file != null) {
                file.delete();
            }
        }
    }

    private void itm(ProductLicenseKey productLicenseKey, boolean bl) {
        this.wnl.notifyListeners(productLicenseKey, bl, null);
    }

    @Override
    public void installLicense(InputStream inputStream) throws IOException, LicensingException {
        File file = null;
        try {
            file = File.createTempFile("license-", ".lic");
            FileUtils.copyStreamToFile((InputStreamFacade)new RawInputStreamFacade(inputStream), (File)file);
            TrialLicenseParam trialLicenseParam = this.zuc.buildPublicParam();
            ProductLicenseKey productLicenseKey = this.itm(file, trialLicenseParam);
            this.itm(productLicenseKey, true);
        }
        catch (LicensingException licensingException) {
            this.itm(this.itm(licensingException.getKey()), false);
            throw licensingException;
        }
        finally {
            if (file != null) {
                file.delete();
            }
        }
    }

    private synchronized ProductLicenseKey itm(File file, TrialLicenseParam trialLicenseParam) throws LicensingException {
        return this.itm(this.aff.installLicense(trialLicenseParam, file));
    }

    @Override
    public void uninstallLicense() throws LicensingException {
        this.mpl();
        this.itm(null, false);
    }

    private synchronized void mpl() throws LicensingException {
        this.aff.uninstallLicense(this.zuc.buildPublicParam());
    }

    @Override
    public void verifyLicenseAndFeature(Feature feature) throws LicensingException {
        this.verifyFeature(this.itm(this.aff.verifyLicense(this.zuc.buildPublicParam())), feature);
    }

    @Override
    public void verifyFeature(ProductLicenseKey productLicenseKey, Feature feature) throws LicensingException {
        this.aff.validateFeature(productLicenseKey, feature);
    }
}

