/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing.product.access;

import java.io.IOException;
import java.util.Date;
import java.util.Set;
import org.sonatype.licensing.product.access.AccessEntry;

public interface AccessManager {
    public boolean add(AccessEntry var1);

    public void save() throws IOException;

    public Set<AccessEntry> getSince(Date var1);

    public Set<AccessEntry> expire(Date var1);
}

