/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.annotation.Priority
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.apache.commons.lang.StringUtils
 *  org.apache.shiro.authc.credential.PasswordService
 *  org.sonatype.nexus.common.text.Strings2
 */
package org.sonatype.nexus.security.config;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.credential.PasswordService;
import org.sonatype.nexus.common.text.Strings2;
import org.sonatype.nexus.security.config.AdminPasswordFileManager;
import org.sonatype.nexus.security.config.MemorySecurityConfiguration;
import org.sonatype.nexus.security.config.SecurityConfiguration;
import org.sonatype.nexus.security.config.SecurityConfigurationSource;
import org.sonatype.nexus.security.config.memory.MemoryCUser;
import org.sonatype.nexus.security.config.memory.MemoryCUserRoleMapping;

@Named(value="static")
@Singleton
@Priority(value=-2147483648)
public class StaticSecurityConfigurationSource
implements SecurityConfigurationSource {
    private static final String NEXUS_SECURITY_INITIAL_PASSWORD = "NEXUS_SECURITY_INITIAL_PASSWORD";
    private final PasswordService passwordService;
    private final AdminPasswordFileManager adminPasswordFileManager;
    private final boolean randomPassword;
    private final String password;
    private SecurityConfiguration configuration;

    @Inject
    public StaticSecurityConfigurationSource(PasswordService passwordService, AdminPasswordFileManager adminPasswordFileManager, @Named(value="${nexus.security.randompassword:-true}") boolean randomPassword) {
        this(passwordService, adminPasswordFileManager, randomPassword, System.getenv(NEXUS_SECURITY_INITIAL_PASSWORD));
    }

    public StaticSecurityConfigurationSource(PasswordService passwordService, AdminPasswordFileManager adminPasswordFileManager, boolean randomPassword, @Nullable String password) {
        this.passwordService = passwordService;
        this.adminPasswordFileManager = adminPasswordFileManager;
        this.password = password;
        if (StringUtils.isBlank((String)password)) {
            boolean enabled = Optional.ofNullable(System.getenv("NEXUS_SECURITY_RANDOMPASSWORD")).map(Boolean::valueOf).orElse(true);
            this.randomPassword = randomPassword && enabled;
        } else {
            this.randomPassword = false;
        }
    }

    @Override
    public SecurityConfiguration getConfiguration() {
        if (this.configuration != null) {
            return this.configuration;
        }
        return this.loadConfiguration();
    }

    @Override
    public synchronized SecurityConfiguration loadConfiguration() {
        String encryptedPassword = this.passwordService.encryptPassword((Object)this.getPassword());
        return new MemorySecurityConfiguration().withUsers(new MemoryCUser().withId("admin").withPassword(encryptedPassword).withFirstName("Administrator").withLastName("User").withStatus(this.randomPassword ? "changepassword" : "active").withEmail("admin@example.org"), new MemoryCUser().withId("anonymous").withPassword("$shiro1$SHA-512$1024$CPJm1XWdYNg5eCAYp4L4HA==$HIGwnJhC07ZpgeVblZcFRD1F6KH+xPG8t7mIcEMbfycC+n5Ljudyoj9dzdinrLmChTrmKMCw2/z29F7HeLbTbQ==").withFirstName("Anonymous").withLastName("User").withStatus("active").withEmail("anonymous@example.org")).withUserRoleMappings(new MemoryCUserRoleMapping().withUserId("admin").withSource("default").withRoles("nx-admin"), new MemoryCUserRoleMapping().withUserId("anonymous").withSource("default").withRoles("nx-anonymous"));
    }

    private String getPassword() {
        if (StringUtils.isNotBlank((String)this.password)) {
            return this.password;
        }
        try {
            String savedPassword = this.adminPasswordFileManager.readFile();
            if (!Strings2.isBlank((String)savedPassword)) {
                return savedPassword;
            }
            if (!this.randomPassword) {
                return "admin123";
            }
            savedPassword = UUID.randomUUID().toString();
            if (!this.adminPasswordFileManager.writeFile(savedPassword)) {
                savedPassword = "admin123";
            }
            return savedPassword;
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

