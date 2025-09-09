/*
 * Decompiled with CFR 0.152.
 */
package codeguard.licensing;

import codeguard.licensing.eui;
import codeguard.licensing.pwn;
import codeguard.licensing.qyf;
import de.schlichtherle.license.LicenseNotaryException;
import de.schlichtherle.xml.GenericCertificate;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public class pnd {
    private static final int quo = 5120;
    static final String dpq = new qyf(new long[]{668274362144012114L, -2115765889337599212L}).toString();
    private static final String vjs = new qyf(new long[]{1112708769776922148L, 6703392504509681290L}).toString();
    private static final String sja = new qyf(new long[]{-7210613020960449599L, 222075784786550139L, 9025728610804768010L}).toString();
    private static final String xfl = new qyf(new long[]{-1386002024146642540L, 4133952825992554401L, -8020387964636761861L}).toString();
    private static final String vnt = new qyf(new long[]{-2960555953270849419L, 3827258740935670554L, -3005417608224527600L, 1939660993088349256L, 4750831951568910874L}).toString();
    private static final String fku = new qyf(new long[]{-3872127676557769698L, -2469202953083814859L, 6713970776812571709L, -482260351456063412L}).toString();
    private static final String kjq = new qyf(new long[]{-1509550478491572167L, 1688274905166048601L, -4620167493569680976L}).toString();
    private static final String ynq = new qyf(new long[]{-6234396975553918200L, 2370155821952859770L}).toString();
    private eui ylu;
    private KeyStore age;
    private PrivateKey cnv;
    private PublicKey yww;

    protected pnd() {
    }

    public pnd(eui eui2) {
        this.omj(eui2);
    }

    public eui getKeyStoreParam() {
        return this.ylu;
    }

    public void itm(eui eui2) {
        this.omj(eui2);
    }

    private void omj(eui eui2) {
        if (eui2 == null) {
            throw new NullPointerException(dpq);
        }
        if (eui2.getAlias() == null) {
            throw new NullPointerException(vjs);
        }
        pwn pwn2 = pwn.chr();
        String string = eui2.getStorePwd();
        pwn2.itm(string);
        String string2 = eui2.getKeyPwd();
        if (string2 != null) {
            pwn2.itm(string2);
        }
        this.ylu = eui2;
        this.age = null;
        this.cnv = null;
        this.yww = null;
    }

    public GenericCertificate omj(Object object) throws Exception {
        GenericCertificate genericCertificate = new GenericCertificate();
        this.itm(genericCertificate, object);
        return genericCertificate;
    }

    void itm(GenericCertificate genericCertificate, Object object) throws Exception {
        genericCertificate.sign(object, this.ifr(), this.ajd());
    }

    public void omj(GenericCertificate genericCertificate) throws Exception {
        genericCertificate.verify(this.bxh(), this.ajd());
    }

    protected PrivateKey ifr() throws LicenseNotaryException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        if (this.cnv == null) {
            eui eui2 = this.getKeyStoreParam();
            String string = eui2.getKeyPwd();
            String string2 = eui2.getAlias();
            if (string == null) {
                throw new LicenseNotaryException(sja, string2);
            }
            KeyStore keyStore = this.zts();
            try {
                this.cnv = (PrivateKey)keyStore.getKey(string2, string.toCharArray());
            }
            catch (KeyStoreException keyStoreException) {
                throw new AssertionError((Object)keyStoreException);
            }
            if (this.cnv == null) {
                throw new LicenseNotaryException(xfl, string2);
            }
        }
        return this.cnv;
    }

    protected PublicKey bxh() throws LicenseNotaryException, IOException, CertificateException, NoSuchAlgorithmException {
        if (this.yww == null) {
            String string = this.getKeyStoreParam().getAlias();
            KeyStore keyStore = this.zts();
            try {
                if (this.getKeyStoreParam().getKeyPwd() != null != keyStore.isKeyEntry(string)) {
                    throw new LicenseNotaryException(vnt, string);
                }
                Certificate certificate = keyStore.getCertificate(string);
                if (certificate == null) {
                    throw new LicenseNotaryException(fku, string);
                }
                this.yww = certificate.getPublicKey();
            }
            catch (KeyStoreException keyStoreException) {
                throw new AssertionError((Object)keyStoreException);
            }
        }
        return this.yww;
    }

    protected Signature ajd() {
        try {
            return Signature.getInstance(kjq);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new AssertionError((Object)noSuchAlgorithmException);
        }
    }

    protected KeyStore zts() throws IOException, CertificateException, NoSuchAlgorithmException {
        if (this.age != null) {
            return this.age;
        }
        InputStream inputStream = null;
        try {
            this.age = KeyStore.getInstance(ynq);
            inputStream = new BufferedInputStream(this.ylu.getStream(), 5120);
            this.age.load(inputStream, this.getKeyStoreParam().getStorePwd().toCharArray());
        }
        catch (KeyStoreException keyStoreException) {
            throw new AssertionError((Object)keyStoreException);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (Exception exception) {}
        }
        return this.age;
    }
}

