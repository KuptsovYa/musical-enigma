/*
 * Decompiled with CFR 0.152.
 */
package codeguard.licensing;

import codeguard.licensing.qyf;
import java.text.MessageFormat;
import java.util.ResourceBundle;

public class bxh {
    private static final String dhn = new qyf(new long[]{6055227168551487552L, 5834857350928301681L, 4273642861057763346L, -1622634856111526471L, 6636310379452924396L, -2362982053890309465L}).toString();
    private static final ResourceBundle mar = ResourceBundle.getBundle(dhn);

    public static String omj(String string) {
        return mar.getString(string);
    }

    public static String itm(String string, Object[] objectArray) {
        return MessageFormat.format(bxh.omj(string), objectArray);
    }

    public static String itm(String string, Object object) {
        return MessageFormat.format(bxh.omj(string), object);
    }

    protected bxh() {
    }
}

