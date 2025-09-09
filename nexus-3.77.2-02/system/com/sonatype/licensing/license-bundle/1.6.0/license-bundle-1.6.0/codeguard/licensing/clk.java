/*
 * Decompiled with CFR 0.152.
 */
package codeguard.licensing;

import codeguard.licensing.itm;

public class clk
extends itm {
    private final String clk;
    private final String rkn;
    private final String zxn;

    public clk(Class clazz, String string, String string2, String string3, String string4) {
        super(clazz, string);
        this.clk = string2;
        this.rkn = string3;
        this.zxn = string4;
    }

    @Override
    public String getAlias() {
        return this.clk;
    }

    @Override
    public String getStorePwd() {
        return this.rkn;
    }

    @Override
    public String getKeyPwd() {
        return this.zxn;
    }
}

