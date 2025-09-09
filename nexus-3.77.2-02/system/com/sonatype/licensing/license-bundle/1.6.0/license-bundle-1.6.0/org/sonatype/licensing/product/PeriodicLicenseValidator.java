/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing.product;

public interface PeriodicLicenseValidator {
    public void start();

    public void stop();

    public boolean isRunning();
}

