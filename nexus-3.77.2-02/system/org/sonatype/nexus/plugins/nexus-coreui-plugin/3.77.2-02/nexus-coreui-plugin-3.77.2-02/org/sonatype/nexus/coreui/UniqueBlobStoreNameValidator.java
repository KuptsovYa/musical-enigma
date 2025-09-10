/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.validation.ConstraintValidatorContext
 *  org.sonatype.nexus.blobstore.api.BlobStoreManager
 *  org.sonatype.nexus.validation.ConstraintValidatorSupport
 */
package org.sonatype.nexus.coreui;

import com.google.common.base.Preconditions;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintValidatorContext;
import org.sonatype.nexus.blobstore.api.BlobStoreManager;
import org.sonatype.nexus.coreui.UniqueBlobStoreName;
import org.sonatype.nexus.validation.ConstraintValidatorSupport;

@Named
public class UniqueBlobStoreNameValidator
extends ConstraintValidatorSupport<UniqueBlobStoreName, String> {
    private final BlobStoreManager blobStoreManager;

    @Inject
    public UniqueBlobStoreNameValidator(BlobStoreManager blobStoreManager) {
        this.blobStoreManager = (BlobStoreManager)Preconditions.checkNotNull((Object)blobStoreManager);
    }

    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return !this.blobStoreManager.exists(s);
    }
}

