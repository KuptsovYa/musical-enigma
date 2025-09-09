/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Provider
 */
package codeguard.licensing;

import codeguard.licensing.qyf;
import codeguard.licensing.zsv;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseContentException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import org.sonatype.licensing.CustomLicenseContent;
import org.sonatype.licensing.LicenseValidator;
import org.sonatype.licensing.product.internal.DefaultLicenseKey;

@Named(value="licensing.default")
public class djy
extends zsv
implements LicenseValidator {
    private static final String xlf = new qyf(new long[]{-9211605111142713620L, 391714365510707393L, -7356761750428556372L, 6379560902598103028L}).toString();
    private final Provider<DefaultLicenseKey> cgs;

    @Inject
    public djy(Provider<DefaultLicenseKey> provider) {
        this.cgs = provider;
    }

    @Override
    protected void itm(LicenseContent licenseContent, String string, String string2) throws LicenseContentException {
        if (string == null || !string.contains(string2) && !this.itm(licenseContent, string2)) {
            throw new LicenseContentException(xlf);
        }
    }

    private boolean itm(LicenseContent licenseContent, String string) {
        if (!(licenseContent instanceof CustomLicenseContent)) {
            return false;
        }
        CustomLicenseContent customLicenseContent = (CustomLicenseContent)licenseContent;
        DefaultLicenseKey defaultLicenseKey = (DefaultLicenseKey)this.cgs.get();
        defaultLicenseKey.populateFromLicenseContent(customLicenseContent);
        return defaultLicenseKey.isProductLicensed(string);
    }
}

