/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.enterprise.inject.Typed
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.codehaus.plexus.util.Base64
 *  org.codehaus.plexus.util.IOUtil
 */
package org.sonatype.licensing.product.access.io.internal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.enterprise.inject.Typed;
import javax.inject.Named;
import javax.inject.Singleton;
import org.codehaus.plexus.util.Base64;
import org.codehaus.plexus.util.IOUtil;
import org.sonatype.licensing.product.access.AccessEntrySet;
import org.sonatype.licensing.product.access.io.FileFormat;
import org.sonatype.licensing.product.access.io.internal.XStreamFormat;

@Named
@Singleton
@Typed(value={FileFormat.class})
public class XStreamBase64Format
extends XStreamFormat {
    public static final int ID = 1;

    @Override
    public AccessEntrySet read(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOUtil.copy((InputStream)inputStream, (OutputStream)byteArrayOutputStream);
        IOUtil.close((InputStream)inputStream);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.decodeBase64((byte[])byteArrayOutputStream.toByteArray()));
        return super.read(byteArrayInputStream);
    }

    @Override
    public void write(AccessEntrySet accessEntrySet, OutputStream outputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        super.write(accessEntrySet, byteArrayOutputStream);
        outputStream.write(Base64.encodeBase64((byte[])byteArrayOutputStream.toByteArray()));
    }

    @Override
    public int identifier() {
        return 1;
    }
}

