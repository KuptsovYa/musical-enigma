/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.enterprise.inject.Typed
 *  javax.inject.Named
 *  javax.inject.Singleton
 */
package org.sonatype.licensing.product.access.io.internal;

import codeguard.licensing.dvh;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.enterprise.inject.Typed;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.licensing.product.access.AccessEntrySet;
import org.sonatype.licensing.product.access.io.FileFormat;
import org.sonatype.licensing.product.access.io.internal.XStreamFormat;

@Named
@Singleton
@Typed(value={FileFormat.class})
public class XStreamGzipFormat
extends XStreamFormat {
    public static final int ID = 2;

    @Override
    public AccessEntrySet read(InputStream inputStream) throws IOException {
        return super.read(new GZIPInputStream(inputStream));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void write(AccessEntrySet accessEntrySet, OutputStream outputStream) throws IOException {
        GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(new dvh(outputStream));
        try {
            super.write(accessEntrySet, gZIPOutputStream);
        }
        finally {
            ((OutputStream)gZIPOutputStream).close();
        }
    }

    @Override
    public int identifier() {
        return 2;
    }
}

