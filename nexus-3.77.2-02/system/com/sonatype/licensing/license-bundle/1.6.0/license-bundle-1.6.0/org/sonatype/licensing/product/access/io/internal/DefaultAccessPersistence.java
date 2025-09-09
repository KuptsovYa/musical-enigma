/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.sonatype.licensing.product.access.io.internal;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.licensing.product.access.AccessEntrySet;
import org.sonatype.licensing.product.access.io.AccessPersistence;
import org.sonatype.licensing.product.access.io.FileFormat;
import org.sonatype.licensing.product.access.io.FileFormatRegistry;

@Named(value="licensing.default")
public class DefaultAccessPersistence
implements AccessPersistence {
    private static final Logger evv = LoggerFactory.getLogger(DefaultAccessPersistence.class);
    public static final int DEFAULT_SAVE_FORMAT = 0;
    private final FileFormat wmd;
    private final File rvm;
    private final FileFormatRegistry cfw;

    @Inject
    public DefaultAccessPersistence(FileFormatRegistry fileFormatRegistry, @Named(value="licensing.access.file") File file, @Nullable @Named(value="licensing.access.file.formatId") Integer n) {
        this.cfw = fileFormatRegistry;
        this.rvm = file.getAbsoluteFile();
        if (!this.rvm.exists()) {
            this.rvm.getParentFile().mkdirs();
        }
        evv.debug("File: {}", (Object)file);
        this.wmd = fileFormatRegistry.getFormat(n != null ? n : 0);
        evv.debug("Save format: {}", (Object)this.wmd);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public AccessEntrySet load() throws IOException {
        if (!this.rvm.isFile() || this.rvm.length() == 0L) {
            return new AccessEntrySet();
        }
        BufferedInputStream bufferedInputStream = null;
        try {
            evv.debug("Loading");
            bufferedInputStream = new BufferedInputStream(new FileInputStream(this.rvm));
            FileFormat fileFormat = this.cfw.getFormat(bufferedInputStream.read());
            if (fileFormat == null) {
                throw new IOException("Could not read access entries: FileFormat unavailable");
            }
            AccessEntrySet accessEntrySet = fileFormat.read(bufferedInputStream);
            AccessEntrySet accessEntrySet2 = accessEntrySet == null ? new AccessEntrySet() : accessEntrySet;
            return accessEntrySet2;
        }
        finally {
            if (bufferedInputStream != null) {
                bufferedInputStream.close();
            }
        }
    }

    @Override
    public void save(AccessEntrySet accessEntrySet) throws IOException {
        OutputStream outputStream = null;
        try {
            evv.debug("Saving");
            outputStream = new BufferedOutputStream(new FileOutputStream(this.rvm));
            outputStream.write(this.wmd.identifier());
            this.wmd.write(accessEntrySet, outputStream);
        }
        finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }
}

