/*
 * Decompiled with CFR 0.152.
 */
package codeguard.licensing;

import java.io.IOException;
import java.io.InputStream;

public interface eui {
    public InputStream getStream() throws IOException;

    public String getAlias();

    public String getStorePwd();

    public String getKeyPwd();
}

