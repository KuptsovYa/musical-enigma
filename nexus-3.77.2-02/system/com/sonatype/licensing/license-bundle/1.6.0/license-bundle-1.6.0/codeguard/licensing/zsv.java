/*
 * Decompiled with CFR 0.152.
 */
package codeguard.licensing;

import codeguard.licensing.qyf;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseContentException;
import de.schlichtherle.license.LicenseParam;
import java.util.Date;
import java.util.Locale;
import java.util.prefs.Preferences;
import org.sonatype.licensing.LicenseValidator;

public class zsv
implements LicenseValidator {
    private static final String xlf = new qyf(new long[]{-9211605111142713620L, 391714365510707393L, -7356761750428556372L, 6379560902598103028L}).toString();
    private static final String lsy = new qyf(new long[]{7150026245468079143L, 6314884536402738366L, -1360923923476698800L}).toString();
    private static final String brx = new qyf(new long[]{-3034693013076752554L, -1011266899694033610L, 6775785917404597234L}).toString();
    private static final String mts = new qyf(new long[]{-6084371209004858580L, 3028840747031697166L, -3524637886726219307L}).toString();
    private static final String xae = new qyf(new long[]{1000558500458715757L, -6998261911041258483L, -5490039629745846648L, 3561172928787106880L}).toString();
    private static final String rog = new qyf(new long[]{-3274088377466921882L, -1704115158449736962L, -1134622897105293263L, 2875630655915253859L}).toString();
    private static final String hep = new qyf(new long[]{-3559580260061340089L, 8807812719464926891L, 3255622466169980128L, 3208430498260873670L, 8772089725159421213L}).toString();
    private static final String ygy = new qyf(new long[]{6854702630454082314L, -1676630527348424687L, 4853969635229547239L, -7087814313396201500L, 7133601245775504376L}).toString();
    private static final String jke = new qyf(new long[]{-5670394608177286583L, -3674104453170648872L, 4159301984262248157L, 7442355638167795990L, 4780252201915657674L}).toString();
    private static final String djy = new qyf(new long[]{-6950934198262740461L, -10280221617836935L}).toString();

    @Override
    public void validate(LicenseContent licenseContent, LicenseParam licenseParam) throws LicenseContentException {
        String string = licenseContent.getSubject().toLowerCase(Locale.US);
        String string2 = licenseParam.getSubject().toLowerCase(Locale.US);
        this.itm(licenseContent, string, string2);
        if (licenseContent.getHolder() == null) {
            throw this.itm(lsy, licenseContent);
        }
        if (licenseContent.getIssuer() == null) {
            throw this.itm(brx, licenseContent);
        }
        if (licenseContent.getIssued() == null) {
            throw this.itm(mts, licenseContent);
        }
        Date date = new Date();
        Date date2 = licenseContent.getNotAfter();
        if (date2 != null && date.after(date2)) {
            throw this.itm(xae, licenseContent);
        }
        String string3 = licenseContent.getConsumerType();
        if (string3 == null) {
            throw this.itm(rog, licenseContent);
        }
        Preferences preferences = licenseParam.getPreferences();
        if (preferences != null && preferences.isUserNode()) {
            if (!djy.equalsIgnoreCase(string3)) {
                throw this.itm(hep, licenseContent);
            }
            if (licenseContent.getConsumerAmount() != 1) {
                throw this.itm(ygy, licenseContent);
            }
        } else if (licenseContent.getConsumerAmount() <= 0) {
            throw this.itm(jke, licenseContent);
        }
    }

    protected void itm(LicenseContent licenseContent, String string, String string2) throws LicenseContentException {
        if (string == null || !string.contains(string2)) {
            throw this.itm(xlf, licenseContent);
        }
    }

    private LicenseContentException itm(String string, LicenseContent licenseContent) {
        return new org.sonatype.licensing.LicenseContentException(string, licenseContent);
    }
}

