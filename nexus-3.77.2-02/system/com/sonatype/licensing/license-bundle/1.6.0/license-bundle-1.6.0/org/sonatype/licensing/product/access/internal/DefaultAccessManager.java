/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.sonatype.licensing.product.access.internal;

import codeguard.licensing.fvc;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.licensing.product.access.AccessEntry;
import org.sonatype.licensing.product.access.AccessEntrySet;
import org.sonatype.licensing.product.access.AccessManager;
import org.sonatype.licensing.product.access.io.AccessPersistence;

@Singleton
@Named(value="licensing.default")
public class DefaultAccessManager
implements AccessManager {
    public static final int DEFAULT_MAX_ENTRIES_BETWEEN_SAVE = 100;
    public static final int DEFAULT_MAX_TIME_BETWEEN_SAVE = 300000;
    private int orn = 100;
    private int xgj = 300000;
    private final Logger fjz = LoggerFactory.getLogger(this.getClass());
    private final AccessPersistence svd;
    private AccessEntrySet qks = new AccessEntrySet();
    private final ConcurrentLinkedQueue<AccessEntry> lak;
    private final itm erd = new itm(null);
    private Future<?> fnf;

    @Inject
    public DefaultAccessManager(AccessPersistence accessPersistence) throws IOException {
        this.lak = new ConcurrentLinkedQueue();
        this.svd = accessPersistence;
        this.qks = accessPersistence.load();
        Thread thread = new Thread(this.erd);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public boolean add(AccessEntry accessEntry) {
        this.fjz.trace("Adding access entry: {}", (Object)accessEntry);
        return this.lak.add(accessEntry);
    }

    @Override
    public synchronized Set<AccessEntry> getSince(Date date) {
        long l = date.getTime();
        HashSet<AccessEntry> hashSet = new HashSet<AccessEntry>();
        for (AccessEntry accessEntry : this.qks) {
            if (accessEntry.getDate().getTime() <= l) continue;
            hashSet.add(accessEntry);
        }
        return hashSet;
    }

    @Override
    public Set<AccessEntry> expire(Date date) {
        Set<AccessEntry> set = this.getSince(date);
        this.fjz.debug(String.format("expiring access entries: %s from %s", set.size(), this.qks.size()));
        this.qks.retainAll(set);
        return set;
    }

    @Override
    public void save() throws IOException {
        this.svd.save(this.qks);
    }

    public void purge() {
        this.qks.clear();
    }

    public boolean hasItemsToBeProcessed() {
        return this.lak.size() != 0;
    }

    public synchronized void shutdown() throws IOException {
        this.fjz.debug("Shutdown");
        try {
            this.erd.shutdown();
        }
        catch (Exception exception) {
            this.fjz.warn("Failure when shutting down queue processor", (Throwable)exception);
        }
        this.erd.omz();
        this.save();
        this.qks = null;
    }

    public int getMaxEntriesBetweenSave() {
        return this.orn;
    }

    public void setMaxEntriesBetweenSave(int n) {
        this.orn = n;
    }

    public int getMaxTimeBetweenSave() {
        return this.xgj;
    }

    public void setMaxTimeBetweenSave(int n) {
        this.xgj = n;
    }

    class itm
    implements Runnable {
        private boolean xbq = true;
        private int uqq = 0;
        private long cmy = System.currentTimeMillis();
        private Object fhi = new Object();

        private itm() {
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private void shutdown() throws InterruptedException {
            this.xbq = false;
            Object object = this.fhi;
            synchronized (object) {
                this.fhi.wait(2000L);
            }
        }

        private void aua() {
            boolean bl = false;
            if (this.uqq >= DefaultAccessManager.this.orn) {
                DefaultAccessManager.this.fjz.trace("Entry count reached save threshold");
                bl = true;
            } else if (this.uqq > 0 && System.currentTimeMillis() - this.cmy >= (long)DefaultAccessManager.this.xgj) {
                DefaultAccessManager.this.fjz.trace("Time elapsed since last save exceeded");
                bl = true;
            }
            if (bl) {
                try {
                    DefaultAccessManager.this.save();
                    this.uqq = 0;
                    this.cmy = System.currentTimeMillis();
                }
                catch (IOException iOException) {
                    DefaultAccessManager.this.fjz.error("Failed to save", (Throwable)iOException);
                }
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void run() {
            while (this.xbq) {
                this.omz();
                this.aua();
                if (!this.xbq) continue;
                try {
                    Thread.sleep(500L);
                }
                catch (InterruptedException interruptedException) {}
            }
            Object object = this.fhi;
            synchronized (object) {
                this.fhi.notifyAll();
            }
        }

        private void omz() {
            while (!DefaultAccessManager.this.lak.isEmpty()) {
                DefaultAccessManager.this.qks.add((AccessEntry)DefaultAccessManager.this.lak.remove());
                ++this.uqq;
                DefaultAccessManager.this.fjz.trace("Processed {} entries since last save.", (Object)this.uqq);
            }
        }

        /* synthetic */ itm(fvc fvc2) {
            this();
        }
    }
}

