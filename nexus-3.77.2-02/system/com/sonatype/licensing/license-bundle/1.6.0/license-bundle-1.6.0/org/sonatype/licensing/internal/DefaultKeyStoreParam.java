/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing.internal;

import codeguard.licensing.clk;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DefaultKeyStoreParam
extends clk {
    String alq = null;

    public DefaultKeyStoreParam(Class<?> clazz, String string, String string2, String string3, String string4) {
        super(clazz, string, string2, string3, string4);
        this.alq = string;
    }

    @Override
    public InputStream getStream() throws IOException {
        try {
            return super.getStream();
        }
        catch (IOException iOException) {
            return new FileInputStream(this.alq);
        }
    }
}

