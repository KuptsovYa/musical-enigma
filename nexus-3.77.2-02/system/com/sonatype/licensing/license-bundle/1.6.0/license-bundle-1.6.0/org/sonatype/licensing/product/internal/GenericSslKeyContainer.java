/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.codehaus.plexus.util.Base64
 */
package org.sonatype.licensing.product.internal;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import org.codehaus.plexus.util.Base64;
import org.sonatype.licensing.product.SslKeyContainer;

public class GenericSslKeyContainer
implements SslKeyContainer {
    private String tlx;
    private String ooh;
    private SslKeyContainer.Type ury;

    public GenericSslKeyContainer(String string, String string2, SslKeyContainer.Type type) {
        this.tlx = string;
        this.ooh = string2;
        this.ury = type;
    }

    public GenericSslKeyContainer(File file, String string, SslKeyContainer.Type type) throws IOException {
        this(GenericSslKeyContainer.eui(file), string, type);
    }

    public static String b64encode(byte[] byArray) throws UnsupportedEncodingException {
        byte[] byArray2 = Base64.encodeBase64((byte[])byArray);
        return new String(byArray2, "US-ASCII");
    }

    private static String eui(File file) {
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            byte[] byArray = new byte[(int)randomAccessFile.length()];
            randomAccessFile.readFully(byArray);
            String string = GenericSslKeyContainer.b64encode(byArray);
            return string;
        }
        catch (IOException iOException) {
            throw new IllegalArgumentException("Cannot base64encode file: " + iOException.toString(), iOException);
        }
        finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                }
                catch (IOException iOException) {}
            }
        }
    }

    @Override
    public String getPassword() {
        return this.ooh;
    }

    public void setPassword(String string) {
        this.ooh = string;
    }

    @Override
    public String getEntry() {
        return this.tlx;
    }

    public void setEntry(String string) {
        this.tlx = string;
    }

    @Override
    public SslKeyContainer.Type getType() {
        return this.ury;
    }

    public void setType(SslKeyContainer.Type type) {
        this.ury = type;
    }
}

