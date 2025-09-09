/*
 * Decompiled with CFR 0.152.
 */
package codeguard.licensing;

import codeguard.licensing.bab;
import codeguard.licensing.pnd;
import codeguard.licensing.zsv;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseContentException;
import java.io.File;
import org.sonatype.licensing.CustomLicenseContent;
import org.sonatype.licensing.LicenseParam;
import org.sonatype.licensing.LicenseValidator;

public class aec
extends bab {
    private final LicenseValidator mus;

    public aec(LicenseParam licenseParam, LicenseValidator licenseValidator) {
        super(licenseParam);
        this.mus = licenseValidator == null ? new zsv() : licenseValidator;
    }

    @Override
    protected synchronized LicenseContent itm(byte[] byArray, pnd pnd2) throws Exception {
        return new CustomLicenseContent(super.itm(byArray, pnd2));
    }

    @Override
    protected synchronized LicenseContent itm(File file, pnd pnd2) throws Exception {
        return new CustomLicenseContent(super.itm(file, pnd2));
    }

    @Override
    protected synchronized LicenseContent omj(byte[] byArray, pnd pnd2) throws Exception {
        return new CustomLicenseContent(super.omj(byArray, pnd2));
    }

    @Override
    protected synchronized LicenseContent itm(pnd pnd2) throws Exception {
        return new CustomLicenseContent(super.itm(pnd2));
    }

    public CustomLicenseContent rkn(File file) throws Exception {
        return new CustomLicenseContent(this.itm(aec.omj(file)));
    }

    @Override
    protected synchronized void zxn(LicenseContent licenseContent) throws LicenseContentException {
        this.mus.validate(licenseContent, this.eui());
    }
}

