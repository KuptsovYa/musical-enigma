/*
 * Decompiled with CFR 0.152.
 */
package codeguard.licensing;

import codeguard.licensing.bab;
import codeguard.licensing.baj;
import codeguard.licensing.chr;
import codeguard.licensing.eui;
import codeguard.licensing.pnd;
import codeguard.licensing.qyf;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseParam;
import de.schlichtherle.license.NoLicenseInstalledException;
import java.util.Calendar;
import java.util.Date;
import javax.security.auth.x500.X500Principal;

public class zts
extends bab {
    private static final String tlv = new qyf(new long[]{6540942263743768505L, -6059958959638303164L, 6377465279464853391L}).toString();
    private static final String bqj = new qyf(new long[]{5508595700258006979L, 4658549383733098011L, 4696761698113697678L, 7544341226212718087L, 8821059208312938461L, 302546095417378367L, 4227466611521490562L, -1772332321125311578L}).toString();
    private static final String dyd = new qyf(new long[]{8356037110043899424L, 4664078144517272142L}).toString();
    private pnd ews;
    private static final String pid = seo + baj.omj(new qyf(new long[]{-6534999889298787160L, -4220048377725910008L}).toString());

    protected zts() {
    }

    public zts(chr chr2) {
        this.omj(chr2);
    }

    @Override
    public synchronized void itm(LicenseParam licenseParam) {
        this.omj(licenseParam);
    }

    private void omj(LicenseParam licenseParam) {
        chr chr2 = (chr)licenseParam;
        eui eui2 = chr2.getFTPKeyStoreParam();
        if (eui2 == null) {
            throw new NullPointerException(tlv);
        }
        if (eui2.equals(chr2.getKeyStoreParam())) {
            throw new IllegalArgumentException(bqj);
        }
        int n = chr2.getFTPDays();
        if (0 >= n || n > 365) {
            throw new IllegalArgumentException(new qyf(new long[]{-8447909007698265338L, 3963998191526449092L}).toString());
        }
        super.itm(chr2);
    }

    @Override
    protected synchronized LicenseContent itm(pnd pnd2) throws Exception {
        try {
            return super.itm(pnd2);
        }
        catch (Exception exception) {
            chr chr2 = (chr)this.eui();
            pnd pnd3 = this.klp();
            byte[] byArray = this.vep();
            if (byArray != null) {
                return super.itm(pnd3);
            }
            if (!chr2.isFTPEligible()) {
                throw new NoLicenseInstalledException(chr2.getSubject());
            }
            LicenseContent licenseContent = chr2.createFTPLicenseContent();
            licenseContent.setNotAfter(this.itm(chr2.getFTPDays()));
            licenseContent = this.itm(this.itm(licenseContent, pnd3), pnd3);
            chr2.removeFTPEligibility();
            chr2.ftpGranted(licenseContent);
            return licenseContent;
        }
    }

    @Override
    protected synchronized void omj(LicenseContent licenseContent) {
        if (licenseContent.getHolder() == null) {
            licenseContent.setHolder(new X500Principal(pid));
        }
        super.omj(licenseContent);
    }

    protected synchronized pnd klp() throws Exception {
        if (this.ews == null) {
            this.ews = new pnd(((chr)this.eui()).getFTPKeyStoreParam());
        }
        return this.ews;
    }

    protected Date itm(int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(zts.rkn());
        calendar.add(5, n);
        return calendar.getTime();
    }
}

