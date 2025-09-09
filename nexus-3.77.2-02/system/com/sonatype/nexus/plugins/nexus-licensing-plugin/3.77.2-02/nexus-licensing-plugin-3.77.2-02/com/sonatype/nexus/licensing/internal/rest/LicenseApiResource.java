/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.io.ByteStreams
 *  com.sonatype.nexus.licensing.ext.LicenseSource
 *  javax.validation.constraints.NotNull
 *  javax.ws.rs.Consumes
 *  javax.ws.rs.DELETE
 *  javax.ws.rs.GET
 *  javax.ws.rs.POST
 *  javax.ws.rs.Produces
 *  javax.ws.rs.WebApplicationException
 *  javax.ws.rs.core.Response$Status
 *  org.apache.shiro.authz.annotation.RequiresAuthentication
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.licensing.LicensingException
 *  org.sonatype.licensing.feature.Feature
 *  org.sonatype.licensing.product.ProductLicenseKey
 *  org.sonatype.nexus.common.app.ApplicationLicense
 *  org.sonatype.nexus.rest.Resource
 *  org.sonatype.nexus.rest.WebApplicationMessageException
 */
package com.sonatype.nexus.licensing.internal.rest;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;
import com.sonatype.nexus.licensing.ext.LicenseSource;
import com.sonatype.nexus.licensing.internal.LicenseUtil;
import com.sonatype.nexus.licensing.internal.rest.ApiLicenseDetailsXO;
import com.sonatype.nexus.licensing.internal.rest.LicenseApiResourceDoc;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.licensing.LicensingException;
import org.sonatype.licensing.feature.Feature;
import org.sonatype.licensing.product.ProductLicenseKey;
import org.sonatype.nexus.common.app.ApplicationLicense;
import org.sonatype.nexus.rest.Resource;
import org.sonatype.nexus.rest.WebApplicationMessageException;

@Produces(value={"application/json"})
public abstract class LicenseApiResource
extends ComponentSupport
implements Resource,
LicenseApiResourceDoc {
    private final LicenseUtil licenseUtil;
    private final ApplicationLicense applicationLicense;

    protected LicenseApiResource(ApplicationLicense applicationLicense, LicenseUtil licenseUtil) {
        this.applicationLicense = (ApplicationLicense)Preconditions.checkNotNull((Object)applicationLicense);
        this.licenseUtil = (LicenseUtil)Preconditions.checkNotNull((Object)licenseUtil);
    }

    @Override
    @RequiresPermissions(value={"nexus:licensing:read"})
    @RequiresAuthentication
    @GET
    public ApiLicenseDetailsXO getLicenseStatus() {
        return this.getInternalLicense();
    }

    @Override
    @RequiresPermissions(value={"nexus:licensing:create"})
    @RequiresAuthentication
    @Consumes(value={"application/octet-stream"})
    @POST
    public ApiLicenseDetailsXO setLicense(@NotNull InputStream in) throws IOException {
        try {
            Throwable throwable = null;
            Object var3_6 = null;
            try (InputStream i = in;){
                return this.getInternalLicense();
            }
            catch (Throwable throwable2) {
                if (throwable == null) {
                    throwable = throwable2;
                } else if (throwable != throwable2) {
                    throwable.addSuppressed(throwable2);
                }
                throw throwable;
            }
        }
        catch (WebApplicationException e) {
            this.log.info("An error occurred reading an uploaded license", (Throwable)e);
            throw new WebApplicationMessageException(Response.Status.fromStatusCode((int)e.getResponse().getStatus()), (Object)(String.valueOf('\"') + e.getMessage() + '\"'), "application/json");
        }
        catch (IOException e) {
            this.log.info("An error occurred reading an uploaded license", (Throwable)e);
            throw new WebApplicationMessageException(Response.Status.BAD_REQUEST, (Object)"\"An error occurred reading uploaded license.\"", "application/json");
        }
    }

    @Override
    @RequiresPermissions(value={"nexus:licensing:delete"})
    @RequiresAuthentication
    @DELETE
    public void removeLicense() {
        this.licenseUtil.uninstallLicense(LicenseSource.API);
    }

    private ApiLicenseDetailsXO getInternalLicense() {
        ApiLicenseDetailsXO result = new ApiLicenseDetailsXO();
        try {
            ProductLicenseKey key = this.licenseUtil.getLicenseDetails();
            result.setContactCompany(key.getContactCompany());
            result.setContactEmail(key.getContactEmailAddress());
            result.setContactName(key.getContactName());
            result.setEffectiveDate(key.getEffectiveDate());
            result.setExpirationDate(key.getExpirationDate());
            result.setFingerprint(this.applicationLicense.getFingerprint());
            result.setLicensedUsers(key.getLicensedUsers() == -1 ? "Unlimited" : String.valueOf(key.getLicensedUsers()));
            result.setLicenseType(StreamSupport.stream(key.getFeatureSet().spliterator(), false).map(Feature::getDescription).collect(Collectors.joining(", ")));
            result.setFeatures(key.getRawFeatures().stream().collect(Collectors.joining(", ")));
            return result;
        }
        catch (LicensingException e) {
            this.log.debug("License exception retrieving current license.", (Throwable)e);
            throw new WebApplicationMessageException(Response.Status.PAYMENT_REQUIRED, (Object)"\"Missing or invalid license\"", "application/json");
        }
    }
}

