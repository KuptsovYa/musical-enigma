/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing.product.internal;

import java.util.Collection;
import java.util.List;
import java.util.Properties;
import org.sonatype.licensing.LicenseKeyRequest;
import org.sonatype.licensing.product.SslKeyContainer;

public interface ProductLicenseKeyRequest
extends LicenseKeyRequest {
    public boolean isFreeLicense();

    public void setFreeLicense(boolean var1);

    public int getLicensedUsers();

    public void setLicensedUsers(int var1);

    public List<SslKeyContainer> getKeys();

    public void setKeys(SslKeyContainer[] var1);

    public void setLicensedProducts(Collection<String> var1);

    public Collection<String> getLicensedProducts();

    public void setProperties(Properties var1);

    public Properties getProperties();
}

