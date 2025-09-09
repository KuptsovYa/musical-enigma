/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Named
 *  org.sonatype.licensing.LicenseKey
 *  org.sonatype.licensing.feature.Features
 *  org.sonatype.licensing.product.ProductLicenseKey
 *  org.sonatype.licensing.product.internal.DefaultLicenseKey
 */
package com.sonatype.nexus.licensing.ext;

import javax.inject.Inject;
import javax.inject.Named;
import org.sonatype.licensing.LicenseKey;
import org.sonatype.licensing.feature.Features;
import org.sonatype.licensing.product.ProductLicenseKey;
import org.sonatype.licensing.product.internal.DefaultLicenseKey;

@Named(value="default")
public class NexusLicenseKey
extends DefaultLicenseKey
implements LicenseKey,
ProductLicenseKey {
    @Inject
    public NexusLicenseKey(Features features) {
        super(features);
    }
}

