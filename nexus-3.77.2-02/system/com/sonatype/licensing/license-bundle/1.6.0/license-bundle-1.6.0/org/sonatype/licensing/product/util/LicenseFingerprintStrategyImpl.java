/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.Lists
 *  javax.inject.Named
 *  javax.inject.Singleton
 */
package org.sonatype.licensing.product.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.licensing.product.ProductLicenseKey;
import org.sonatype.licensing.product.SslKeyContainer;
import org.sonatype.licensing.product.util.LicenseFingerprintStrategy;

@Named
@Singleton
public class LicenseFingerprintStrategyImpl
implements LicenseFingerprintStrategy {
    @Override
    public String calculate(ProductLicenseKey productLicenseKey) {
        MessageDigest messageDigest;
        Preconditions.checkArgument((productLicenseKey.getRawFeatures() != null ? 1 : 0) != 0, (Object)"Product License Key has null raw features");
        Preconditions.checkArgument((productLicenseKey.getSslKeys() != null ? 1 : 0) != 0, (Object)"Product License Key has null ssl keys");
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new IllegalStateException(noSuchAlgorithmException);
        }
        this.itm(messageDigest, productLicenseKey.getContactCompany());
        this.itm(messageDigest, productLicenseKey.getContactCountry());
        this.itm(messageDigest, productLicenseKey.getContactEmailAddress());
        this.itm(messageDigest, productLicenseKey.getContactName());
        this.itm(messageDigest, productLicenseKey.getContactTelephone());
        this.itm(messageDigest, productLicenseKey.getEffectiveDate());
        this.itm(messageDigest, productLicenseKey.getExpirationDate());
        this.itm(messageDigest, productLicenseKey.getLicensedUsers());
        messageDigest.update((byte)(productLicenseKey.isEvaluation() ? 1 : 0));
        messageDigest.update((byte)(productLicenseKey.isFreeLicense() ? 1 : 0));
        ArrayList arrayList = Lists.newArrayList(productLicenseKey.getRawFeatures());
        Collections.sort(arrayList);
        for (String object2 : arrayList) {
            this.itm(messageDigest, object2);
        }
        for (SslKeyContainer sslKeyContainer : productLicenseKey.getSslKeys()) {
            this.itm(messageDigest, sslKeyContainer.getType().name());
            this.itm(messageDigest, sslKeyContainer.getPassword());
            this.itm(messageDigest, sslKeyContainer.getEntry());
        }
        Object object3 = messageDigest.digest();
        StringBuilder stringBuilder = new StringBuilder(((Object)object3).length * 2);
        for (int i = 0; i < ((Object)object3).length; ++i) {
            int n = object3[i] & 0xFF;
            if (n < 16) {
                stringBuilder.append('0');
            }
            stringBuilder.append(Integer.toHexString(n));
        }
        return stringBuilder.toString();
    }

    private void itm(MessageDigest messageDigest, String string) {
        if (string != null) {
            try {
                messageDigest.update(string.getBytes("UTF-8"));
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                throw new IllegalStateException(unsupportedEncodingException);
            }
        }
    }

    private void itm(MessageDigest messageDigest, Date date) {
        if (date != null) {
            this.itm(messageDigest, date.getTime());
        }
    }

    private void itm(MessageDigest messageDigest, long l) {
        byte[] byArray = new byte[]{(byte)(l >> 56 & 0xFFL), (byte)(l >> 48 & 0xFFL), (byte)(l >> 40 & 0xFFL), (byte)(l >> 32 & 0xFFL), (byte)(l >> 24 & 0xFFL), (byte)(l >> 16 & 0xFFL), (byte)(l >> 8 & 0xFFL), (byte)(l >> 0 & 0xFFL)};
        messageDigest.update(byArray, 0, 8);
    }
}

