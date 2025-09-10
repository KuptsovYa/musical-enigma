/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.apache.shiro.authc.credential.DefaultPasswordService
 *  org.apache.shiro.authc.credential.HashingPasswordService
 *  org.apache.shiro.authc.credential.PasswordService
 *  org.apache.shiro.crypto.hash.DefaultHashService
 *  org.apache.shiro.crypto.hash.Hash
 *  org.apache.shiro.crypto.hash.HashService
 */
package org.sonatype.nexus.security.internal;

import com.google.common.base.Preconditions;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.HashingPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.HashService;

@Named(value="default")
@Singleton
public class DefaultSecurityPasswordService
implements HashingPasswordService {
    private static final String DEFAULT_HASH_ALGORITHM = "SHA-512";
    private static final int DEFAULT_HASH_ITERATIONS = 1024;
    private final DefaultPasswordService passwordService = new DefaultPasswordService();
    private final PasswordService legacyPasswordService;

    @Inject
    public DefaultSecurityPasswordService(@Named(value="legacy") PasswordService legacyPasswordService) {
        this.legacyPasswordService = (PasswordService)Preconditions.checkNotNull((Object)legacyPasswordService);
        DefaultHashService hashService = new DefaultHashService();
        hashService.setHashAlgorithmName(DEFAULT_HASH_ALGORITHM);
        hashService.setHashIterations(1024);
        hashService.setGeneratePublicSalt(true);
        this.passwordService.setHashService((HashService)hashService);
    }

    public String encryptPassword(Object plaintextPassword) {
        return this.passwordService.encryptPassword(plaintextPassword);
    }

    public boolean passwordsMatch(Object submittedPlaintext, String encrypted) {
        return this.passwordService.passwordsMatch(submittedPlaintext, encrypted) || this.legacyPasswordService.passwordsMatch(submittedPlaintext, encrypted);
    }

    public Hash hashPassword(Object plaintext) {
        return this.passwordService.hashPassword(plaintext);
    }

    public boolean passwordsMatch(Object plaintext, Hash savedPasswordHash) {
        return this.passwordService.passwordsMatch(plaintext, savedPasswordHash);
    }
}

