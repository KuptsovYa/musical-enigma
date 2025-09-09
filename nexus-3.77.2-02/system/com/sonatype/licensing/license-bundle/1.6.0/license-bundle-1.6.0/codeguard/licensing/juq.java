/*
 * Decompiled with CFR 0.152.
 */
package codeguard.licensing;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import org.sonatype.licensing.product.SslKeyContainer;
import org.sonatype.licensing.product.internal.GenericSslKeyContainer;

public class juq
extends GenericSslKeyContainer {
    public juq(File file, String string) throws IOException {
        super(file, string, SslKeyContainer.Type.JKS);
    }

    public juq(String string, String string2) {
        super(string, string2, SslKeyContainer.Type.JKS);
    }

    public juq(KeyStore keyStore, String string) {
        super(juq.itm(keyStore, string), string, SslKeyContainer.Type.JKS);
    }

    private static String itm(KeyStore keyStore, String string) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            keyStore.store(byteArrayOutputStream, string.toCharArray());
            byte[] byArray = byteArrayOutputStream.toByteArray();
            return juq.b64encode(byArray);
        }
        catch (Exception exception) {
            throw new IllegalArgumentException("Failed to read and base64-encode key store", exception);
        }
    }
}

