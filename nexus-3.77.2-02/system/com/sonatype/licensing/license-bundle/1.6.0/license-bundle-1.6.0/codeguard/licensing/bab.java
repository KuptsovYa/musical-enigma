/*
 * Decompiled with CFR 0.152.
 */
package codeguard.licensing;

import codeguard.licensing.bao;
import codeguard.licensing.bxh;
import codeguard.licensing.ifr;
import codeguard.licensing.omj;
import codeguard.licensing.pej;
import codeguard.licensing.pnd;
import codeguard.licensing.pwn;
import codeguard.licensing.qyf;
import codeguard.licensing.vep;
import codeguard.licensing.yiu;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseContentException;
import de.schlichtherle.license.LicenseParam;
import de.schlichtherle.license.NoLicenseInstalledException;
import de.schlichtherle.xml.GenericCertificate;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.prefs.Preferences;
import javax.security.auth.x500.X500Principal;
import javax.swing.filechooser.FileFilter;

public class bab
implements bao,
pej {
    private static final long dyw = 1800000L;
    private static final String fvc = new qyf(new long[]{-2999492566024573771L, -1728025856628382701L}).toString();
    public static final String mpl = new qyf(new long[]{-7559156485370438418L, 5084921010819724770L}).toString();
    private static final String dpq;
    private static final String dvh;
    private static final String juq;
    private static final String xjx;
    protected static final String seo;
    private static final String ydu;
    private static final String djy;
    private static final String tpg;
    private static final String xlf;
    private static final String lsy;
    private static final String brx;
    private static final String mts;
    private static final String ndv;
    private static final String xae;
    private static final String rog;
    private static final String hep;
    private static final String ygy;
    private static final String jke;
    private static final String ajo;
    private static final String lpm;
    private LicenseParam ajw;
    private pnd xah;
    private ifr zoz;
    private GenericCertificate ldh;
    private long rjt;
    private FileFilter oaa;
    private Preferences mif;

    protected static Date rkn() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTime();
    }

    protected bab() {
    }

    public bab(LicenseParam licenseParam) {
        this.omj(licenseParam);
    }

    public LicenseParam eui() {
        return this.ajw;
    }

    public synchronized void itm(LicenseParam licenseParam) {
        this.omj(licenseParam);
    }

    private void omj(LicenseParam licenseParam) {
        if (licenseParam == null) {
            throw new NullPointerException(dpq);
        }
        if (licenseParam.getSubject() == null) {
            throw new NullPointerException(dvh);
        }
        if (licenseParam.getKeyStoreParam() == null) {
            throw new NullPointerException(juq);
        }
        omj omj2 = licenseParam.getCipherParam();
        if (omj2 == null) {
            throw new NullPointerException(xjx);
        }
        pwn.chr().itm(omj2.getKeyPwd());
        this.ajw = licenseParam;
        this.xah = null;
        this.ldh = null;
        this.rjt = 0L;
        this.oaa = null;
        this.mif = null;
    }

    public final synchronized void itm(LicenseContent licenseContent, File file) throws Exception {
        this.itm(licenseContent, this.yiu(), file);
    }

    protected synchronized void itm(LicenseContent licenseContent, pnd pnd2, File file) throws Exception {
        bab.itm(this.itm(licenseContent, pnd2), file);
    }

    @Override
    public final synchronized byte[] itm(LicenseContent licenseContent) throws Exception {
        return this.itm(licenseContent, this.yiu());
    }

    protected synchronized byte[] itm(LicenseContent licenseContent, pnd pnd2) throws Exception {
        this.omj(licenseContent);
        this.zxn(licenseContent);
        GenericCertificate genericCertificate = pnd2.omj(licenseContent);
        byte[] byArray = this.pnd().zxn(genericCertificate);
        return byArray;
    }

    public final synchronized LicenseContent itm(File file) throws Exception {
        return this.itm(file, this.yiu());
    }

    protected synchronized LicenseContent itm(File file, pnd pnd2) throws Exception {
        return this.itm(bab.omj(file), pnd2);
    }

    protected synchronized LicenseContent itm(byte[] byArray, pnd pnd2) throws Exception {
        GenericCertificate genericCertificate = this.pnd().zxn(byArray);
        pnd2.omj(genericCertificate);
        LicenseContent licenseContent = (LicenseContent)genericCertificate.getContent();
        this.zxn(licenseContent);
        this.omj(byArray);
        this.itm(genericCertificate);
        return licenseContent;
    }

    public final synchronized LicenseContent mif() throws Exception {
        return this.itm(this.yiu());
    }

    protected synchronized LicenseContent itm(pnd pnd2) throws Exception {
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

    @Override
    public final synchronized LicenseContent itm(byte[] byArray) throws Exception {
        return this.omj(byArray, this.yiu());
    }

    protected synchronized LicenseContent omj(byte[] byArray, pnd pnd2) throws Exception {
        GenericCertificate genericCertificate = this.pnd().zxn(byArray);
        pnd2.omj(genericCertificate);
        LicenseContent licenseContent = (LicenseContent)genericCertificate.getContent();
        this.zxn(licenseContent);
        return licenseContent;
    }

    public synchronized void bao() throws Exception {
        this.omj((byte[])null);
        this.itm((GenericCertificate)null);
    }

    protected synchronized void omj(LicenseContent licenseContent) {
        Preferences preferences;
        if (licenseContent.getHolder() == null) {
            licenseContent.setHolder(new X500Principal(ydu));
        }
        if (licenseContent.getSubject() == null) {
            licenseContent.setSubject(this.eui().getSubject());
        }
        if (licenseContent.getConsumerType() == null && (preferences = this.eui().getPreferences()) != null) {
            if (preferences.isUserNode()) {
                licenseContent.setConsumerType(djy);
            } else {
                licenseContent.setConsumerType(tpg);
            }
            licenseContent.setConsumerAmount(1);
        }
        if (licenseContent.getIssuer() == null) {
            licenseContent.setIssuer(new X500Principal(seo + this.eui().getSubject()));
        }
        if (licenseContent.getIssued() == null) {
            licenseContent.setIssued(new Date());
        }
        if (licenseContent.getNotBefore() == null) {
            licenseContent.setNotBefore(bab.rkn());
        }
    }

    protected synchronized void zxn(LicenseContent licenseContent) throws LicenseContentException {
        LicenseParam licenseParam = this.eui();
        if (!licenseParam.getSubject().equals(licenseContent.getSubject())) {
            throw new LicenseContentException(xlf);
        }
        if (licenseContent.getHolder() == null) {
            throw new LicenseContentException(lsy);
        }
        if (licenseContent.getIssuer() == null) {
            throw new LicenseContentException(brx);
        }
        if (licenseContent.getIssued() == null) {
            throw new LicenseContentException(mts);
        }
        Date date = new Date();
        Date date2 = licenseContent.getNotBefore();
        if (date2 != null && date.before(date2)) {
            throw new LicenseContentException(ndv);
        }
        Date date3 = licenseContent.getNotAfter();
        if (date3 != null && date.after(date3)) {
            throw new LicenseContentException(xae);
        }
        String string = licenseContent.getConsumerType();
        if (string == null) {
            throw new LicenseContentException(rog);
        }
        Preferences preferences = licenseParam.getPreferences();
        if (preferences != null && preferences.isUserNode()) {
            if (!djy.equalsIgnoreCase(string)) {
                throw new LicenseContentException(hep);
            }
            if (licenseContent.getConsumerAmount() != 1) {
                throw new LicenseContentException(ygy);
            }
        } else if (licenseContent.getConsumerAmount() <= 0) {
            throw new LicenseContentException(jke);
        }
    }

    protected GenericCertificate bab() {
        if (this.ldh != null && System.currentTimeMillis() < this.rjt + 1800000L) {
            return this.ldh;
        }
        return null;
    }

    protected synchronized void itm(GenericCertificate genericCertificate) {
        this.ldh = genericCertificate;
        this.rjt = System.currentTimeMillis();
    }

    protected byte[] vep() {
        return this.eui().getPreferences().getByteArray(fvc, null);
    }

    protected synchronized void omj(byte[] byArray) {
        Preferences preferences = this.eui().getPreferences();
        if (byArray != null) {
            preferences.putByteArray(fvc, byArray);
        } else {
            preferences.remove(fvc);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected static void itm(byte[] byArray, File file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        try {
            ((OutputStream)fileOutputStream).write(byArray);
        }
        finally {
            try {
                ((OutputStream)fileOutputStream).close();
            }
            catch (IOException iOException) {}
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected static byte[] omj(File file) throws IOException {
        int n = Math.min((int)file.length(), 0x100000);
        byte[] byArray = new byte[n];
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            ((InputStream)fileInputStream).read(byArray);
        }
        finally {
            ((InputStream)fileInputStream).close();
        }
        return byArray;
    }

    protected synchronized pnd yiu() {
        if (this.xah == null) {
            this.xah = new pnd(this.eui().getKeyStoreParam());
        }
        return this.xah;
    }

    protected synchronized ifr pnd() {
        if (this.zoz == null) {
            this.zoz = new ifr(this.eui().getCipherParam());
        }
        return this.zoz;
    }

    public synchronized FileFilter pej() {
        if (this.oaa != null) {
            return this.oaa;
        }
        String string = bxh.itm(ajo, this.eui().getSubject());
        this.oaa = File.separatorChar == '\\' ? new vep(this, string) : new yiu(this, string);
        return this.oaa;
    }

    static /* synthetic */ String pwn() {
        return lpm;
    }

    static {
        assert (mpl.equals(mpl.toLowerCase()));
        dpq = pnd.dpq;
        dvh = new qyf(new long[]{-6788193907359448604L, -2787711522493615434L}).toString();
        juq = new qyf(new long[]{4943981370588954830L, 8065447823433585419L, -2749528823549501332L}).toString();
        xjx = new qyf(new long[]{-3651048337721043740L, 1928803483347080380L, 1649789960289346230L}).toString();
        seo = new qyf(new long[]{7165044359350484836L, -6008675436704023088L}).toString();
        ydu = seo + bxh.omj(new qyf(new long[]{-883182015789302099L, 6587252612286394632L}).toString());
        djy = new qyf(new long[]{-6950934198262740461L, -10280221617836935L}).toString();
        tpg = new qyf(new long[]{-1441033263392531498L, 6113162389128247115L}).toString();
        xlf = new qyf(new long[]{-9211605111142713620L, 391714365510707393L, -7356761750428556372L, 6379560902598103028L}).toString();
        lsy = new qyf(new long[]{7150026245468079143L, 6314884536402738366L, -1360923923476698800L}).toString();
        brx = new qyf(new long[]{-3034693013076752554L, -1011266899694033610L, 6775785917404597234L}).toString();
        mts = new qyf(new long[]{-6084371209004858580L, 3028840747031697166L, -3524637886726219307L}).toString();
        ndv = new qyf(new long[]{5434633639502011825L, -3406117476263181371L, 6903673940810780388L, -6816911225052310716L}).toString();
        xae = new qyf(new long[]{1000558500458715757L, -6998261911041258483L, -5490039629745846648L, 3561172928787106880L}).toString();
        rog = new qyf(new long[]{-3274088377466921882L, -1704115158449736962L, -1134622897105293263L, 2875630655915253859L}).toString();
        hep = new qyf(new long[]{-3559580260061340089L, 8807812719464926891L, 3255622466169980128L, 3208430498260873670L, 8772089725159421213L}).toString();
        ygy = new qyf(new long[]{6854702630454082314L, -1676630527348424687L, 4853969635229547239L, -7087814313396201500L, 7133601245775504376L}).toString();
        jke = new qyf(new long[]{-5670394608177286583L, -3674104453170648872L, 4159301984262248157L, 7442355638167795990L, 4780252201915657674L}).toString();
        ajo = new qyf(new long[]{3160933239845492228L, -2320904495012387647L, -5935185636215549881L, -3418607682842311949L}).toString();
        lpm = new qyf(new long[]{-6576160320308571504L, 7010427383913371869L}).toString();
    }
}

