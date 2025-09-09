/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.licensing.ProductDetails
 */
package com.sonatype.nexus.licensing.ext.builder;

import com.sonatype.nexus.licensing.ext.builder.DefaultNexusLicenseBuilder;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.licensing.ProductDetails;

@Named
@Singleton
public class DefaultNexusProductDetails
implements ProductDetails {
    public String getProductName() {
        return DefaultNexusLicenseBuilder.SUBJECT;
    }

    public String getBrandName() {
        return DefaultNexusLicenseBuilder.BRAND;
    }
}

