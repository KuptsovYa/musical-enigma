/*
 * Decompiled with CFR 0.152.
 */
package org.sonatype.nexus.security.authc;

public enum AuthenticationFailureReason {
    USER_NOT_FOUND,
    PASSWORD_EMPTY,
    INCORRECT_CREDENTIALS,
    DISABLED_ACCOUNT,
    LICENSE_LIMITATION,
    EXPIRED_CREDENTIALS,
    UNKNOWN;

}

