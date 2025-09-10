/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.apache.shiro.authc.credential.DefaultPasswordService
 *  org.apache.shiro.authc.credential.PasswordService
 *  org.apache.shiro.crypto.hash.DefaultHashService
 *  org.apache.shiro.crypto.hash.HashService
 *  org.apache.shiro.crypto.hash.format.HashFormat
 *  org.apache.shiro.crypto.hash.format.HexFormat
 */
package org.sonatype.nexus.security.internal;

import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.HashService;
import org.apache.shiro.crypto.hash.format.HashFormat;
import org.apache.shiro.crypto.hash.format.HexFormat;

@Named(value="legacy")
@Singleton
public class LegacyNexusPasswordService
implements PasswordService {
    private final DefaultPasswordService sha1PasswordService = new DefaultPasswordService();
    private final DefaultPasswordService md5PasswordService;

    public LegacyNexusPasswordService() {
        DefaultHashService sha1HashService = new DefaultHashService();
        sha1HashService.setHashAlgorithmName("SHA-1");
        sha1HashService.setHashIterations(1);
        sha1HashService.setGeneratePublicSalt(false);
        this.sha1PasswordService.setHashService((HashService)sha1HashService);
        this.sha1PasswordService.setHashFormat((HashFormat)new HexFormat());
        this.md5PasswordService = new DefaultPasswordService();
        DefaultHashService md5HashService = new DefaultHashService();
        md5HashService.setHashAlgorithmName("MD5");
        md5HashService.setHashIterations(1);
        md5HashService.setGeneratePublicSalt(false);
        this.md5PasswordService.setHashService((HashService)md5HashService);
        this.md5PasswordService.setHashFormat((HashFormat)new HexFormat());
    }

    public String encryptPassword(Object plaintextPassword) {
        throw new UnsupportedOperationException();
    }

    public boolean passwordsMatch(Object submittedPlaintext, String encrypted) {
        return this.sha1PasswordService.passwordsMatch(submittedPlaintext, encrypted) || this.md5PasswordService.passwordsMatch(submittedPlaintext, encrypted);
    }
}

