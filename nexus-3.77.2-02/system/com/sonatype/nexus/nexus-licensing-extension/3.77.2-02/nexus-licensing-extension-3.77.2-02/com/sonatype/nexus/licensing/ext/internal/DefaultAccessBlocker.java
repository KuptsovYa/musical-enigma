/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.licensing.product.access.AccessBlocker
 */
package com.sonatype.nexus.licensing.ext.internal;

import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.licensing.product.access.AccessBlocker;

@Named
@Singleton
public class DefaultAccessBlocker
implements AccessBlocker {
    public void enable() {
    }

    public void disable() {
    }
}

