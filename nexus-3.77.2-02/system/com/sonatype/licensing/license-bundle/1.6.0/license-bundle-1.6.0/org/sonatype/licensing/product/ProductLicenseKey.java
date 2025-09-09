/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing.product;

import java.util.List;
import java.util.Properties;
import org.sonatype.licensing.LicenseKey;
import org.sonatype.licensing.product.SslKeyContainer;

public interface ProductLicenseKey
extends LicenseKey {
    public List<SslKeyContainer> getSslKeys();

    public int getLicensedUsers();

    public boolean isFreeLicense();

    public void setProperties(Properties var1);

    public Properties getProperties();
}

