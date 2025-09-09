/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.licensing.product.access.AccessEntry
 *  org.sonatype.licensing.product.access.AccessManager
 */
package com.sonatype.nexus.licensing.ext.internal;

import java.io.IOException;
import java.util.Date;
import java.util.Set;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.licensing.product.access.AccessEntry;
import org.sonatype.licensing.product.access.AccessManager;

@Named(value="default")
@Singleton
public class NexusAccessManager
implements AccessManager {
    public boolean add(AccessEntry accessEntry) {
        throw new UnsupportedOperationException();
    }

    public void save() throws IOException {
        throw new UnsupportedOperationException();
    }

    public Set<AccessEntry> getSince(Date date) {
        throw new UnsupportedOperationException();
    }

    public Set<AccessEntry> expire(Date date) {
        throw new UnsupportedOperationException();
    }
}

