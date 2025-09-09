/*
 * Decompiled with CFR 0.152.
 */
package codeguard.licensing;

import codeguard.licensing.aua;
import codeguard.licensing.bjw;
import codeguard.licensing.haa;
import codeguard.licensing.whz;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class agq
implements Serializable {
    private String gae;
    private boolean ymv = false;
    private int bzn = -1;
    private boolean zeb = false;
    private List<bjw> aab;
    private List<whz> dlc;
    private List<haa> fvf;
    private List<aua> bko;
    private String ifu = "UTF-8";
    public static final String wzy = "1.0.2";

    public void itm(whz whz2) {
        this.zhj().add(whz2);
    }

    public void itm(haa haa2) {
        this.getKeys().add(haa2);
    }

    public void itm(bjw bjw2) {
        this.bos().add(bjw2);
    }

    public void itm(aua aua2) {
        this.zsv().add(aua2);
    }

    public List<whz> zhj() {
        if (this.dlc == null) {
            this.dlc = new ArrayList<whz>();
        }
        return this.dlc;
    }

    public List<haa> getKeys() {
        if (this.fvf == null) {
            this.fvf = new ArrayList<haa>();
        }
        return this.fvf;
    }

    public int getLicensedUsers() {
        return this.bzn;
    }

    public String rnn() {
        return this.ifu;
    }

    public List<bjw> bos() {
        if (this.aab == null) {
            this.aab = new ArrayList<bjw>();
        }
        return this.aab;
    }

    public List<aua> zsv() {
        if (this.bko == null) {
            this.bko = new ArrayList<aua>();
        }
        return this.bko;
    }

    public String aec() {
        return this.gae;
    }

    public boolean isEvaluation() {
        return this.zeb;
    }

    public boolean isFreeLicense() {
        return this.ymv;
    }

    public void omj(whz whz2) {
        this.zhj().remove(whz2);
    }

    public void omj(haa haa2) {
        this.getKeys().remove(haa2);
    }

    public void omj(bjw bjw2) {
        this.bos().remove(bjw2);
    }

    public void omj(aua aua2) {
        this.zsv().remove(aua2);
    }

    public void setEvaluation(boolean bl) {
        this.zeb = bl;
    }

    public void itm(List<whz> list) {
        this.dlc = list;
    }

    public void setFreeLicense(boolean bl) {
        this.ymv = bl;
    }

    public void omj(List<haa> list) {
        this.fvf = list;
    }

    public void setLicensedUsers(int n) {
        this.bzn = n;
    }

    public void eui(String string) {
        this.ifu = string;
    }

    public void zxn(List<bjw> list) {
        this.aab = list;
    }

    public void clk(List<aua> list) {
        this.bko = list;
    }

    public void mif(String string) {
        this.gae = string;
    }
}

