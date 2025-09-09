/*
 * Decompiled with CFR 0.152.
 */
package codeguard.licensing;

import java.io.IOException;
import java.io.OutputStream;

public class dvh
extends OutputStream {
    private final OutputStream eel;

    public dvh(OutputStream outputStream) {
        this.eel = outputStream;
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public void write(int n) throws IOException {
        this.eel.write(n);
    }
}

