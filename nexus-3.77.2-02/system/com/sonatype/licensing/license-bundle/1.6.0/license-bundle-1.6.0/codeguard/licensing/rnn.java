/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package codeguard.licensing;

import com.google.common.collect.Lists;
import java.util.Date;
import java.util.List;
import org.sonatype.licensing.License;
import org.sonatype.licensing.feature.FeatureSet;

public abstract class rnn
implements License {
    private String syx;
    private String zkh;
    private String irg;
    private String jrn;
    private String sxl;
    private Date sqr;
    private Date nqm;
    private boolean zeb;
    private FeatureSet llc = new FeatureSet();
    private List<String> aaz = Lists.newArrayList();

    @Override
    public String getContactCompany() {
        return this.syx;
    }

    @Override
    public String getContactCountry() {
        return this.zkh;
    }

    @Override
    public String getContactEmailAddress() {
        return this.irg;
    }

    @Override
    public String getContactName() {
        return this.jrn;
    }

    @Override
    public String getContactTelephone() {
        return this.sxl;
    }

    @Override
    public Date getEffectiveDate() {
        return this.sqr;
    }

    @Override
    public Date getExpirationDate() {
        return this.nqm;
    }

    @Override
    public boolean isEvaluation() {
        return this.zeb;
    }

    @Override
    public FeatureSet getFeatureSet() {
        return this.llc;
    }

    @Override
    public List<String> getRawFeatures() {
        return this.aaz;
    }

    @Override
    public void setContactCompany(String string) {
        this.syx = string;
    }

    @Override
    public void setContactCountry(String string) {
        this.zkh = string;
    }

    @Override
    public void setContactEmailAddress(String string) {
        this.irg = string;
    }

    @Override
    public void setContactName(String string) {
        this.jrn = string;
    }

    @Override
    public void setContactTelephone(String string) {
        this.sxl = string;
    }

    @Override
    public void setEvaluation(boolean bl) {
        this.zeb = bl;
    }

    @Override
    public void setEffectiveDate(Date date) {
        this.sqr = date;
    }

    @Override
    public void setExpirationDate(Date date) {
        this.nqm = date;
    }

    @Override
    public void setFeatureSet(FeatureSet featureSet) {
        this.llc = featureSet;
    }

    public void setRawFeatures(List<String> list) {
        this.aaz = list;
    }
}

