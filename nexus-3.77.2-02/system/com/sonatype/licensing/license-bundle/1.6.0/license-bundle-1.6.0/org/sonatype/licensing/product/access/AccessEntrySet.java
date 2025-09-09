/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.thoughtworks.xstream.annotations.XStreamAlias
 *  com.thoughtworks.xstream.annotations.XStreamImplicit
 *  com.thoughtworks.xstream.annotations.XStreamInclude
 */
package org.sonatype.licensing.product.access;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamInclude;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.sonatype.licensing.product.access.AccessEntry;

@XStreamAlias(value="entries")
@XStreamInclude(value={AccessEntry.class})
public class AccessEntrySet
implements Serializable,
Iterable<AccessEntry> {
    private static final long vep = 1L;
    @XStreamImplicit
    private Set<AccessEntry> hcr;

    private Set<AccessEntry> bjw() {
        if (this.hcr == null) {
            this.hcr = new LinkedHashSet<AccessEntry>();
        }
        return this.hcr;
    }

    @Override
    public Iterator<AccessEntry> iterator() {
        return this.bjw().iterator();
    }

    public int size() {
        return this.bjw().size();
    }

    public void clear() {
        this.bjw().clear();
    }

    public void add(AccessEntry accessEntry) {
        assert (accessEntry != null);
        if (this.bjw().contains(accessEntry)) {
            this.bjw().remove(accessEntry);
        }
        this.bjw().add(accessEntry);
    }

    public boolean retainAll(Collection<?> collection) {
        return this.bjw().retainAll(collection);
    }
}

