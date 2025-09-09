/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.thoughtworks.xstream.XStream
 *  com.thoughtworks.xstream.XStreamException
 *  javax.enterprise.inject.Typed
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.sonatype.licensing.product.access.io.internal;

import codeguard.licensing.dpq;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.enterprise.inject.Typed;
import javax.inject.Named;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.licensing.product.access.AccessEntrySet;
import org.sonatype.licensing.product.access.io.FileFormat;

@Named
@Singleton
@Typed(value={FileFormat.class})
public class XStreamFormat
extends dpq {
    private static final Logger evv = LoggerFactory.getLogger(XStreamFormat.class);
    public static final int ID = 0;
    private final XStream xxd = new XStream();

    public XStreamFormat() {
        this.xxd.processAnnotations(AccessEntrySet.class);
    }

    @Override
    public AccessEntrySet read(InputStream inputStream) throws IOException {
        try {
            return (AccessEntrySet)this.xxd.fromXML(inputStream);
        }
        catch (XStreamException xStreamException) {
            if (evv.isDebugEnabled()) {
                evv.error("Unable to parse access data", (Throwable)xStreamException);
            } else {
                evv.error("Unable to parse access data: " + xStreamException.getMessage());
            }
            return new AccessEntrySet();
        }
    }

    @Override
    public void write(AccessEntrySet accessEntrySet, OutputStream outputStream) throws IOException {
        this.xxd.toXML((Object)accessEntrySet, outputStream);
    }

    @Override
    public int identifier() {
        return 0;
    }
}

