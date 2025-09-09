/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.sonatype.licensing.product.internal;

import com.google.common.annotations.VisibleForTesting;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.licensing.LicensingException;
import org.sonatype.licensing.feature.Feature;
import org.sonatype.licensing.feature.FeatureValidator;
import org.sonatype.licensing.feature.LicenseFeatureVerifier;
import org.sonatype.licensing.product.ProductLicenseKey;

@Named(value="licensing.default")
@Singleton
public class DefaultLicenseFeatureVerifier
implements LicenseFeatureVerifier {
    private final Logger evv = LoggerFactory.getLogger(this.getClass());
    private final FeatureValidator gyk;
    private final ReadWriteLock ztm = new ReentrantReadWriteLock();
    private ProductLicenseKey ugf;
    private boolean utz;

    @Inject
    public DefaultLicenseFeatureVerifier(FeatureValidator featureValidator) {
        this.gyk = featureValidator;
    }

    @Override
    public void verifyLicenseAndFeature(Feature feature) throws LicensingException {
        Lock lock = this.ztm.readLock();
        lock.lock();
        try {
            if (this.evv.isTraceEnabled()) {
                this.evv.trace("Verify (throws) {}", (Object)feature.getId());
                this.evv.trace("Current state: key={}, valid={}", (Object)this.ugf, (Object)this.utz);
            }
            this.gyk.validate(feature, this.ugf);
        }
        finally {
            lock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean verify(Feature feature) {
        Lock lock = this.ztm.readLock();
        lock.lock();
        try {
            if (this.evv.isTraceEnabled()) {
                this.evv.trace("Verify (boolean) {}", (Object)feature.getId());
                this.evv.trace("Current state: key={}, valid={}", (Object)this.ugf, (Object)this.utz);
            }
            boolean bl = this.utz && this.gyk.isValid(feature, this.ugf);
            return bl;
        }
        finally {
            lock.unlock();
        }
    }

    @VisibleForTesting
    ProductLicenseKey dyw() {
        return this.ugf;
    }

    @VisibleForTesting
    boolean fvc() {
        return this.utz;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @VisibleForTesting
    public void updateState(ProductLicenseKey productLicenseKey, boolean bl) {
        Lock lock = this.ztm.writeLock();
        lock.lock();
        try {
            this.evv.debug("Update state: key={}, valid={}", (Object)productLicenseKey, (Object)bl);
            this.ugf = productLicenseKey;
            this.utz = bl;
        }
        finally {
            lock.unlock();
        }
    }
}

