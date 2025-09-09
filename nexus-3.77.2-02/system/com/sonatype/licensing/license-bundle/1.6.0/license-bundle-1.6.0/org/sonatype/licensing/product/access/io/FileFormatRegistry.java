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
package org.sonatype.licensing.product.access.io;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.licensing.product.access.io.FileFormat;

@Named(value="licensing.default")
@Singleton
public class FileFormatRegistry {
    private static final Logger evv = LoggerFactory.getLogger(FileFormatRegistry.class);
    private final Map<Integer, FileFormat> wie = new HashMap<Integer, FileFormat>();

    @Inject
    public FileFormatRegistry(List<FileFormat> list) {
        for (FileFormat fileFormat : list) {
            this.addFormat(fileFormat);
        }
    }

    public synchronized FileFormat getFormat(int n) {
        return this.wie.get(n);
    }

    public synchronized void addFormat(FileFormat fileFormat) {
        evv.debug("Adding format: {}", (Object)fileFormat);
        int n = fileFormat.identifier();
        if (this.wie.containsKey(n)) {
            throw new IllegalStateException(String.format("Format already exists with ID: %d", n));
        }
        this.wie.put(n, fileFormat);
    }

    public synchronized Collection<FileFormat> getAllFormats() {
        return Collections.unmodifiableCollection(this.wie.values());
    }
}

