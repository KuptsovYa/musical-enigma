/*
 * Decompiled with CFR 0.152.
 */
package codeguard.licensing;

import org.sonatype.licensing.product.access.io.FileFormat;

public abstract class dpq
implements FileFormat {
    public String toString() {
        return this.getClass().getSimpleName() + "{id=" + this.identifier() + '}';
    }
}

