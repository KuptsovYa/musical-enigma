/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.config;

import java.io.IOException;

public interface AdminPasswordFileManager {
    public boolean exists();

    public String getPath();

    public boolean writeFile(String var1) throws IOException;

    public String readFile() throws IOException;

    public void removeFile();
}

