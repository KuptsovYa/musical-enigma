/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing.product;

import org.sonatype.licensing.LicensingException;
import org.sonatype.licensing.trial.TrialLicenseParam;

public interface LicenseBuilder {
    public TrialLicenseParam buildPublicParam() throws LicensingException;

    public String getPublicKeyStorePath();

    public String getPublicKeyStorePassword();

    public String getPublicKeyStoreAlias();

    public String getTrialKeyStorePassword();

    public String getTrialKeyStoreAlias();

    public String getTrialKeyStorePath();

    public String getPreferenceNodePath();
}

