/*
 * Decompiled with CFR 0.152.
 */
package codeguard.licensing;

import codeguard.licensing.pnd;
import codeguard.licensing.zsv;
import codeguard.licensing.zts;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseContentException;
import de.schlichtherle.license.NoLicenseInstalledException;
import de.schlichtherle.xml.GenericCertificate;
import java.io.File;
import org.sonatype.licensing.CustomLicenseContent;
import org.sonatype.licensing.LicenseValidator;
import org.sonatype.licensing.trial.TrialLicenseParam;

public class tpg
extends zts {
    private final LicenseValidator mus;

    public tpg(TrialLicenseParam trialLicenseParam, LicenseValidator licenseValidator) {
        super(trialLicenseParam);
        this.mus = licenseValidator == null ? new zsv() : licenseValidator;
    }

    protected synchronized CustomLicenseContent zxn(byte[] byArray, pnd pnd2) throws Exception {
        return new CustomLicenseContent(super.itm(byArray, pnd2));
    }

    protected synchronized CustomLicenseContent omj(File file, pnd pnd2) throws Exception {
        return new CustomLicenseContent(super.itm(file, pnd2));
    }

    protected synchronized CustomLicenseContent clk(byte[] byArray, pnd pnd2) throws Exception {
        return new CustomLicenseContent(super.omj(byArray, pnd2));
    }

    protected synchronized CustomLicenseContent omj(pnd pnd2) throws Exception {
        try {
            return (CustomLicenseContent)this.zxn(pnd2);
        }
        catch (LicenseContentException licenseContentException) {
            throw licenseContentException;
        }
        catch (Exception exception) {
            return new CustomLicenseContent(super.itm(pnd2));
        }
    }

    protected synchronized LicenseContent zxn(pnd pnd2) throws Exception {
        GenericCertificate genericCertificate = this.bab();
        if (genericCertificate != null) {
            return (LicenseContent)genericCertificate.getContent();
        }
        byte[] byArray = this.vep();
        if (byArray == null) {
            throw new NoLicenseInstalledException(this.eui().getSubject());
        }
        genericCertificate = this.pnd().zxn(byArray);
        pnd2.omj(genericCertificate);
        LicenseContent licenseContent = (LicenseContent)genericCertificate.getContent();
        this.zxn(licenseContent);
        this.itm(genericCertificate);
        return licenseContent;
    }

    public CustomLicenseContent rkn(File file) throws Exception {
        return new CustomLicenseContent(this.itm(tpg.omj(file)));
    }

    @Override
    protected synchronized void zxn(LicenseContent licenseContent) throws LicenseContentException {
        this.mus.validate(licenseContent, this.eui());
    }

    @Override
    protected /* synthetic */ LicenseContent itm(pnd pnd2) throws Exception {
        return this.omj(pnd2);
    }

    @Override
    protected /* synthetic */ LicenseContent omj(byte[] byArray, pnd pnd2) throws Exception {
        return this.clk(byArray, pnd2);
    }

    @Override
    protected /* synthetic */ LicenseContent itm(byte[] byArray, pnd pnd2) throws Exception {
        return this.zxn(byArray, pnd2);
    }

    @Override
    protected /* synthetic */ LicenseContent itm(File file, pnd pnd2) throws Exception {
        return this.omj(file, pnd2);
    }
}

