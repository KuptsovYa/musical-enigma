/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.sonatype.nexus.coreui.internal.log;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogXO {
    private final Logger log = LoggerFactory.getLogger((String)this.getClass().getName());
    private String fileName = null;
    private long size = -1L;
    private long lastModified = -1L;

    public LogXO(Path path) {
        Preconditions.checkNotNull((Object)path);
        try {
            this.fileName = "tasks/".startsWith(path.getParent().getFileName().toString()) ? "tasks/" + path.getFileName().toString() : ("replication/".startsWith(path.getParent().getFileName().toString()) ? "replication/" + path.getFileName().toString() : path.getFileName().toString());
            this.size = Files.size(path);
            this.lastModified = Files.getLastModifiedTime(path, new LinkOption[0]).toMillis();
        }
        catch (IOException e) {
            this.log.debug(String.format("Unable to get information about log file at {%s}", path));
        }
    }

    public String getFileName() {
        return this.fileName;
    }

    public long getSize() {
        return this.size;
    }

    public long getLastModified() {
        return this.lastModified;
    }
}

