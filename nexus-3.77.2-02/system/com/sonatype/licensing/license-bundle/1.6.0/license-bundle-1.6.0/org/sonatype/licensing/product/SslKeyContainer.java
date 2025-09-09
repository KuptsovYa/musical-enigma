/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing.product;

public interface SslKeyContainer {
    public String getPassword();

    public String getEntry();

    public Type getType();

    public static enum Type {
        PKCS12,
        JKS;

    }
}

