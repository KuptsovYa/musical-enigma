/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package org.sonatype.licensing.util;

import codeguard.licensing.qyf;
import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class LicensingUtil {
    public static final String LINE_SEPERATOR = System.getProperty("line.separator");
    public static final String PACKAGE_PREFIX = new qyf(new long[]{-1956725044763736505L, -8653607204494632877L, -1534585266971579493L}).toString();
    private static final char yyj = '\\';

    public static String unobfuscate(long[] lArray) {
        return new qyf(lArray).toString();
    }

    public static String obfuscate(String string) {
        return qyf.obfuscate(string);
    }

    public static String getPackage(String string) {
        return PACKAGE_PREFIX + "/" + string;
    }

    public static void clearPreferences() throws BackingStoreException {
        Preferences preferences = Preferences.userRoot().node(PACKAGE_PREFIX);
        LicensingUtil.itm(preferences);
    }

    private static void itm(Preferences preferences) throws BackingStoreException {
        for (String string : preferences.childrenNames()) {
            Preferences preferences2 = preferences.node(string);
            for (String string2 : preferences2.keys()) {
                preferences2.remove(string2);
            }
            LicensingUtil.itm(preferences2);
        }
    }

    public static void main(String[] stringArray) throws IOException {
        String string;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        while ((string = bufferedReader.readLine()) != null) {
            String string2 = qyf.obfuscate(string);
            System.out.println(string2);
        }
    }

    public static String encodeDNPart(String string) {
        if (string == null || string.length() == 0) {
            return string;
        }
        StringBuilder stringBuilder = new StringBuilder();
        char[] cArray = new char[string.length()];
        string.getChars(0, string.length(), cArray, 0);
        block4: for (char c : cArray) {
            switch (c) {
                case '\"': 
                case '#': 
                case '+': 
                case ',': 
                case ';': 
                case '\\': {
                    stringBuilder.append('\\').append(c);
                    continue block4;
                }
                case '\n': {
                    stringBuilder.append(' ');
                    continue block4;
                }
                default: {
                    stringBuilder.append(c);
                }
            }
        }
        return stringBuilder.toString();
    }

    public static String decodeDNPart(String string) {
        if (string == null || string.length() == 0) {
            return string;
        }
        StringBuilder stringBuilder = new StringBuilder();
        char[] cArray = new char[string.length()];
        string.getChars(0, string.length(), cArray, 0);
        boolean bl = false;
        block3: for (char c : cArray) {
            switch (c) {
                case '\\': {
                    if (bl) {
                        stringBuilder.append(c);
                        continue block3;
                    }
                    bl = true;
                    continue block3;
                }
                default: {
                    bl = false;
                    stringBuilder.append(c);
                }
            }
        }
        return stringBuilder.toString();
    }

    public static List<String> getDNParts(String string) {
        ArrayList arrayList = Lists.newArrayList();
        if (string == null || string.length() == 0) {
            return arrayList;
        }
        StringBuilder stringBuilder = new StringBuilder();
        String string2 = string.trim();
        char[] cArray = new char[string2.length()];
        string2.getChars(0, string.length(), cArray, 0);
        boolean bl = false;
        block4: for (char c : cArray) {
            switch (c) {
                case '\\': {
                    if (bl) {
                        stringBuilder.append(c);
                        continue block4;
                    }
                    bl = true;
                    continue block4;
                }
                case ',': {
                    if (bl) {
                        stringBuilder.append(c);
                        bl = false;
                        continue block4;
                    }
                    arrayList.add(stringBuilder.toString());
                    stringBuilder = new StringBuilder();
                    continue block4;
                }
                default: {
                    bl = false;
                    stringBuilder.append(c);
                }
            }
        }
        arrayList.add(stringBuilder.toString());
        return arrayList;
    }
}

