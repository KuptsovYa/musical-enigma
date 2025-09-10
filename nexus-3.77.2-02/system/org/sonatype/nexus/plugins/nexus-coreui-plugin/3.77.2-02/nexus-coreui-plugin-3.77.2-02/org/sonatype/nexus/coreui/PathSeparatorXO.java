/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.constraints.NotEmpty
 */
package org.sonatype.nexus.coreui;

import javax.validation.constraints.NotEmpty;

public class PathSeparatorXO {
    @NotEmpty
    private String path;
    @NotEmpty
    private String fileSeparator;

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileSeparator() {
        return this.fileSeparator;
    }

    public void setFileSeparator(String fileSeparator) {
        this.fileSeparator = fileSeparator;
    }
}

