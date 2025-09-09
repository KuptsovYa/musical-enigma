/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing.product.access.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.sonatype.licensing.product.access.AccessEntrySet;

public interface FileFormat {
    public AccessEntrySet read(InputStream var1) throws IOException;

    public void write(AccessEntrySet var1, OutputStream var2) throws IOException;

    public int identifier();
}

