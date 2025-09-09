/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.inject.Key
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.eclipse.sisu.BeanEntry
 *  org.eclipse.sisu.Mediator
 *  org.eclipse.sisu.inject.BeanLocator
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.sonatype.licensing.product.internal;

import codeguard.licensing.ydu;
import com.google.common.base.Preconditions;
import com.google.inject.Key;
import java.util.Timer;
import java.util.TimerTask;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.eclipse.sisu.BeanEntry;
import org.eclipse.sisu.Mediator;
import org.eclipse.sisu.inject.BeanLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.licensing.LicensingException;
import org.sonatype.licensing.product.LicenseChangeListener;
import org.sonatype.licensing.product.LicenseChangeNotifier;
import org.sonatype.licensing.product.PeriodicLicenseValidator;
import org.sonatype.licensing.product.ProductLicenseKey;
import org.sonatype.licensing.product.ProductLicenseManager;

@Named(value="licensing.default")
@Singleton
public class PeriodicLicenseValidatorImpl
implements PeriodicLicenseValidator {
    private static final String kkm = "${org.sonatype.licensing.product.internal.PeriodicLicenseValidatorImpl";
    private static final long tgn = 300000L;
    private final Logger evv = LoggerFactory.getLogger(this.getClass());
    private final ProductLicenseManager sua;
    private final LicenseChangeNotifier wnl;
    private final long hbz;
    private Timer zgk;
    private TimerTask ely;
    private boolean nwg;

    @Inject
    public PeriodicLicenseValidatorImpl(ProductLicenseManager productLicenseManager, LicenseChangeNotifier licenseChangeNotifier, BeanLocator beanLocator, @Named(value="${org.sonatype.licensing.product.internal.PeriodicLicenseValidatorImpl.period:-300000}") long l) {
        this.sua = (ProductLicenseManager)Preconditions.checkNotNull((Object)productLicenseManager);
        this.wnl = (LicenseChangeNotifier)Preconditions.checkNotNull((Object)licenseChangeNotifier);
        this.hbz = l;
        this.evv.debug("Period: {}", (Object)l);
        beanLocator.watch(Key.get(LicenseChangeListener.class), (Mediator)new itm(), (Object)this);
    }

    @Override
    public synchronized void start() {
        Preconditions.checkState((!this.nwg ? 1 : 0) != 0, (Object)"Already started");
        this.evv.debug("Starting");
        this.ely = new ydu(this);
        this.zgk = new Timer(this.getClass().getSimpleName() + "-timer", true);
        this.zgk.schedule(this.ely, 0L, this.hbz);
        this.nwg = true;
    }

    @Override
    public synchronized void stop() {
        Preconditions.checkState((boolean)this.nwg, (Object)"Not started");
        this.evv.debug("Stopping");
        if (this.ely != null) {
            this.ely.cancel();
            this.ely = null;
        }
        if (this.zgk != null) {
            this.zgk.purge();
            this.zgk.cancel();
            this.zgk = null;
        }
        this.nwg = false;
    }

    @Override
    public synchronized boolean isRunning() {
        return this.nwg;
    }

    private void dpq() {
        this.evv.debug("Validating");
        try {
            ProductLicenseKey productLicenseKey = this.sua.getLicenseDetails();
            assert (productLicenseKey != null);
            this.notifyListeners(productLicenseKey, true, null);
        }
        catch (LicensingException licensingException) {
            this.notifyListeners((ProductLicenseKey)licensingException.getKey(), false, licensingException);
        }
        catch (Exception exception) {
            this.evv.error("Failed to get license details", (Throwable)exception);
        }
    }

    private void notifyListeners(ProductLicenseKey productLicenseKey, boolean bl, Exception exception) {
        try {
            this.wnl.notifyListeners(productLicenseKey, bl, exception);
        }
        catch (Exception exception2) {
            if (exception != null) {
                this.evv.error("Failed to notify listeners about key='{}', valid={}, failure={}/{}", new Object[]{productLicenseKey, bl, exception.getClass().getName(), exception.getMessage(), exception2});
            }
            this.evv.error("Failed to notify listeners about key='{}', valid={}", new Object[]{productLicenseKey, bl, exception2});
        }
    }

    static final class itm
    implements Mediator<Named, LicenseChangeListener, PeriodicLicenseValidatorImpl> {
        itm() {
        }

        public void itm(BeanEntry<Named, LicenseChangeListener> beanEntry, PeriodicLicenseValidatorImpl periodicLicenseValidatorImpl) {
            if (periodicLicenseValidatorImpl.nwg) {
                periodicLicenseValidatorImpl.dpq();
            }
        }

        public void omj(BeanEntry<Named, LicenseChangeListener> beanEntry, PeriodicLicenseValidatorImpl periodicLicenseValidatorImpl) {
        }

        public /* synthetic */ void remove(BeanEntry beanEntry, Object object) throws Exception {
            this.omj((BeanEntry<Named, LicenseChangeListener>)beanEntry, (PeriodicLicenseValidatorImpl)object);
        }

        public /* synthetic */ void add(BeanEntry beanEntry, Object object) throws Exception {
            this.itm((BeanEntry<Named, LicenseChangeListener>)beanEntry, (PeriodicLicenseValidatorImpl)object);
        }
    }
}

