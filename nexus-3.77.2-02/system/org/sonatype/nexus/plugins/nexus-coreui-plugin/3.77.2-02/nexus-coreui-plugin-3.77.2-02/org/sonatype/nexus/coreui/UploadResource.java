/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.codahale.metrics.annotation.ExceptionMetered
 *  com.codahale.metrics.annotation.Timed
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.softwarementors.extjs.djn.EncodingUtils
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.servlet.http.HttpServletRequest
 *  javax.ws.rs.Consumes
 *  javax.ws.rs.POST
 *  javax.ws.rs.Path
 *  javax.ws.rs.PathParam
 *  javax.ws.rs.Produces
 *  javax.ws.rs.WebApplicationException
 *  javax.ws.rs.core.Context
 *  javax.ws.rs.core.Response$Status
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.repository.upload.UploadConfiguration
 *  org.sonatype.nexus.rest.Resource
 *  org.sonatype.nexus.validation.Validate
 */
package org.sonatype.nexus.coreui;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwarementors.extjs.djn.EncodingUtils;
import java.io.IOException;
import java.util.Arrays;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.coreui.internal.UploadService;
import org.sonatype.nexus.repository.upload.UploadConfiguration;
import org.sonatype.nexus.rest.Resource;
import org.sonatype.nexus.validation.Validate;

@Named
@Singleton
@Path(value="internal/ui/upload")
public class UploadResource
extends ComponentSupport
implements Resource {
    public static final String RESOURCE_PATH = "internal/ui/upload";
    private UploadService uploadService;
    private UploadConfiguration configuration;
    private ObjectMapper objectMapper;

    @Inject
    public UploadResource(UploadService uploadService, UploadConfiguration configuration, ObjectMapper objectMapper) {
        this.uploadService = uploadService;
        this.configuration = configuration;
        this.objectMapper = objectMapper;
    }

    @Timed
    @ExceptionMetered
    @Validate
    @POST
    @Path(value="{repositoryName}")
    @Consumes(value={"multipart/form-data"})
    @Produces(value={"application/json"})
    @RequiresPermissions(value={"nexus:component:create"})
    public String postComponent(@PathParam(value="repositoryName") String repositoryName, @Context HttpServletRequest request) throws IOException {
        try {
            if (!this.configuration.isEnabled()) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
            Packet responseJson = new Packet(this.uploadService.upload(repositoryName, request));
            return this.objectMapper.writeValueAsString((Object)responseJson);
        }
        catch (Exception e) {
            this.log.error("Unable to perform upload to repository {}", (Object)repositoryName, (Object)e);
            ErrorPacket responseJson = new ErrorPacket(e.getMessage());
            return this.objectMapper.writeValueAsString(Arrays.asList(responseJson));
        }
    }

    @Timed
    @ExceptionMetered
    @Validate
    @POST
    @Path(value="{repositoryName}")
    @Consumes(value={"multipart/form-data"})
    @Produces(value={"text/html"})
    @RequiresPermissions(value={"nexus:component:create"})
    public String postComponentWithHtmlResponse(@PathParam(value="repositoryName") String repositoryName, @Context HttpServletRequest request) throws IOException {
        return this.htmlWrap(this.postComponent(repositoryName, request));
    }

    private String htmlWrap(String contents) {
        return "<html><body><textarea>" + EncodingUtils.htmlEncode((String)contents) + "</textarea></body></html>";
    }

    public static class Packet {
        private String data;

        public Packet(String data) {
            this.data = data;
        }

        public boolean isSuccess() {
            return true;
        }

        public String getData() {
            return this.data;
        }
    }

    public static class ErrorPacket {
        private String message;

        public ErrorPacket(String message) {
            this.message = message;
        }

        public boolean isSuccess() {
            return false;
        }

        public int getTid() {
            return 1;
        }

        public String getAction() {
            return "upload";
        }

        public String getMethod() {
            return "upload";
        }

        public String getType() {
            return "rpc";
        }

        public String getMessage() {
            return this.message;
        }
    }
}

