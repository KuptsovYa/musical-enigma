/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.validation.ConstraintValidatorContext
 *  org.apache.shiro.mgt.RealmSecurityManager
 *  org.apache.shiro.realm.Realm
 *  org.sonatype.nexus.validation.ConstraintValidatorSupport
 */
package org.sonatype.nexus.security.realm;

import com.google.common.base.Preconditions;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintValidatorContext;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.Realm;
import org.sonatype.nexus.security.realm.RealmExists;
import org.sonatype.nexus.validation.ConstraintValidatorSupport;

@Named
public class RealmExistsValidator
extends ConstraintValidatorSupport<RealmExists, String> {
    private final RealmSecurityManager realmSecurityManager;

    @Inject
    public RealmExistsValidator(RealmSecurityManager realmSecurityManager) {
        this.realmSecurityManager = (RealmSecurityManager)Preconditions.checkNotNull((Object)realmSecurityManager);
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        this.log.trace("Validating realm exists: {}", (Object)value);
        for (Realm realm : this.realmSecurityManager.getRealms()) {
            if (!value.equals(realm.getName())) continue;
            return true;
        }
        return false;
    }
}

