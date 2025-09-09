/*
 * Decompiled with CFR 0.152.
 */
package codeguard.licensing;

import codeguard.licensing.omj;
import codeguard.licensing.pnd;
import codeguard.licensing.pwn;
import codeguard.licensing.qeu;
import codeguard.licensing.qyf;
import de.schlichtherle.xml.GenericCertificate;
import de.schlichtherle.xml.PersistenceServiceException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class ifr {
    private static final String ztd = new qyf(new long[]{2860604316472308139L, 5030391952891038168L, -6110818099732428353L}).toString();
    private omj wdm;
    private Cipher fyp;
    private SecretKey tbk;
    private AlgorithmParameterSpec nch;

    protected ifr() {
    }

    public ifr(omj omj2) {
        this.omj(omj2);
    }

    public omj getCipherParam() {
        return this.wdm;
    }

    public void itm(omj omj2) {
        this.omj(omj2);
    }

    private void omj(omj omj2) {
        if (omj2 == null) {
            throw new NullPointerException(pnd.dpq);
        }
        pwn.chr().itm(omj2.getKeyPwd());
        this.wdm = omj2;
        this.fyp = null;
        this.tbk = null;
        this.nch = null;
    }

    public byte[] zxn(GenericCertificate genericCertificate) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(new CipherOutputStream(byteArrayOutputStream, this.baj()));
        try {
            qeu.itm((Object)genericCertificate, gZIPOutputStream);
        }
        catch (PersistenceServiceException persistenceServiceException) {
            throw new AssertionError((Object)persistenceServiceException);
        }
        return byteArrayOutputStream.toByteArray();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public GenericCertificate zxn(byte[] byArray) throws Exception {
        GenericCertificate genericCertificate;
        GZIPInputStream gZIPInputStream = new GZIPInputStream(new ByteArrayInputStream(this.qyf().doFinal(byArray)));
        try {
            genericCertificate = (GenericCertificate)qeu.itm(gZIPInputStream);
        }
        finally {
            try {
                ((InputStream)gZIPInputStream).close();
            }
            catch (IOException iOException) {}
        }
        return genericCertificate;
    }

    protected Cipher baj() {
        Cipher cipher = this.qeu();
        try {
            cipher.init(1, (Key)this.tbk, this.nch);
        }
        catch (InvalidKeyException invalidKeyException) {
            throw new AssertionError((Object)invalidKeyException);
        }
        catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
            throw new AssertionError((Object)invalidAlgorithmParameterException);
        }
        return cipher;
    }

    protected Cipher qyf() {
        Cipher cipher = this.qeu();
        try {
            cipher.init(2, (Key)this.tbk, this.nch);
        }
        catch (InvalidKeyException invalidKeyException) {
            throw new AssertionError((Object)invalidKeyException);
        }
        catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
            throw new AssertionError((Object)invalidAlgorithmParameterException);
        }
        return cipher;
    }

    protected Cipher qeu() {
        if (this.fyp != null) {
            return this.fyp;
        }
        this.nch = new PBEParameterSpec(new byte[]{-50, -5, -34, -84, 5, 2, 25, 113}, 2005);
        try {
            PBEKeySpec pBEKeySpec = new PBEKeySpec(this.getCipherParam().getKeyPwd().toCharArray());
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(ztd);
            this.tbk = secretKeyFactory.generateSecret(pBEKeySpec);
            this.fyp = Cipher.getInstance(ztd);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new AssertionError((Object)noSuchAlgorithmException);
        }
        catch (InvalidKeySpecException invalidKeySpecException) {
            throw new AssertionError((Object)invalidKeySpecException);
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            throw new AssertionError((Object)noSuchPaddingException);
        }
        return this.fyp;
    }
}

