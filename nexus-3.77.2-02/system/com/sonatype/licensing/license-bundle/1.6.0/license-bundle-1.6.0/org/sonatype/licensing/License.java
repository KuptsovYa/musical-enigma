/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.licensing;

import java.util.Date;
import java.util.List;
import org.sonatype.licensing.feature.FeatureSet;

public interface License {
    public String getContactName();

    public void setContactName(String var1);

    public String getContactEmailAddress();

    public void setContactEmailAddress(String var1);

    public String getContactTelephone();

    public void setContactTelephone(String var1);

    public String getContactCompany();

    public void setContactCompany(String var1);

    public String getContactCountry();

    public void setContactCountry(String var1);

    public boolean isEvaluation();

    public void setEvaluation(boolean var1);

    public List<String> getRawFeatures();

    public FeatureSet getFeatureSet();

    public void setFeatureSet(FeatureSet var1);

    public Date getEffectiveDate();

    public void setEffectiveDate(Date var1);

    public Date getExpirationDate();

    public void setExpirationDate(Date var1);
}

