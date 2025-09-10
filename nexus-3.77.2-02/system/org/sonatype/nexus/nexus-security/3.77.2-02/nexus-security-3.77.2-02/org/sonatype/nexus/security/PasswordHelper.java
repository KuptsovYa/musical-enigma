/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.crypto.PhraseService
 *  org.sonatype.nexus.crypto.maven.MavenCipher
 */
package org.sonatype.nexus.security;

import com.google.common.base.Preconditions;
import java.nio.CharBuffer;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.crypto.PhraseService;
import org.sonatype.nexus.crypto.maven.MavenCipher;

@Singleton
@Named
public class PasswordHelper
extends ComponentSupport {
    private static final String ENC = "CMMDwoV";
    private final MavenCipher mavenCipher;
    private final PhraseService phraseService;

    @Inject
    public PasswordHelper(MavenCipher mavenCipher, PhraseService phraseService) {
        this.mavenCipher = (MavenCipher)Preconditions.checkNotNull((Object)mavenCipher);
        this.phraseService = (PhraseService)Preconditions.checkNotNull((Object)phraseService);
    }

    @Nullable
    public String encrypt(@Nullable String password) {
        if (password == null) {
            return null;
        }
        if (this.mavenCipher.isPasswordCipher((CharSequence)password)) {
            return password;
        }
        String encodedPassword = this.mavenCipher.encrypt((CharSequence)password, this.phraseService.getPhrase(ENC));
        if (encodedPassword != null && !encodedPassword.equals(password)) {
            return this.phraseService.mark(encodedPassword);
        }
        return encodedPassword;
    }

    @Nullable
    public String encryptChars(@Nullable char[] chars) {
        return chars != null ? this.encryptCharBuffer(CharBuffer.wrap(chars)) : null;
    }

    @Nullable
    public String encryptChars(@Nullable char[] chars, int offset, int length) {
        return chars != null ? this.encryptCharBuffer(CharBuffer.wrap(chars, offset, length)) : null;
    }

    private String encryptCharBuffer(CharBuffer charBuffer) {
        if (this.mavenCipher.isPasswordCipher((CharSequence)charBuffer)) {
            return charBuffer.toString();
        }
        String encodedPassword = this.mavenCipher.encrypt((CharSequence)charBuffer, this.phraseService.getPhrase(ENC));
        if (encodedPassword != null && !encodedPassword.contentEquals(charBuffer)) {
            return this.phraseService.mark(encodedPassword);
        }
        return encodedPassword;
    }

    @Nullable
    public String decrypt(@Nullable String encodedPassword) {
        if (encodedPassword == null) {
            return null;
        }
        if (!this.mavenCipher.isPasswordCipher((CharSequence)encodedPassword)) {
            return encodedPassword;
        }
        if (this.phraseService.usesLegacyEncoding(encodedPassword)) {
            return this.mavenCipher.decrypt(encodedPassword, ENC);
        }
        return this.mavenCipher.decrypt(encodedPassword, this.phraseService.getPhrase(ENC));
    }

    @Nullable
    public char[] decryptChars(@Nullable String encodedPassword) {
        if (encodedPassword == null) {
            return null;
        }
        if (!this.mavenCipher.isPasswordCipher((CharSequence)encodedPassword)) {
            return encodedPassword.toCharArray();
        }
        if (this.phraseService.usesLegacyEncoding(encodedPassword)) {
            return this.mavenCipher.decryptChars(encodedPassword, ENC);
        }
        return this.mavenCipher.decryptChars(encodedPassword, this.phraseService.getPhrase(ENC));
    }

    @Nullable
    public String tryDecrypt(@Nullable String encodedPassword) {
        try {
            return this.decrypt(encodedPassword);
        }
        catch (RuntimeException e) {
            this.log.warn("Failed to decrypt value, loading as plain text", (Throwable)(this.log.isDebugEnabled() ? e : null));
            return encodedPassword;
        }
    }

    @Nullable
    public char[] tryDecryptChars(@Nullable String encodedPassword) {
        try {
            return this.decryptChars(encodedPassword);
        }
        catch (RuntimeException e) {
            this.log.warn("Failed to decrypt value, loading as plain text", (Throwable)(this.log.isDebugEnabled() ? e : null));
            return encodedPassword.toCharArray();
        }
    }
}

