/*
 * Decompiled with CFR 0.152.
 */
package codeguard.licensing;

import de.schlichtherle.license.IllegalPasswordException;

public class pwn {
    private static pwn jjo;

    public static void itm(pwn pwn2) {
        jjo = pwn2;
    }

    public static pwn chr() {
        if (jjo == null) {
            jjo = new pwn();
        }
        return jjo;
    }

    protected pwn() {
    }

    public void itm(String string) {
        int n = string.length();
        if (string == null) {
            throw new IllegalPasswordException();
        }
        if (n < 6) {
            throw new IllegalPasswordException();
        }
        boolean bl = false;
        boolean bl2 = false;
        for (int i = 0; i < n; ++i) {
            char c = string.charAt(i);
            if (Character.isLetter(c)) {
                bl = true;
                continue;
            }
            if (!Character.isDigit(c)) continue;
            bl2 = true;
        }
        if (!bl || !bl2) {
            throw new IllegalPasswordException();
        }
    }
}

