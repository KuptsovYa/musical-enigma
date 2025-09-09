/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.sonatype.licensing.product.internal;

import codeguard.licensing.xjx;
import com.google.common.base.Preconditions;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicReference;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.licensing.product.LicenseChangeListener;
import org.sonatype.licensing.product.LicenseChangeNotifier;
import org.sonatype.licensing.product.ProductLicenseKey;
import org.sonatype.licensing.product.internal.DefaultLicenseFeatureVerifier;
import org.sonatype.licensing.product.util.LicenseFingerprintStrategy;

@Named(value="licensing.default")
@Singleton
public class LicenseChangeNotifierImpl
implements LicenseChangeNotifier {
    private final Logger evv = LoggerFactory.getLogger(this.getClass());
    private final List<LicenseChangeListener> bji;
    private final DefaultLicenseFeatureVerifier yhh;
    private final LicenseFingerprintStrategy fcp;
    private final Map<LicenseChangeListener, AtomicReference<itm>> wfo = new WeakHashMap<LicenseChangeListener, AtomicReference<itm>>();

    @Inject
    public LicenseChangeNotifierImpl(List<LicenseChangeListener> list, DefaultLicenseFeatureVerifier defaultLicenseFeatureVerifier, LicenseFingerprintStrategy licenseFingerprintStrategy) {
        this.yhh = (DefaultLicenseFeatureVerifier)Preconditions.checkNotNull((Object)defaultLicenseFeatureVerifier);
        this.bji = (List)Preconditions.checkNotNull(list);
        this.fcp = (LicenseFingerprintStrategy)Preconditions.checkNotNull((Object)licenseFingerprintStrategy);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void notifyListeners(ProductLicenseKey productLicenseKey, boolean bl, Exception exception) {
        if (exception != null) {
            this.evv.debug("License invalid", (Throwable)exception);
        } else {
            this.evv.debug("License {}", (Object)(bl ? "valid" : "invalid"));
        }
        this.yhh.updateState(productLicenseKey, bl);
        String string = productLicenseKey != null ? this.fcp.calculate(productLicenseKey) : null;
        for (LicenseChangeListener licenseChangeListener : this.bji) {
            AtomicReference<itm> atomicReference;
            Object object = this.wfo;
            synchronized (object) {
                atomicReference = this.wfo.get(licenseChangeListener);
                if (atomicReference == null) {
                    atomicReference = new AtomicReference<Object>(null);
                    this.wfo.put(licenseChangeListener, atomicReference);
                }
            }
            object = new itm(string, bl, null);
            if (!((itm)object).equals(atomicReference.getAndSet((itm)object))) {
                this.evv.trace("Notifying listener: {}", (Object)licenseChangeListener);
                try {
                    licenseChangeListener.licenseChanged(productLicenseKey, bl);
                }
                catch (Exception exception2) {
                    this.evv.trace("Failed to notify listener", (Throwable)exception2);
                }
                continue;
            }
            this.evv.trace("Skipping listener notification; state has not changed: {}", (Object)licenseChangeListener);
        }
    }

    static class itm {
        final String uec;
        final boolean utz;

        private itm(String string, boolean bl) {
            this.uec = string;
            this.utz = bl;
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }
            itm itm2 = (itm)object;
            if (this.utz != itm2.utz) {
                return false;
            }
            return !(this.uec != null ? !this.uec.equals(itm2.uec) : itm2.uec != null);
        }

        public int hashCode() {
            return this.uec != null ? this.uec.hashCode() : 0;
        }

        /* synthetic */ itm(String string, boolean bl, xjx xjx2) {
            this(string, bl);
        }
    }
}

