/*
 * Decompiled with CFR 0.152.
 */
package codeguard.licensing;

import java.io.File;
import java.io.IOException;
import org.sonatype.licensing.product.SslKeyContainer;
import org.sonatype.licensing.product.internal.GenericSslKeyContainer;

public class seo
extends GenericSslKeyContainer {
    public seo(File file, String string) throws IOException {
        super(file, string, SslKeyContainer.Type.PKCS12);
    }

    public seo(String string, String string2) {
        super(string, string2, SslKeyContainer.Type.PKCS12);
    }
}

