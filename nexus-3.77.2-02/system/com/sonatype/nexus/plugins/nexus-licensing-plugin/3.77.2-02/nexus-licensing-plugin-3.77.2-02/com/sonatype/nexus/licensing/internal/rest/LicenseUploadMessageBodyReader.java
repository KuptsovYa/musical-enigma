/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.io.ByteStreams
 *  javax.inject.Named
 *  javax.ws.rs.Consumes
 *  javax.ws.rs.core.MediaType
 *  javax.ws.rs.core.MultivaluedMap
 *  javax.ws.rs.ext.MessageBodyReader
 *  javax.ws.rs.ext.Provider
 *  org.apache.shiro.codec.Base64
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.rest.Component
 */
package com.sonatype.nexus.licensing.internal.rest;

import com.google.common.io.ByteStreams;
import com.sonatype.nexus.licensing.internal.rest.LicenseBytes;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import org.apache.shiro.codec.Base64;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.rest.Component;

@Named
@Provider
@Consumes(value={"application/octet-stream", "text/plain"})
public class LicenseUploadMessageBodyReader
extends ComponentSupport
implements MessageBodyReader<LicenseBytes>,
Component {
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return LicenseBytes.class.isAssignableFrom(type);
    }

    public LicenseBytes readFrom(Class<LicenseBytes> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException {
        byte[] rawBytes = ByteStreams.toByteArray((InputStream)entityStream);
        if (MediaType.TEXT_PLAIN_TYPE.isCompatible(mediaType)) {
            rawBytes = Base64.decode((byte[])rawBytes);
        }
        return new LicenseBytes(rawBytes);
    }
}

