/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Named
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.sonatype.licensing.product.internal;

import codeguard.licensing.agq;
import codeguard.licensing.aua;
import codeguard.licensing.bjw;
import codeguard.licensing.bos;
import codeguard.licensing.haa;
import codeguard.licensing.omz;
import codeguard.licensing.whz;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.auth.x500.X500Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.licensing.ProductDetails;
import org.sonatype.licensing.feature.Feature;
import org.sonatype.licensing.product.SslKeyContainer;
import org.sonatype.licensing.product.internal.ProductLicenseKeyRequest;
import org.sonatype.licensing.util.LicensingUtil;

@Named(value="licensing.default")
public class DefaultLicenseKeyRequest
extends bos
implements ProductLicenseKeyRequest {
    private final Logger fjz = LoggerFactory.getLogger(DefaultLicenseKeyRequest.class);
    private static final String xbb = LicensingUtil.unobfuscate(new long[]{-1075740369556149950L, 6473667505195108430L});
    private static final String eur = LicensingUtil.unobfuscate(new long[]{5431597888647840358L, -3003659393323666576L});
    private static final String sux = LicensingUtil.unobfuscate(new long[]{-2011739764646339905L, 1056792054092558862L});
    private boolean ymv;
    private int bzn = -1;
    private SslKeyContainer[] sjl = new SslKeyContainer[0];
    private final ProductDetails xrw;
    private Collection<String> zir = Collections.emptySet();
    private Properties kxn = new Properties();

    @Inject
    public DefaultLicenseKeyRequest(ProductDetails productDetails) {
        super(productDetails);
        this.xrw = productDetails;
    }

    @Override
    public boolean isFreeLicense() {
        return this.ymv;
    }

    @Override
    public void setFreeLicense(boolean bl) {
        this.ymv = bl;
    }

    @Override
    public int getLicensedUsers() {
        return this.bzn;
    }

    @Override
    public void setLicensedUsers(int n) {
        this.bzn = n;
    }

    @Override
    public X500Principal getIssuer() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CN=").append(xbb).append(", ");
        stringBuilder.append("OU=").append(this.xrw.getBrandName()).append(", ");
        stringBuilder.append("O=").append(this.xrw.getBrandName()).append(", ");
        stringBuilder.append("L=").append(eur).append(", ");
        stringBuilder.append("C=").append(sux);
        return new X500Principal(stringBuilder.toString());
    }

    @Override
    public String getExtraContent() {
        Object object2;
        agq agq2 = new agq();
        agq2.mif("1.0.2");
        agq2.setEvaluation(this.isEvaluation());
        agq2.setFreeLicense(this.isFreeLicense());
        agq2.setLicensedUsers(this.getLicensedUsers());
        for (String object22 : this.getLicensedProducts()) {
            object2 = new bjw();
            ((bjw)object2).rkn(object22);
            agq2.itm((bjw)object2);
        }
        for (Feature feature : this.getFeatureSet()) {
            object2 = new whz();
            ((whz)object2).rkn(feature.getId());
            agq2.itm((whz)object2);
        }
        for (SslKeyContainer sslKeyContainer : this.getKeys()) {
            object2 = new haa();
            ((haa)object2).setEntry(sslKeyContainer.getEntry());
            ((haa)object2).setPassword(sslKeyContainer.getPassword());
            ((haa)object2).bao(sslKeyContainer.getType().toString());
            agq2.itm((haa)object2);
        }
        Properties properties = this.getProperties();
        for (Object object2 : properties.keySet()) {
            String string = (String)object2;
            aua aua2 = new aua();
            aua2.bab(string);
            aua2.vep(properties.getProperty(string));
            agq2.itm(aua2);
        }
        StringWriter stringWriter = new StringWriter();
        object2 = new omz();
        try {
            ((omz)object2).itm(stringWriter, agq2);
            return stringWriter.toString();
        }
        catch (IOException iOException) {
            this.fjz.error("Unable to store license content properly", (Throwable)iOException);
            return null;
        }
    }

    @Override
    public List<SslKeyContainer> getKeys() {
        return Arrays.asList(this.sjl);
    }

    @Override
    public void setKeys(SslKeyContainer[] sslKeyContainerArray) {
        this.sjl = sslKeyContainerArray;
    }

    @Override
    public void setLicensedProducts(Collection<String> collection) {
        this.zir = collection;
    }

    @Override
    public Collection<String> getLicensedProducts() {
        return this.zir;
    }

    @Override
    public Properties getProperties() {
        return this.kxn;
    }

    @Override
    public void setProperties(Properties properties) {
        this.kxn = properties;
    }
}

