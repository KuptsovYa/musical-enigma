/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.thoughtworks.xstream.annotations.XStreamAlias
 *  com.thoughtworks.xstream.annotations.XStreamAsAttribute
 */
package org.sonatype.licensing.product.access;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import java.io.Serializable;
import java.util.Date;

@XStreamAlias(value="entry")
public class AccessEntry
implements Serializable {
    private static final long vep = 1L;
    @XStreamAsAttribute
    private final Date jic;
    private final Object dcn;

    public AccessEntry(Date date, Object object) {
        this.jic = date;
        this.dcn = object;
    }

    public AccessEntry(Object object) {
        this(new Date(), object);
    }

    public Date getDate() {
        return this.jic;
    }

    public Object getValue() {
        return this.dcn;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        AccessEntry accessEntry = (AccessEntry)object;
        return !(this.dcn == null ? accessEntry.dcn != null : !this.dcn.equals(accessEntry.dcn));
    }

    public int hashCode() {
        return this.dcn != null ? this.dcn.hashCode() : 0;
    }

    public String toString() {
        return "AccessEntry{date=" + this.jic + ", value=" + this.dcn + '}';
    }
}

