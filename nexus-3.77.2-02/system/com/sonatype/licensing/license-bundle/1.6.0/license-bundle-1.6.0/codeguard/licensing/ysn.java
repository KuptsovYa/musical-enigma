/*
 * Decompiled with CFR 0.152.
 */
package codeguard.licensing;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ysn
extends ClassLoader {
    private static final Set<String> llk;

    public ysn() {
        super(Thread.currentThread().getContextClassLoader());
    }

    @Override
    protected Class<?> loadClass(String string, boolean bl) throws ClassNotFoundException {
        if (!llk.contains(string)) {
            System.err.format("ERROR [%s] %s - Banned class: %s%n", Thread.currentThread().getName(), ysn.class.getName(), string);
            throw new itm(string);
        }
        return super.loadClass(string, bl);
    }

    static {
        HashSet<String> hashSet = new HashSet<String>();
        hashSet.add("de.schlichtherle.xml.GenericCertificate");
        hashSet.add("de.schlichtherle.license.LicenseContent");
        hashSet.add("org.sonatype.licensing.CustomLicenseContent");
        hashSet.add("com.sonatype.license.PlexusLicenseContent");
        hashSet.add("java.beans.XMLDecoder");
        hashSet.add("java.util.Date");
        hashSet.add("javax.security.auth.x500.X500Principal");
        llk = Collections.unmodifiableSet(hashSet);
    }

    static class itm
    extends RuntimeException {
        public itm(String string) {
            super(string);
        }
    }
}

