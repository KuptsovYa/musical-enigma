/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing.product;

import codeguard.licensing.dyw;
import codeguard.licensing.qyf;
import org.sonatype.licensing.CustomLicenseContent;
import org.sonatype.licensing.LicensingException;
import org.sonatype.licensing.PreferencesFactory;
import org.sonatype.licensing.ProductDetails;
import org.sonatype.licensing.internal.DefaultKeyStoreParam;
import org.sonatype.licensing.product.LicenseBuilder;
import org.sonatype.licensing.trial.TrialLicenseParam;
import org.sonatype.licensing.trial.TrialParam;
import org.sonatype.licensing.util.LicensingUtil;

public abstract class AbstractLicenseBuilder
implements LicenseBuilder {
    private static final String rfk = new qyf(new long[]{-1690058102093644793L, 3975167369736229458L, 6881864084162147984L}).toString();
    private static final String ohr = new qyf(new long[]{348214743486407250L, -5665724013535777408L, 5783893877095155298L, -9222320762758361454L, 6192500194308087316L, 3477852850060463659L, 7694194449945358034L, -362616691434936395L, -8857163466429480282L}).toString();
    private final ProductDetails xrw;
    private final TrialParam uho;
    private final String gyh;
    private final PreferencesFactory xpt;

    public AbstractLicenseBuilder(ProductDetails productDetails, TrialParam trialParam, String string, PreferencesFactory preferencesFactory) {
        this.xrw = productDetails;
        this.uho = trialParam == null ? new itm(null) : trialParam;
        this.gyh = string == null ? LicensingUtil.getPackage(productDetails.getProductName()) : string;
        this.xpt = preferencesFactory;
    }

    public AbstractLicenseBuilder(ProductDetails productDetails, TrialParam trialParam, PreferencesFactory preferencesFactory) {
        this(productDetails, trialParam, null, preferencesFactory);
    }

    public AbstractLicenseBuilder(ProductDetails productDetails, PreferencesFactory preferencesFactory) {
        this(productDetails, null, null, preferencesFactory);
    }

    public AbstractLicenseBuilder(ProductDetails productDetails, String string, PreferencesFactory preferencesFactory) {
        this(productDetails, null, string, preferencesFactory);
        if (this.getClass().getResource(this.getPublicKeyStorePath()) == null) {
            throw new IllegalArgumentException("Could not find public key store resource");
        }
        if (this.getClass().getResource(this.getTrialKeyStorePath()) == null) {
            throw new IllegalArgumentException("Could not find trial key store resource");
        }
    }

    @Override
    public TrialLicenseParam buildPublicParam() throws LicensingException {
        DefaultKeyStoreParam defaultKeyStoreParam = new DefaultKeyStoreParam(this.getClass(), this.getPublicKeyStorePath(), this.getPublicKeyStoreAlias(), this.getPublicKeyStorePassword(), null);
        DefaultKeyStoreParam defaultKeyStoreParam2 = new DefaultKeyStoreParam(this.getClass(), this.getTrialKeyStorePath(), this.getTrialKeyStoreAlias(), this.getTrialKeyStorePassword(), this.getTrialKeyStorePassword());
        return new TrialLicenseParam(this.xrw.getProductName(), this.xpt.nodeForPath(this.gyh), defaultKeyStoreParam, defaultKeyStoreParam2, this.uho);
    }

    @Override
    public String getPreferenceNodePath() {
        return this.gyh;
    }

    @Override
    public String getPublicKeyStoreAlias() {
        return this.xrw.getProductName();
    }

    @Override
    public String getTrialKeyStorePassword() {
        return rfk;
    }

    @Override
    public String getTrialKeyStoreAlias() {
        return rfk;
    }

    @Override
    public String getTrialKeyStorePath() {
        return ohr;
    }

    static class itm
    implements TrialParam {
        private itm() {
        }

        @Override
        public boolean isEligibleForTrial() {
            return false;
        }

        @Override
        public void removeTrialEligibility() {
        }

        @Override
        public int getDaysForTrial() {
            return 1;
        }

        @Override
        public CustomLicenseContent createTrialLicenseContent() {
            throw new UnsupportedOperationException("This trial parameter is never eligible for trial licensing.");
        }

        @Override
        public void trialGranted(CustomLicenseContent customLicenseContent) {
            throw new UnsupportedOperationException("This trial parameter is never eligible for trial licensing.");
        }

        /* synthetic */ itm(dyw dyw2) {
            this();
        }
    }
}

