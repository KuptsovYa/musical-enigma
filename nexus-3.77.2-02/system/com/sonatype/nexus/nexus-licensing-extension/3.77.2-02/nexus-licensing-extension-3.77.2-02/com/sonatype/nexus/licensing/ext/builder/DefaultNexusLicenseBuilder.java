/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.licensing.PreferencesFactory
 *  org.sonatype.licensing.ProductDetails
 *  org.sonatype.licensing.internal.DefaultKeyStoreParam
 *  org.sonatype.licensing.product.AbstractLicenseBuilder
 *  org.sonatype.licensing.trial.TrialLicenseParam
 *  org.sonatype.licensing.trial.TrialParam
 */
package com.sonatype.nexus.licensing.ext.builder;

import com.google.common.base.Preconditions;
import com.sonatype.nexus.licensing.ext.builder.NexusLicenseBuilder;
import java.util.prefs.Preferences;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.licensing.PreferencesFactory;
import org.sonatype.licensing.ProductDetails;
import org.sonatype.licensing.internal.DefaultKeyStoreParam;
import org.sonatype.licensing.product.AbstractLicenseBuilder;
import org.sonatype.licensing.trial.TrialLicenseParam;
import org.sonatype.licensing.trial.TrialParam;

@Named
@Singleton
public class DefaultNexusLicenseBuilder
extends AbstractLicenseBuilder
implements NexusLicenseBuilder {
    private TrialParam trialParameter;
    private final PreferencesFactory preferencesFactory;

    @Inject
    public DefaultNexusLicenseBuilder(ProductDetails product, TrialParam trialParam, PreferencesFactory preferencesFactory) {
        super(product, trialParam, preferencesFactory);
        this.trialParameter = trialParam;
        this.preferencesFactory = (PreferencesFactory)Preconditions.checkNotNull((Object)preferencesFactory);
    }

    public String getPublicKeyStoreAlias() {
        return PUBLIC_KEYSTORE_ALIAS;
    }

    public String getPublicKeyStorePassword() {
        return PUBLIC_KEYSTORE_PASSWORD;
    }

    public String getPreferenceNodePath() {
        return PACKAGE;
    }

    public String getPublicKeyStorePath() {
        return PUBLIC_KEYSTORE_PATH;
    }

    public String getTrialKeyStoreAlias() {
        return TRIAL_KEYSTORE_ALIAS;
    }

    public String getTrialKeyStorePassword() {
        return TRIAL_KEYSTORE_PASSWORD;
    }

    public String getTrialKeyStorePath() {
        return TRIAL_KEYSTORE_PATH;
    }

    public TrialLicenseParam buildPublicParam() {
        DefaultKeyStoreParam keyStoreParam = new DefaultKeyStoreParam(NexusLicenseBuilder.class, PUBLIC_KEYSTORE_PATH, PUBLIC_KEYSTORE_ALIAS, PUBLIC_KEYSTORE_PASSWORD, null);
        DefaultKeyStoreParam trialStoreParam = new DefaultKeyStoreParam(NexusLicenseBuilder.class, TRIAL_KEYSTORE_PATH, TRIAL_KEYSTORE_ALIAS, TRIAL_KEYSTORE_PASSWORD, TRIAL_KEYSTORE_PASSWORD);
        Preferences preferences = this.preferencesFactory.nodeForPath(PACKAGE);
        return new TrialLicenseParam(SUBJECT, preferences, keyStoreParam, trialStoreParam, this.trialParameter);
    }
}

