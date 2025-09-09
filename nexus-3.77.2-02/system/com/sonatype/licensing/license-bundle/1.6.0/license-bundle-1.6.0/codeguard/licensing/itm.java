/*
 * Decompiled with CFR 0.152.
 */
package codeguard.licensing;

import codeguard.licensing.eui;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public abstract class itm
implements eui {
    private final Class itm;
    private final String omj;

    protected itm(Class clazz, String string) {
        this.itm = clazz;
        this.omj = string;
    }

    @Override
    public InputStream getStream() throws IOException {
        InputStream inputStream = this.itm.getResourceAsStream(this.omj);
        if (inputStream == null) {
            throw new FileNotFoundException(this.omj);
        }
        return inputStream;
    }

    public int hashCode() {
        int n = 5;
        n = 97 * n + (this.itm != null ? this.itm.hashCode() : 0);
        n = 97 * n + (this.omj != null ? this.omj.hashCode() : 0);
        return n;
    }

    public boolean equals(Object object) {
        if (!(object instanceof itm)) {
            return false;
        }
        itm itm2 = (itm)object;
        return this.itm.getResource(this.omj).equals(itm2.itm.getResource(itm2.omj)) && this.getAlias().equals(itm2.getAlias());
    }
}

