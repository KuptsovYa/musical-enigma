/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing.product.access;

import org.sonatype.licensing.product.access.AccessEntry;
import org.sonatype.licensing.product.access.AccessManager;

public interface AccessChecker {
    public boolean check(AccessManager var1, AccessEntry var2);
}

