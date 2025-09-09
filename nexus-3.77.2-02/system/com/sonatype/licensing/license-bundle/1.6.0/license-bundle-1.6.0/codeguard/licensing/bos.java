/*
 * Decompiled with CFR 0.152.
 */
package codeguard.licensing;

import codeguard.licensing.rnn;
import com.sonatype.license.PlexusLicenseContent;
import java.io.File;
import java.util.Date;
import javax.security.auth.x500.X500Principal;
import org.sonatype.licensing.CustomLicenseContent;
import org.sonatype.licensing.LicenseKeyRequest;
import org.sonatype.licensing.ProductDetails;
import org.sonatype.licensing.util.LicensingUtil;

public abstract class bos
extends rnn
implements LicenseKeyRequest {
    public static final String CONSUMER_TYPE_USER = LicensingUtil.unobfuscate(new long[]{-5315844356430302770L, -3200891647243649522L});
    private File wab;
    private final ProductDetails xrw;

    public bos(ProductDetails productDetails) {
        this.xrw = productDetails;
    }

    @Override
    public CustomLicenseContent getLicenseContent() {
        PlexusLicenseContent plexusLicenseContent = new PlexusLicenseContent();
        plexusLicenseContent.setSubject(this.xrw.getProductName());
        plexusLicenseContent.setIssuer(this.getIssuer());
        plexusLicenseContent.setIssued(new Date());
        plexusLicenseContent.setNotBefore(this.getEffectiveDate());
        plexusLicenseContent.setNotAfter(this.getExpirationDate());
        plexusLicenseContent.setHolder(this.getContactDetails());
        plexusLicenseContent.setIssuer(this.getIssuer());
        plexusLicenseContent.setConsumerType(CONSUMER_TYPE_USER);
        plexusLicenseContent.setExtra(this.getExtraContent());
        return plexusLicenseContent;
    }

    @Override
    public File getLicenseKeyFile() {
        return this.wab;
    }

    @Override
    public void setLicenseKeyFile(File file) {
        this.wab = file;
    }

    protected X500Principal getContactDetails() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CN=").append(LicensingUtil.encodeDNPart(this.getContactName())).append(", ");
        stringBuilder.append("OU=").append("").append(", ");
        stringBuilder.append("O=").append(LicensingUtil.encodeDNPart(this.getContactCompany())).append(", ");
        stringBuilder.append("UID=").append(LicensingUtil.encodeDNPart(this.getContactEmailAddress())).append(", ");
        stringBuilder.append("L=").append(LicensingUtil.encodeDNPart(this.getContactTelephone())).append(", ");
        stringBuilder.append("C=").append(LicensingUtil.encodeDNPart(this.getContactCountry()));
        return new X500Principal(stringBuilder.toString());
    }

    @Override
    public abstract String getExtraContent();
}

