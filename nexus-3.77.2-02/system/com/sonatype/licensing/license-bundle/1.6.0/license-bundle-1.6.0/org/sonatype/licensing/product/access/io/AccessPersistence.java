/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing.product.access.io;

import java.io.IOException;
import org.sonatype.licensing.product.access.AccessEntrySet;

public interface AccessPersistence {
    public AccessEntrySet load() throws IOException;

    public void save(AccessEntrySet var1) throws IOException;
}

