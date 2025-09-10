/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 *  com.google.common.base.Supplier
 *  com.google.common.base.Suppliers
 *  com.google.common.base.Throwables
 *  com.google.common.io.Files
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.nexus.crypto.AbstractPhraseService
 */
package org.sonatype.nexus.security;

import com.google.common.base.Charsets;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.base.Throwables;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.nexus.crypto.AbstractPhraseService;

@Named(value="file")
@Singleton
public class FilePhraseService
extends AbstractPhraseService {
    private final Supplier<String> masterPhraseSupplier;

    @Inject
    public FilePhraseService(final @Named(value="${nexus.security.masterPhraseFile}") @Nullable File masterPhraseFile) {
        super(masterPhraseFile != null);
        this.masterPhraseSupplier = Suppliers.memoize((Supplier)new Supplier<String>(){

            public String get() {
                try {
                    return Files.asCharSource((File)masterPhraseFile, (Charset)Charsets.UTF_8).read().trim();
                }
                catch (IOException e) {
                    throw Throwables.propagate((Throwable)e);
                }
            }
        });
    }

    protected String getMasterPhrase() {
        return (String)this.masterPhraseSupplier.get();
    }
}

