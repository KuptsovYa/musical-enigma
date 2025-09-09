/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 */
package org.sonatype.licensing.product.access.internal;

import codeguard.licensing.mpl;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import org.sonatype.licensing.product.access.AccessBlocker;
import org.sonatype.licensing.product.access.AccessChecker;
import org.sonatype.licensing.product.access.AccessEntry;
import org.sonatype.licensing.product.access.AccessManager;
import org.sonatype.licensing.product.access.AccessTracker;

@Named(value="licensing.default")
public class DefaultAccessTracker
implements AccessTracker {
    private final AccessManager krh;
    private final AccessChecker onp;
    private final AccessBlocker rbr;

    @Inject
    public DefaultAccessTracker(AccessManager accessManager, @Nullable AccessChecker accessChecker, @Nullable AccessBlocker accessBlocker) {
        this.krh = accessManager;
        this.onp = accessChecker == null ? new omj(null) : accessChecker;
        this.rbr = accessBlocker == null ? new itm(null) : accessBlocker;
    }

    @Override
    public void add(Object object) {
        AccessEntry accessEntry = new AccessEntry(object);
        if (!this.onp.check(this.krh, accessEntry)) {
            this.rbr.enable();
        } else {
            this.rbr.disable();
        }
        this.krh.add(accessEntry);
    }

    class itm
    implements AccessBlocker {
        private itm() {
        }

        @Override
        public void enable() {
        }

        @Override
        public void disable() {
        }

        /* synthetic */ itm(mpl mpl2) {
            this();
        }
    }

    class omj
    implements AccessChecker {
        private omj() {
        }

        @Override
        public boolean check(AccessManager accessManager, AccessEntry accessEntry) {
            return true;
        }

        /* synthetic */ omj(mpl mpl2) {
            this();
        }
    }
}

