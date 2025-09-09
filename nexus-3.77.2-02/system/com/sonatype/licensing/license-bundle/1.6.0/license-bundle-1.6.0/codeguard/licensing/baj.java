/*
 * Decompiled with CFR 0.152.
 */
package codeguard.licensing;

import codeguard.licensing.qyf;
import java.text.MessageFormat;
import java.util.ResourceBundle;

class baj {
    private static final String dhn = new qyf(new long[]{-1350665201776450749L, -216656036460523244L, -1481304226977776272L, -2463710388362402838L, -6044068851221630479L, -2266461419465064627L}).toString();
    private static final ResourceBundle mar = ResourceBundle.getBundle(dhn);

    public static String omj(String string) {
        return mar.getString(string);
    }

    public static String itm(String string, Object[] objectArray) {
        return MessageFormat.format(baj.omj(string), objectArray);
    }

    public static String itm(String string, Object object) {
        return MessageFormat.format(baj.omj(string), object);
    }

    protected baj() {
    }
}

