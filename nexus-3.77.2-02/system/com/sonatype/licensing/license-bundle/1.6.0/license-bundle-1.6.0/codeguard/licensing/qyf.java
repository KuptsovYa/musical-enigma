/*
 * Decompiled with CFR 0.152.
 */
package codeguard.licensing;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public final class qyf {
    private static final String yaf = new String(new char[]{'U', 'T', 'F', '8'});
    private final long[] lbx;

    public static void main(String[] stringArray) {
        for (int i = 0; i < stringArray.length; ++i) {
            System.out.println(qyf.obfuscate(stringArray[i]));
        }
    }

    public static String obfuscate(String string) {
        long l;
        byte[] byArray;
        if (-1 != string.indexOf(0)) {
            throw new IllegalArgumentException(new qyf(new long[]{2598583114197433456L, -2532951909540716745L, 1850312903926917213L, -7324743161950196342L, 3319654553699491298L}).toString());
        }
        try {
            byArray = string.getBytes(yaf);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new AssertionError((Object)unsupportedEncodingException);
        }
        Random random = new Random();
        while ((l = random.nextLong()) == 0L) {
        }
        random = new Random(l);
        StringBuffer stringBuffer = new StringBuffer(new qyf(new long[]{-6733388613909857970L, -557652741307719956L, 563088487624542180L, 5623833171491374716L, -2309350771052518321L, 2627844803624578169L}).toString());
        qyf.itm(stringBuffer, l);
        int n = byArray.length;
        for (int i = 0; i < n; i += 8) {
            long l2 = random.nextLong();
            long l3 = qyf.itm(byArray, i) ^ l2;
            stringBuffer.append(", ");
            qyf.itm(stringBuffer, l3);
        }
        stringBuffer.append(new qyf(new long[]{4756003162039514438L, -7241174029104351587L, 2576762727660584163L, 2432800632635846553L}).toString());
        stringBuffer.append(string.replaceAll("\\\\", new qyf(new long[]{7866777055383403009L, -5101749501440392498L}).toString()).replaceAll("\"", new qyf(new long[]{-8797265930671803829L, -5738757606858957305L}).toString()));
        stringBuffer.append(new qyf(new long[]{-4228881123273879289L, 1823585417647083411L}).toString());
        return stringBuffer.toString();
    }

    private static void itm(StringBuffer stringBuffer, long l) {
        stringBuffer.append('0');
        stringBuffer.append('x');
        stringBuffer.append(Long.toHexString(l).toUpperCase());
        stringBuffer.append('L');
    }

    private static long itm(byte[] byArray, int n) {
        int n2 = Math.min(byArray.length, n + 8);
        long l = 0L;
        int n3 = n2;
        while (--n3 >= n) {
            l <<= 8;
            l |= (long)(byArray[n3] & 0xFF);
        }
        return l;
    }

    private static void itm(long l, byte[] byArray, int n) {
        int n2 = Math.min(byArray.length, n + 8);
        for (int i = n; i < n2; ++i) {
            byArray[i] = (byte)l;
            l >>= 8;
        }
    }

    public qyf(long[] lArray) {
        this.lbx = (long[])lArray.clone();
        this.lbx[0] = lArray[0];
    }

    public String toString() {
        String string;
        int n = this.lbx.length;
        byte[] byArray = new byte[8 * (n - 1)];
        long l = this.lbx[0];
        Random random = new Random(l);
        for (int i = 1; i < n; ++i) {
            long l2 = random.nextLong();
            qyf.itm(this.lbx[i] ^ l2, byArray, 8 * (i - 1));
        }
        try {
            string = new String(byArray, yaf);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new AssertionError((Object)unsupportedEncodingException);
        }
        int n2 = string.indexOf(0);
        return -1 == n2 ? string : string.substring(0, n2);
    }
}

