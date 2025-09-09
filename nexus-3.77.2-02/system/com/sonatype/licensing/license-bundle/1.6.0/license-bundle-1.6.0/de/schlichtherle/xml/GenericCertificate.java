/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.codec.binary.Base64
 */
package de.schlichtherle.xml;

import codeguard.licensing.qeu;
import codeguard.licensing.zhj;
import de.schlichtherle.xml.GenericCertificateIntegrityException;
import de.schlichtherle.xml.GenericCertificateIsLockedException;
import de.schlichtherle.xml.GenericCertificateNotLockedException;
import de.schlichtherle.xml.PersistenceServiceException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import org.apache.commons.codec.binary.Base64;

public final class GenericCertificate
implements zhj,
Serializable {
    private static final long vep = 6247620498526484734L;
    private static final String cpa = "US-ASCII";
    private static final String evy = "US-ASCII/Base64";
    private transient boolean bcd;
    private String tpm;
    private String noa;
    private String ytv;
    private String ydd;
    private transient PropertyChangeSupport nlo;
    private transient VetoableChangeSupport dcg;

    public GenericCertificate() {
    }

    public GenericCertificate(GenericCertificate genericCertificate) {
        try {
            this.setEncoded(genericCertificate.getEncoded());
            this.setSignature(genericCertificate.getSignature());
            this.setSignatureAlgorithm(genericCertificate.getSignatureAlgorithm());
            this.setSignatureEncoding(genericCertificate.getSignatureEncoding());
        }
        catch (PropertyVetoException propertyVetoException) {
            throw new AssertionError((Object)propertyVetoException);
        }
    }

    public final synchronized void sign(Object object, PrivateKey privateKey, Signature signature) throws NullPointerException, GenericCertificateIsLockedException, PropertyVetoException, PersistenceServiceException, InvalidKeyException {
        if (privateKey == null) {
            throw new NullPointerException("signingKey");
        }
        if (signature == null) {
            throw new NullPointerException("signingEngine");
        }
        PropertyChangeEvent propertyChangeEvent = new PropertyChangeEvent(this, "locked", this.isLocked(), Boolean.TRUE);
        if (this.isLocked()) {
            throw new GenericCertificateIsLockedException(propertyChangeEvent);
        }
        this.fireVetoableChange(propertyChangeEvent);
        try {
            byte[] byArray = qeu.zxn(object);
            signature.initSign(privateKey);
            signature.update(byArray);
            byte[] byArray2 = Base64.encodeBase64((byte[])signature.sign());
            String string = new String(byArray2, 0, byArray2.length, cpa);
            this.setEncoded(new String(byArray, "UTF-8"));
            this.setSignature(string);
            this.setSignatureAlgorithm(signature.getAlgorithm());
            this.setSignatureEncoding(evy);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new AssertionError((Object)unsupportedEncodingException);
        }
        catch (SignatureException signatureException) {
            throw new AssertionError((Object)signatureException);
        }
        this.bcd = true;
        this.firePropertyChange(propertyChangeEvent);
    }

    public final synchronized void verify(PublicKey publicKey, Signature signature) throws NullPointerException, GenericCertificateIsLockedException, PropertyVetoException, InvalidKeyException, SignatureException, GenericCertificateIntegrityException {
        if (publicKey == null) {
            throw new NullPointerException("verificationKey");
        }
        if (signature == null) {
            throw new NullPointerException("verificationEngine");
        }
        PropertyChangeEvent propertyChangeEvent = new PropertyChangeEvent(this, "locked", this.isLocked(), Boolean.TRUE);
        if (this.isLocked()) {
            throw new GenericCertificateIsLockedException(propertyChangeEvent);
        }
        this.fireVetoableChange(propertyChangeEvent);
        try {
            byte[] byArray = this.getEncoded().getBytes("UTF-8");
            signature.initVerify(publicKey);
            signature.update(byArray);
            byte[] byArray2 = Base64.decodeBase64((byte[])this.getSignature().getBytes(cpa));
            if (!signature.verify(byArray2)) {
                throw new GenericCertificateIntegrityException();
            }
            this.setSignatureAlgorithm(signature.getAlgorithm());
            this.setSignatureEncoding(evy);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new AssertionError((Object)unsupportedEncodingException);
        }
        this.bcd = true;
        this.firePropertyChange(propertyChangeEvent);
    }

    public final boolean isLocked() {
        return this.bcd;
    }

    public Object getContent() throws GenericCertificateNotLockedException, PersistenceServiceException {
        if (!this.isLocked()) {
            throw new GenericCertificateNotLockedException();
        }
        return qeu.zxn(this.getEncoded());
    }

    public final String getEncoded() {
        return this.tpm;
    }

    public synchronized void setEncoded(String string) throws GenericCertificateIsLockedException {
        if (string == null ? this.tpm == null : string.equals(this.tpm)) {
            return;
        }
        PropertyChangeEvent propertyChangeEvent = new PropertyChangeEvent(this, "encoded", this.getEncoded(), string);
        if (this.isLocked()) {
            throw new GenericCertificateIsLockedException(propertyChangeEvent);
        }
        this.tpm = string;
        this.firePropertyChange(propertyChangeEvent);
    }

    public final String getSignature() {
        return this.noa;
    }

    public synchronized void setSignature(String string) throws GenericCertificateIsLockedException {
        if (string == null ? this.noa == null : string.equals(this.noa)) {
            return;
        }
        PropertyChangeEvent propertyChangeEvent = new PropertyChangeEvent(this, "signature", this.getSignature(), string);
        if (this.isLocked()) {
            throw new GenericCertificateIsLockedException(propertyChangeEvent);
        }
        this.noa = string;
        this.firePropertyChange(propertyChangeEvent);
    }

    public final String getSignatureAlgorithm() {
        return this.ytv;
    }

    public synchronized void setSignatureAlgorithm(String string) throws GenericCertificateIsLockedException {
        if (string == null ? this.ytv == null : string.equals(this.ytv)) {
            return;
        }
        PropertyChangeEvent propertyChangeEvent = new PropertyChangeEvent(this, "signatureAlgorithm", this.getSignatureAlgorithm(), string);
        if (this.isLocked()) {
            throw new GenericCertificateIsLockedException(propertyChangeEvent);
        }
        this.ytv = string;
        this.firePropertyChange(propertyChangeEvent);
    }

    public final String getSignatureEncoding() {
        return this.ydd;
    }

    public synchronized void setSignatureEncoding(String string) throws GenericCertificateIsLockedException {
        if (string == null ? this.ydd == null : string.equals(this.ydd)) {
            return;
        }
        PropertyChangeEvent propertyChangeEvent = new PropertyChangeEvent(this, "signatureEncoding", this.getSignatureEncoding(), string);
        if (this.isLocked()) {
            throw new GenericCertificateIsLockedException(propertyChangeEvent);
        }
        this.ydd = string;
        this.firePropertyChange(propertyChangeEvent);
    }

    public final synchronized void addVetoableChangeListener(VetoableChangeListener vetoableChangeListener) {
        if (this.dcg == null) {
            this.dcg = new VetoableChangeSupport(this);
        }
        this.dcg.addVetoableChangeListener(vetoableChangeListener);
    }

    public final void removeVetoableChangeListener(VetoableChangeListener vetoableChangeListener) {
        if (this.dcg == null) {
            return;
        }
        this.dcg.removeVetoableChangeListener(vetoableChangeListener);
    }

    protected final void fireVetoableChange(PropertyChangeEvent propertyChangeEvent) throws PropertyVetoException {
        if (this.dcg == null) {
            return;
        }
        this.dcg.fireVetoableChange(propertyChangeEvent);
    }

    public final synchronized void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        if (this.nlo == null) {
            this.nlo = new PropertyChangeSupport(this);
        }
        this.nlo.addPropertyChangeListener(propertyChangeListener);
    }

    public final void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        if (this.nlo == null) {
            return;
        }
        this.nlo.removePropertyChangeListener(propertyChangeListener);
    }

    protected final void firePropertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (this.nlo == null) {
            return;
        }
        this.nlo.firePropertyChange(propertyChangeEvent);
    }
}

