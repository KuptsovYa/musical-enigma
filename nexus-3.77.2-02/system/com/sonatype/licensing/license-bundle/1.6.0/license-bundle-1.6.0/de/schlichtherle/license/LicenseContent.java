/*
 * Decompiled with CFR 0.152.
 */
package de.schlichtherle.license;

import codeguard.licensing.qeu;
import java.beans.DefaultPersistenceDelegate;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Date;
import javax.security.auth.x500.X500Principal;

public class LicenseContent
implements Serializable,
Cloneable {
    private static final long vep = 1L;
    private X500Principal yiu;
    private X500Principal pnd;
    private String eui;
    private Date pej;
    private Date pwn;
    private Date ifr;
    private String bxh;
    private int ajd = 1;
    private String zts;
    private Object chr;
    private transient PropertyChangeSupport baj;

    protected Object clone() {
        try {
            LicenseContent licenseContent = (LicenseContent)super.clone();
            licenseContent.pej = (Date)this.pej.clone();
            licenseContent.pwn = (Date)this.pwn.clone();
            licenseContent.ifr = (Date)this.ifr.clone();
            return licenseContent;
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new AssertionError((Object)cloneNotSupportedException);
        }
    }

    public X500Principal getHolder() {
        return this.yiu;
    }

    public synchronized void setHolder(X500Principal x500Principal) {
        X500Principal x500Principal2 = this.yiu;
        this.yiu = x500Principal;
        this.firePropertyChange("holder", x500Principal2, x500Principal);
    }

    public X500Principal getIssuer() {
        return this.pnd;
    }

    public synchronized void setIssuer(X500Principal x500Principal) {
        X500Principal x500Principal2 = this.pnd;
        this.pnd = x500Principal;
        this.firePropertyChange("issuer", x500Principal2, x500Principal);
    }

    public String getSubject() {
        return this.eui;
    }

    public synchronized void setSubject(String string) {
        String string2 = this.eui;
        this.eui = string;
        this.firePropertyChange("subject", string2, string);
    }

    public Date getIssued() {
        return this.pej;
    }

    public void setIssued(Date date) {
        Date date2 = this.pej;
        this.pej = date;
        this.firePropertyChange("issued", date2, date);
    }

    public Date getNotBefore() {
        return this.pwn;
    }

    public void setNotBefore(Date date) {
        Date date2 = this.pwn;
        this.pwn = date;
        this.firePropertyChange("notBefore", date2, date);
    }

    public Date getNotAfter() {
        return this.ifr;
    }

    public void setNotAfter(Date date) {
        Date date2 = this.ifr;
        this.ifr = date;
        this.firePropertyChange("notAfter", date2, date);
    }

    public String getConsumerType() {
        return this.bxh;
    }

    public void setConsumerType(String string) {
        String string2 = this.bxh;
        this.bxh = string;
        this.firePropertyChange("consumerType", string2, string);
    }

    public int getConsumerAmount() {
        return this.ajd;
    }

    public void setConsumerAmount(int n) {
        int n2 = this.ajd;
        this.ajd = n;
        this.firePropertyChange("consumerAmount", new Integer(n2), new Integer(n));
    }

    public String getInfo() {
        return this.zts;
    }

    public void setInfo(String string) {
        String string2 = this.zts;
        this.zts = string;
        this.firePropertyChange("info", string2, string);
    }

    public Object getExtra() {
        return this.chr;
    }

    public void setExtra(Object object) {
        Object object2 = this.chr;
        this.chr = object;
        this.firePropertyChange("extra", object2, object);
    }

    public int hashCode() {
        return this.getConsumerAmount() + LicenseContent.itm(this.getConsumerType()) + LicenseContent.itm(this.getHolder()) + LicenseContent.itm(this.getInfo()) + LicenseContent.itm(this.getIssued()) + LicenseContent.itm(this.getIssuer()) + LicenseContent.itm(this.getNotAfter()) + LicenseContent.itm(this.getNotBefore()) + LicenseContent.itm(this.getSubject());
    }

    private static int itm(Object object) {
        return null == object ? 0 : object.hashCode();
    }

    public boolean equals(Object object) {
        if (!(object instanceof LicenseContent)) {
            return false;
        }
        LicenseContent licenseContent = (LicenseContent)object;
        return licenseContent.getConsumerAmount() == this.getConsumerAmount() && LicenseContent.itm(licenseContent.getConsumerType(), this.getConsumerType()) && LicenseContent.itm(licenseContent.getHolder(), this.getHolder()) && LicenseContent.itm(licenseContent.getInfo(), this.getInfo()) && LicenseContent.itm(licenseContent.getIssued(), this.getIssued()) && LicenseContent.itm(licenseContent.getIssuer(), this.getIssuer()) && LicenseContent.itm(licenseContent.getNotAfter(), this.getNotAfter()) && LicenseContent.itm(licenseContent.getNotBefore(), this.getNotBefore()) && LicenseContent.itm(licenseContent.getSubject(), this.getSubject());
    }

    private static boolean itm(Object object, Object object2) {
        return object == object2 || null != object && object.equals(object2);
    }

    public final synchronized void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        if (this.baj == null) {
            this.baj = new PropertyChangeSupport(this);
        }
        this.baj.addPropertyChangeListener(propertyChangeListener);
    }

    public final synchronized void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        if (this.baj == null) {
            return;
        }
        this.baj.removePropertyChangeListener(propertyChangeListener);
    }

    protected final void firePropertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (this.baj == null) {
            return;
        }
        this.baj.firePropertyChange(propertyChangeEvent);
    }

    protected final void firePropertyChange(String string, Object object, Object object2) {
        if (this.baj == null) {
            return;
        }
        this.baj.firePropertyChange(string, object, object2);
    }

    static {
        qeu.itm(X500Principal.class, new DefaultPersistenceDelegate(new String[]{"name"}));
    }
}

