/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.common.app.ApplicationDirectories
 */
package org.sonatype.nexus.security.internal;

import com.google.common.base.Preconditions;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.common.app.ApplicationDirectories;
import org.sonatype.nexus.security.config.AdminPasswordFileManager;

@Named
@Singleton
public class AdminPasswordFileManagerImpl
extends ComponentSupport
implements AdminPasswordFileManager {
    private static final String FILENAME = "admin.password";
    public final ApplicationDirectories applicationDirectories;
    private final File adminPasswordFile;

    @Inject
    public AdminPasswordFileManagerImpl(ApplicationDirectories applicationDirectories) {
        this.applicationDirectories = (ApplicationDirectories)Preconditions.checkNotNull((Object)applicationDirectories);
        this.adminPasswordFile = new File(applicationDirectories.getWorkDirectory(), FILENAME);
    }

    @Override
    public boolean writeFile(String password) throws IOException {
        File workdir = this.applicationDirectories.getWorkDirectory();
        if (!workdir.isDirectory() && !workdir.mkdirs()) {
            this.log.error("Failed to create work directory {}", (Object)workdir);
            return false;
        }
        if (this.adminPasswordFile.createNewFile()) {
            this.adminPasswordFile.setReadable(true, true);
        }
        try {
            this.log.info("Writing admin user temporary password to {}", (Object)this.adminPasswordFile.toString());
            Files.write(this.adminPasswordFile.toPath(), password.getBytes(StandardCharsets.UTF_8), new OpenOption[0]);
        }
        catch (Exception e) {
            this.log.error("Failed to write temporary password to disk", (Throwable)e);
            return false;
        }
        return true;
    }

    @Override
    public boolean exists() {
        return this.adminPasswordFile.exists();
    }

    @Override
    public String getPath() {
        return this.adminPasswordFile.getAbsolutePath();
    }

    @Override
    public String readFile() throws IOException {
        if (this.adminPasswordFile.exists()) {
            return new String(Files.readAllBytes(this.adminPasswordFile.toPath()), StandardCharsets.UTF_8);
        }
        return null;
    }

    @Override
    public void removeFile() {
        if (this.adminPasswordFile.exists() && !this.adminPasswordFile.delete()) {
            this.log.error("Failed to delete admin.password file {}", (Object)this.adminPasswordFile);
        }
    }
}

